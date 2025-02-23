package com.denfop.tiles.reactors.heat.coolant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;

public class TileEntityAdvCoolant extends TileEntityBaseCoolant {

    public TileEntityAdvCoolant() {
        super(1, 10000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_adv_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
