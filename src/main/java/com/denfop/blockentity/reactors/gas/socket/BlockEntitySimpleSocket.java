package com.denfop.blockentity.reactors.gas.socket;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.reactors.gas.ISocket;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleSocket extends BlockEntityMainSocket implements ISocket {

    public BlockEntitySimpleSocket(BlockPos pos, BlockState state) {
        super(10000, BlockGasReactorEntity.gas_socket, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasReactorEntity.gas_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 0;
    }

}
