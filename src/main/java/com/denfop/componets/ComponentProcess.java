package com.denfop.componets;

import com.denfop.IUCore;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ComponentProcess extends AbstractComponent {

    protected final double defaultEnergyConsume;
    protected final int defaultOperationLength;
    protected double energyConsume;
    protected int operationLength;
    protected ComponentProgress componentProgress;
    protected InvSlotRecipes invSlotRecipes;
    protected int operationsPerTick;
    protected int tick;
    protected InvSlotOutput outputSlot;
    protected IUpdateTick updateTick;
    protected boolean hasTank = false;
    protected boolean hasAudio = false;
    protected HeatComponent heatComponent;
    protected CoolComponent coldComponent;
    protected ComponentBaseEnergy componentSE;
    AdvEnergy advEnergy;
    private boolean audoFix;
    private Action action;

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

        this.operationsPerTick = invSlotUpgrade.getOperationsPerTick1(this.defaultOperationLength);
        this.operationLength = invSlotUpgrade.getOperationLength1(this.defaultOperationLength);
        this.energyConsume = invSlotUpgrade.getEnergyDemand1(this.defaultEnergyConsume);
        getOperationLength();
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.componentProgress = this.getParent().getComp(ComponentProgress.class);
        this.advEnergy = this.getParent().getComp(AdvEnergy.class);
        this.heatComponent = this.getParent().getComp(HeatComponent.class);
        this.coldComponent = this.getParent().getComp(CoolComponent.class);
        this.componentSE = this.getParent().getComp("com.denfop.componets.ComponentBaseEnergysolarium");
        this.audoFix = this.getParent() instanceof IAudioFixer;
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

    public boolean checkSE() {
        return componentSE == null || componentSE.getEnergy() > 5;
    }

    public boolean checkFluidRecipe() {
        return (!hasTank || this.updateTick.getRecipeOutput().getRecipe().input.getFluid() == null || (this.invSlotRecipes
                .getTank()
                .getFluid() != null && this.updateTick.getRecipeOutput()
                .getRecipe().input
                .getFluid() != null && this.invSlotRecipes.getTank()
                .getFluid()
                .getFluid()
                .equals(this.updateTick.getRecipeOutput()
                        .getRecipe().input
                        .getFluid()
                        .getFluid()) && this.invSlotRecipes.getTank().getFluidAmount() >= this.updateTick.getRecipeOutput()
                .getRecipe().input
                .getFluid().amount));
    }

    public boolean checkHeatRecipe() {
        if (this.heatComponent == null) {
            return true;
        }
        if (this.updateTick.getRecipeOutput().getRecipe().output.metadata.getShort(
                "temperature") <= this.heatComponent.getEnergy()) {
            return true;
        } else {
            if (!this.heatComponent.need) {
                this.heatComponent.need = true;
            }
        }
        return false;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.updateTick.getRecipeOutput() != null && this.advEnergy.canUseEnergy(energyConsume) && !this.invSlotRecipes.isEmpty() && this.outputSlot.canAdd(
                this.updateTick
                        .getRecipeOutput()
                        .getRecipe()
                        .getOutput().items) && checkFluidRecipe() && checkHeatRecipe() && checkSE()) {
            if (this.heatComponent != null) {
                this.heatComponent.need = true;
            }
            if (!this.parent.getActive()) {
                this.parent.setActive(true);
            }
            if (this.componentProgress.getProgress() == 0 && this.hasAudio) {
                if (!this.audoFix) {
                    IUCore.network.get(true).initiateTileEntityEvent(this.getParent(), 0, true);
                } else {
                    ((IAudioFixer) this.getParent()).initiate(0);
                }
            }
            this.componentProgress.addProgress();
            if (action != null && action.needAction(TypeLoad.PROGRESS)) {
                action.doAction();
            }
            if (this.componentProgress.getMaxValue() != this.operationLength) {
                this.componentProgress.setMaxValue((short) this.operationLength);
            }
            if (this.componentSE != null) {
                this.componentSE.useEnergy(5);
            }
            this.advEnergy.useEnergy(energyConsume);
            if (this.componentProgress.getProgress() >= this.operationLength) {
                this.componentProgress.cancellationProgress();
                operate(this.updateTick.getRecipeOutput());
                if (action != null && action.needAction(TypeLoad.AFTER_PROGRESS)) {
                    action.doAction();
                }
                if (this.hasAudio) {
                    if (!this.audoFix) {
                        IUCore.network.get(true).initiateTileEntityEvent(this.getParent(), 2, true);
                    } else {
                        ((IAudioFixer) this.getParent()).initiate(2);
                    }
                }
            }
        } else {
            if (this.heatComponent != null && this.updateTick.getRecipeOutput() == null) {
                this.heatComponent.need = false;
            }
            if (this.componentProgress.getProgress() != 0 && this.getParent().getActive() && this.hasAudio) {
                if (!this.audoFix) {
                    IUCore.network.get(true).initiateTileEntityEvent(this.getParent(), 1, true);
                } else {
                    ((IAudioFixer) this.getParent()).initiate(1);
                }
            }
            if (this.updateTick.getRecipeOutput() == null) {
                this.componentProgress.cancellationProgress();
            }
            if (this.getParent().getActive()) {
                this.getParent().setActive(false);
            }
        }
        if (action != null && action.needAction(TypeLoad.ALWAYS)) {
            action.doAction();
        }
        if (this.heatComponent != null) {
            this.heatComponent.useEnergy(1);
        }
        if (this.coldComponent != null) {
            this.tick++;
            if (!this.getParent().getActive()) {
                if (this.tick - 120 >= 0) {
                    this.coldComponent.useEnergy(0.35);
                    this.tick = 0;
                }
            } else {
                if (this.tick - 240 >= 0) {
                    this.coldComponent.useEnergy(0.35);
                    this.tick = 0;
                }
            }
            this.getOperationDefaultLength();
        }
    }

    public void getOperationDefaultLength() {
        if (this.coldComponent == null) {
            return;
        }
        final double fillratio = this.coldComponent.getFillRatio();
        if (fillratio >= 0.75 && fillratio < 1) {
            this.operationLength = this.defaultOperationLength * 2;
        }
        if (fillratio >= 1) {
            this.operationLength = Integer.MAX_VALUE;
        }
        if (fillratio >= 0.5 && fillratio < 0.75) {
            this.operationLength = (int) (this.defaultOperationLength * 1.5);
        }
    }

    public void getOperationLength() {
        if (this.coldComponent == null) {
            return;
        }
        final double fillratio = this.coldComponent.getFillRatio();
        if (fillratio >= 0.75 && fillratio < 1) {
            this.operationLength *= 2;
        }
        if (fillratio >= 1) {
            this.operationLength = Integer.MAX_VALUE;
        }
        if (fillratio >= 0.5 && fillratio < 0.75) {
            this.operationLength *= 1.5;
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

    public void setAction(Action action) {
        this.action = action;
    }

}
