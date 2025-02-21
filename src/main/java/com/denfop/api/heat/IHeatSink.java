package com.denfop.api.heat;

import com.denfop.api.cool.CoolTick;
import com.denfop.api.cool.ICoolSource;

import java.util.List;

public interface IHeatSink extends IHeatAcceptor {

    double getDemandedHeat();

    void receivedHeat(double var2);

    boolean needTemperature();

    List<IHeatSource> getEnergyTickList();
}
