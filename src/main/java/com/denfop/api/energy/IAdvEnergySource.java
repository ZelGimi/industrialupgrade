package com.denfop.api.energy;

public interface IAdvEnergySource extends IEnergyEmitter {

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    boolean isSource();

    double getOfferedEnergy();

    void drawEnergy(double var1);

    int getSourceTier();

}
