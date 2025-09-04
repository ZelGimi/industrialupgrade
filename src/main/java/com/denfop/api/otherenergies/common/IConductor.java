package com.denfop.api.otherenergies.common;


import com.denfop.api.energy.networking.ConductorInfo;

import java.util.List;

public interface IConductor extends IEmitter, IAcceptor {

    double getConductorBreakdownEnergy(EnergyType type);

    InfoCable getCable(EnergyType type);

    void setCable(EnergyType type, InfoCable cable);

    void removeConductor();

    EnergyType getEnergyType();

    boolean hasEnergies();

    List<EnergyType> getEnergies();

    ConductorInfo getInfo(EnergyType energyType);
}
