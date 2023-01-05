package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergySource;

public interface IAdvEnergySource extends IEnergySource {

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    boolean isSource();

}
