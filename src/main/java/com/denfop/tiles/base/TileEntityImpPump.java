package com.denfop.tiles.base;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.TileEntityPump;

public class TileEntityImpPump extends TileEntityPump {

    public TileEntityImpPump() {
        super("blockImpPump.name", 15, 10);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
