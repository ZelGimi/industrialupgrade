package com.denfop.tiles.reactors.heat.fueltank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;

public class TileEntityPerFuelTank extends TileEntityMainTank {

    public TileEntityPerFuelTank() {
        super(160000);
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_per_fueltank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
