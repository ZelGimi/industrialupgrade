package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IAdvEnergyTile extends IEnergyTile {

    TileEntity getTileEntity();

    BlockPos getBlockPos();

}
