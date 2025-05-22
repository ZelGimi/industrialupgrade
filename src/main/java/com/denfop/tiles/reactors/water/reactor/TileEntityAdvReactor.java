package com.denfop.tiles.reactors.water.reactor;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactors;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.IReactor;

public class TileEntityAdvReactor extends TileEntityMultiBlockElement implements IReactor {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWaterReactors.water_adv_reactor;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component;
    }

    @Override
    public int getBlockLevel() {
        return 1;
    }

}
