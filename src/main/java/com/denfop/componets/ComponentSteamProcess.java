package com.denfop.componets;

import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ComponentSteamProcess extends AbstractComponent {

    protected final double defaultEnergyConsume;
    protected final int defaultOperationLength;
    private final int pressure;
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
    ComponentSteamEnergy advEnergy;
    PressureComponent pressureComponent;
    private boolean audoFix;
    private Action action;

    public ComponentSteamProcess(
            final TileEntityInventory parent,
            int operationLength,
            double energyConsume, int pressure
    ) {
        super(parent);
        this.defaultEnergyConsume = this.energyConsume = energyConsume;
        this.defaultOperationLength = this.operationLength = operationLength;
        this.pressure = pressure;
        this.operationsPerTick = 1;

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
    public void onLoaded() {
        super.onLoaded();
        this.componentProgress = this.getParent().getComp("com.denfop.componets.ComponentProgress");
        this.advEnergy = this.getParent().getComp(ComponentSteamEnergy.class);
        this.pressureComponent = this.getParent().getComp(PressureComponent.class);
        this.audoFix = this.getParent() instanceof IAudioFixer;
        this.heatComponent = this.getParent().getComp(HeatComponent.class);

    }

    public void setSlotOutput(final InvSlotOutput slotOutput) {
        this.outputSlot = slotOutput;
    }

    public double getDefaultEnergyConsume() {
        return defaultEnergyConsume;
    }

    public double getEnergyConsume() {
        return energyConsume;
    }

    public int getOperationsPerTick() {
        return operationsPerTick;
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


    public void updateRecipe() {

    }

    public boolean checkRecipe() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        double energyConsume = this.energyConsume;


        updateRecipe();
        energyConsume *= 1;
        if (this.updateTick.getRecipeOutput() != null && this.advEnergy.canUseEnergy(energyConsume) && !this.invSlotRecipes.isEmpty() && this.outputSlot.canAdd(
                this.updateTick
                        .getRecipeOutput()
                        .getRecipe()
                        .getOutput().items) && checkHeatRecipe() && checkRecipe() && pressureComponent.getEnergy() == pressure && checkFluidRecipe() && this.invSlotRecipes.continue_process(
                this.updateTick.getRecipeOutput())) {
            if (this.heatComponent != null) {
                this.heatComponent.need = true;
            }
            if (!this.parent.getActive()) {
                this.parent.setActive(true);
            }
            if (this.componentProgress.getProgress() == 0 && this.hasAudio) {
                if (this.operationLength > this.defaultOperationLength * 0.1) {
                    if (this.audoFix) {
                        ((IAudioFixer) this.getParent()).initiate(0);
                    }
                }
            }
            this.componentProgress.addProgress();
            if (action != null && action.needAction(TypeLoad.PROGRESS)) {
                action.doAction();
            }
            if (this.componentProgress.getMaxValue() != this.operationLength) {
                this.componentProgress.setMaxValue((short) this.operationLength);
            }
            this.consumeEnergy();
            this.advEnergy.useEnergy(energyConsume);
            if (this.componentProgress.getProgress() >= this.operationLength) {
                this.componentProgress.cancellationProgress();
                operateWithMax(this.updateTick.getRecipeOutput());
                if (action != null && action.needAction(TypeLoad.AFTER_PROGRESS)) {
                    action.doAction();
                }
                if (this.hasAudio) {
                    if (this.audoFix) {
                        ((IAudioFixer) this.getParent()).initiate(2);
                    }
                }

            }
        } else {
            if (this.heatComponent != null && this.updateTick.getRecipeOutput() == null) {
                this.heatComponent.need = false;
            }
            if (componentProgress == null) {
                this.heatComponent = this.getParent().getComp(HeatComponent.class);

                this.componentProgress = this.getParent().getComp("com.denfop.componets.ComponentProgress");
                this.advEnergy = this.getParent().getComp(ComponentSteamEnergy.class);
                this.pressureComponent = this.getParent().getComp(PressureComponent.class);
                this.audoFix = this.getParent() instanceof IAudioFixer;

            }
            if (this.componentProgress.getProgress() != 0 && this.getParent().getActive() && this.hasAudio) {
                if (this.audoFix) {
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
    }

    public void consumeEnergy() {
    }


    public void operateOnce(List<ItemStack> processResult) {
        this.invSlotRecipes.consume();
        this.outputSlot.add(processResult);
    }


    public void operateWithMax(MachineRecipe output) {
        if (output.getRecipe() == null) {
            return;
        }
        int size = 64;
        final List<Integer> list = this.updateTick.getRecipeOutput().getList();
        if (this.invSlotRecipes.getRecipe().workbench()) {
            size = 1;
        } else {
            for (int i = 0; i < list.size(); i++) {

                size = Math.min(
                        size,
                        this.invSlotRecipes.get(i).getCount() / list.get(i)
                );
            }
        }
        int maxSize = size;
        int count = this.outputSlot.get().isEmpty() ? output.getRecipe().output.items.get(0).getMaxStackSize() :
                this.outputSlot.get().getMaxStackSize() - this.outputSlot.get().getCount();
        ItemStack outputStack = this.updateTick.getRecipeOutput().getRecipe().output.items.get(0);
        count = count / Math.max(outputStack.getCount(), 1);
        size = Math.min(size, count);
        size = Math.min(size, this.updateTick.getRecipeOutput().getRecipe().output.items.get(0).getItem().getItemStackLimit());
        if (this.updateTick.getRecipeOutput().getRecipe().input.getFluid() != null) {
            final int size1 = this.invSlotRecipes.getTank().getFluidAmount() / this.updateTick
                    .getRecipeOutput()
                    .getRecipe().input.getFluid().amount;
            size = Math.min(size, size1);
        }
        size = Math.min(size, this.operationsPerTick);
        this.invSlotRecipes.consume(size, output);
        this.outputSlot.add(output.getRecipe().getOutput().items, size);
        if (maxSize == size) {
            this.updateTick.setRecipeOutput(null);
        }
    }


    public void operate(MachineRecipe output) {
        if (output.getRecipe() == null) {
            return;
        }
        for (int i = 0; i < this.operationsPerTick; i++) {

            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(processResult);
            if ((!this.invSlotRecipes.continue_process(this.updateTick.getRecipeOutput()) || !this.outputSlot.canAdd(output.getRecipe().output.items))) {
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
