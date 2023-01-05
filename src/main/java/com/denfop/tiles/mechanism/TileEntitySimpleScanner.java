package com.denfop.tiles.mechanism;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityScanner;

public class TileEntitySimpleScanner extends TileEntityScanner {

    public TileEntitySimpleScanner() {
        super(3300);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
