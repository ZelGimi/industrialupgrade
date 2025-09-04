package com.denfop.api.energy.event.unload;

import com.denfop.api.energy.EnergyTileEvent;
import com.denfop.api.energy.interfaces.EnergyController;
import net.minecraft.world.level.Level;

public class EventUnloadController extends EnergyTileEvent {


    public EventUnloadController(final EnergyController tile, Level level) {
        super(tile, level);
    }

}
