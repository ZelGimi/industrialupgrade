package com.denfop.api.energy;

import net.minecraftforge.event.level.LevelEvent;

public class EnergyTileEvent extends LevelEvent {

    public final IEnergyTile tile;

    public EnergyTileEvent(IEnergyTile tile) {
        super(EnergyNetGlobal.instance.getWorld(tile));
        if (this.getLevel() == null) {
            throw new NullPointerException("world is null");
        } else {
            this.tile = tile;
        }
    }

}
