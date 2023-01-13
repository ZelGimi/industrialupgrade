package com.denfop.tiles.mechanism.generator.energy.coal;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityAdvGenerator;

public class TileEntityGeneratorAdv extends TileEntityAdvGenerator {

    public TileEntityGeneratorAdv() {
        super(2.2, 8000, 2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
