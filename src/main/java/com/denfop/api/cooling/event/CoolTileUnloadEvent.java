package com.denfop.api.cooling.event;

import com.denfop.api.cooling.ICoolTile;
import net.minecraft.world.World;

public class CoolTileUnloadEvent extends  CoolTileEvent {

    public CoolTileUnloadEvent(final ICoolTile tile, World world) {
        super(tile,world);
    }

}
