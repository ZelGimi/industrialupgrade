package com.denfop.tiles.transport.tiles.heatcold;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatColdPipe;
import com.denfop.tiles.transport.tiles.TileEntityHeatColdPipes;
import com.denfop.tiles.transport.types.HeatColdType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityHeatColdPipe1 extends TileEntityHeatColdPipes {
    public TileEntityHeatColdPipe1(BlockPos pos, BlockState state) {
        super(HeatColdType.heatcool1, BlockHeatColdPipe.heatcool1, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatColdPipe.heatcool1;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heatcold_pipes.getBlock(getTeBlock().getId());
    }
}
