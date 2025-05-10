package com.denfop.api.pressure.event;

import com.denfop.api.pressure.IPressureTile;
import net.minecraft.world.level.Level;

public class PressureTileLoadEvent extends PressureTileEvent {

    public PressureTileLoadEvent(final IPressureTile tile, Level world) {
        super(tile, world);
    }

}
