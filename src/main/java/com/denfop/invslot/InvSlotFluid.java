package com.denfop.invslot;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.apache.commons.lang3.mutable.MutableObject;

public class InvSlotFluid extends InvSlot {

    private TypeFluidSlot typeFluidSlot;

    public InvSlotFluid(IAdvInventory<?> base1, int count) {
        this(base1, TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
    }

    public InvSlotFluid(
            IAdvInventory<?> base1,
            TypeItemSlot typeItemSlot1,
            int count,
            TypeFluidSlot typeFluidSlot1
    ) {
        super(base1, typeItemSlot1, count);
        this.typeFluidSlot = typeFluidSlot1;
    }

    public void setTypeFluidSlot(final TypeFluidSlot typeFluidSlot) {
        this.typeFluidSlot = typeFluidSlot;
    }

    public boolean accepts(ItemStack stack, final int index) {
        if (ModUtils.isEmpty(stack)) {
            return false;
        } else if (FluidUtil.getFluidHandler(stack) == null) {
            return false;
        } else {
            if (this.typeFluidSlot == TypeFluidSlot.INPUT || this.typeFluidSlot == TypeFluidSlot.INPUT_OUTPUT) {
                FluidStack containerFluid = null;
                if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                    ItemStack singleStack = ModUtils.setSize(stack, 1);
                    IFluidHandlerItem handler = singleStack.getCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                            null
                    );
                    if (handler != null) {
                        containerFluid = handler.drain(2147483647, false);
                    }
                }

                if (containerFluid != null && containerFluid.amount > 0 && this.acceptsLiquid(containerFluid.getFluid())) {
                    return true;
                }
            }

            return (this.typeFluidSlot == TypeFluidSlot.OUTPUT || this.typeFluidSlot == TypeFluidSlot.INPUT_OUTPUT) && FluidUtil.getFluidHandler(
                    stack) != null && FluidUtil.getFluidContained(
                    stack) == null;
        }
    }


    public FluidStack drain(Fluid fluid, int maxAmount, MutableObject<ItemStack> output, boolean simulate) {
        output.setValue(null);
        if (fluid != null && !this.acceptsLiquid(fluid)) {
            return null;
        } else if (this.typeFluidSlot != TypeFluidSlot.INPUT && this.typeFluidSlot != TypeFluidSlot.INPUT_OUTPUT) {
            return null;
        } else {
            ItemStack stack = this.get().copy();
            stack.setCount(1);
            if (ModUtils.isEmpty(stack)) {
                return null;
            } else {
                IFluidHandlerItem handler = stack.getCapability(
                        CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                        null
                );
                FluidStack fs;
                if (fluid == null) {
                    fs = handler.drain(maxAmount, true);
                } else {
                    fs = handler.drain(new FluidStack(fluid, maxAmount), true);
                }
                if (fs != null && fs.amount > 0) {

                    ItemStack inPlace;
                    inPlace = handler.getContainer();


                    assert fs.amount > 0;

                    if (!this.acceptsLiquid(fs.getFluid())) {
                        return null;
                    } else {
                        output.setValue(inPlace);
                        if (!simulate) {
                            this.get().shrink(1);
                        }

                        return fs;
                    }
                } else {
                    return null;
                }
            }
        }
    }

    public int fill(FluidStack fs, MutableObject<ItemStack> output, boolean simulate) {
        output.setValue(null);
        if (fs != null && fs.amount > 0) {
            if (this.typeFluidSlot != TypeFluidSlot.OUTPUT && this.typeFluidSlot != TypeFluidSlot.INPUT_OUTPUT) {
                return 0;
            } else {
                ItemStack stack = this.get().copy();
                stack.setCount(1);
                if (ModUtils.isEmpty(stack)) {
                    return 0;
                } else {
                    IFluidHandlerItem handler = stack.getCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                            null
                    );
                    FluidStack fsChange = fs.copy();
                    int amount = handler.fill(fsChange, true);
                    if (amount <= 0) {
                        return 0;
                    } else {
                        fsChange.amount = amount;
                        FluidStack fillTestFs = fs.copy();
                        fillTestFs.amount = Integer.MAX_VALUE;
                        handler.fill(fillTestFs, false);
                        stack = handler.getContainer();


                        ItemStack inPlace;
                        inPlace = stack;
                        output.setValue(inPlace);
                        if (!simulate) {
                            this.get().shrink(1);
                        }

                        return fsChange.amount;
                    }
                }
            }
        } else {
            return 0;
        }
    }

    public boolean transferToTank(IFluidTank tank, MutableObject<ItemStack> output, boolean simulate) {
        if (this.isEmpty()) {
            return false;
        }
        int space = tank.getCapacity();
        Fluid fluidRequired = null;
        FluidStack tankFluid = tank.getFluid();
        if (tankFluid != null) {
            space -= tankFluid.amount;
            fluidRequired = tankFluid.getFluid();
        }

        FluidStack fluid = this.drain(fluidRequired, space, output, true);
        if (fluid == null) {
            return false;
        } else {
            int amount = tank.fill(fluid, !simulate);
            if (amount <= 0) {
                return false;
            } else {
                if (!simulate) {
                    this.drain(fluidRequired, amount, output, false);
                }

                return true;
            }
        }
    }

    public boolean transferFromTank(IFluidTank tank, MutableObject<ItemStack> output, boolean simulate) {
        if (this.isEmpty()) {
            return false;
        }
        FluidStack tankFluid = tank.drain(tank.getFluidAmount(), false);
        if (tankFluid != null && tankFluid.amount > 0) {
            int amount = this.fill(tankFluid, output, simulate);
            if (amount <= 0) {
                return false;
            } else {
                if (!simulate) {
                    tank.drain(amount, true);
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public boolean processIntoTank(IFluidTank tank, InvSlotOutput outputSlot) {
        if (this.isEmpty()) {
            return false;
        } else {
            MutableObject<ItemStack> output = new MutableObject<>();
            boolean wasChange = false;
            if (this.transferToTank(tank, output, true) && (ModUtils.isEmpty(output.getValue()) || outputSlot.canAdd(
                    output.getValue()))) {
                wasChange = this.transferToTank(tank, output, false);
                if (!ModUtils.isEmpty(output.getValue())) {
                    outputSlot.add(output.getValue());
                }
            }

            return wasChange;
        }
    }

    public boolean processFromTank(IFluidTank tank, InvSlotOutput outputSlot) {
        if (!this.isEmpty() && tank.getFluidAmount() > 0) {
            MutableObject<ItemStack> output = new MutableObject<>();
            boolean wasChange = false;
            if (this.transferFromTank(
                    tank,
                    output,
                    true
            ) && (ModUtils.isEmpty(output.getValue()) || outputSlot.canAdd(output.getValue()))) {
                wasChange = this.transferFromTank(tank, output, false);
                if (!ModUtils.isEmpty(output.getValue())) {
                    outputSlot.add(output.getValue());
                }
            }

            return wasChange;
        } else {
            return false;
        }
    }

    protected boolean acceptsLiquid(Fluid fluid) {
        return true;
    }

    protected Iterable<Fluid> getPossibleFluids() {
        return null;
    }

    public enum TypeFluidSlot {
        INPUT,
        OUTPUT,
        INPUT_OUTPUT


    }

}
