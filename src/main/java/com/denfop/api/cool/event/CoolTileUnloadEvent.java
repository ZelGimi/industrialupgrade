package com.denfop.api.cool.event;

import com.denfop.api.cool.ICoolTile;
import net.minecraft.world.level.Level;

public class CoolTileUnloadEvent extends CoolTileEvent {

    public CoolTileUnloadEvent(final ICoolTile tile, Level world) {
        super(tile, world);
    }

}
