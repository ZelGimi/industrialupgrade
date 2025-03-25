package com.denfop.api.transport;

import net.minecraft.util.EnumFacing;

import java.util.List;

public interface ITransportSource<T, E> extends ITransportEmitter<T, E> {

    TransportItem<T> getOffered(int type, EnumFacing facing);

    void draw(T var, int col, EnumFacing facing);

    boolean isItem();

    boolean isFluid();

    boolean isSource();


}
