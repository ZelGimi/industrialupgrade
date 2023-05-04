package com.denfop.invslot;

import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.tiles.base.IInventorySlotHolder;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InvSlotConsumableLiquidByListRemake extends InvSlotConsumableLiquid {

    private final Set<Fluid> acceptedFluids;

    public InvSlotConsumableLiquidByListRemake(
            IInventorySlotHolder<?> base1,
            String name1,
            Access access1,
            int count,
            InvSide preferredSide1,
            OpType opType,
            Fluid... fluidlist
    ) {
        super(base1, name1, access1, count, preferredSide1, opType);
        this.acceptedFluids = new HashSet<>(Arrays.asList(fluidlist));
    }

    public boolean acceptsLiquid(Fluid fluid) {
        return this.acceptedFluids.contains(fluid);
    }

    public Iterable<Fluid> getPossibleFluids() {
        return this.acceptedFluids;
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

}
