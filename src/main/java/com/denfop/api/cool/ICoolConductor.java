package com.denfop.api.cool;


public interface ICoolConductor extends ICoolAcceptor, ICoolEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownCold();


    void removeInsulation();


    void removeConductor();

    void update_render();


}

