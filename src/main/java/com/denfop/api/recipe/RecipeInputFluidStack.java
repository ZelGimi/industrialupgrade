package com.denfop.api.recipe;

import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class RecipeInputFluidStack implements IRecipeInputFluidStack {

    private final List<FluidStack> input;

    public RecipeInputFluidStack(FluidStack input) {
        this.input = Collections.singletonList(input);

    }

    @Override
    public List<FluidStack> getItemStack() {
        return input;
    }

    @Override
    public boolean matched(final FluidStack stack) {
        for (FluidStack input : getItemStack()) {
            if (FluidStack.isSameFluid(stack, input)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeInputFluidStack that = (RecipeInputFluidStack) o;
        for (FluidStack input : getItemStack()) {
            for (FluidStack input1 : that.getItemStack()) {
                if (FluidStack.isSameFluid(input, input1)) {
                    return true;
                }

            }
        }
        return false;
    }


}
