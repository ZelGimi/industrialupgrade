package com.denfop.blockentity.cokeoven;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface IOutputFluid extends IMultiElement {

    FluidTank getFluidTank();

    Fluids getFluid();

}
