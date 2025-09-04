package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.otherenergies.common.IDual;
import com.denfop.api.otherenergies.common.ISource;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.sound.AudioFixer;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.FluidName;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.utils.Timer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public class ComponentProcess extends AbstractComponent {

    protected final double defaultEnergyConsume;
    protected final int defaultOperationLength;
    protected double energyConsume;
    protected int operationLength;
    protected ComponentProgress componentProgress;
    protected InventoryRecipes invSlotRecipes;
    protected int operationsPerTick;
    protected int tick;
    protected InventoryOutput outputSlot;
    protected IUpdateTick updateTick;
    protected boolean hasTank = false;
    protected boolean hasAudio = false;
    protected HeatComponent heatComponent;
    protected CoolComponent coldComponent;
    protected ComponentBaseEnergy componentSE;
    Energy energy;
    private boolean audoFix;
    private Action action;
    private ComponentUpgrade componentUpgrade;
    private boolean instant;
    private boolean stack;
    private ComponentBaseEnergy componentRad;
    private boolean exp;
    private ComponentBaseEnergy componentExp;
    private Timer timer1 = new Timer(0, 0, 0);
    private Timer timer = null;

    public ComponentProcess(
            final BlockEntityInventory parent,
            int operationLength,
            double energyConsume
    ) {
        super(parent);
        this.defaultEnergyConsume = this.energyConsume = energyConsume;
        this.defaultOperationLength = this.operationLength = operationLength;
        this.operationsPerTick = 1;

    }

    public void setOverclockRates(InventoryUpgrade invSlotUpgrade) {

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
        this.componentProgress = this.getParent().getComp("com.denfop.componets.ComponentProgress");
        this.energy = this.getParent().getComp(Energy.class);
        this.heatComponent = this.getParent().getComp(HeatComponent.class);
        this.coldComponent = this.getParent().getComp(CoolComponent.class);
        this.componentSE = this.getParent().getComp("com.denfop.componets.ComponentBaseEnergysolarium");
        this.componentRad = this.getParent().getComp("com.denfop.componets.ComponentBaseEnergyradiation");
        if (this.exp) {
            this.componentExp = this.getParent().getComp("com.denfop.componets.ComponentBaseEnergyexperience");
        }
        this.audoFix = this.getParent() instanceof AudioFixer;
        this.componentUpgrade = this.getParent().getComp(ComponentUpgrade.class);
    }

    @Override
    public boolean onBlockActivated(final Player player, final InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem().equals(IUItem.canister.getItem())) {
            IFluidHandlerItem handler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((IFluidHandlerItem) stack.getItem().initCapabilities(stack, stack.getTag()));
            FluidStack fluid = handler.getFluidInTank(0);
            if (!fluid.isEmpty() && fluid.getFluid() == FluidName.fluidmotoroil.getInstance().get() && fluid.getAmount() >= 125 && (!timer1.canWork() || timer1.getBar() == 0) && (timer == null || !timer.canWork())) {
                this.timer = new Timer(0, 0, 35);
                handler.drain(125, IFluidHandler.FluidAction.EXECUTE);
                return true;
            }
        }
        return super.onBlockActivated(player, hand);
    }

    public void setSlotOutput(final InventoryOutput slotOutput) {
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

    public void setInvSlotRecipes(final InventoryRecipes invSlotRecipes) {
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
        int energy = 5;
        if (this.instant) {
            energy *= this.operationLength;
        }
        return componentSE == null || componentSE.getEnergy() > energy;
    }

    public boolean checkFluidRecipe() {
        return (!hasTank || this.updateTick.getRecipeOutput().getRecipe().input.getFluid() == null || (!this.invSlotRecipes
                .getTank()
                .getFluid().isEmpty() && this.updateTick.getRecipeOutput()
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

    public boolean checkRadiation(boolean consume) {
        if (componentRad == null) {
            return true;
        }
        if (componentRad.getDelegate() instanceof ISource && !(componentRad.getDelegate() instanceof IDual)) {
            return this.componentRad.getCapacity() - this.componentRad.getEnergy() >= 150;
        } else {
            if (this.updateTick.getRecipeOutput() == null) {
                return false;
            } else {
                final int amount = this.updateTick.getRecipeOutput().getRecipe().output.metadata.getInt("rad_amount");
                if (consume) {
                    this.componentRad.useEnergy(amount);
                }
                return this.componentRad.getEnergy() >= amount;
            }
        }
    }

    public boolean checkExp(boolean consume) {
        if (componentExp == null) {
            return true;
        }
        if (this.updateTick.getRecipeOutput() == null) {
            return false;
        } else {
            final int amount = this.updateTick.getRecipeOutput().getRecipe().output.metadata.getInt("exp");
            if (consume) {
                this.componentExp.useEnergy(amount);
            }
            return this.componentExp.getEnergy() >= amount;
        }
    }

    public boolean checkHeatRecipe() {
        if (this.heatComponent == null) {
            return true;
        }
        if (this.updateTick.getRecipeOutput().getRecipe().output.metadata.getShort(
                "temperature") <= this.heatComponent.getEnergy()) {
            return true;
        } else {
            if (!this.heatComponent.buffer.need) {
                this.heatComponent.buffer.need = true;
            }
        }
        return false;
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
        int size = 64;
        if (this.componentUpgrade != null) {
            if (this.componentUpgrade.isChange()) {
                this.instant = this.componentUpgrade.hasUpgrade(TypeUpgrade.INSTANT);
                this.stack = this.componentUpgrade.hasUpgrade(TypeUpgrade.STACK);
                this.componentUpgrade.setChange(false);
            }
        }
        if (this.instant) {
            energyConsume *= this.operationLength;
        }
        if (this.stack) {
            if (this.updateTick.getRecipeOutput() != null) {
                final List<Integer> list = this.updateTick.getRecipeOutput().getList();
                for (int i = 0; i < list.size(); i++) {
                    size = Math.min(
                            size,
                            this.invSlotRecipes.get(i).getCount() / list.get(i)
                    );
                }
                int count = this.outputSlot.get(0).isEmpty() ? this.updateTick
                        .getRecipeOutput()
                        .getRecipe().output.items
                        .get(0)
                        .getMaxStackSize() :
                        this.outputSlot.get(0).getMaxStackSize() - this.outputSlot.get(0).getCount();
                ItemStack outputStack = this.updateTick.getRecipeOutput().getRecipe().output.items.get(0);
                count = count / Math.max(outputStack.getCount(), 1);
                size = Math.min(size, count);
                size = Math.min(
                        size,
                        this.updateTick.getRecipeOutput().getRecipe().output.items.get(0).getMaxStackSize()
                );
                if (this.updateTick.getRecipeOutput().getRecipe().input.getFluid() != null) {
                    final int size1 = this.invSlotRecipes.getTank().getFluidAmount() / this.updateTick
                            .getRecipeOutput()
                            .getRecipe().input.getFluid().getAmount();
                    size = Math.min(size, size1);
                }
            }
        } else {
            size = 1;
        }
        if (this.parent.getLevel().getGameTime() % 20 == 0)
            if (this.updateTick.getRecipeOutput() != null && this.hasTank && this.updateTick.getRecipeOutput().getRecipe().input.getFluid() != null && !this.invSlotRecipes.getTank().isEmpty() && !this.invSlotRecipes.getTank().getFluid().isFluidEqual(this.updateTick.getRecipeOutput().getRecipe().input.getFluid())) {
                getOutput();
            }
        updateRecipe();
        energyConsume *= size;
        if (this.updateTick.getRecipeOutput() != null && this.energy.canUseEnergy(energyConsume) && !this.invSlotRecipes.isEmpty() && canAddItemStack() && checkRecipe() && checkExp(
                false) && checkFluidRecipe() && checkHeatRecipe() && checkSE() && checkRadiation(
                false) && this.invSlotRecipes.continue_process(this.updateTick.getRecipeOutput())) {
            if (this.heatComponent != null) {
                this.heatComponent.buffer.need = true;
            }
            if (!this.parent.getActive()) {
                this.parent.setActive(true);
            }
            if (this.componentProgress.getProgress() == 0 && this.hasAudio) {
                if (this.operationLength > this.defaultOperationLength * 0.1) {
                    if (this.audoFix) {
                        ((AudioFixer) this.getParent()).initiate(0);
                    }
                }
            }
            this.componentProgress.addProgress();
            if (timer != null && timer.canWork()) {
                this.componentProgress.addProgress();

            }
            if (action != null && action.needAction(TypeLoad.PROGRESS)) {
                action.doAction();
            }
            if (this.componentProgress.getMaxValue() != this.operationLength) {
                this.componentProgress.setMaxValue((short) this.operationLength);
            }
            if (this.componentSE != null) {
                int energy = 5;
                if (this.instant) {
                    energy *= this.operationLength;
                }
                this.componentSE.useEnergy(energy);
            }
            this.consumeEnergy();
            this.energy.useEnergy(energyConsume);
            if (this.instant) {
                this.componentProgress.setProgress((short) this.operationLength);
            }
            if (this.componentProgress.getProgress() >= this.operationLength) {
                this.componentProgress.cancellationProgress();
                this.consumeEnergy1();
                if (size > 1) {
                    operateWithMax(this.updateTick.getRecipeOutput(), size);
                } else {
                    operateWithMax(this.updateTick.getRecipeOutput());
                }
                if (action != null && action.needAction(TypeLoad.AFTER_PROGRESS)) {
                    action.doAction();
                }
                if (this.hasAudio) {
                    if (this.audoFix) {
                        ((AudioFixer) this.getParent()).initiate(2);
                    }
                }

            }
        } else {
            if (this.heatComponent != null && this.updateTick.getRecipeOutput() == null) {
                this.heatComponent.buffer.need = false;
            }
            onFailedProcess();
            if (componentProgress == null) {
                this.componentProgress = this.getParent().getComp("com.denfop.componets.ComponentProgress");
                this.energy = this.getParent().getComp(Energy.class);
                this.heatComponent = this.getParent().getComp(HeatComponent.class);
                this.coldComponent = this.getParent().getComp(CoolComponent.class);
                this.componentSE = this.getParent().getComp("com.denfop.componets.ComponentBaseEnergysolarium");
                this.audoFix = this.getParent() instanceof AudioFixer;
                this.componentUpgrade = this.getParent().getComp(ComponentUpgrade.class);

            }
            if (this.componentProgress.getProgress() != 0 && this.getParent().getActive() && this.hasAudio) {
                if (this.audoFix) {
                    ((AudioFixer) this.getParent()).initiate(1);
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

    public void onFailedProcess() {
    }

    public void consumeEnergy1() {
    }

    public boolean canAddItemStack() {
        return this.updateTick.getRecipeOutput().getRecipe().output.items.size() < 2 ? this.outputSlot.canAdd(
                this.updateTick
                        .getRecipeOutput()
                        .getRecipe()
                        .getOutput().items) : outputSlot.addWithoutIgnoring(updateTick.getRecipeOutput().getRecipe().output.items, true);
    }

    public void consumeEnergy() {
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
        this.outputSlot.addAll(processResult);
        checkRadiation(true);
        checkExp(true);
    }

    public void operateWithMax(MachineRecipe output, int size) {
        if (output.getRecipe() == null) {
            return;
        }
        int maxSize = 64;
        final List<Integer> list = this.updateTick.getRecipeOutput().getList();
        for (int i = 0; i < list.size(); i++) {
            maxSize = Math.min(
                    maxSize,
                    this.invSlotRecipes.get(i).getCount() / list.get(i)
            );
        }
        size = Math.min(this.getSESize(size), this.getRadiationSize(size));
        this.invSlotRecipes.consume(size, output);
        this.outputSlot.add(output.getRecipe().getOutput().items, size);
        this.consumeSE(size);
        this.consumeRadiation(size);
        if (maxSize == size) {
            this.updateTick.setRecipeOutput(null);
        }
    }

    public void operateWithMax(MachineRecipe output) {
        if (output.getRecipe() == null) {
            return;
        }
        int size = 64;
        int maxSize1;
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
        size = Math.min(this.getSESize(size), this.getRadiationSize(size));
        this.invSlotRecipes.consume(size, output);
        this.outputSlot.add(output.getRecipe().getOutput().items, size);
        this.consumeSE(size);
        this.consumeRadiation(size);
        if (maxSize == size) {
            this.updateTick.setRecipeOutput(null);
        }
    }

    protected void consumeSE(int size) {
        if (this.componentSE == null) {
            return;
        }

        this.componentSE.useEnergy(5 * size);
    }

    protected void consumeRadiation(int size) {
        if (this.componentRad == null) {
            return;
        }
        if (this.componentRad.delegate instanceof ISource) {
            this.componentRad.addEnergy(150 * size);
            return;
        }
        final int amount = this.updateTick.getRecipeOutput().getRecipe().output.metadata.getInt("rad_amount");

        this.componentRad.useEnergy(amount * size);
    }

    protected int getRadiationSize(int size) {
        if (this.componentRad == null) {
            return size;
        }
        if (this.componentRad.delegate instanceof ISource) {
            return (int) ((this.componentRad.getCapacity() - this.componentRad.getEnergy()) / 150);
        }
        final int amount = this.updateTick.getRecipeOutput().getRecipe().output.metadata.getInt("rad_amount");

        return (int) Math.min(size, this.componentRad.getEnergy() / amount);
    }

    protected int getSESize(int size) {
        if (this.componentSE == null) {
            return size;
        }


        return (int) Math.min(size, this.componentSE.getEnergy() / 5);
    }

    public void operate(MachineRecipe output) {
        if (output.getRecipe() == null) {
            return;
        }
        for (int i = 0; i < this.operationsPerTick; i++) {

            List<ItemStack> processResult = output.getRecipe().output.items;
            operateOnce(processResult);
            if ((!this.invSlotRecipes.continue_process(this.updateTick.getRecipeOutput()) || !this.outputSlot.canAdd(output.getRecipe().output.items)) || !checkRadiation(
                    false) || !checkExp(false)) {
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

    public void setExpSource() {
        this.exp = true;
    }

}
