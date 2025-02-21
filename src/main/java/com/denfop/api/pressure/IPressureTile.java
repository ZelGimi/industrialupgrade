package com.denfop.api.pressure;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IPressureTile {

    BlockPos getBlockPos();

    TileEntity getTile();

}
