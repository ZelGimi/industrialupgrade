package com.denfop.api.energy.event;

import com.denfop.api.energy.IAdvEnergyTile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EnergyTileUnLoadEvent extends WorldEvent {

    public final IAdvEnergyTile tile;

    public EnergyTileUnLoadEvent(World world, IAdvEnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
