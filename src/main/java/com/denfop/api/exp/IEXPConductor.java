package com.denfop.api.exp;


public interface IEXPConductor extends IEXPAcceptor, IEXPEmitter {

    double getConductionLoss();


    double getInsulationEnergyAbsorption();


    double getInsulationBreakdownEnergy();


    double getConductorBreakdownEnergy();


    void removeInsulation();


    void removeConductor();

}

