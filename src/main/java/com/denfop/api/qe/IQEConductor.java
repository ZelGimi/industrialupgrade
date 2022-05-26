package com.denfop.api.qe;


public interface IQEConductor extends IQEAcceptor, IQEEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownEnergy();


    void removeInsulation();


    void removeConductor();

}

