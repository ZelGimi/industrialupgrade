package com.denfop.api.energy.event.unload;

import com.denfop.api.energy.interfaces.EnergyTile;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;


public class EnergyTileUnLoadEvent extends LevelEvent {

    public final EnergyTile tile;

    public EnergyTileUnLoadEvent(Level world, EnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
