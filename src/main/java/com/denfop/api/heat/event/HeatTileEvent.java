package com.denfop.api.heat.event;

import com.denfop.api.heat.IHeatTile;
import net.minecraftforge.event.world.WorldEvent;

public class HeatTileEvent extends WorldEvent {

    public final IHeatTile tile;

    public HeatTileEvent(IHeatTile tile) {
        super(tile.getWorldTile());
        this.tile = tile;
    }

}
