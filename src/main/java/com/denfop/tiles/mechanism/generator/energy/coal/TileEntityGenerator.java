package com.denfop.tiles.mechanism.generator.energy.coal;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityGenerator extends TileEntityAdvGenerator {

    public TileEntityGenerator() {
        super(1, 4000, 1);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
