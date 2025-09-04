package com.denfop.blockentity.base;


import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.componets.*;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class BlockEntityBaseSunnariumMaker extends BlockEntityElectricMachine
        implements BlockEntityUpgrade {


    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    private final ComponentUpgrade componentUpgrades;
    public ComponentBaseEnergy sunenergy;
    public InventoryRecipes inputSlotA;
    public MachineRecipe output;

    public BlockEntityBaseSunnariumMaker(int energyPerTick, int length, int outputSlots, MultiBlockEntity block, BlockPos pos, BlockState state) {
        this(energyPerTick, length, outputSlots, 1, block, pos, state);
    }

    public BlockEntityBaseSunnariumMaker(int energyPerTick, int length, int outputSlots, int aDefaultTier, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(energyPerTick * length, 1, outputSlots, block, pos, state);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.output = null;
        this.sunenergy = this.addComponent(ComponentBaseEnergy
                .asBasicSink(EnergyType.SOLARIUM, this, 10000, 1));
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) length
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, length, energyPerTick));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }


    public void onLoaded() {
        super.onLoaded();
        if (!getLevel().isClientSide) {
            inputSlotA.load();
            this.getOutput();
        }

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.solarium_energy_sink.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate(
                    "iu.machines_work_energy_type_se"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());

        }
        super.addInformation(stack, tooltip);

    }


    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();


        return this.output;
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }


}
