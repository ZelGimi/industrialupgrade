package com.denfop.tiles.mechanism.replicator;

import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.mechanism.TileEntityBaseReplicator;

public class TileEntityImpReplicator extends TileEntityBaseReplicator {

    public TileEntityImpReplicator() {
        super(0.85);
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.IMPROVED;
    }

}
