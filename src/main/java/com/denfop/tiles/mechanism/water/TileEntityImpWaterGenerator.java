package com.denfop.tiles.mechanism.water;

import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityImpWaterGenerator extends TileEntityBaseWaterGenerator {

    public TileEntityImpWaterGenerator() {
        super(EnumLevelGenerators.THREE);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
