package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityAdvRedstoneGenerator extends TileEntityBaseRedstoneGenerator{

    public TileEntityAdvRedstoneGenerator() {
        super(2.2, 2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
