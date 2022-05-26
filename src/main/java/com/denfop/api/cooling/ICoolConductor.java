package com.denfop.api.cooling;


public interface ICoolConductor extends ICoolAcceptor, ICoolEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownEnergy();


    void removeInsulation();


    void removeConductor();

}

