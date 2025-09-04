package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.sound.AudioFixer;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.mechanism.EnumTypeMachines;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.BioProcessMultiComponent;
import com.denfop.componets.ComponentBioFuelEnergy;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuBioMultiMachine;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenBioMultiMachine;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class BlockEntityBioMultiMachine extends BlockEntityInventory implements
        AudioFixer, IUpgradableBlock, IUpdatableTileEvent, IHasRecipe, IBioMachine {


    public final int type;
    public final BioProcessMultiComponent multi_process;
    public final int sizeWorkingSlot;
    public final ComponentBioFuelEnergy bioFuel;
    public final Fluids.InternalFluidTank fluidTank;
    public HeatComponent heat;
    public Fluids.InternalFluidTank tank;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean sound;
    private Fluids fluid = null;

    public BlockEntityBioMultiMachine(int energyconsume, int OperationsPerTick, int type, MultiBlockEntity block, BlockPos pos, BlockState state) {
        this(1, energyconsume, OperationsPerTick, type, block, pos, state);
    }

    public BlockEntityBioMultiMachine(
            int aDefaultTier,
            int energyconsume,
            int OperationsPerTick,
            int type,
            MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.sizeWorkingSlot = this.getMachine().sizeWorkingSlot;
        this.bioFuel = this.addComponent(ComponentBioFuelEnergy.asBasicSink(this, 1000));
        fluid = this.addComponent(new Fluids(this));
        this.fluidTank = fluid.addTank("tank", OperationsPerTick * energyconsume, Inventory.TypeItemSlot.NONE);
        bioFuel.setFluidTank(fluidTank);
        if (this.getMachine().type == EnumTypeMachines.OreWashing) {
            this.tank = fluid.addTank("tank", 64000, Inventory.TypeItemSlot.INPUT,
                    Fluids.fluidPredicate(Fluids.WATER)
            );
        }
        if (this.getMachine().type == EnumTypeMachines.Centrifuge) {
            this.heat = this.addComponent(HeatComponent.asBasicSink(this, 5000));
        }
        this.type = type;
        this.sound = true;
        this.multi_process = this.addComponent(new BioProcessMultiComponent(this, getMachine()));
    }

    @Override
    public Fluids.InternalFluidTank getTank() {
        return tank;
    }

    public HeatComponent getHeat() {
        return heat;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            sound = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
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
            tooltip.add(Localization.translate("iu.machines_work_length") + this.multi_process.operationLength);
        }
        tooltip.add(Localization.translate("bio_machine.info9"));
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
                this.getWorld().playSound(
                        null,
                        this.pos,
                        EnumSound.biomass_progress.getSoundEvent(),
                        SoundSource.BLOCKS,
                        1F,
                        1
                );
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.pos);
                this.getWorld().playSound(
                        null,
                        this.pos,
                        EnumSound.biomass_interrupt.getSoundEvent(),
                        SoundSource.BLOCKS,
                        1F,
                        1
                );
            } else {
                new PacketStopSound(getWorld(), this.pos);
            }
        }
    }


    public void onUpdate() {

    }


    public List<ItemStack> getDrop() {

        return getAuxDrops(0);

    }


    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
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
        return drop;
    }

    public List<ItemStack> getWrenchDrops(Player player, int fortune) {


        return super.getWrenchDrops(player, fortune);
    }


    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.hasFluidHandler(player.getItemInHand(hand)) && fluid != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluid.getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }


    public abstract EnumMultiMachine getMachine();

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("sound", this.sound);

        return nbttagcompound;
    }

    public void updateTileServer(Player player, double event) {
        if (event == 0) {

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
    }


    public ContainerMenuBioMultiMachine getGuiContainer(Player player) {
        return new ContainerMenuBioMultiMachine(player, this, this.sizeWorkingSlot);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {


        return new ScreenBioMultiMachine((ContainerMenuBioMultiMachine) menu);


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

}
