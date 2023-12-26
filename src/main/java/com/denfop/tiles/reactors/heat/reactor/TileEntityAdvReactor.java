package com.denfop.tiles.reactors.heat.reactor;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.IReactor;

public class TileEntityAdvReactor  extends TileEntityMultiBlockElement implements IReactor {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_adv_reactor;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }
    @Override
    public int getLevel() {
        return 1;
    }
}
