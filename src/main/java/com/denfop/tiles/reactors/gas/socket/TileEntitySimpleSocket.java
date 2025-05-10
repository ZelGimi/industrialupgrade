package com.denfop.tiles.reactors.gas.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.reactors.gas.ISocket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySimpleSocket extends TileEntityMainSocket implements ISocket {

    public TileEntitySimpleSocket(BlockPos pos, BlockState state) {
        super(10000,BlockGasReactor.gas_socket,pos,state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.gas_socket;
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
