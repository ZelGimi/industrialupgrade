package com.denfop.tiles.mechanism.wind;

import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityAdvWindGenerator extends TileEntityWindGenerator {

    public TileEntityAdvWindGenerator() {
        super(EnumLevelGenerators.TWO);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
