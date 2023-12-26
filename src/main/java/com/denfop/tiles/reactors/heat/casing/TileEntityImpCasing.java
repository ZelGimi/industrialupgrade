package com.denfop.tiles.reactors.heat.casing;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.ICasing;

public class TileEntityImpCasing  extends TileEntityMultiBlockElement  implements ICasing {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_imp_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }
    @Override
    public int getLevel() {
        return 2;
    }
}
