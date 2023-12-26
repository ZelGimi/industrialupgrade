package com.denfop.tiles.reactors.graphite.graphite_controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntitySimpleGraphiteController  extends TileEntityGraphiteController {
    public TileEntitySimpleGraphiteController() {
        super(0);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_graphite_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

}
