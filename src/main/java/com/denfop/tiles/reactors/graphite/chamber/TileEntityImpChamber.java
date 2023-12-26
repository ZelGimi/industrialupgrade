package com.denfop.tiles.reactors.graphite.chamber;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.IChamber;

public class TileEntityImpChamber  extends TileEntityMultiBlockElement implements IChamber {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_imp_chamber;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

    @Override
    public int getLevel() {
        return 2;
    }
}
