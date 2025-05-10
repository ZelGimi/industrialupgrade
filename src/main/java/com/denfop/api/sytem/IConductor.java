package com.denfop.api.sytem;


import java.util.List;

public interface IConductor extends IEmitter, IAcceptor {

    double getConductorBreakdownEnergy(EnergyType type);

    InfoCable getCable(EnergyType type);

    void setCable(EnergyType type, InfoCable cable);

    void removeConductor();

    EnergyType getEnergyType();

    boolean hasEnergies();

    List<EnergyType> getEnergies();

}
