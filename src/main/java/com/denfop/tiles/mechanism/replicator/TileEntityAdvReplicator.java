package com.denfop.tiles.mechanism.replicator;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.TileEntityBaseReplicator;

public class TileEntityAdvReplicator extends TileEntityBaseReplicator {

    public TileEntityAdvReplicator() {
        super(0.95);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

}
