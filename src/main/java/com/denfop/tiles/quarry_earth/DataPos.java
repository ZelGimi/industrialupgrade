package com.denfop.tiles.quarry_earth;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class DataPos {

    public final BlockPos pos;
    public final IBlockState state;

    public DataPos(BlockPos pos, IBlockState state) {
        this.pos = pos;
        this.state = state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockState getState() {
        return state;
    }

}
