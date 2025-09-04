package com.denfop.blockentity.cokeoven;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IOutputFluid extends MultiBlockElement {

    FluidTank getFluidTank();

    Fluids getFluid();

}
