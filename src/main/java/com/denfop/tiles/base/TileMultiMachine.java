package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.ProcessMultiComponent;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.componets.client.EffectType;
import com.denfop.container.ContainerMultiMachine;
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
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileMultiMachine extends TileEntityInventory implements
        IAudioFixer, IUpgradableBlock, IUpdatableTileEvent, IHasRecipe {


    public final int type;
    public final AdvEnergy energy;
    public final InvSlotDischarge dischargeSlot;
    public final ProcessMultiComponent multi_process;
    public final int sizeWorkingSlot;
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

    public TileMultiMachine(int energyconsume, int OperationsPerTick, int type) {
        this(1, energyconsume, OperationsPerTick, type);
    }

    public TileMultiMachine(
            int aDefaultTier,
            int energyconsume,
            int OperationsPerTick,
            int type
    ) {

        this.sizeWorkingSlot = this.getMachine().sizeWorkingSlot;
        this.dischargeSlot = new InvSlotDischarge(this, InvSlot.TypeItemSlot.INPUT, aDefaultTier, false);
        this.energy = this.addComponent(AdvEnergy
                .asBasicSink(this, (double) energyconsume * OperationsPerTick, (int) Math.pow(2, this.sizeWorkingSlot - 1))
                .addManagedSlot(this.dischargeSlot));
        if (this.getMachine().type == EnumTypeMachines.OreWashing) {
            this.fluid = this.addComponent(new Fluids(this));
            this.tank = fluid.addTank("tank", 10000, InvSlot.TypeItemSlot.INPUT,
                    Fluids.fluidPredicate(FluidRegistry.WATER)
            );
        }
        this.type = type;
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
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        final CustomPacketBuffer packet = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packet, cold,false);
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
            EncoderHandler.encode(packet, cold,false);
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

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.multimachine.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.multi_process.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.multi_process.defaultOperationLength);
        }

        if (this.getComp(AdvEnergy.class) != null) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    energy.getCapacity())
                    + " EF");
        }
    }


    public EnumTypeAudio getType() {
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
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (getSound() == null) {
            return;
        }
        if (getEnable()) {
            if (soundEvent == 0) {
                this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.pos);
                this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
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
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.energy.addEnergy(energy1);
        }
        final double energy3 = nbt.getDouble("energy1");
        if (energy1 != 0) {
            if (this.exp != null) {
                this.exp.addEnergy(energy3);
            }
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
                    final AdvEnergy component = this.energy;
                    if (component != null) {
                        if (component.getEnergy() != 0) {
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy", component.getEnergy());
                        }
                    }
                    final ComponentBaseEnergy component2 = this.exp;
                    if (component2 != null) {
                        if (component2.getEnergy() != 0) {
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy1", component2.getEnergy());
                        }
                    }
                    final CoolComponent component3 = this.cold;
                    if (component3 != null) {
                        if (component3.getEnergy() != 0) {
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy2", component3.getEnergy());
                        }
                    }
                    return drop;
                case None:
                    return null;
                case Generator:
                    return new ItemStack(IUItem.basemachine2, 1, 78);
                case Machine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                case AdvMachine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
            }
        }
        final AdvEnergy component = this.getComp(AdvEnergy.class);
        if (component != null) {
            if (component.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy", component.getEnergy());
            }
        }
        final ComponentBaseEnergy component2 = this.exp;
        if (component2 != null) {
            if (component2.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy1", component2.getEnergy());
            }
        }
        final CoolComponent component3 = this.cold;
        if (component3 != null) {
            if (component3.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy2", component3.getEnergy());
            }
        }
        return drop;
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        ItemStack stack_quickly = ItemStack.EMPTY;
        ItemStack stack_modulesize = ItemStack.EMPTY;
        ItemStack stack_modulestorage = ItemStack.EMPTY;
        ItemStack panel = ItemStack.EMPTY;
        ItemStack colling = ItemStack.EMPTY;
        if (this.multi_process.quickly) {
            stack_quickly = new ItemStack(IUItem.module_quickly);
        }
        if (this.multi_process.modulesize) {
            stack_modulesize = new ItemStack(IUItem.module_stack);
        }
        if (this.multi_process.modulestorage) {
            stack_modulestorage = new ItemStack(IUItem.module_storage);
        }
        if (solartype != null) {
            panel = new ItemStack(IUItem.module6, 1, solartype.meta);
        }

        if (this.cold.upgrade && this.getMachine().type != EnumTypeMachines.Centrifuge) {
            colling = new ItemStack(IUItem.coolupgrade, 1, this.cold.meta);

        }
        if (!stack_modulestorage.isEmpty() || !stack_quickly.isEmpty() || !stack_modulesize.isEmpty() || !panel.isEmpty() || !colling.isEmpty()) {
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
            final EntityPlayer entityPlayer,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {

        if (!entityPlayer.getHeldItem(hand).isEmpty()) {
            if (entityPlayer.getHeldItem(hand).getItem() instanceof ItemModuleTypePanel) {
                if (this.solartype != null) {
                    EnumSolarPanels type = this.solartype;
                    int meta = type.meta;
                    ItemStack stack = new ItemStack(IUItem.module6, 1, meta);
                    if (!entityPlayer.inventory.addItemStackToInventory(stack)) {
                        EntityItem item = new EntityItem(
                                entityPlayer.getEntityWorld(),
                                (int) (entityPlayer.posX),
                                (int) entityPlayer.posY - 1,
                                (int) (entityPlayer.posZ)
                        );
                        item.setItem(stack);
                        item.setPosition(entityPlayer.posX, entityPlayer.posY - 1, entityPlayer.posZ);
                        item.setDefaultPickupDelay();
                        world.spawnEntity(item);
                    }

                }
                this.solartype = ItemModuleTypePanel.getSolarType(entityPlayer.getHeldItem(hand).getItemDamage());
                entityPlayer.getHeldItem(hand).setCount(entityPlayer.getHeldItem(hand).getCount() - 1);
                return true;
            } else if (this.multi_process.onActivated(entityPlayer.getHeldItem(hand))) {
                return true;
            } else if (!this.getWorld().isRemote && FluidUtil.getFluidHandler(entityPlayer.getHeldItem(hand)) != null && this.fluid != null) {

                return FluidUtil.interactWithFluidHandler(entityPlayer, hand,
                        this.fluid.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                );
            }


        }
        return super.onActivated(entityPlayer, hand, side, hitX, hitY, hitZ);
    }


    public abstract EnumMultiMachine getMachine();

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        int id = nbttagcompound.getInteger("panelid");
        if (id != -1) {
            this.solartype = IUItem.map1.get(id);
        }
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (this.solartype != null) {
            nbttagcompound.setInteger("panelid", this.solartype.meta);
        } else {
            nbttagcompound.setInteger("panelid", -1);
        }
        nbttagcompound.setBoolean("sound", this.sound);

        return nbttagcompound;
    }

    public void updateTileServer(EntityPlayer player, double event) {
        if (event == 0) {
            this.multi_process.cycleMode();
        } else {

            sound = !sound;
            new PacketUpdateFieldTile(this, "sound", this.sound);

            if (!sound) {
                if (this.getType() == EnumTypeAudio.ON) {
                    setType(EnumTypeAudio.OFF);
                    new PacketStopSound(getWorld(), this.pos);


                }
            }
        }
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            new PacketUpdateFieldTile(this, "sound", this.sound);
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
                    this.panel = new TileSolarPanel(solartype);
                    panel.setWorld(this.getWorld());
                    panel.setPos(this.pos);
                    IAdvEnergyNet advEnergyNet = EnergyNetGlobal.instance;
                    panel.canRain = (this.world.getBiome(this.pos).canRain() || this.world
                            .getBiome(this.pos)
                            .getRainfall() > 0.0F);
                    panel.hasSky = !this.world.provider.isNether();
                    panel.biome = this.world.getBiome(this.pos);
                    panel.sunCoef = advEnergyNet.getSunCoefficient(this.world);
                    panel.skyIsVisible = this.world.canBlockSeeSky(this.pos.up()) &&
                            (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                                    MapColor.AIR) && !panel.noSunWorld;
                    panel.wetBiome = panel.getWorld().getBiome(this.pos).getRainfall() > 0.0F;
                    panel.rain = panel.wetBiome && (this.world.isRaining() || this.world.isThundering());
                    panel.sunIsUp = this.getWorld().isDaytime();

                }

                if (panel.activeState == null || this.getWorld().provider.getWorldTime() % 40 == 0) {
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


    public ContainerMultiMachine getGuiContainer(EntityPlayer player) {
        return new ContainerMultiMachine(player, this, this.sizeWorkingSlot);
    }

    @SideOnly(Side.CLIENT)
    public GuiMultiMachine getGui(EntityPlayer player, boolean isAdmin) {

        return new GuiMultiMachine(new ContainerMultiMachine(player, this, sizeWorkingSlot));


    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing
        );
    }


    public int getMode() {
        return 0;
    }

}
