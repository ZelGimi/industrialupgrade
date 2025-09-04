package com.denfop.blockentity.reactors.gas.regenerator;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvRegenerator extends BlockEntityRegenerator {

    public BlockEntityAdvRegenerator(BlockPos pos, BlockState state) {
        super(1, 7500, BlockGasReactorEntity.adv_gas_regenerator, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.adv_gas_regenerator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }

}
