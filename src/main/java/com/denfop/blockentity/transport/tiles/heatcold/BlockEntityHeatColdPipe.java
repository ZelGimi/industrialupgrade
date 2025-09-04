package com.denfop.blockentity.transport.tiles.heatcold;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityHeatColdPipes;
import com.denfop.blockentity.transport.types.HeatColdType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatColdPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityHeatColdPipe extends BlockEntityHeatColdPipes {
    public BlockEntityHeatColdPipe(BlockPos pos, BlockState state) {
        super(HeatColdType.heatcool, BlockHeatColdPipeEntity.heatcool, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatColdPipeEntity.heatcool;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heatcold_pipes.getBlock(getTeBlock().getId());
    }
}
