package com.simplequarries;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityAdvSimplyQuarry extends TileEntityBaseQuarry {

    public TileEntityAdvSimplyQuarry() {
        super("", 1.25, 2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
