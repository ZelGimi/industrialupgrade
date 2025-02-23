package com.denfop.tiles.reactors.graphite.capacitor;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;

public class TileEntityImpCapacitor extends TileEntityCapacitor {

    public TileEntityImpCapacitor() {
        super(2);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_imp_capacitor;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

}
