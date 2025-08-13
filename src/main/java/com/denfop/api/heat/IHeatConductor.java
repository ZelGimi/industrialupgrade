package com.denfop.api.heat;


import com.denfop.api.energy.ConductorInfo;

public interface IHeatConductor extends IHeatAcceptor, IHeatEmitter {


    double getConductorBreakdownHeat();


    void removeConductor();

    void update_render();

    InfoCable getHeatCable();

    void setHeatCable(InfoCable cable);

    ConductorInfo getHeatConductor();
}

