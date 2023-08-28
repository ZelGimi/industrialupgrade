package com.denfop.api.energy;

import net.minecraftforge.event.world.WorldEvent;

public class EnergyTileEvent extends WorldEvent {

    public final IEnergyTile tile;

    public EnergyTileEvent(IEnergyTile tile) {
        super(EnergyNetGlobal.instance.getWorld(tile));
        if (this.getWorld() == null) {
            throw new NullPointerException("world is null");
        } else {
            this.tile = tile;
        }
    }

}
