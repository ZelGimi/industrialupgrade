package com.denfop.api.energy.interfaces;

public interface EnergyController extends EnergyTile {

    boolean getWork();

    void work();

    void unload();

}
