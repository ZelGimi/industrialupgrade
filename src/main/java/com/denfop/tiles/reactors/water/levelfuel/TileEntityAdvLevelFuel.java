package com.denfop.tiles.reactors.water.levelfuel;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ILevelFuel;

public class TileEntityAdvLevelFuel  extends TileEntityMainLevelFuel implements ILevelFuel {
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_adv_levelfuel;
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
