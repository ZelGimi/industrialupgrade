package com.denfop.api.otherenergies.heat;

import java.util.List;

public interface IHeatSink extends IHeatAcceptor {

    double getDemandedHeat();

    void receivedHeat(double var2);

    boolean needTemperature();

    List<IHeatSource> getEnergyTickList();
}
