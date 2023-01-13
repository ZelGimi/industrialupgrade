package com.denfop.tiles.mechanism.generator.energy.coal;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityAdvGenerator;

public class TileEntityGeneratorPer extends TileEntityAdvGenerator {

    public TileEntityGeneratorPer() {
        super(4.6, 32000, 4);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
