package com.denfop.tiles.mechanism;

import com.denfop.componets.EnumTypeStyle;

public class TileEntitySimplePump extends TileEntityPump {

    public TileEntitySimplePump() {
        super(10, 20);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
