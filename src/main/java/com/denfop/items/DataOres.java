package com.denfop.items;


import net.minecraft.world.level.block.state.BlockState;

public class DataOres {

    private final int color;
    private final BlockState blockState;

    public DataOres(BlockState blockState, int color) {
        this.blockState = blockState;
        this.color = color;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public int getColor() {
        return color;
    }

}
