package com.denfop.api.recipe;

import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface IRecipeInputFluidStack {

    List<FluidStack> getItemStack();

    boolean matched(FluidStack stack);

}
