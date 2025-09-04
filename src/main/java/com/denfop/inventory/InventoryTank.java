package com.denfop.inventory;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import java.util.Collections;

public class InventoryTank extends InventoryFluid implements ITypeSlot {

    public final IFluidTank tank;

    public InventoryTank(
            CustomWorldContainer base1,
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

