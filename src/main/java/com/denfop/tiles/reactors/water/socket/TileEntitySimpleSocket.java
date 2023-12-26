package com.denfop.tiles.reactors.water.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntitySimpleSocket  extends TileEntityMainSocket {

    public TileEntitySimpleSocket() {
        super(10000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }
    @Override
    public int getLevel() {
        return 0;
    }
}
