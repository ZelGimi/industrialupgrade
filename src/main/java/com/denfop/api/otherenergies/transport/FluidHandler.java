package com.denfop.api.otherenergies.transport;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class FluidHandler implements IFluidHandler {


    @Override
    public int getTanks() {
        return 0;
    }


    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return null;
    }


    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }


    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }


    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }


    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }


    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }
}
