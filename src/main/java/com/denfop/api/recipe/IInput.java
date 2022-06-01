package com.denfop.api.recipe;

import ic2.api.recipe.IRecipeInput;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IInput {

    List<IRecipeInput> getInputs();

    boolean hasFluids();

    FluidStack getFluid();

    List<FluidStack> getFluidInputs();

}
