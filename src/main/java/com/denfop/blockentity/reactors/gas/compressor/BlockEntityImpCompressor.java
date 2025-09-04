package com.denfop.blockentity.reactors.gas.compressor;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpCompressor extends BlockEntityBaseCompressor {

    public BlockEntityImpCompressor(BlockPos pos, BlockState state) {
        super(2, BlockGasReactorEntity.imp_gas_compressor, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.imp_gas_compressor;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }

}
