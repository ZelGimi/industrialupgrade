package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileQuarryPipe extends TileEntityBlock {

    public TileQuarryPipe(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockBaseMachine3.quarry_pipe, p_155229_, p_155230_);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.quarry_pipe;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
