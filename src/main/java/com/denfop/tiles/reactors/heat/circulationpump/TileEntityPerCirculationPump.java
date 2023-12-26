package com.denfop.tiles.reactors.heat.circulationpump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityPerCirculationPump  extends TileEntityBaseCirculationPump {
    public TileEntityPerCirculationPump() {
        super(3);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_per_circulationpump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
