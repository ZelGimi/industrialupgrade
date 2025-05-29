package com.denfop.tiles.transport.tiles.heatpipe;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPipe;
import com.denfop.tiles.transport.tiles.TileEntityHeatPipes;
import com.denfop.tiles.transport.types.HeatType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPipes2 extends TileEntityHeatPipes {
    public TileEntityPipes2(BlockPos pos, BlockState state) {
        super(HeatType.pipes2, BlockPipe.pipes2, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockPipe.pipes2;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.pipes.getBlock(getTeBlock().getId());
    }
}
