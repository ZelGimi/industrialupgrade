package com.denfop.blockentity.reactors.gas.controller;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import com.denfop.register.InitMultiBlockSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleController extends BlockEntityMainController {

    public BlockEntitySimpleController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.GasReactorMultiBlock, EnumGasReactors.S, BlockGasReactorEntity.gas_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.gas_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
