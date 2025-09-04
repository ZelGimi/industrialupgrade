package com.denfop.blockentity.transport.tiles.heatpipe;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityHeatPipes;
import com.denfop.blockentity.transport.types.HeatType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPipes extends BlockEntityHeatPipes {
    public BlockEntityPipes(BlockPos pos, BlockState state) {
        super(HeatType.pipes, BlockPipeEntity.pipes, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockPipeEntity.pipes;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.pipes.getBlock(getTeBlock().getId());
    }
}
