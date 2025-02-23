package com.denfop.tiles.reactors.graphite.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;

public class TileEntityImpSocket extends TileEntityMainSocket {

    public TileEntityImpSocket() {
        super(40000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_imp_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

    @Override
    public int getLevel() {
        return 2;
    }

}
