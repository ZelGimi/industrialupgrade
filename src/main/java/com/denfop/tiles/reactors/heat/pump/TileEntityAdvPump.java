package com.denfop.tiles.reactors.heat.pump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;

public class TileEntityAdvPump extends TileEntityBasePump {

    public TileEntityAdvPump() {
        super(1);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_adv_pump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
