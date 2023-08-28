package com.denfop.api.transport;

import net.minecraft.util.EnumFacing;

public interface ITransportEmitter<T, E> extends ITransportTile<T, E> {

    boolean emitsTo(ITransportAcceptor<T, E> var1, EnumFacing var2);

}
