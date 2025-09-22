package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.inv.IAdvInventory;
import net.minecraftforge.fluids.Fluid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InventoryDrainTank extends InventoryFluid implements ITypeSlot {

    private final Set<Fluid> acceptedFluids;

    public InventoryDrainTank(
            IAdvInventory<?> base1,
            TypeItemSlot typeItemSlot1,
            int count,
            TypeFluidSlot typeFluidSlot,
            Fluid... fluidlist
    ) {
        super(base1, typeItemSlot1, count, typeFluidSlot);
        this.acceptedFluids = new HashSet<>(Arrays.asList(fluidlist));
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.BUCKET;
    }

    public boolean acceptsLiquid(Fluid fluid) {
        return this.acceptedFluids.contains(fluid);
    }

    public Iterable<Fluid> getPossibleFluids() {
        return this.acceptedFluids;
    }


}
