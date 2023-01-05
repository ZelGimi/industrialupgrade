package com.denfop.api.se.event;

import com.denfop.api.se.ISETile;
import net.minecraft.world.World;

public class SETileUnloadEvent extends SETileEvent {

    public SETileUnloadEvent(final ISETile tile, World world) {
        super(tile, world);
    }

}
