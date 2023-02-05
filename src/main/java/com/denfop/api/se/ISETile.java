package com.denfop.api.se;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface ISETile {

    BlockPos getBlockPos();

    TileEntity getTile();

}
