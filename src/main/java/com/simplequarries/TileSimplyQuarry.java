package com.simplequarries;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;

public class TileSimplyQuarry extends TileBaseQuarry {

    public TileSimplyQuarry() {
        super("", 1, 1);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQuarry.simply_quarry;
    }

    public BlockTileEntity getBlock() {
        return SimplyQuarries.quarry;
    }

}
