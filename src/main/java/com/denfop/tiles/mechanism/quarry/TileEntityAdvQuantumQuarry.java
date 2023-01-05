package com.denfop.tiles.mechanism.quarry;


import com.denfop.componets.EnumTypeStyle;

public class TileEntityAdvQuantumQuarry extends TileEntityBaseQuantumQuarry {

    public TileEntityAdvQuantumQuarry() {
        super(3);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
