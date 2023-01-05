package com.denfop.tiles.mechanism.generator.things.matter;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityMultiMatter;

public class TileEntityUltimateMatter extends TileEntityMultiMatter {

    public TileEntityUltimateMatter() {
        super(700000F, 16, 256000000);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
