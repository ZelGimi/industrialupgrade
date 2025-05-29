package com.denfop.api.cool.event;

import com.denfop.api.cool.ICoolTile;
import net.minecraft.world.level.Level;

public class CoolTileLoadEvent extends CoolTileEvent {

    public CoolTileLoadEvent(final ICoolTile tile, Level world) {
        super(tile, world);
    }

}
