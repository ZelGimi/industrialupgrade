package com.denfop.api.energy.event;

import com.denfop.api.energy.EnergyTileEvent;
import com.denfop.api.energy.IEnergyController;
import net.minecraft.world.level.Level;

public class EventUnloadController extends EnergyTileEvent {


    public EventUnloadController(final IEnergyController tile, Level level) {
        super(tile,level);
    }

}
