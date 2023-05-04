package com.denfop.api.energy;

public interface IAdvConductor extends IEnergyAcceptor, IEnergyEmitter {

    void update_render();

    double getConductionLoss();

    double getInsulationEnergyAbsorption();

    double getInsulationBreakdownEnergy();

    double getConductorBreakdownEnergy();

    void removeInsulation();

    void removeConductor();

}
