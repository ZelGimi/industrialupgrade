package com.denfop.api.pressure;

public interface IPressureSink extends IPressureAcceptor {

    double getDemandedPressure();

    void receivedPressure(double var2);

    boolean needTemperature();

}
