package com.denfop.tiles.mechanism;

import com.denfop.tiles.base.TileEntityBaseHeatMachine;

public class TileEntityElectricHeat extends TileEntityBaseHeatMachine {

    public TileEntityElectricHeat() {
        super("iu.Electricheat.name", false);
    }

    @Override
    public double getOfferedHeat() {
        return Math.min(4, this.temperature);
    }

}
