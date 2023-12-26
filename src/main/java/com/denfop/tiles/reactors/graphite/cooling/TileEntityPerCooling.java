package com.denfop.tiles.reactors.graphite.cooling;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityPerCooling  extends TileEntityCoolant {
    public TileEntityPerCooling() {
        super(3);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_per_cooling;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

}
