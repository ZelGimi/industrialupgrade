package com.denfop.api.recipe;

import ic2.api.recipe.IRecipeInput;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

public class InputFluid implements IInput {

    private final List<FluidStack> inputsfluid;

    public InputFluid(FluidStack... inputs) {
        this.inputsfluid = Arrays.asList(inputs);
    }

    @Override
    public List<IRecipeInput> getInputs() {
        return null;
    }

    @Override
    public boolean hasFluids() {
        return true;
    }

    @Override
    public FluidStack getFluid() {
        return this.inputsfluid.get(0);
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return this.inputsfluid;
    }


}
