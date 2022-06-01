package com.denfop.api.cooling.event;

import com.denfop.api.cooling.ICoolTile;
import net.minecraft.world.World;

public class CoolTileLoadEvent extends CoolTileEvent {

    public CoolTileLoadEvent(final ICoolTile tile, World world) {
        super(tile, world);
    }

}
