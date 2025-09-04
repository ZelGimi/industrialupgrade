package com.denfop.blockentity.reactors.heat.controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvController extends BlockEntityMainController {

    public BlockEntityAdvController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.advHeatReactorMultiBlock, EnumHeatReactors.A, BlockHeatReactorEntity.heat_adv_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_adv_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
