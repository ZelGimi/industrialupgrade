package com.denfop.blockentity.base;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.componets.*;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityBasePlasticCreator extends BlockEntityElectricLiquidTankInventory
        implements IUpdateTick, BlockEntityUpgrade {

    public final InventoryFluidByList fluidSlot;

    public final InventoryOutput outputSlot1;
    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    private final ComponentUpgrade componentUpgrades;

    public InventoryRecipes inputSlotA;
    public MachineRecipe output;

    public BlockEntityBasePlasticCreator(int energyPerTick, int length, int aDefaultTier, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(energyPerTick * length, aDefaultTier, 12, Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER), block, pos, state);
        this.outputSlot1 = new InventoryOutput(this, 1);
        this.fluidSlot = new InventoryFluidByList(this, 1, net.minecraft.world.level.material.Fluids.WATER);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.output = null;
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) length
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, length, energyPerTick));
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setHasTank(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());
        }
        super.addInformation(stack, tooltip);

    }


    public void onLoaded() {
        super.onLoaded();


        inputSlotA.load();
        this.getOutput();
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        MutableObject<ItemStack> output1 = new MutableObject<>();
        if (this.fluidSlot.transferToTank(
                this.fluidTank,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot1.canAdd(output1.getValue()))) {
            this.fluidSlot.transferToTank(this.fluidTank, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot1.add(output1.getValue());
            }
        }

    }


    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Processing, EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.ItemExtract, EnumBlockEntityUpgrade.ItemInput
        );
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

    @Override
    public SoundEvent getSound() {
        return EnumSound.plastic.getSoundEvent();
    }

}
