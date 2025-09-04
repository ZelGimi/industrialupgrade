package com.denfop.api.energy;

import com.denfop.api.energy.interfaces.EnergyTile;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class EnergyTileEvent extends LevelEvent {

    public final EnergyTile tile;

    public EnergyTileEvent(EnergyTile tile, Level level) {
        super(level);
        if (this.getLevel() == null) {
            throw new NullPointerException("world is null");
        } else {
            this.tile = tile;
        }
    }

}
