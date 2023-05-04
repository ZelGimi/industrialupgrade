package com.denfop.invslot;

import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.tiles.base.IInventorySlotHolder;
import ic2.api.util.FluidContainerOutputMode;
import ic2.core.util.LiquidUtil;
import ic2.core.util.LiquidUtil.FluidOperationResult;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.apache.commons.lang3.mutable.MutableObject;

public class InvSlotConsumableLiquid extends InvSlotConsumable {

    private InvSlotConsumableLiquid.OpType opType;

    public InvSlotConsumableLiquid(IInventorySlotHolder<?> base1, String name1, int count) {
        this(base1, name1, Access.I, count, InvSide.ANY, InvSlotConsumableLiquid.OpType.Drain);
    }

    public InvSlotConsumableLiquid(
            IInventorySlotHolder<?> base1,
            String name1,
            Access access1,
            int count,
            InvSide preferredSide1,
            InvSlotConsumableLiquid.OpType opType1
    ) {
        super(base1, name1, access1, count, preferredSide1);
        this.opType = opType1;
    }

    public boolean accepts(ItemStack stack, final int index) {
        if (StackUtil.isEmpty(stack)) {
            return false;
        } else if (!LiquidUtil.isFluidContainer(stack)) {
            return false;
        } else {
            if (this.opType == InvSlotConsumableLiquid.OpType.Drain || this.opType == InvSlotConsumableLiquid.OpType.Both) {
                FluidStack containerFluid = null;
                if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                    ItemStack singleStack = StackUtil.copyWithSize(stack, 1);
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

            return (this.opType == InvSlotConsumableLiquid.OpType.Fill || this.opType == InvSlotConsumableLiquid.OpType.Both) && LiquidUtil.isFillableFluidContainer(
                    stack,
                    this.getPossibleFluids()
            );
        }
    }

    public FluidStack drain(Fluid fluid, int maxAmount, MutableObject<ItemStack> output, boolean simulate) {
        output.setValue(null);
        if (fluid != null && !this.acceptsLiquid(fluid)) {
            return null;
        } else if (this.opType != InvSlotConsumableLiquid.OpType.Drain && this.opType != InvSlotConsumableLiquid.OpType.Both) {
            return null;
        } else {
            ItemStack stack = this.get();
            if (StackUtil.isEmpty(stack)) {
                return null;
            } else {
                FluidOperationResult result = LiquidUtil.drainContainer(
                        stack,
                        fluid,
                        maxAmount,
                        FluidContainerOutputMode.EmptyFullToOutput
                );
                if (result == null) {
                    return null;
                } else if (fluid == null && !this.acceptsLiquid(result.fluidChange.getFluid())) {
                    return null;
                } else {
                    output.setValue(result.extraOutput);
                    if (!simulate) {
                        this.put(result.inPlaceOutput);
                    }

                    return result.fluidChange;
                }
            }
        }
    }

    public int fill(FluidStack fs, MutableObject<ItemStack> output, boolean simulate) {
        output.setValue(null);
        if (fs != null && fs.amount > 0) {
            if (this.opType != InvSlotConsumableLiquid.OpType.Fill && this.opType != InvSlotConsumableLiquid.OpType.Both) {
                return 0;
            } else {
                ItemStack stack = this.get();
                if (StackUtil.isEmpty(stack)) {
                    return 0;
                } else {
                    FluidOperationResult result = LiquidUtil.fillContainer(stack, fs, FluidContainerOutputMode.EmptyFullToOutput);
                    if (result == null) {
                        return 0;
                    } else {
                        output.setValue(result.extraOutput);
                        if (!simulate) {
                            this.put(result.inPlaceOutput);
                        }

                        return result.fluidChange.amount;
                    }
                }
            }
        } else {
            return 0;
        }
    }

    public boolean transferToTank(IFluidTank tank, MutableObject<ItemStack> output, boolean simulate) {
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
            if (this.transferToTank(tank, output, true) && (StackUtil.isEmpty(output.getValue()) || outputSlot.canAdd(
                    output.getValue()))) {
                wasChange = this.transferToTank(tank, output, false);
                if (!StackUtil.isEmpty(output.getValue())) {
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
            ) && (StackUtil.isEmpty(output.getValue()) || outputSlot.canAdd(output.getValue()))) {
                wasChange = this.transferFromTank(tank, output, false);
                if (!StackUtil.isEmpty(output.getValue())) {
                    outputSlot.add(output.getValue());
                }
            }

            return wasChange;
        } else {
            return false;
        }
    }

    public void setOpType(InvSlotConsumableLiquid.OpType opType1) {
        this.opType = opType1;
    }

    protected boolean acceptsLiquid(Fluid fluid) {
        return true;
    }

    protected Iterable<Fluid> getPossibleFluids() {
        return null;
    }

    public enum OpType {
        Drain,
        Fill,
        Both,
        None;

        OpType() {
        }
    }

}
