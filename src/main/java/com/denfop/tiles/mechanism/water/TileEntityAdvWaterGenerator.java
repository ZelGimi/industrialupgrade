package com.denfop.tiles.mechanism.water;

import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityAdvWaterGenerator extends TileEntityBaseWaterGenerator {

    public TileEntityAdvWaterGenerator() {
        super(EnumLevelGenerators.TWO);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
