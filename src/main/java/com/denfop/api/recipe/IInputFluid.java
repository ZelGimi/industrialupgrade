package com.denfop.api.recipe;


import com.denfop.recipe.IInputItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface IInputFluid {

    List<FluidStack> getInputs();

    IInputItemStack getStack();

}
