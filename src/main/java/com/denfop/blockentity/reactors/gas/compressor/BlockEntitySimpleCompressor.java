package com.denfop.blockentity.reactors.gas.compressor;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleCompressor extends BlockEntityBaseCompressor {

    public BlockEntitySimpleCompressor(BlockPos pos, BlockState state) {
        super(0, BlockGasReactorEntity.gas_compressor, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.gas_compressor;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }


}
