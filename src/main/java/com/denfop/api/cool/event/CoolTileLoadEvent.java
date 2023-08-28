package com.denfop.api.cool.event;

import com.denfop.api.cool.ICoolTile;
import net.minecraft.world.World;

public class CoolTileLoadEvent extends CoolTileEvent {

    public CoolTileLoadEvent(final ICoolTile tile, World world) {
        super(tile, world);
    }

}
