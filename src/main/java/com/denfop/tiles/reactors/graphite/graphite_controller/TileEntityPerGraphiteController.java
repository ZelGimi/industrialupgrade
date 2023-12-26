package com.denfop.tiles.reactors.graphite.graphite_controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityPerGraphiteController  extends TileEntityGraphiteController {
    public TileEntityPerGraphiteController() {
        super(3);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_per_graphite_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

}
