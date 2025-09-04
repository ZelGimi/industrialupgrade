package com.denfop.api.otherenergies.cool.event;

import com.denfop.api.otherenergies.cool.ICoolTile;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class CoolTileEvent extends LevelEvent {

    public final ICoolTile tile;

    public CoolTileEvent(ICoolTile tile, Level world) {
        super(world);
        this.tile = tile;
    }

}
