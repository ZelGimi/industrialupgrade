package com.denfop.tiles.reactors.graphite.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;

public class TileEntityAdvSocket extends TileEntityMainSocket {

    public TileEntityAdvSocket() {
        super(20000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_adv_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

    @Override
    public int getBlockLevel() {
        return 1;
    }

}
