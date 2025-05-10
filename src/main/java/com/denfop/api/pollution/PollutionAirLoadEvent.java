package com.denfop.api.pollution;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class PollutionAirLoadEvent extends LevelEvent {

    public final IPollutionMechanism tile;


    public PollutionAirLoadEvent(Level world, IPollutionMechanism energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
