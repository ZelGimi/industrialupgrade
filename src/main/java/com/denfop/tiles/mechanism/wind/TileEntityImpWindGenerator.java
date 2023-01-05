package com.denfop.tiles.mechanism.wind;

import com.denfop.api.windsystem.EnumLevelGenerators;
import com.denfop.componets.EnumTypeStyle;

public class TileEntityImpWindGenerator extends TileEntityWindGenerator {

    public TileEntityImpWindGenerator() {
        super(EnumLevelGenerators.THREE);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
