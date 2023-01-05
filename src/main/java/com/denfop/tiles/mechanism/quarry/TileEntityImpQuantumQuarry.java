package com.denfop.tiles.mechanism.quarry;


import com.denfop.componets.EnumTypeStyle;

public class TileEntityImpQuantumQuarry extends TileEntityBaseQuantumQuarry {

    public TileEntityImpQuantumQuarry() {
        super(2);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
