package com.denfop.api.energy.event;

import com.denfop.api.energy.IEnergyController;
import ic2.api.energy.event.EnergyTileEvent;

public class EventLoadController extends EnergyTileEvent {

    public EventLoadController(final IEnergyController tile) {
        super(tile);
    }

}
