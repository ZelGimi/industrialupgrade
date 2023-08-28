package com.denfop.api.cool;

public interface ICoolSink extends ICoolAcceptor {

    double getDemandedCool();

    void receivedCold(double var2);

    boolean needCooling();

}
