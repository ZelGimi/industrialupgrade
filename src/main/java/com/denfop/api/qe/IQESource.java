package com.denfop.api.qe;

public interface IQESource extends IQEEmitter {

    double getOfferedQE();

    void drawQE(double var1);

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    boolean isSource();

}
