package com.denfop.api;


import com.denfop.tiles.base.TileEntityElectricMachine;
import net.minecraftforge.fluids.FluidStack;

public interface ITemperature {

    short getTemperature();

    void setTemperature(short temperature);

    short getMaxTemperature();

    boolean isFluidTemperature();

    FluidStack getFluid();

    TileEntityElectricMachine getTile();

    boolean reveiver();

    boolean requairedTemperature();
}
