package com.denfop.tiles.reactors.graphite.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;

public class TileEntityPerSocket  extends TileEntityMainSocket {
    public TileEntityPerSocket() {
        super(80000);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_per_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }
    @Override
    public int getLevel() {
        return 3;
    }
}
