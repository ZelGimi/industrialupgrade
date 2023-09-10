package com.denfop.api.energy;

public interface IEnergyConductor extends IEnergyAcceptor, IEnergyEmitter {

    void update_render();

    double getConductionLoss();

    double getConductorBreakdownEnergy();

    void removeConductor();

}
