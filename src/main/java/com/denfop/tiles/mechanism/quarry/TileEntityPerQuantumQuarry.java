package com.denfop.tiles.mechanism.quarry;


import com.denfop.componets.EnumTypeStyle;

public class TileEntityPerQuantumQuarry extends TileEntityBaseQuantumQuarry {

    public TileEntityPerQuantumQuarry() {
        super(1);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
