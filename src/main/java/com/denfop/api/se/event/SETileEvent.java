package com.denfop.api.se.event;

import com.denfop.api.se.ISETile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class SETileEvent extends WorldEvent {

    public final ISETile tile;

    public SETileEvent(ISETile tile, World world) {
        super(world);
        this.tile = tile;
    }

}
