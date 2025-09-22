package com.denfop.tiles.reactors.water.chamber;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.IChamber;

public class TileEntityPerChamber extends TileEntityMultiBlockElement implements IChamber {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_per_chamber;
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
