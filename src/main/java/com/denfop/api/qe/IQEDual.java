package com.denfop.api.qe;

public interface IQEDual extends IQESink, IQESource {

    double getPerEnergy1();

    double getPastEnergy1();

    void setPastEnergy1(double pastEnergy);

    void addPerEnergy1(double setEnergy);

    void addTick1(double tick);

    double getTick1();

}