package com.denfop.blockentity.reactors.gas.controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerController extends BlockEntityMainController {

    public BlockEntityPerController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.perGasReactorMultiBlock, EnumGasReactors.P, BlockGasReactorEntity.per_gas_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.per_gas_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }

}
