package com.denfop.invslot;

import com.denfop.tiles.base.TileEntityInventory;
import net.minecraftforge.fluids.Fluid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InvSlotConsumableLiquidByList extends InvSlotConsumableLiquid {

    private final Set<Fluid> acceptedFluids;

    public InvSlotConsumableLiquidByList(
            TileEntityInventory base1,
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


    protected boolean acceptsLiquid(Fluid fluid) {
        return true;
    }

    protected Iterable<Fluid> getPossibleFluids() {
        return this.acceptedFluids;
    }

}
