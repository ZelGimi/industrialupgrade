package com.denfop.tiles.reactors.graphite.exchanger;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityAdvExchanger extends TileEntityExchanger {
    public TileEntityAdvExchanger() {
        super(1);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_adv_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

}
