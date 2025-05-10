package com.denfop.api.energy.event;

import com.denfop.api.energy.IEnergyTile;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;


public class EnergyTileUnLoadEvent extends LevelEvent {

    public final IEnergyTile tile;

    public EnergyTileUnLoadEvent(Level world, IEnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
