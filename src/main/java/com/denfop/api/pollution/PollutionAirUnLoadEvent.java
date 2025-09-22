package com.denfop.api.pollution;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class PollutionAirUnLoadEvent extends WorldEvent {

    public final IPollutionMechanism tile;


    public PollutionAirUnLoadEvent(World world, IPollutionMechanism energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
