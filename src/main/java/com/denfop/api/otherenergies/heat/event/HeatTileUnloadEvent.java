package com.denfop.api.otherenergies.heat.event;

import com.denfop.api.otherenergies.heat.IHeatTile;
import net.minecraft.world.level.Level;

public class HeatTileUnloadEvent extends HeatTileEvent {

    public HeatTileUnloadEvent(final IHeatTile tile, Level world) {
        super(tile, world);
    }

}
