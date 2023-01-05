package com.denfop.api.energy.event;

import com.denfop.api.energy.IEnergyController;
import ic2.api.energy.event.EnergyTileEvent;

public class EventUnloadController extends EnergyTileEvent {


    public EventUnloadController(final IEnergyController tile) {
        super(tile);
    }

}
