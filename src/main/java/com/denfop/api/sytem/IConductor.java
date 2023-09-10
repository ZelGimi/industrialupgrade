package com.denfop.api.sytem;

import java.util.List;

public interface IConductor extends IEmitter, IAcceptor {


    double getConductorBreakdownEnergy(EnergyType energyType);


    void removeConductor();

    void update_render();

    EnergyType getEnergyType();

    boolean hasEnergies();

    List<EnergyType> getEnergies();

}
