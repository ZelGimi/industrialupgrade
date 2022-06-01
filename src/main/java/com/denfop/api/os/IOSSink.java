package com.denfop.api.os;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public interface IOSSink extends IOSAcceptor {

    FluidTank getDemandedSE();

    double injectSE(EnumFacing var1, FluidStack var2, double var4);

    FluidStack getNeedFluid();

}
