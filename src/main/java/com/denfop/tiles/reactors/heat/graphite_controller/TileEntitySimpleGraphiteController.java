package com.denfop.tiles.reactors.heat.graphite_controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;

public class TileEntitySimpleGraphiteController extends TileEntityGraphiteController {

    public TileEntitySimpleGraphiteController() {
        super(0);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_graphite_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
