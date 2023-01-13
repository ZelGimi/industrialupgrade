package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.TileEntityGeoGenerator;

public class TileEntityPerGeoGenerator extends TileEntityGeoGenerator {

    public TileEntityPerGeoGenerator() {
        super(32, 4.6, 4);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
