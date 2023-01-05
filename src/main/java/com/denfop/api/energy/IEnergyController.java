package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergyTile;

public interface IEnergyController extends IEnergyTile {

    boolean getWork();

    void work();

    void unload();

}
