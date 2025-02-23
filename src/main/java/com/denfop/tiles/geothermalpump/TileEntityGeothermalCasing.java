package com.denfop.tiles.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityGeothermalCasing extends TileEntityMultiBlockElement implements ICasing {
    public TileEntityGeothermalCasing(){}
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGeothermalPump.geothermal_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump;
    }

}
