package com.denfop.api.sytem;

public interface ISink extends IAcceptor {

    double getDemanded();

    void receivedEnergy(double var2);

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    void addTick(double tick);

    double getTick();

    boolean isSink();

}
