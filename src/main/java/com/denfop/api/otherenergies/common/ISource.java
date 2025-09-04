package com.denfop.api.otherenergies.common;

public interface ISource extends IEmitter {

    double canProvideEnergy();

    void extractEnergy(double var1);

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    boolean isSource();

}
