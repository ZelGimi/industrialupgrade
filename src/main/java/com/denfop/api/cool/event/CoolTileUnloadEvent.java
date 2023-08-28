package com.denfop.api.cool.event;

import com.denfop.api.cool.ICoolTile;
import net.minecraft.world.World;

public class CoolTileUnloadEvent extends CoolTileEvent {

    public CoolTileUnloadEvent(final ICoolTile tile, World world) {
        super(tile, world);
    }

}
