package com.denfop.tiles.reactors.graphite.graphite_controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;

public class TileEntityImpGraphiteController extends TileEntityGraphiteController {

    public TileEntityImpGraphiteController() {
        super(2);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_imp_graphite_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

}
