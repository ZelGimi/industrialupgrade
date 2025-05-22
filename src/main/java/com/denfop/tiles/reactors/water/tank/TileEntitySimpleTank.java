package com.denfop.tiles.reactors.water.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.reactors.water.ITank;

public class TileEntitySimpleTank extends TileEntityMainTank implements ITank {

    public TileEntitySimpleTank() {
        super(10000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }

    @Override
    public int getBlockLevel() {
        return 0;
    }

}
