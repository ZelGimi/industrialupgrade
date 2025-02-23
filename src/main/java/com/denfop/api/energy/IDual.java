package com.denfop.api.energy;

public interface IDual extends IEnergySink, IEnergySource {

    double getPerEnergy1();

    double getPastEnergy1();

    void setPastEnergy1(double pastEnergy);

    void addPerEnergy1(double setEnergy);

}
