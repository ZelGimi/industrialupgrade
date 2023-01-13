package com.denfop.tiles.mechanism.generator.energy.coal;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityAdvGenerator;

public class TileEntityGeneratorImp extends TileEntityAdvGenerator {

    public TileEntityGeneratorImp() {
        super(3.4, 16000, 3);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
