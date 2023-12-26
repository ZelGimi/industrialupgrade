package com.denfop.tiles.reactors.heat.graphite_controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityPerGraphiteController  extends TileEntityGraphiteController {
    public TileEntityPerGraphiteController() {
        super(3);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_per_graphite_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
