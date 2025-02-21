package com.denfop.tiles.cokeoven;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.FluidTank;

public interface IOutputFluid extends IMultiElement {

    FluidTank getFluidTank();

    Fluids getFluid();

}
