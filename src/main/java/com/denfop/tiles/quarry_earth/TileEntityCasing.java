package com.denfop.tiles.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import forestry.book.gui.elements.MultiblockElement;

public class TileEntityCasing extends TileEntityMultiBlockElement implements ICasing {

    public TileEntityCasing(){

    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockEarthQuarry.earth_casing;
    }
}
