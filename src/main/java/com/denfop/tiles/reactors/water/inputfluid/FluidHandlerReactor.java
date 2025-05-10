package com.denfop.tiles.reactors.water.inputfluid;

import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FluidHandlerReactor implements IFluidHandler {

    private final List<Fluids> list;
    private final FluidTank[] fluidTankProperties;

    public FluidHandlerReactor(List<Fluids> list) {
        this.list = list;
        this.fluidTankProperties = new FluidTank[this.list.size()];
        for (int i = 0; i < list.size(); i++) {
            fluidTankProperties[i] = this.list.get(i).getAllTanks().iterator().next();
        }
    }









    @Override
    public int getTanks() {
        return fluidTankProperties.length;
    }


    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return fluidTankProperties[tank].getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidTankProperties[tank].getTankCapacity(0);
    }


    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction doFill) {
        for (int i = 1; i < this.list.size(); i++) {
            Fluids fluids = this.list.get(i);
            final int f = fluids.getAllTanks().iterator().next().fill(resource, doFill);
            if (f != 0) {
                return f;
            }
        }
        return !this.list.isEmpty() ? this.list.get(0).getAllTanks().iterator().next().fill(resource, doFill) : 0;
    }


    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction doDrain) {
        for (int i = 1; i < this.list.size(); i++) {
            Fluids fluids = this.list.get(i);
            FluidStack f = fluids.getAllTanks().iterator().next().drain(resource, doDrain);
            if (f != null) {
                return f;
            }
        }
        return !this.list.isEmpty() ? this.list.get(0).getAllTanks().iterator().next().drain(resource, doDrain) : FluidStack.EMPTY;

    }


    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction doDrain) {
        for (int i = 1; i < this.list.size(); i++) {
            Fluids fluids = this.list.get(i);
            FluidStack f = fluids.getAllTanks().iterator().next().drain(maxDrain, doDrain);
            if (f != null) {
                return f;
            }
        }
        return !this.list.isEmpty() ? this.list.get(0).getAllTanks().iterator().next().drain(maxDrain, doDrain) : FluidStack.EMPTY;

    }
}
