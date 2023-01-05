package com.denfop.api.heat.event;

import com.denfop.api.heat.IHeatTile;
import net.minecraft.world.World;

public class HeatTileLoadEvent extends HeatTileEvent {

    public HeatTileLoadEvent(final IHeatTile tile, World world) {
        super(tile, world);
    }

}
