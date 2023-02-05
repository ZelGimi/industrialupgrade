package com.denfop.api.qe;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IQETile {

    BlockPos getBlockPos();

    TileEntity getTile();

}
