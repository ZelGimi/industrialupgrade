package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityPerGeoGenerator extends TileEntityGeoGenerator {

    public TileEntityPerGeoGenerator() {
        super(32, 4.6, 4);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
