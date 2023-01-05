package com.denfop.api.se.event;

import com.denfop.api.se.ISETile;
import net.minecraft.world.World;

public class SETileLoadEvent extends SETileEvent {

    public SETileLoadEvent(final ISETile tile, World world) {
        super(tile, world);
    }

}
