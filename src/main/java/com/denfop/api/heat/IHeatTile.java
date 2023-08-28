package com.denfop.api.heat;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IHeatTile {

    BlockPos getBlockPos();

    TileEntity getTile();

}
