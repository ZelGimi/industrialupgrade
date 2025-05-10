package com.denfop.blocks;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface IMineral {

    public BlockState getStateMeta(int meta);

    Block getBlock();

}
