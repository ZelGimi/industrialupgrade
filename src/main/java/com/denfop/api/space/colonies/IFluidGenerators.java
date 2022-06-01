package com.denfop.api.space.colonies;

import net.minecraftforge.fluids.FluidStack;

public interface IFluidGenerators extends IGenerator {

    EnumFluidGenerators getType();

    FluidStack getFluidStack();

}
