package com.simplequarries;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.EnumTypeStyle;

public class TilePerSimplyQuarry extends TileBaseQuarry {

    public TilePerSimplyQuarry() {
        super("", 2, 4);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQuarry.per_simply_quarry;
    }

    public BlockTileEntity getBlock() {
        return SimplyQuarries.quarry;
    }

}
