package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.blocks.FluidName;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.Timer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

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
    private Timer timer1 = new Timer(0, 0, 0);
    private Timer timer = null;

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

    @Override
    public boolean onBlockActivated(final Player player, final InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem().equals(IUItem.canister.getItem())) {
            IFluidHandlerItem handler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((IFluidHandlerItem) stack.getItem().initCapabilities(stack, stack.getTag()));
            FluidStack fluid = handler.getFluidInTank(0);
            if (fluid != FluidStack.EMPTY && fluid.getFluid() == FluidName.fluidsteam_oil.getInstance().get() && fluid.getAmount() >= 250 && (!timer1.canWork() || timer1.getBar() == 0) && (timer == null || !timer.canWork())) {
                this.timer = new Timer(0, 0, 40);
                handler.drain(250, IFluidHandler.FluidAction.EXECUTE);
                return true;
            }
        }
        return super.onBlockActivated(player, hand);
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
                .getFluid() != FluidStack.EMPTY && this.updateTick.getRecipeOutput()
                .getRecipe().input
                .getFluid() != null && this.invSlotRecipes.getTank()
                .getFluid()
                .getFluid()
                .equals(this.updateTick.getRecipeOutput()
                        .getRecipe().input
                        .getFluid()
                        .getFluid()) && this.invSlotRecipes.getTank().getFluidAmount() >= this.updateTick.getRecipeOutput()
                .getRecipe().input
                .getFluid().getAmount()));
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
            if (timer != null && timer.canWork()) {
                this.componentProgress.addProgress();

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
        if (this.parent.getActive()) {
            if (this.parent.getWorld().getGameTime() % 20 == 0) {
                if (this.timer != null && this.timer.canWork()) {
                    this.timer.work();
                    if (!this.timer.canWork()) {
                        timer1 = new Timer(0, 0, 10);
                    }
                }
                if (timer1.canWork()) {
                    timer1.work();
                }
            }
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
        int count = this.outputSlot.get(0).isEmpty() ? output.getRecipe().output.items.get(0).getMaxStackSize() :
                this.outputSlot.get(0).getMaxStackSize() - this.outputSlot.get(0).getCount();
        ItemStack outputStack = this.updateTick.getRecipeOutput().getRecipe().output.items.get(0);
        count = count / Math.max(outputStack.getCount(), 1);
        size = Math.min(size, count);
        size = Math.min(size, this.updateTick.getRecipeOutput().getRecipe().output.items.get(0).getMaxStackSize());
        if (this.updateTick.getRecipeOutput().getRecipe().input.getFluid() != null) {
            final int size1 = this.invSlotRecipes.getTank().getFluidAmount() / this.updateTick
                    .getRecipeOutput()
                    .getRecipe().input.getFluid().getAmount();
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
