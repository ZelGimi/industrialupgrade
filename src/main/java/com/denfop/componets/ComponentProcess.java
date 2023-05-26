package com.denfop.componets;

import com.denfop.IUCore;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.IC2;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ComponentProcess extends AbstractComponent {

    private double energyConsume;
    private int operationLength;
    private final double defaultEnergyConsume;
    private final int defaultOperationLength;
    ComponentProgress componentProgress;
    InvSlotRecipes invSlotRecipes;

    AdvEnergy advEnergy;
    private int operationsPerTick;

    private InvSlotOutput outputSlot;

    IUpdateTick updateTick;

    boolean hasTank = false;

    boolean hasAudio = false;

    public ComponentProcess(
            final TileEntityInventory parent,
            int operationLength,
            double energyConsume
    ) {
        super(parent);
        this.defaultEnergyConsume = this.energyConsume = energyConsume;
        this.defaultOperationLength = this.operationLength = this.operationsPerTick = operationLength;


    }

    public void setOverclockRates(InvSlotUpgrade invSlotUpgrade) {
        this.operationsPerTick = invSlotUpgrade.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = invSlotUpgrade.getOperationLength(this.defaultOperationLength);
        this.energyConsume = invSlotUpgrade.getEnergyDemand(this.defaultEnergyConsume);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.componentProgress = this.getParent().getComp(ComponentProgress.class);
        this.advEnergy = this.getParent().getComp(AdvEnergy.class);
    }

    public void setSlotOutput(final InvSlotOutput slotOutput) {
        this.outputSlot = slotOutput;
    }

    public double getDefaultEnergyConsume() {
        return defaultEnergyConsume;
    }

    public int getDefaultOperationLength() {
        return defaultOperationLength;
    }

    public void setHasTank(final boolean hasTank) {
        this.hasTank = hasTank;
    }

    public void setHasAudio(final boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public void setInvSlotRecipes(final InvSlotRecipes invSlotRecipes) {
        this.invSlotRecipes = invSlotRecipes;
        this.updateTick = invSlotRecipes.getTile();

    }

    public MachineRecipe getOutput() {
        this.updateTick.setRecipeOutput(this.invSlotRecipes.process());
        return this.updateTick.getRecipeOutput();
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.updateTick.getRecipeOutput() != null && this.advEnergy.canUseEnergy(energyConsume) && !this.invSlotRecipes.isEmpty() && this.outputSlot.canAdd(
                this.updateTick.getRecipeOutput().getRecipe().getOutput().items) && (!hasTank || (this.invSlotRecipes
                .getTank()
                .getFluid() != null && this.invSlotRecipes.getTank()
                .getFluid()
                .getFluid()
                .equals(this.updateTick.getRecipeOutput()
                        .getRecipe().input
                        .getFluid()
                        .getFluid()) && this.invSlotRecipes.getTank().getFluidAmount() >= this.updateTick.getRecipeOutput()
                .getRecipe().input
                .getFluid().amount))) {
            if (!this.parent.getActive()) {
                this.parent.setActive(true);
            }
            if (this.componentProgress.getProgress() == 0 && this.hasAudio) {
                IUCore.network.get(true).initiateTileEntityEvent(this.getParent(), 0, true);
            }
            this.componentProgress.addProgress();
            if (this.componentProgress.getMaxValue() != this.operationLength) {
                this.componentProgress.setMaxValue((short) this.operationLength);
            }
            this.advEnergy.useEnergy(energyConsume);
            if (this.componentProgress.getProgress() >= this.operationLength) {
                this.componentProgress.cancellationProgress();
                operate(this.updateTick.getRecipeOutput());
                if (this.hasAudio) {
                    IC2.network.get(true).initiateTileEntityEvent(this.getParent(), 2, true);
                }
            }
        } else {
            if (this.componentProgress.getProgress() != 0 && this.getParent().getActive() && this.hasAudio) {
                IC2.network.get(true).initiateTileEntityEvent(this.getParent(), 1, true);
            }
            if (this.updateTick.getRecipeOutput() == null) {
                this.componentProgress.cancellationProgress();
            }
            if (this.getParent().getActive()) {
                this.getParent().setActive(false);
            }
        }
    }

    public void operateOnce(List<ItemStack> processResult) {
        this.invSlotRecipes.consume();
        this.outputSlot.add(processResult);
    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {
            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(processResult);
            if (!this.invSlotRecipes.continue_process(this.updateTick.getRecipeOutput()) || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                getOutput();
                break;
            }

            if (this.updateTick.getRecipeOutput() == null) {
                break;
            }
        }
    }

}
