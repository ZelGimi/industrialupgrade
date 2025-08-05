package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.inv.IAdvInventory;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;

import java.util.Collections;

public class InvSlotTank extends InvSlotFluid implements ITypeSlot {

    public final IFluidTank tank;

    public InvSlotTank(
            IAdvInventory<?> base1,
            TypeItemSlot typeItemSlot1,
            int count,
            TypeFluidSlot typeFluidSlot,
            IFluidTank tank1
    ) {
        super(base1, typeItemSlot1, count, typeFluidSlot);
        this.tank = tank1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.BUCKET;
    }

    protected boolean acceptsLiquid(Fluid fluid) {
        FluidStack fs = this.tank.getFluid();
        return fs.isEmpty() || fs.getFluid() == fluid;
    }

    protected Iterable<Fluid> getPossibleFluids() {
        FluidStack fs = this.tank.getFluid();
        return fs.isEmpty() ? null : Collections.singletonList(fs.getFluid());
    }


}

