package com.denfop.api.cool;


public interface ICoolConductor extends ICoolAcceptor, ICoolEmitter {


    double getConductorBreakdownCold();


    void removeConductor();

    void update_render();

    InfoCable getCoolCable();

    void setCoolCable(InfoCable cable);
}

