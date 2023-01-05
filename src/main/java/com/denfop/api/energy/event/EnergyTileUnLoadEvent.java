package com.denfop.api.energy.event;

import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EnergyTileUnLoadEvent extends WorldEvent {

    public final IEnergyTile tile;

    public EnergyTileUnLoadEvent(World world, IEnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
