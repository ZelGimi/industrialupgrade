package com.denfop.tiles.mechanism;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityScanner;

public class TileEntityImpScanner extends TileEntityScanner {

    public TileEntityImpScanner() {
        super(2000);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
