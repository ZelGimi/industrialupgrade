package com.denfop.api.sytem;

import java.util.List;

public interface IConductor extends IEmitter, IAcceptor {

    double getConductionLoss(EnergyType energyType);


    double getInsulationEnergyAbsorption(EnergyType energyType);


    double getInsulationBreakdownEnergy(EnergyType energyType);


    double getConductorBreakdownEnergy(EnergyType energyType);


    void removeInsulation(EnergyType energyType);


    void removeConductor();

    void update_render();

    EnergyType getEnergyType();

    boolean hasEnergies();

    List<EnergyType> getEnergies();

}
