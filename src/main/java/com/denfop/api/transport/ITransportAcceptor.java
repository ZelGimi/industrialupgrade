package com.denfop.api.transport;

import net.minecraft.util.EnumFacing;

public interface ITransportAcceptor<T, E> extends ITransportTile<T, E> {

    boolean acceptsFrom(ITransportEmitter<T, E> var1, EnumFacing var2);


}
