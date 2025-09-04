package com.denfop.blockentity.reactors.gas.intercooler;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvInterCooler extends BlockEntityBaseInterCooler {

    public BlockEntityAdvInterCooler(BlockPos pos, BlockState state) {
        super(1, BlockGasReactorEntity.adv_gas_intercooler, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.adv_gas_intercooler;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
