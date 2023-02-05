package com.denfop.api.transport;

import net.minecraft.util.EnumFacing;

import java.util.List;

public interface ITransportConductor<T, E> extends ITransportAcceptor<T, E>, ITransportEmitter<T, E> {


    boolean isOutput();

    void update_render();

    boolean isItem();

    List<TypeSlots> getTypeSlotsFromFacing(EnumFacing facing, boolean input);

}


