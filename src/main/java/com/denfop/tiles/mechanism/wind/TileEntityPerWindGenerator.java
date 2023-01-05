package com.denfop.tiles.mechanism.wind;

import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityPerWindGenerator extends TileEntityWindGenerator {

    public TileEntityPerWindGenerator() {
        super(EnumLevelGenerators.FOUR);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
