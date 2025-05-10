package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.*;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.componets.client.EffectType;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiMultiMachine;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotDischarge;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.items.modules.ItemModuleTypePanel;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import com.denfop.tiles.mechanism.multimechanism.IMultiMachine;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileMultiMachine extends TileEntityInventory implements
        IAudioFixer, IUpgradableBlock, IUpdatableTileEvent, IHasRecipe, IMultiMachine {


    public final Energy energy;
    public final InvSlotDischarge dischargeSlot;
    public final ProcessMultiComponent multi_process;
    public final int sizeWorkingSlot;
    public final InvSlot input_slot;
    private final ComponentUpgradeSlots componentUpgrades;
    public CoolComponent cold;
    public HeatComponent heat = null;
    public FluidTank tank = null;
    public ComponentBaseEnergy exp;
    public EnumSolarPanels solartype;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean sound;
    private Fluids fluid = null;
    private int tick;
    private TileSolarPanel panel;

    public TileMultiMachine(EnumMultiMachine enumMultiMachine, IMultiTileBlock block, BlockPos pos, BlockState state) {
        this(1, enumMultiMachine.usagePerTick, enumMultiMachine.lenghtOperation, block, pos, state);
    }


    public TileMultiMachine(int energyconsume, int OperationsPerTick, IMultiTileBlock block, BlockPos pos, BlockState state) {
        this(1, energyconsume, OperationsPerTick, block, pos, state);
    }

    public TileMultiMachine(
            int aDefaultTier,
            int energyconsume,
            int OperationsPerTick, IMultiTileBlock block, BlockPos pos, BlockState state
    ) {
        super(block, pos, state);
        this.sizeWorkingSlot = this.getMachine().sizeWorkingSlot;
        this.dischargeSlot = new InvSlotDischarge(this, InvSlot.TypeItemSlot.INPUT, aDefaultTier, false);
        this.energy = this.addComponent(Energy
                .asBasicSink(this, (double) energyconsume * OperationsPerTick, (int) Math.min(14, Math.pow(
                        2,
                        this.sizeWorkingSlot - 1
                )))
                .addManagedSlot(this.dischargeSlot));
        if (this.getMachine().type == EnumTypeMachines.OreWashing) {
            this.fluid = this.addComponent(new Fluids(this));
            this.tank = fluid.addTank("tank", 64000, InvSlot.TypeItemSlot.INPUT,
                    Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER)
            );
        }

        this.solartype = null;
        this.cold = this.addComponent(CoolComponent.asBasicSink(this, 100));
        this.sound = true;

        this.exp = null;
        if (this.getMachine().type == EnumTypeMachines.ELECTRICFURNACE) {
            this.exp = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.EXPERIENCE, this, 5000, 14));
        }
        if (this.getMachine().type == EnumTypeMachines.Centrifuge) {
            this.heat = this.addComponent(HeatComponent.asBasicSink(this, 5000));
        }
        this.multi_process = this.addComponent(new ProcessMultiComponent(this, getMachine()));
        this.componentClientEffectRender = new ComponentClientEffectRender(this, EffectType.HEAT);
        this.componentUpgrades = this.addComponent(new ComponentUpgradeSlots(this, this.multi_process.getUpgradeSlot()) {
            @Override
            public void setOverclockRates(final InvSlotUpgrade invSlotUpgrade) {
                super.setOverclockRates(invSlotUpgrade);
                invSlotUpgrade.isUpdate = true;
                ((TileMultiMachine) this.getParent()).multi_process.setOverclockRates();
                invSlotUpgrade.isUpdate = false;
            }
        });
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileMultiMachine) this.base).multi_process.inputSlots.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileMultiMachine) this.base).multi_process.inputSlots.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }
        };
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public HeatComponent getHeat() {
        return heat;
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        final CustomPacketBuffer packet = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packet, cold, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer customPacketBuffer) {
        super.readUpdatePacket(customPacketBuffer);
        try {
            cold.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cold, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cold.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            Object o = DecoderHandler.decode(customPacketBuffer);
            if (o != null) {
                solartype = EnumSolarPanels.values()[(int) o];
            }
            sound = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, solartype);
            EncoderHandler.encode(packet, sound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void init() {

    }

    @Override
    public SoundEvent getSound() {
        return this.getMachine().type.getSound();
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if (!this.cold.upgrade) {
                tooltip.add(Localization.translate("iu.multimachine.info"));
            }
            if (this.heat != null) {
                tooltip.add(Localization.translate("iu.heatmachine.info"));
            }
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.multi_process.energyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.multi_process.operationLength);
        }

        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
        final CompoundTag nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    energy.getCapacity())
                    + " EF");
        }
        super.addInformation(stack, tooltip);
    }


    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (getSound() == null) {
            return;
        }
        if (getEnable()) {
            if (soundEvent == 0) {
                this.getWorld().playSound(null, this.pos, getSound(), SoundSource.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.pos);
                this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
            } else {
                new PacketStopSound(getWorld(), this.pos);
            }
        }
    }


    public void onUpdate() {

    }

    public CoolComponent getComponent() {
        return this.cold;
    }

    public List<ItemStack> getDrop() {

        return getAuxDrops(0);

    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        final CompoundTag nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.energy.addEnergy(energy1);
        }

        final double energy4 = nbt.getDouble("energy2");
        if (energy1 != 0) {
            if (this.cold != null) {
                this.cold.addEnergy(energy4);
            }
        }
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final Energy component = this.energy;
                    if (component != null) {
                        if (component.getEnergy() != 0) {
                            final CompoundTag nbt = ModUtils.nbt(drop);
                            nbt.putDouble("energy", component.getEnergy());
                        }
                    }

                    final CoolComponent component3 = this.cold;
                    if (component3 != null) {
                        if (component3.getEnergy() != 0) {
                            final CompoundTag nbt = ModUtils.nbt(drop);
                            nbt.putDouble("energy2", component3.getEnergy());
                        }
                    }
                    return drop;
                case None:
                    return null;
                case Generator:
                    return new ItemStack(IUItem.basemachine2.getItem(78), 1);
                case Machine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                case AdvMachine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
            }
        }
        final Energy component = this.getComp(Energy.class);
        if (component != null) {
            if (component.getEnergy() != 0) {
                final CompoundTag nbt = ModUtils.nbt(drop);
                nbt.putDouble("energy", component.getEnergy());
            }
        }

        final CoolComponent component3 = this.cold;
        if (component3 != null) {
            if (component3.getEnergy() != 0) {
                final CompoundTag nbt = ModUtils.nbt(drop);
                nbt.putDouble("energy2", component3.getEnergy());
            }
        }
        return drop;
    }

    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        ItemStack stack_quickly = ItemStack.EMPTY;
        ItemStack stack_modulesize = ItemStack.EMPTY;
        ItemStack stack_modulestorage = ItemStack.EMPTY;
        ItemStack panel = ItemStack.EMPTY;
        ItemStack colling = ItemStack.EMPTY;
        ItemStack module_separate = ItemStack.EMPTY;
        ItemStack module_infinity_water = ItemStack.EMPTY;
        if (this.multi_process.quickly) {
            stack_quickly = new ItemStack(IUItem.module_quickly.getItem());
        }
        if (this.multi_process.modulesize) {
            stack_modulesize = new ItemStack(IUItem.module_stack.getItem());
        }
        if (this.multi_process.modulestorage) {
            stack_modulestorage = new ItemStack(IUItem.module_storage.getItem());
        }
        if (this.multi_process.module_separate) {
            module_separate = new ItemStack(IUItem.module_separate.getItem());
        }
        if (this.multi_process.module_infinity_water) {
            module_infinity_water = new ItemStack(IUItem.module_infinity_water.getItem());
        }
        if (solartype != null) {
            panel = new ItemStack(IUItem.module6.getStack(solartype.meta), 1);
        }

        if (this.cold.upgrade && this.getMachine().type != EnumTypeMachines.Centrifuge) {
            colling = new ItemStack(IUItem.coolupgrade.getStack(this.cold.meta), 1);

        }
        if (!stack_modulestorage.isEmpty() || !stack_quickly.isEmpty() || !module_separate.isEmpty() || !module_infinity_water.isEmpty() || !stack_modulesize.isEmpty() || !panel.isEmpty() || !colling.isEmpty()) {
            if (!stack_modulestorage.isEmpty()) {
                ret.add(stack_modulestorage);
                this.multi_process.shrinkModule(1);
                this.multi_process.setModulesize(false);
            }
            if (!stack_quickly.isEmpty()) {
                ret.add(stack_quickly);
                this.multi_process.shrinkModule(1);
                this.multi_process.setQuickly(false);
            }
            if (!module_separate.isEmpty()) {
                ret.add(module_separate);
                this.multi_process.shrinkModule(1);
                this.multi_process.module_separate = false;
            }
            if (!module_infinity_water.isEmpty()) {
                ret.add(module_infinity_water);
                this.multi_process.shrinkModule(1);
                this.multi_process.module_infinity_water = false;
            }
            if (!stack_modulesize.isEmpty()) {
                ret.add(stack_modulesize);
                this.multi_process.shrinkModule(1);
                this.multi_process.setModulesize(false);
            }
            if (solartype != null) {
                ret.add(panel);
                solartype = null;
            }
            if (!colling.isEmpty()) {
                ret.add(colling);
                this.cold.upgrade = false;
                this.cold.meta = 0;
            }
        }
        return ret;
    }


    @Override
    public boolean onActivated(
            final Player entityPlayer,
            final InteractionHand hand,
            final Direction side,
            final Vec3 hitX
    ) {

        if (!entityPlayer.getItemInHand(hand).isEmpty()) {
            if (entityPlayer.getItemInHand(hand).getItem() instanceof ItemModuleTypePanel) {
                if (this.solartype != null) {
                    EnumSolarPanels type = this.solartype;
                    int meta = type.meta;
                    ItemStack stack = new ItemStack(IUItem.module6.getStack(meta));
                    if (!entityPlayer.getInventory().add(stack)) {
                        ItemEntity item = new ItemEntity(
                                entityPlayer.getLevel(),
                                (int) (entityPlayer.getX()),
                                (int) entityPlayer.getY() - 1,
                                (int) (entityPlayer.getZ()), stack
                        );

                        item.setDefaultPickUpDelay();
                        level.addFreshEntity(item);
                    }

                }
                this.solartype = ItemModuleTypePanel.getSolarType(IUItem.module6.getMeta((ItemModuleTypePanel) entityPlayer.getItemInHand(hand).getItem()));
                entityPlayer.getItemInHand(hand).shrink(1);
                return true;
            } else if (this.multi_process.onActivated(entityPlayer.getItemInHand(hand))) {
                return true;
            } else if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(entityPlayer.getItemInHand(hand)) != null && this.fluid != null) {

                return ModUtils.interactWithFluidHandler(entityPlayer, hand,
                        this.fluid.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
                );
            }


        }
        return super.onActivated(entityPlayer, hand, side, hitX);
    }


    public abstract EnumMultiMachine getMachine();

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        int id = nbttagcompound.getInt("panelid");
        if (id != -1) {
            this.solartype = IUItem.map1.get(id);
        }
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (this.solartype != null) {
            nbttagcompound.putInt("panelid", this.solartype.meta);
        } else {
            nbttagcompound.putInt("panelid", -1);
        }
        nbttagcompound.putBoolean("sound", this.sound);

        return nbttagcompound;
    }

    public void updateTileServer(Player player, double event) {
        if (event == 0) {
            this.multi_process.cycleMode();
        } else {

            sound = !sound;
            new PacketUpdateFieldTile(this, "sound", this.sound);

            if (!sound) {
                if (this.getTypeAudio() == EnumTypeAudio.ON) {
                    setType(EnumTypeAudio.OFF);
                    new PacketStopSound(getWorld(), this.pos);


                }
            }
        }
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            new PacketUpdateFieldTile(this, "sound", this.sound);
            if (this.input_slot.isEmpty()) {
                (this).multi_process.inputSlots.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).multi_process.inputSlots.changeAccepts(this.input_slot.get(0));
            }
        }

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    public EnumTypeMachines getTypeMachine() {
        return this.getMachine().type;
    }


    public void onUnloaded() {
        super.onUnloaded();


    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (solartype != null) {
            if (this.energy.getEnergy() < this.energy.getCapacity()) {
                if (panel == null) {
                    this.panel = new TileSolarPanel(solartype, (IMultiTileBlock) solartype.block.getBlock(solartype.meta).getValue(), pos, solartype.block.getBlock(solartype.meta).defaultBlockState());
                    panel.setLevel(this.getWorld());
                    IAdvEnergyNet advEnergyNet = EnergyNetGlobal.instance;
                    if (panel.biome == null) {
                        panel.biome = this.getLevel().getBiome(this.pos);
                    }
                    panel.sunCoef = advEnergyNet.getSunCoefficient(this.level);
                    panel.canRain = (panel.biome.value().getPrecipitation() == Biome.Precipitation.RAIN || panel.biome.value().getDownfall() > 0.0F);
                    panel.hasSky = !(this.getLevel().dimension() == Level.NETHER);

                    panel.wetBiome = panel.biome.value().getDownfall() > 0.0F;
                    panel.noSunWorld = this.getLevel().dimension() == Level.NETHER;

                    panel.rain = panel.wetBiome && (this.getLevel().isRaining() || this.getLevel().isThundering());
                    panel.sunIsUp = this.getLevel().isDay();
                    panel.skyIsVisible = this.getLevel().canSeeSky(this.worldPosition) &&
                            (this.getLevel().getBlockState(this.worldPosition.above()).getMapColor(getLevel(), this.worldPosition.above()) == MaterialColor.NONE) &&
                            !panel.noSunWorld;

                }

                if (panel.activeState == null || this.getWorld().getGameTime() % 40 == 0) {
                    panel.updateVisibility();
                }
                panel.gainFuel();
                if (this.energy.getEnergy() < this.energy.getCapacity()) {
                    this.energy.addEnergy(Math.min(panel.generating, energy.getFreeEnergy()));
                }
            }
        } else {
            if (panel != null) {
                panel = null;
            }
        }
        this.tick++;
        if (!this.getActive()) {
            if (this.tick - 120 >= 0) {
                this.cold.useEnergy(0.35);
                this.tick = 0;
            }
        } else {
            if (this.tick - 240 >= 0) {
                this.cold.useEnergy(0.35);
                this.tick = 0;
            }
        }
    }


    public ContainerMultiMachine getGuiContainer(Player player) {
        return new ContainerMultiMachine(player, this, this.sizeWorkingSlot);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> isAdmin) {

        return new GuiMultiMachine((ContainerMultiMachine) isAdmin);


    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


    public int getMode() {
        return 0;
    }

    public boolean canoperate(int size) {
        return true;
    }

    public int getSize(int size) {
        return size;
    }

    public void consume(int size) {
    }

}
