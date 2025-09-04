package com.denfop.api.otherenergies.cool;


import com.denfop.api.energy.networking.ConductorInfo;

public interface ICoolConductor extends ICoolAcceptor, ICoolEmitter {


    double getConductorBreakdownCold();


    void removeConductor();

    void update_render();

    InfoCable getCoolCable();

    void setCoolCable(InfoCable cable);

    ConductorInfo getCoolConductor();
}

