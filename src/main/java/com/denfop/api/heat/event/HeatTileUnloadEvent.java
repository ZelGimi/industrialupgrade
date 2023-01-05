package com.denfop.api.heat.event;

import com.denfop.api.heat.IHeatTile;
import net.minecraft.world.World;

public class HeatTileUnloadEvent extends HeatTileEvent {

    public HeatTileUnloadEvent(final IHeatTile tile, World world) {
        super(tile, world);
    }

}
