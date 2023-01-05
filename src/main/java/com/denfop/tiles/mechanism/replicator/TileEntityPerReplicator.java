package com.denfop.tiles.mechanism.replicator;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.TileEntityBaseReplicator;

public class TileEntityPerReplicator extends TileEntityBaseReplicator {

    public TileEntityPerReplicator() {
        super(0.8);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.PERFECT;
    }

}
