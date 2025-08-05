package com.denfop.api.cool.event;

import com.denfop.api.cool.ICoolTile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class CoolTileEvent extends LevelEvent {

    public final ICoolTile tile;

    public CoolTileEvent(ICoolTile tile, Level world) {
        super(world);
        this.tile = tile;
    }

}
