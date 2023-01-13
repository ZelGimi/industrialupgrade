package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityImpRedstoneGenerator extends TileEntityBaseRedstoneGenerator{

    public TileEntityImpRedstoneGenerator() {
        super(3.4, 4);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
