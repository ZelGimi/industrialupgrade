package com.denfop.api.otherenergies.cool.event;

import com.denfop.api.otherenergies.cool.ICoolTile;
import net.minecraft.world.level.Level;

public class CoolTileUnloadEvent extends CoolTileEvent {

    public CoolTileUnloadEvent(final ICoolTile tile, Level world) {
        super(tile, world);
    }

}
