package com.denfop.api.pollution.air;

import com.denfop.api.pollution.PollutionMechanism;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class PollutionAirLoadEvent extends LevelEvent {

    public final PollutionMechanism tile;


    public PollutionAirLoadEvent(Level world, PollutionMechanism energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
