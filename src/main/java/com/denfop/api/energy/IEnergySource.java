package com.denfop.api.energy;

public interface IEnergySource extends IEnergyEmitter {

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    boolean isSource();

    double canExtractEnergy();

    void extractEnergy(double var1);

    int getSourceTier();

}
