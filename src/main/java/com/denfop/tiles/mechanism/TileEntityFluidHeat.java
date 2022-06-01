package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.TileEntityBaseHeatMachine;

public class TileEntityFluidHeat extends TileEntityBaseHeatMachine {

    public TileEntityFluidHeat() {
        super("iu.Fluidheat.name", true);
    }

    @Override
    public double getOfferedHeat() {
        return this.temperature;
    }

}
