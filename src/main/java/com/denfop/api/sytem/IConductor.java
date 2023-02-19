package com.denfop.api.sytem;

public interface IConductor extends IEmitter, IAcceptor {

    double getConductionLoss(EnergyType energyType);


    double getInsulationEnergyAbsorption(EnergyType energyType);


    double getInsulationBreakdownEnergy(EnergyType energyType);


    double getConductorBreakdownEnergy(EnergyType energyType);


    void removeInsulation(EnergyType energyType);


    void removeConductor();

    void update_render();

}
