package com.denfop.api.otherenergies.common;

public interface IDual extends ISink, ISource {

    double getPerEnergy1();

    double getPastEnergy1();

    void setPastEnergy1(double pastEnergy);

    void addPerEnergy1(double setEnergy);

    void addTick1(double tick);

    double getTick1();

}
