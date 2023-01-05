package com.denfop.api.recipe;

import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class BaseFluidMachineRecipe {

    public final IInputFluid input;
    public final List<FluidStack> output;

    public BaseFluidMachineRecipe(IInputFluid input, List<FluidStack> output) {
        this.input = input;
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

    public List<FluidStack> getOutput() {
        return this.output;
    }

}
