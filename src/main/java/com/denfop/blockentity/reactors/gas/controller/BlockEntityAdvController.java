package com.denfop.blockentity.reactors.gas.controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvController extends BlockEntityMainController {

    public BlockEntityAdvController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.advGasReactorMultiBlock, EnumGasReactors.A, BlockGasReactorEntity.adv_gas_controller, pos, state);
    }


    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.adv_gas_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
