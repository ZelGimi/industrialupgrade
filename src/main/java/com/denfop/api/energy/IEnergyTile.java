package com.denfop.api.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IEnergyTile {

    TileEntity getTileEntity();

    BlockPos getBlockPos();

}
