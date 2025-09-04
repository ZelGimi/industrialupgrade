package com.denfop.api.otherenergies.pressure.event;

import com.denfop.api.otherenergies.pressure.IPressureTile;
import net.minecraft.world.level.Level;

public class PressureTileUnloadEvent extends PressureTileEvent {

    public PressureTileUnloadEvent(final IPressureTile tile, Level world) {
        super(tile, world);
    }

}
