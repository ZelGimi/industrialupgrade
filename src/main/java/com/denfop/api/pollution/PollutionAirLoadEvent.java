package com.denfop.api.pollution;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class PollutionAirLoadEvent extends WorldEvent {

    public final IPollutionMechanism tile;


    public PollutionAirLoadEvent(World world, IPollutionMechanism energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
