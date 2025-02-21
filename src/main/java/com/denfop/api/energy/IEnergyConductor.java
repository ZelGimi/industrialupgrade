package com.denfop.api.energy;

import net.minecraft.util.EnumFacing;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IEnergyConductor extends IEnergyAcceptor, IEnergyEmitter {

    double getConductionLoss();

    double getConductorBreakdownEnergy();

    void removeConductor();

    InfoCable getCable();

    void setCable(InfoCable cable);

}
