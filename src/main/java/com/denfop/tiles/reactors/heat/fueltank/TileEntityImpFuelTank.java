package com.denfop.tiles.reactors.heat.fueltank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;

public class TileEntityImpFuelTank extends TileEntityMainTank {

    public TileEntityImpFuelTank() {
        super(80000);
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_imp_fueltank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
