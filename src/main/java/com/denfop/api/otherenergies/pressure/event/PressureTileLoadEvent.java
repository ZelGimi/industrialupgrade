package com.denfop.api.otherenergies.pressure.event;

import com.denfop.api.otherenergies.pressure.IPressureTile;
import net.minecraft.world.level.Level;

public class PressureTileLoadEvent extends PressureTileEvent {

    public PressureTileLoadEvent(final IPressureTile tile, Level world) {
        super(tile, world);
    }

}
