package com.denfop.api.energy.interfaces;


import com.denfop.api.energy.InfoCable;
import com.denfop.api.energy.networking.ConductorInfo;

public interface EnergyConductor extends EnergyAcceptor, EnergyEmitter {

    double getConductionLoss();

    double getConductorBreakdownEnergy();

    void removeConductor();

    InfoCable getCable();

    void setCable(InfoCable cable);

    ConductorInfo getInfo();
}
