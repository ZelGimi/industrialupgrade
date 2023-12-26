package com.denfop.tiles.reactors.heat.circulationpump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityAdvCirculationPump  extends TileEntityBaseCirculationPump {

    public TileEntityAdvCirculationPump() {
        super(1);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_adv_circulationpump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
