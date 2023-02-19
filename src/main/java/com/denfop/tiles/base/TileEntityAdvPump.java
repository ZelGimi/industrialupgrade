package com.denfop.tiles.base;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.TileEntityPump;

public class TileEntityAdvPump extends TileEntityPump {

    public TileEntityAdvPump() {
        super(10, 15);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
