package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;

public class TileEntityAdvGeoGenerator extends TileEntityGeoGenerator {

    public TileEntityAdvGeoGenerator() {
        super(16, 2.2, 2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
