package com.simplequarries;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityPerSimplyQuarry extends TileEntityBaseQuarry {

    public TileEntityPerSimplyQuarry() {
        super("", 2, 4);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
