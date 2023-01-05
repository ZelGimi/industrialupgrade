package com.denfop.api.heat;


public interface IHeatConductor extends IHeatAcceptor, IHeatEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownHeat();


    void removeInsulation();


    void removeConductor();

    void update_render();

}

