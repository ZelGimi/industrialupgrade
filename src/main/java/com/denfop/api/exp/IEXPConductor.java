package com.denfop.api.exp;


public interface IEXPConductor extends IEXPAcceptor, IEXPEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownExperienceEnergy();


    void removeInsulation();


    void removeConductor();

    void update_render();

}

