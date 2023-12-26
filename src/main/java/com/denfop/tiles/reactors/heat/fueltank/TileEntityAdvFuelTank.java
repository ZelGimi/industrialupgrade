package com.denfop.tiles.reactors.heat.fueltank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityAdvFuelTank  extends TileEntityMainTank {
    public TileEntityAdvFuelTank() {
        super(40000);
    }

    @Override
    public int getLevel() {
        return 1;
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_adv_fueltank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
