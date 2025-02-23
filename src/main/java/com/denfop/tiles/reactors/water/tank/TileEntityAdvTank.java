package com.denfop.tiles.reactors.water.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;

public class TileEntityAdvTank extends TileEntityMainTank {

    public TileEntityAdvTank() {
        super(25000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_adv_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }

    @Override
    public int getLevel() {
        return 1;
    }

}
