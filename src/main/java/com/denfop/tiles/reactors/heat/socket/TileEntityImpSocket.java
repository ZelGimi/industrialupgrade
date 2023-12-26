package com.denfop.tiles.reactors.heat.socket;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityImpSocket  extends TileEntityMainSocket {
    public TileEntityImpSocket() {
        super(40000);
    }

    @Override
    public int getLevel() {
        return 2;
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_imp_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
