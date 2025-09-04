package com.denfop.blockentity.reactors.heat.controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpController extends BlockEntityMainController {

    public BlockEntityImpController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.impHeatReactorMultiBlock, EnumHeatReactors.I, BlockHeatReactorEntity.heat_imp_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_imp_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
