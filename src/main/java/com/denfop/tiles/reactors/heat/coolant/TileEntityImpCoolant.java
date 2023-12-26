package com.denfop.tiles.reactors.heat.coolant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityImpCoolant  extends TileEntityBaseCoolant {

    public TileEntityImpCoolant() {
        super(2, 15000);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_imp_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
