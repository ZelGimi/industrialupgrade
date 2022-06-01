package com.denfop.api.os;


public interface IOSConductor extends IOSAcceptor, IOSEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownEnergy();


    void removeInsulation();


    void removeConductor();

}

