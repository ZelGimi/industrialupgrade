package com.denfop.tiles.reactors.heat.graphite_controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityImpGraphiteController  extends TileEntityGraphiteController {
    public TileEntityImpGraphiteController() {
        super(2);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_imp_graphite_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
