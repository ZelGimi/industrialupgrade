package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;

public class TileEntityImpGeoGenerator extends TileEntityGeoGenerator {

    public TileEntityImpGeoGenerator() {
        super(20, 3.4, 3);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
