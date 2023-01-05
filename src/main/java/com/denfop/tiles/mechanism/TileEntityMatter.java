package com.denfop.tiles.mechanism;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityMultiMatter;

public class TileEntityMatter extends TileEntityMultiMatter {

    public TileEntityMatter() {
        super(1000000F, 10, 5000000);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
