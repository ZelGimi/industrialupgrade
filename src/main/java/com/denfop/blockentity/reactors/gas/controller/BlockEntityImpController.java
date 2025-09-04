package com.denfop.blockentity.reactors.gas.controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpController extends BlockEntityMainController {

    public BlockEntityImpController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.impGasReactorMultiBlock, EnumGasReactors.I, BlockGasReactorEntity.imp_gas_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.imp_gas_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
