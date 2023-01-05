package com.denfop.tiles.mechanism.water;

import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityPerWaterGenerator extends TileEntityBaseWaterGenerator {

    public TileEntityPerWaterGenerator() {
        super(EnumLevelGenerators.FOUR);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
