package com.denfop.tiles.base;

import com.denfop.Localization;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.componets.*;
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotDischarge;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileTripleElectricMachine extends TileStandartMachine
        implements IUpgradableBlock, IUpdateTick {

    public final InvSlotDischarge dischargeSlot;
    public final InvSlotRecipes inputSlotA;
    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    protected final String name;
    protected final EnumTripleElectricMachine type;
    private final ComponentUpgrade componentUpgrades;
    public double energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public MachineRecipe output;
    protected boolean sound = true;


    public TileTripleElectricMachine(
            int energyPerTick, int length, int outputSlots, String name,
            EnumTripleElectricMachine type, IMultiTileBlock block, BlockPos pos, BlockState state
    ) {
        super(outputSlots, block, pos, state);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.name = name;
        this.inputSlotA = new InvSlotRecipes(this, type.recipe_name, this);
        this.type = type;
        this.dischargeSlot = new InvSlotDischarge(this, InvSlot.TypeItemSlot.INPUT, 1, false);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) length
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, length, energyPerTick));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.energy = this.addComponent(Energy
                .asBasicSink(this, (double) energyPerTick * length, 1)
                .addManagedSlot(this.dischargeSlot));
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));

    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (type == EnumTripleElectricMachine.ADV_ALLOY_SMELTER) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.heatmachine.info"));
                tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                        "iu.machines_work_energy_type_eu"));
                tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());

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
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
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


    public MachineRecipe getOutput() {


        this.output = this.inputSlotA.process();

        return this.output;
    }

    public ContainerTripleElectricMachine getGuiContainer(Player entityPlayer) {
        return new ContainerTripleElectricMachine(entityPlayer, this, type);
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


    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (!getEnable()) {
            return;
        }
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

    public final float getChargeLevel() {
        return Math.min((float) this.energy.getEnergy() / (float) this.energy.getCapacity(), 1);
    }


    public String getInventoryName() {
        return this.name;
    }


}
