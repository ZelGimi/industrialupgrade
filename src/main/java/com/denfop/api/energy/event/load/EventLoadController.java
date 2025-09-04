package com.denfop.api.energy.event.load;

import com.denfop.api.energy.EnergyTileEvent;
import com.denfop.api.energy.interfaces.EnergyController;
import net.minecraft.world.level.Level;

public class EventLoadController extends EnergyTileEvent {

    public EventLoadController(final EnergyController tile, Level level) {
        super(tile, level);
    }

}
