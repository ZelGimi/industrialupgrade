package com.denfop.api.heat;


public interface IHeatConductor extends IHeatAcceptor, IHeatEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownEnergy();


    void removeInsulation();


    void removeConductor();

}

