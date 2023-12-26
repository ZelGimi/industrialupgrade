package com.denfop.tiles.reactors.gas.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.reactors.gas.ISocket;

public class TileEntityImpSocket   extends TileEntityMainSocket implements ISocket {
    public TileEntityImpSocket() {
        super(40000);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.imp_gas_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }

    @Override
    public int getLevel() {
        return 2;
    }

}
