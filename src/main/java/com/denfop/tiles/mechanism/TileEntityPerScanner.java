package com.denfop.tiles.mechanism;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityScanner;

public class TileEntityPerScanner extends TileEntityScanner {

    public TileEntityPerScanner() {
        super(1500);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
