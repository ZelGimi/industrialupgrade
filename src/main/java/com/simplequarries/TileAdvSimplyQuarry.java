package com.simplequarries;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.EnumTypeStyle;

public class TileAdvSimplyQuarry extends TileBaseQuarry {

    public TileAdvSimplyQuarry() {
        super("", 1.25, 2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQuarry.adv_simply_quarry;
    }

    public BlockTileEntity getBlock() {
        return SimplyQuarries.quarry;
    }

}
