package com.denfop.api.otherenergies.pressure;


public interface IPressureConductor extends IPressureAcceptor, IPressureEmitter {


    void removeConductor();

    void update_render();

}

