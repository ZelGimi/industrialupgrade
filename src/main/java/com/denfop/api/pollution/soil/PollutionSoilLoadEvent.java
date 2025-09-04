package com.denfop.api.pollution.soil;

import com.denfop.api.pollution.PollutionMechanism;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class PollutionSoilLoadEvent extends LevelEvent {

    public final PollutionMechanism tile;


    public PollutionSoilLoadEvent(Level world, PollutionMechanism energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
