package com.denfop.tiles.mechanism;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityScanner;

public class TileEntityAdvScanner extends TileEntityScanner {

    public TileEntityAdvScanner() {
        super(2500);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
