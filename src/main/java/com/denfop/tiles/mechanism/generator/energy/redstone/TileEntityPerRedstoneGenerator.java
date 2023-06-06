package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.componets.EnumTypeStyle;

public class TileEntityPerRedstoneGenerator extends TileEntityBaseRedstoneGenerator {

    public TileEntityPerRedstoneGenerator() {
        super(4.6, 8);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
