package com.denfop.api.heat.event;

import com.denfop.api.heat.IHeatTile;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class HeatTileEvent extends LevelEvent {

    public final IHeatTile tile;

    public HeatTileEvent(IHeatTile tile, Level world) {
        super(world);
        this.tile = tile;

    }

}
