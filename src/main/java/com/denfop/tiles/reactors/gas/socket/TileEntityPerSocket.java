package com.denfop.tiles.reactors.gas.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.reactors.gas.ISocket;

public class TileEntityPerSocket extends TileEntityMainSocket implements ISocket {

    public TileEntityPerSocket() {
        super(80000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.per_gas_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }

    @Override
    public int getBlockLevel() {
        return 3;
    }

}
