package com.denfop.api.pollution;

import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class PollutionSoilLoadEvent extends LevelEvent {

    public final IPollutionMechanism tile;


    public PollutionSoilLoadEvent(Level world, IPollutionMechanism energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
