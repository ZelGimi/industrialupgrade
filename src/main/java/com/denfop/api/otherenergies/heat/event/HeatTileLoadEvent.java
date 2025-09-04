package com.denfop.api.otherenergies.heat.event;

import com.denfop.api.otherenergies.heat.IHeatTile;
import net.minecraft.world.level.Level;

public class HeatTileLoadEvent extends HeatTileEvent {

    public HeatTileLoadEvent(final IHeatTile tile, Level world) {
        super(tile, world);
    }

}
