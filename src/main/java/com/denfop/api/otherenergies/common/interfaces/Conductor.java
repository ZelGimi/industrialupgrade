package com.denfop.api.otherenergies.common.interfaces;


import com.denfop.api.energy.networking.ConductorInfo;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.otherenergies.common.InfoCable;

import java.util.List;

public interface Conductor extends Emitter, Acceptor {

    double getConductorBreakdownEnergy(EnergyType type);

    InfoCable getCable(EnergyType type);

    void setCable(EnergyType type, InfoCable cable);

    void removeConductor();

    EnergyType getEnergyType();

    boolean hasEnergies();

    List<EnergyType> getEnergies();

    ConductorInfo getInfo(EnergyType energyType);
}
