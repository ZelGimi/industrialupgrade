package com.denfop.api.energy.event;

import com.denfop.api.energy.EnergyTileEvent;
import com.denfop.api.energy.IEnergyController;

public class EventUnloadController extends EnergyTileEvent {


    public EventUnloadController(final IEnergyController tile) {
        super(tile);
    }

}
