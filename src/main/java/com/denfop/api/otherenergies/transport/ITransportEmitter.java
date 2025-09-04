package com.denfop.api.otherenergies.transport;

import net.minecraft.core.Direction;

public interface ITransportEmitter<T, E> extends ITransportTile<T, E> {

    boolean emitsTo(ITransportAcceptor<T, E> var1, Direction var2);

}
