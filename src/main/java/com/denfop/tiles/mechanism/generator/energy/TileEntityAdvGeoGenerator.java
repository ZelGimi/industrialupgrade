package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityAdvGeoGenerator extends TileEntityGeoGenerator {

    public TileEntityAdvGeoGenerator() {
        super(16, 2.2, 2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
