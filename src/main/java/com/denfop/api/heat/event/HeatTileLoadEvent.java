package com.denfop.api.heat.event;

import com.denfop.api.heat.IHeatTile;

public class HeatTileLoadEvent extends HeatTileEvent {

    public HeatTileLoadEvent(final IHeatTile tile) {
        super(tile);
    }

}
