package com.denfop.api.exp.event;

import com.denfop.api.exp.IEXPTile;
import net.minecraft.world.World;

public class EXPTileLoadEvent extends EXPTileEvent {

    public EXPTileLoadEvent(final IEXPTile tile, World world) {
        super(tile, world);
    }

}
