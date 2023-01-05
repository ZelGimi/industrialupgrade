package com.denfop.api.se;


public interface ISEConductor extends ISEAcceptor, ISEEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownSolariumEnergy();


    void removeInsulation();


    void removeConductor();

    void update_render();

}

