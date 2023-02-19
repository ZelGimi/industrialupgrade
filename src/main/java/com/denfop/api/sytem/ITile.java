package com.denfop.api.sytem;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface ITile {

    BlockPos getBlockPos();

    TileEntity getTile();

}
