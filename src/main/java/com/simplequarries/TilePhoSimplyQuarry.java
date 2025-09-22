package com.simplequarries;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.EnumTypeStyle;

public class TilePhoSimplyQuarry extends TileBaseQuarry {

    public TilePhoSimplyQuarry() {
        super("", 2.5, 5);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PHOTONIC;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQuarry.pho_simply_quarry;
    }

    public BlockTileEntity getBlock() {
        return SimplyQuarries.quarry;
    }

}
