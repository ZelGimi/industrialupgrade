package com.denfop.api.transport;


import net.minecraft.core.Direction;

public interface ITransportAcceptor<T, E> extends ITransportTile<T, E> {

    boolean acceptsFrom(ITransportEmitter<T, E> var1, Direction var2);


}
