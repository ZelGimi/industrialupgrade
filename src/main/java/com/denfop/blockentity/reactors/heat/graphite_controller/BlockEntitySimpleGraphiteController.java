package com.denfop.blockentity.reactors.heat.graphite_controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleGraphiteController extends BlockEntityGraphiteController {

    public BlockEntitySimpleGraphiteController(BlockPos pos, BlockState state) {
        super(0, BlockHeatReactorEntity.heat_graphite_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_graphite_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
