package com.denfop.api.otherenergies.cool;


import java.util.List;

public interface ICoolSink extends ICoolAcceptor {

    double getDemandedCool();

    void receivedCold(double var2);

    boolean needCooling();

    List<ICoolSource> getEnergyTickList();
}
