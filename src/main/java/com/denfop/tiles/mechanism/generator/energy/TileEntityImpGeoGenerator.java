package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityImpGeoGenerator extends TileEntityGeoGenerator {

    public TileEntityImpGeoGenerator() {
        super(20, 3.4, 3);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
