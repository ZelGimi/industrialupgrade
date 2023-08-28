package com.denfop.api.transport;

import net.minecraft.util.EnumFacing;

import java.util.List;


public interface ITransportSink<T, E> extends ITransportAcceptor<T, E> {

    List<Integer> getDemanded();

    boolean isSink();

    List<T> getItemStackFromFacing(EnumFacing facing);

    boolean canAccept(EnumFacing facing);

    void removeFacing(EnumFacing facing);

    boolean canAdd(EnumFacing facing);

    List<EnumFacing> getFacingList();

}
