package com.denfop.api.energy.event;

import com.denfop.api.energy.EnergyTileEvent;
import com.denfop.api.energy.IEnergyController;

public class EventLoadController extends EnergyTileEvent {

    public EventLoadController(final IEnergyController tile) {
        super(tile);
    }

}
