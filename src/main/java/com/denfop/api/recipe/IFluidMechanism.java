package com.denfop.api.recipe;

import net.minecraftforge.fluids.FluidTank;

import java.util.List;

public interface IFluidMechanism {

    List<FluidTank> getInputTank();

    List<FluidTank> getOutputTank();

}
