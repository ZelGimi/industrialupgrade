package com.denfop.tiles.transport.tiles.heatcold;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatColdPipe;
import com.denfop.tiles.transport.tiles.TileEntityHeatColdPipes;
import com.denfop.tiles.transport.types.HeatColdType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityHeatColdPipe2 extends TileEntityHeatColdPipes {
    public TileEntityHeatColdPipe2(BlockPos pos, BlockState state) {
        super(HeatColdType.heatcool2, BlockHeatColdPipe.heatcool2, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatColdPipe.heatcool2;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heatcold_pipes.getBlock(getTeBlock().getId());
    }
}
