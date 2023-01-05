package com.denfop.tiles.mechanism.water;

import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.componets.EnumTypeStyle;

public class TileEntitySimpleWaterGenerator extends TileEntityBaseWaterGenerator {

    public TileEntitySimpleWaterGenerator() {
        super(EnumLevelGenerators.ONE);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
