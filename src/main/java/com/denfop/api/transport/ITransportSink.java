package com.denfop.api.transport;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

import java.util.List;


public interface ITransportSink<T, E> extends ITransportAcceptor<T, E> {

    List<Integer> getDemanded(IItemHandler handler);

    boolean isSink();

    List<Integer> getEnergyTickList();

    boolean isItemSink();

    boolean isFluidSink();
}
