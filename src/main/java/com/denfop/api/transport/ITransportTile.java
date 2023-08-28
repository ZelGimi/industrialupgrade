package com.denfop.api.transport;

import net.minecraft.util.math.BlockPos;

public interface ITransportTile<T, E> {

    E getHandler();

    BlockPos getBlockPos();

}
