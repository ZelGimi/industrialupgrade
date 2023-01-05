package com.denfop.api.transport;

public interface ITransportConductor<T, E> extends ITransportAcceptor<T, E>, ITransportEmitter<T, E> {


    boolean isOutput();

    void update_render();

    boolean isItem();

}


