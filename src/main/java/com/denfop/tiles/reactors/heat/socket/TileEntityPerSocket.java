package com.denfop.tiles.reactors.heat.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;

public class TileEntityPerSocket extends TileEntityMainSocket {

    public TileEntityPerSocket() {
        super(80000);
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_per_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
