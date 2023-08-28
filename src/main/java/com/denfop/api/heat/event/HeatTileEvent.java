package com.denfop.api.heat.event;

import com.denfop.api.heat.IHeatTile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class HeatTileEvent extends WorldEvent {

    public final IHeatTile tile;

    public HeatTileEvent(IHeatTile tile, World world) {
        super(world);
        this.tile = tile;

    }

}
