package com.denfop.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public interface IMineral {

    public IBlockState getStateMeta(int meta);

    Block getBlock();

}
