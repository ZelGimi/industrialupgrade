package com.denfop.api.heat.event;

import com.denfop.api.heat.IHeatTile;

public class HeatTileUnloadEvent extends HeatTileEvent {

    public HeatTileUnloadEvent(final IHeatTile tile) {
        super(tile);
    }

}
