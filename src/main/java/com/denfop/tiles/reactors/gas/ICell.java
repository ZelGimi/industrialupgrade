package com.denfop.tiles.reactors.gas;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.Fluid;

public interface ICell extends IMultiElement {

    Fluids getFluids();

    Fluids.InternalFluidTank getTank();

    void setFluid(Fluid fluid);

}
