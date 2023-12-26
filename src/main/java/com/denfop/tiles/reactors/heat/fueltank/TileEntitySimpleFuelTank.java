package com.denfop.tiles.reactors.heat.fueltank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntitySimpleFuelTank  extends TileEntityMainTank {

    public TileEntitySimpleFuelTank() {
        super(20000);
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_fueltank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
