package com.denfop.tiles.reactors.graphite;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.Fluid;

public interface ITank extends IMultiElement {

    Fluids getFluids();

    Fluids.InternalFluidTank getTank();

    void setFluid(Fluid fluid);

}
