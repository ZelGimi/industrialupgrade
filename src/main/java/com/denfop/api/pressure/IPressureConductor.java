package com.denfop.api.pressure;


public interface IPressureConductor extends IPressureAcceptor, IPressureEmitter {


    void removeConductor();

    void update_render();

}

