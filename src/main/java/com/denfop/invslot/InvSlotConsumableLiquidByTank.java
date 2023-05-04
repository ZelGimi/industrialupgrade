package com.denfop.invslot;

import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.tiles.base.IInventorySlotHolder;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Collections;

public class InvSlotConsumableLiquidByTank extends InvSlotConsumableLiquid {

    public final IFluidTank tank;

    public InvSlotConsumableLiquidByTank(
            IInventorySlotHolder<?> base1,
            String name1,
            Access access1,
            int count,
            InvSide preferredSide1,
            OpType opType,
            IFluidTank tank1
    ) {
        super(base1, name1, access1, count, preferredSide1, opType);
        this.tank = tank1;
    }

    protected boolean acceptsLiquid(Fluid fluid) {
        FluidStack fs = this.tank.getFluid();
        return fs == null || fs.getFluid() == fluid;
    }

    protected Iterable<Fluid> getPossibleFluids() {
        FluidStack fs = this.tank.getFluid();
        return fs == null ? null : Collections.singletonList(fs.getFluid());
    }

    public boolean processIntoTank(IFluidTank tank, InvSlotOutput outputSlot) {
        if (this.isEmpty()) {
            return false;
        } else {
            MutableObject<ItemStack> output = new MutableObject<>();
            boolean wasChange = false;
            if (this.transferToTank(
                    tank,
                    output,
                    true
            ) && (StackUtil.isEmpty(output.getValue()) || outputSlot.canAdd(output.getValue()))) {
                wasChange = this.transferToTank(tank, output, false);
                if (!StackUtil.isEmpty(output.getValue())) {
                    outputSlot.add(output.getValue());
                }
            }

            return wasChange;
        }
    }

}

