package com.denfop.tiles.reactors.heat.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;

public class TileEntitySimpleSocket extends TileEntityMainSocket {

    public TileEntitySimpleSocket() {
        super(10000);
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
