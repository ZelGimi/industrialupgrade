package com.denfop.api.transport;

import net.minecraft.core.Direction;

import java.util.List;


public interface ITransportSink<T, E> extends ITransportAcceptor<T, E> {

    List<Integer> getDemanded(Direction facing);

    boolean isSink();

    List<Integer> getEnergyTickList();

    boolean isItemSink();

    boolean isFluidSink();

}
