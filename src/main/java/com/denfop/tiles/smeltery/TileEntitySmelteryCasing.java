package com.denfop.tiles.smeltery;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntitySmelteryCasing extends TileEntityMultiBlockElement implements ICasing {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery;
    }

}
