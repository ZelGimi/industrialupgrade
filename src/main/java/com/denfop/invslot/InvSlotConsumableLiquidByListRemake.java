//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.invslot;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import net.minecraftforge.fluids.Fluid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InvSlotConsumableLiquidByListRemake extends InvSlotConsumableLiquid {
    private final Set<Fluid> acceptedFluids;

    public InvSlotConsumableLiquidByListRemake(IInventorySlotHolder<?> base1, String name1, int count, Fluid... fluidlist) {
        super(base1, name1, count);
        this.acceptedFluids = new HashSet(Arrays.asList(fluidlist));
    }

    public InvSlotConsumableLiquidByListRemake(IInventorySlotHolder<?> base1, String name1, Access access1, int count, InvSide preferredSide1, OpType opType, Fluid... fluidlist) {
        super(base1, name1, access1, count, preferredSide1, opType);
        this.acceptedFluids = new HashSet(Arrays.asList(fluidlist));
    }

    public boolean acceptsLiquid(Fluid fluid) {
        return this.acceptedFluids.contains(fluid);
    }

    public Iterable<Fluid> getPossibleFluids() {
        return this.acceptedFluids;
    }
}
