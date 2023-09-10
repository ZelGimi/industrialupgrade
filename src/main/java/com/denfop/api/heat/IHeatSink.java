package com.denfop.api.heat;

public interface IHeatSink extends IHeatAcceptor {

    double getDemandedHeat();

    void receivedHeat(double var2);

    boolean needTemperature();

}
