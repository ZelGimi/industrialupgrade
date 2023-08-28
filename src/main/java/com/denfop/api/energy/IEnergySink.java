package com.denfop.api.energy;

public interface IEnergySink extends IEnergyAcceptor {

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    void addTick(double tick);

    double getTick();

    boolean isSink();

    double getDemandedEnergy();

    int getSinkTier();

    void receiveEnergy(double var2);

}
