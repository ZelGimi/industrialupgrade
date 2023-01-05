package com.denfop.tiles.mechanism.generator.things.matter;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityMultiMatter;

public class TileEntityImprovedMatter extends TileEntityMultiMatter {

    public TileEntityImprovedMatter() {
        super(800000F, 14, 64000000);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
