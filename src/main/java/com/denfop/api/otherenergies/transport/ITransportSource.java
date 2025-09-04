package com.denfop.api.otherenergies.transport;

import net.minecraft.core.Direction;

public interface ITransportSource<T, E> extends ITransportEmitter<T, E> {

    TransportItem<T> getOffered(int type, Direction facing);

    void draw(T var, int col, Direction facing);

    boolean isItem();

    boolean isFluid();

    boolean isSource();


}
