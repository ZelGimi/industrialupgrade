package com.denfop.api.recipe;

import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

public class InputFluid implements IInputFluid {

    private final List<FluidStack> inputsfluid;

    public InputFluid(FluidStack... inputs) {
        this.inputsfluid = Arrays.asList(inputs);
    }

    public InputFluid(List<FluidStack> inputsfluid) {
        this.inputsfluid = inputsfluid;
    }

    @Override
    public List<FluidStack> getInputs() {
        return this.inputsfluid;
    }


}
