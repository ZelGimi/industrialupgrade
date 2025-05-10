package com.denfop.tiles.quarry_earth;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DataPos {

    public final BlockPos pos;
    public final BlockState state;

    public DataPos(BlockPos pos, BlockState state) {
        this.pos = pos;
        this.state = state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

}
