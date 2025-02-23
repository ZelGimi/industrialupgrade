package com.denfop.api.pressure.event;

import com.denfop.api.pressure.IPressureTile;
import net.minecraft.world.World;

public class PressureTileLoadEvent extends PressureTileEvent {

    public PressureTileLoadEvent(final IPressureTile tile, World world) {
        super(tile, world);
    }

}
