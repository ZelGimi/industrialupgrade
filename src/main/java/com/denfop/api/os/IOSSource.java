package com.denfop.api.os;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public interface IOSSource extends IOSEmitter {

    FluidTank getOfferedSE();

    void drawOS(FluidStack var1);

    FluidStack getOfferedFluid();

}
