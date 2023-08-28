package com.denfop.api.cool;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface ICoolTile {

    BlockPos getBlockPos();

    TileEntity getTile();

}
