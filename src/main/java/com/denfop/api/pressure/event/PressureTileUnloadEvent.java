package com.denfop.api.pressure.event;

import com.denfop.api.pressure.IPressureTile;
import net.minecraft.world.World;

public class PressureTileUnloadEvent extends PressureTileEvent {

    public PressureTileUnloadEvent(final IPressureTile tile, World world) {
        super(tile, world);
    }

}
