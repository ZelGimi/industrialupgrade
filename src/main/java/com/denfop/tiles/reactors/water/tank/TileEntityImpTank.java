package com.denfop.tiles.reactors.water.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityImpTank  extends TileEntityMainTank {
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_imp_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }
    public TileEntityImpTank() {
        super(50000);
    }
    @Override
    public int getLevel() {
        return 2;
    }
}
