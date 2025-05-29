package com.denfop.api.energy;


public interface IEnergyConductor extends IEnergyAcceptor, IEnergyEmitter {

    double getConductionLoss();

    double getConductorBreakdownEnergy();

    void removeConductor();

    InfoCable getCable();

    void setCable(InfoCable cable);

}
