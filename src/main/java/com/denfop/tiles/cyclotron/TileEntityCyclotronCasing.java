package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityCyclotronCasing extends TileEntityMultiBlockElement implements ICasing {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron;
    }

}
