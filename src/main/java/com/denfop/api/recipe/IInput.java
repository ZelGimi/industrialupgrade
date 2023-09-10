package com.denfop.api.recipe;

import com.denfop.recipe.IInputItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IInput {

    List<IInputItemStack> getInputs();

    boolean hasFluids();

    FluidStack getFluid();

    List<FluidStack> getFluidInputs();

}
