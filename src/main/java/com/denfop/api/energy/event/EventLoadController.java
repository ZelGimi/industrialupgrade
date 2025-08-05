package com.denfop.api.energy.event;

import com.denfop.api.energy.EnergyTileEvent;
import com.denfop.api.energy.IEnergyController;
import net.minecraft.world.level.Level;

public class EventLoadController extends EnergyTileEvent {

    public EventLoadController(final IEnergyController tile, Level level) {
        super(tile, level);
    }

}
