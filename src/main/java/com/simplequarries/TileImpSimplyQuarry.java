package com.simplequarries;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.EnumTypeStyle;

public class TileImpSimplyQuarry extends TileBaseQuarry {

    public TileImpSimplyQuarry() {
        super("", 1.5, 3);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQuarry.imp_simply_quarry;
    }

    public BlockTileEntity getBlock() {
        return SimplyQuarries.quarry;
    }

}
