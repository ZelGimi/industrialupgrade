package com.denfop.blockentity.reactors.gas.intercooler;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleInterCooler extends BlockEntityBaseInterCooler {

    public BlockEntitySimpleInterCooler(BlockPos pos, BlockState state) {
        super(0, BlockGasReactorEntity.gas_intercooler, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.gas_intercooler;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
