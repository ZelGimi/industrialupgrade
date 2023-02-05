package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergySink;

public interface IAdvEnergySink extends IEnergySink, IAdvEnergyTile {

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    void addTick(double tick);

    double getTick();

    boolean isSink();

}
