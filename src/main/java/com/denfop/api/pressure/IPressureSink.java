package com.denfop.api.pressure;

import java.util.List;

public interface IPressureSink extends IPressureAcceptor {

    double getDemandedPressure();

    void receivedPressure(double var2);

    boolean needTemperature();

    List<IPressureSource> getEnergyTickList();
}
