package com.denfop.tiles.mechanism.generator.things.matter;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityMultiMatter;


public class TileEntityAdvancedMatter extends TileEntityMultiMatter {

    public TileEntityAdvancedMatter() {
        super(900000F, 12, 8000000);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
