package com.denfop.render.oilquarry;


import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

public class DataBlock {

    BakedModel state;
    private BlockState blockState;

    public DataBlock(BlockState blockState) {
        this.blockState = blockState;
    }

    public BakedModel getState() {
        return state;
    }

    public void setState(final BakedModel state) {
        this.state = state;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(final BlockState blockState) {
        this.blockState = blockState;
    }

}
