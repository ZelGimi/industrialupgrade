package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityGeoGenerator extends com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator {

    public TileEntityGeoGenerator() {
        super(10, 1, 1);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
