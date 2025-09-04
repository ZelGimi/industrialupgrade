package com.denfop.blockentity.transport.tiles.heatcold;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.transport.tiles.BlockEntityHeatColdPipes;
import com.denfop.blockentity.transport.types.HeatColdType;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatColdPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityHeatColdPipe3 extends BlockEntityHeatColdPipes {
    public BlockEntityHeatColdPipe3(BlockPos pos, BlockState state) {
        super(HeatColdType.heatcool3, BlockHeatColdPipeEntity.heatcool3, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatColdPipeEntity.heatcool3;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heatcold_pipes.getBlock(getTeBlock().getId());
    }
}
