package com.denfop.tiles.reactors.water.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;

public class TileEntityPerTank extends TileEntityMainTank {

    public TileEntityPerTank() {
        super(100000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_per_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }

    @Override
    public int getBlockLevel() {
        return 3;
    }

}
