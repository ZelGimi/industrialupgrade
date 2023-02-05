package com.denfop.api.exp;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IEXPTile {

    BlockPos getBlockPos();

    TileEntity getTile();

}
