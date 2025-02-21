package com.denfop.items;

import net.minecraft.block.state.IBlockState;

public class DataOres {

    private final int color;
    private final IBlockState blockState;

    public DataOres(IBlockState blockState, int color) {
        this.blockState = blockState;
        this.color = color;
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    public int getColor() {
        return color;
    }

}
