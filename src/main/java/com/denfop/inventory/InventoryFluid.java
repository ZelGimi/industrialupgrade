package com.denfop.inventory;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.apache.commons.lang3.mutable.MutableObject;

public class InventoryFluid extends Inventory {

    private TypeFluidSlot typeFluidSlot;

    public InventoryFluid(CustomWorldContainer base1, int count) {
        this(base1, TypeItemSlot.INPUT, count, TypeFluidSlot.INPUT);
    }

    public InventoryFluid(
            CustomWorldContainer base1,
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

    public boolean canPlaceItem(final int index, ItemStack stack) {
        ItemStack singleStack = ModUtils.setSize(stack, 1);
        IFluidHandlerItem handler = null;
        try {
            handler = singleStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((IFluidHandlerItem) singleStack.getItem().initCapabilities(singleStack, singleStack.getTag()));
        } catch (Exception e) {

        }
        if (ModUtils.isEmpty(singleStack)) {
            return false;
        } else if (handler == null) {
            return false;
        } else {
            if (this.typeFluidSlot == TypeFluidSlot.INPUT || this.typeFluidSlot == TypeFluidSlot.INPUT_OUTPUT) {
                FluidStack containerFluid;
                containerFluid = handler.drain(2147483647, IFluidHandler.FluidAction.SIMULATE);

                if (!containerFluid.isEmpty() && containerFluid.getAmount() > 0 && this.acceptsLiquid(containerFluid.getFluid())) {
                    return true;
                }
            }

            return (this.typeFluidSlot == TypeFluidSlot.OUTPUT || this.typeFluidSlot == TypeFluidSlot.INPUT_OUTPUT) && handler.getFluidInTank(0).isEmpty();
        }
    }


    public FluidStack drain(Fluid fluid, int maxAmount, MutableObject<ItemStack> output, boolean simulate) {
        output.setValue(null);
        if (fluid != Fluids.EMPTY && !this.acceptsLiquid(fluid)) {
            return null;
        } else if (this.typeFluidSlot != TypeFluidSlot.INPUT && this.typeFluidSlot != TypeFluidSlot.INPUT_OUTPUT) {
            return null;
        } else {
            ItemStack stack = this.get(0).copy();
            stack.setCount(1);
            if (ModUtils.isEmpty(stack)) {
                return null;
            } else {
                IFluidHandlerItem handler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((IFluidHandlerItem) stack.getItem().initCapabilities(stack, stack.getTag()));
                FluidStack fs;
                if (fluid == Fluids.EMPTY) {
                    fs = handler.drain(maxAmount, IFluidHandler.FluidAction.EXECUTE);
                } else {
                    fs = handler.drain(new FluidStack(fluid, Math.min(maxAmount, handler.getFluidInTank(0).getAmount())), IFluidHandler.FluidAction.EXECUTE);
                }
                if (!fs.isEmpty() && fs.getAmount() > 0) {

                    ItemStack inPlace;
                    inPlace = handler.getContainer();


                    assert fs.getAmount() > 0;

                    if (!this.acceptsLiquid(fs.getFluid())) {
                        return null;
                    } else {
                        output.setValue(inPlace);
                        if (!simulate) {
                            this.get(0).shrink(1);
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
        if (!fs.isEmpty() && fs.getAmount() > 0) {
            if (this.typeFluidSlot != TypeFluidSlot.OUTPUT && this.typeFluidSlot != TypeFluidSlot.INPUT_OUTPUT) {
                return 0;
            } else {
                ItemStack stack = this.get(0).copy();
                stack.setCount(1);
                if (ModUtils.isEmpty(stack)) {
                    return 0;
                } else {
                    IFluidHandlerItem handler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((IFluidHandlerItem) stack.getItem().initCapabilities(stack, stack.getTag()));
                    FluidStack fsChange = fs.copy();
                    int amount = handler.fill(fsChange, IFluidHandler.FluidAction.EXECUTE);
                    if (amount <= 0) {
                        return 0;
                    } else {
                        fsChange.setAmount(amount);
                        FluidStack fillTestFs = fs.copy();
                        fillTestFs.setAmount(Integer.MAX_VALUE);
                        handler.fill(fillTestFs, IFluidHandler.FluidAction.SIMULATE);
                        stack = handler.getContainer();


                        ItemStack inPlace;
                        inPlace = stack;
                        output.setValue(inPlace);
                        if (!simulate) {
                            this.get(0).shrink(1);
                        }

                        return fsChange.getAmount();
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
        Fluid fluidRequired = Fluids.EMPTY;
        FluidStack tankFluid = tank.getFluid();
        if (!tankFluid.isEmpty()) {
            space -= tankFluid.getAmount();
            fluidRequired = tankFluid.getFluid();
        }
        FluidStack fluid = this.drain(fluidRequired, space, output, true);
        if (fluid == null) {
            return false;
        } else {
            int amount = tank.fill(fluid, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
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
        FluidStack tankFluid = tank.drain(tank.getFluidAmount(), IFluidHandler.FluidAction.SIMULATE);
        if (!tankFluid.isEmpty() && tankFluid.getAmount() > 0) {
            int amount = this.fill(tankFluid, output, simulate);
            if (amount <= 0) {
                return false;
            } else {
                if (!simulate) {
                    tank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public boolean processIntoTank(IFluidTank tank, InventoryOutput outputSlot) {
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

    public boolean processFromTank(IFluidTank tank, InventoryOutput outputSlot) {
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
