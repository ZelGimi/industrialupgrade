package com.denfop.api.cool;



import java.util.List;

public interface ICoolSink extends ICoolAcceptor {

    double getDemandedCool();

    void receivedCold(double var2);

    boolean needCooling();

    List<ICoolSource> getEnergyTickList();
}
