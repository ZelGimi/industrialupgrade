package com.denfop.api.energy.interfaces;

public interface Dual extends EnergySink, EnergySource {

    double getPerEnergy1();

    double getPastEnergy1();

    void setPastEnergy1(double pastEnergy);

    void addPerEnergy1(double setEnergy);

}
