package com.denfop.tiles.reactors.water.inputfluid;

import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidHandlerReactor implements IFluidHandler {

    private final List<Fluids> list;
    private final IFluidTankProperties[] fluidTankProperties;

    public FluidHandlerReactor(List<Fluids> list) {
        this.list = list;
        this.fluidTankProperties = new IFluidTankProperties[this.list.size()];
        for(int i =0; i < list.size();i++){
            fluidTankProperties[i] = this.list.get(i).getAllTanks().iterator().next().getTankProperties()[0];
        }
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return fluidTankProperties;
    }

    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
        for(int i =1; i < this.list.size();i++){
            Fluids fluids = this.list.get(i);
            final int f = fluids.getAllTanks().iterator().next().fill(resource, doFill);
            if(f != 0)
                return f;
        }
        return this.list.get(0).getAllTanks().iterator().next().fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        for(int i =1; i < this.list.size();i++){
           Fluids fluids = this.list.get(i);
            FluidStack f =  fluids.getAllTanks().iterator().next().drain(resource,doDrain);
           if(f != null)
               return f;
        }
        return this.list.get(0).getAllTanks().iterator().next().drain(resource,doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
        for(int i =1; i < this.list.size();i++){
            Fluids fluids = this.list.get(i);
            FluidStack f =  fluids.getAllTanks().iterator().next().drain(maxDrain,doDrain);
            if(f != null)
                return f;
        }
        return this.list.get(0).getAllTanks().iterator().next().drain(maxDrain,doDrain);
    }

}
