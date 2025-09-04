package com.denfop.api.otherenergies.common.interfaces;

public interface Dual extends Sink, Source {

    double getPerEnergy1();

    double getPastEnergy1();

    void setPastEnergy1(double pastEnergy);

    void addPerEnergy1(double setEnergy);

    void addTick1(double tick);

    double getTick1();

}
