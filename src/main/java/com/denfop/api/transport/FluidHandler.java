package com.denfop.api.transport;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class FluidHandler implements IFluidHandler {


    /**
     * @return
     */
    @Override
    public int getTanks() {
        return 0;
    }

    /**
     * @param tank Tank to query.
     * @return
     */
    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return null;
    }

    /**
     * @param tank Tank to query.
     * @return
     */
    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    /**
     * @param tank  Tank to query for validity
     * @param stack Stack to test with for validity
     * @return
     */
    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    /**
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
     * @param action   If SIMULATE, fill will only be simulated.
     * @return
     */
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    /**
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return
     */
    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    /**
     * @param maxDrain Maximum amount of fluid to drain.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return
     */
    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }
}
