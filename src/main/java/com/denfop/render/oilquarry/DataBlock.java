package com.denfop.render.oilquarry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;

public class DataBlock {

    IBakedModel state;
    private IBlockState blockState;

    public DataBlock(IBlockState blockState) {
        this.blockState = blockState;
    }

    public IBakedModel getState() {
        return state;
    }

    public void setState(final IBakedModel state) {
        this.state = state;
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(final IBlockState blockState) {
        this.blockState = blockState;
    }

}
