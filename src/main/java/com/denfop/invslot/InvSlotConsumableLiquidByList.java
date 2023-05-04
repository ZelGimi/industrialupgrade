package com.denfop.invslot;

import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityGasGenerator;
import net.minecraftforge.fluids.Fluid;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class InvSlotConsumableLiquidByList extends InvSlotConsumableLiquid {

    private Set<Fluid> acceptedFluids;

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

    public InvSlotConsumableLiquidByList(TileEntityInventory base1, String name1, int count, Fluid fluidlist) {
        super(base1, name1, Access.I, count, InvSide.ANY, OpType.Drain);
        this.acceptedFluids = new HashSet<>(Collections.singletonList(fluidlist));
    }

    public InvSlotConsumableLiquidByList(TileEntityGasGenerator base1, String name1, int count, Fluid fluidlist, OpType OpType) {
        super(base1, name1, Access.I, count, InvSide.ANY, OpType);
        this.acceptedFluids = new HashSet<>(Collections.singletonList(fluidlist));
    }

    public Set<Fluid> getAcceptedFluids() {
        return acceptedFluids;
    }

    public void setAcceptedFluids(final Set<Fluid> acceptedFluids) {
        this.acceptedFluids = acceptedFluids;
    }

    protected boolean acceptsLiquid(Fluid fluid) {
        return true;
    }

    protected Iterable<Fluid> getPossibleFluids() {
        return this.acceptedFluids;
    }

}
