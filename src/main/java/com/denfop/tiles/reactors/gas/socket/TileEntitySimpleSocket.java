package com.denfop.tiles.reactors.gas.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.reactors.gas.ISocket;

public class TileEntitySimpleSocket extends TileEntityMainSocket implements ISocket {

    public TileEntitySimpleSocket() {
        super(10000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.gas_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }

    @Override
    public int getBlockLevel() {
        return 0;
    }

}
