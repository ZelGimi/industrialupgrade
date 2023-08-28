package com.denfop.api.energy;

public interface IEnergyController extends IEnergyTile {

    boolean getWork();

    void work();

    void unload();

}
