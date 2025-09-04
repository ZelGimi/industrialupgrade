package com.denfop.api.otherenergies.heat;


import com.denfop.api.energy.networking.ConductorInfo;

public interface IHeatConductor extends IHeatAcceptor, IHeatEmitter {


    double getConductorBreakdownHeat();


    void removeConductor();

    void update_render();

    InfoCable getHeatCable();

    void setHeatCable(InfoCable cable);

    ConductorInfo getHeatConductor();
}

