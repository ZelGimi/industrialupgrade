package com.denfop.api.energy;

public interface IEnergyController extends IAdvEnergyTile {

    boolean getWork();

    void work();

    void unload();

}
