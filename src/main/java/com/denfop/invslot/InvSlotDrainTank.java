package com.denfop.invslot;

import com.denfop.api.inv.IAdvInventory;
import net.minecraftforge.fluids.Fluid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InvSlotDrainTank extends InvSlotFluid {

    private final Set<Fluid> acceptedFluids;

    public InvSlotDrainTank(
            IAdvInventory<?> base1,
            TypeItemSlot typeItemSlot1,
            int count,
            TypeFluidSlot typeFluidSlot,
            Fluid... fluidlist
    ) {
        super(base1, typeItemSlot1, count, typeFluidSlot);
        this.acceptedFluids = new HashSet<>(Arrays.asList(fluidlist));
    }

    public boolean acceptsLiquid(Fluid fluid) {
        return this.acceptedFluids.contains(fluid);
    }

    public Iterable<Fluid> getPossibleFluids() {
        return this.acceptedFluids;
    }


}
