package com.denfop.blockentity.reactors.graphite.controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpController extends BlockEntityMainController {

    public BlockEntityImpController(BlockPos pos, BlockState state
    ) {
        super(InitMultiBlockSystem.impGraphiteReactorMultiBlock, EnumGraphiteReactors.I, BlocksGraphiteReactors.graphite_imp_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlocksGraphiteReactors.graphite_imp_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor.getBlock(getTeBlock());
    }

}
