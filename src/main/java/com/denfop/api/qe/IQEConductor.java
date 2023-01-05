package com.denfop.api.qe;


public interface IQEConductor extends IQEAcceptor, IQEEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownQuantumEnergy();


    void removeInsulation();


    void removeConductor();

    void update_render();

}

