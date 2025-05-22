package com.denfop.api.heat;


public interface IHeatConductor extends IHeatAcceptor, IHeatEmitter {


    double getConductorBreakdownHeat();


    void removeConductor();

    void update_render();

    InfoCable getHeatCable();

    void setHeatCable(InfoCable cable);

}

