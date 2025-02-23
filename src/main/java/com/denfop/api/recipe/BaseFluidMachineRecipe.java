package com.denfop.api.recipe;

import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class BaseFluidMachineRecipe {

    public final IInputFluid input;
    public final List<FluidStack> output_fluid;
    private final RecipeOutput output;

    public BaseFluidMachineRecipe(IInputFluid input, List<FluidStack> output) {
        this.input = input;
        this.output_fluid = output;
        this.output = null;
    }

    public BaseFluidMachineRecipe(IInputFluid input, RecipeOutput output) {
        this.input = input;
        this.output_fluid = new ArrayList<>();
        this.output = output;
    }

    public BaseFluidMachineRecipe(IInputFluid input, RecipeOutput output, List<FluidStack> output_fluid) {
        this.input = input;
        this.output_fluid = output_fluid;
        this.output = output;
    }

    public boolean matches(List<FluidStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            if (this.input.getInputs().get(i).getFluid().equals(stacks.get(i).getFluid())) {
                return true;
            }
        }
        return false;
    }

    public RecipeOutput getOutput() {
        return this.output;
    }

    public List<FluidStack> getOutput_fluid() {
        return output_fluid;
    }

    public IInputFluid getInput() {
        return input;
    }

}
