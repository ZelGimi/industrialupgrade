package com.denfop.blockentity.base;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.sound.AudioFixer;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryDischarge;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class BlockEntityDoubleElectricMachine extends BlockEntityInventory implements
        AudioFixer, BlockEntityUpgrade, IUpdateTick, IUpdatableTileEvent {

    public final Energy energy;
    public final InventoryDischarge dischargeSlot;
    public final InventoryRecipes inputSlotA;
    public final InventoryOutput outputSlot;
    public final InventoryUpgrade upgradeSlot;
    public final HeatComponent heat;
    protected final EnumDoubleElectricMachine type;
    private final ComponentUpgrade componentUpgrades;
    public MachineRecipe output;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public ComponentUpgradeSlots componentUpgrade;
    public ComponentProgress componentProgress;
    public ComponentProcess componentProcess;
    private boolean sound = true;

    public BlockEntityDoubleElectricMachine(
            int energyPerTick,
            int length,
            int outputSlots,
            EnumDoubleElectricMachine type, MultiBlockEntity block, BlockPos pos, BlockState state
    ) {
        this(energyPerTick, length, outputSlots, 1, type, true, block, pos, state);
    }

    public BlockEntityDoubleElectricMachine(
            int energyPerTick,
            int length,
            int outputSlots,
            EnumDoubleElectricMachine type, boolean register, MultiBlockEntity block, BlockPos pos, BlockState state
    ) {
        this(energyPerTick, length, outputSlots, 1, type, register, block, pos, state);
    }

    public BlockEntityDoubleElectricMachine(
            int energyPerTick,
            int length,
            int outputSlots,
            int aDefaultTier,
            EnumDoubleElectricMachine type, boolean register, MultiBlockEntity block, BlockPos pos, BlockState state
    ) {
        super(block, pos, state);
        this.outputSlot = new InventoryOutput(this, outputSlots);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.dischargeSlot = new InventoryDischarge(this, Inventory.TypeItemSlot.INPUT, aDefaultTier, false);
        this.energy = this.addComponent(Energy
                .asBasicSink(this, (double) energyPerTick * length, aDefaultTier)
                .addManagedSlot(this.dischargeSlot));
        this.inputSlotA = new InventoryRecipes(this, type.recipe_name, this);
        this.type = type;
        this.output = null;
        if (type.heat) {
            this.heat = this.addComponent(HeatComponent
                    .asBasicSink(this, 5000));
        } else {
            this.heat = null;
        }
        if (register) {
            this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
            this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                    (short) length
            ));
            this.componentProcess = this.addComponent(new ComponentProcess(this, length, energyPerTick));
            this.componentProcess.setHasAudio(true);
            this.componentProcess.setSlotOutput(outputSlot);
            this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        }
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
    }

    public void updateTileServer(Player var1, double var2) {
        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getTypeAudio() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                new PacketStopSound(getWorld(), this.getBlockPos());


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

    @Override
    public void onNetworkEvent(final int var1) {

    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (type == EnumDoubleElectricMachine.ALLOY_SMELTER) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.heatmachine.info"));
                tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                        "iu.machines_work_energy_type_eu"));
                tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProgress.getMaxValue());

            }
        } else if (EnumDoubleElectricMachine.SUNNARIUM_PANEL == type) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.solarium_energy_sink.info"));
                tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                        "iu.machines_work_energy_type_eu"));
                tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                        ".machines_work_energy_type_se"));
                tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProgress.getMaxValue());

            }
        } else {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                        "iu.machines_work_energy_type_eu"));
                tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());
            }
        }
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
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

        if (sound) {
            if (getSound() == null) {
                return;
            }
            if (soundEvent == 0) {
                this.getWorld().playSound(null, this.getBlockPos(), getSound(), SoundSource.BLOCKS, 1F, 1);
            } else if (soundEvent == 1) {
                new PacketStopSound(getWorld(), this.getBlockPos());
                this.getWorld().playSound(null, this.getBlockPos(), EnumSound.InterruptOne.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
            } else {
                new PacketStopSound(getWorld(), this.getBlockPos());
            }
        }
    }

    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    public void setRecipeOutput(MachineRecipe output) {
        this.output = output;
    }

    public void onUpdate() {
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("sound", this.sound);
        return nbttagcompound;
    }


    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            inputSlotA.load();
            this.getOutput();
        }


    }

    public void onUnloaded() {
        super.onUnloaded();


    }


    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    public ContainerMenuDoubleElectricMachine getGuiContainer(Player entityPlayer) {
        return new ContainerMenuDoubleElectricMachine(entityPlayer, this, type);
    }


    @Override
    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Processing,
                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage,
                EnumBlockEntityUpgrade.ItemExtract,
                EnumBlockEntityUpgrade.ItemInput
        );
    }


    public final float getChargeLevel() {
        return Math.min((float) this.energy.getEnergy() / (float) this.energy.getCapacity(), 1);
    }

    public double getProgress() {
        return componentProgress.getBar();
    }

    public int getMode() {
        return 0;
    }


}
