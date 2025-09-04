package com.denfop.api.pollution.soil;


import com.denfop.api.pollution.PollutionMechanism;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class PollutionSoilUnLoadEvent extends LevelEvent {

    public final PollutionMechanism tile;


    public PollutionSoilUnLoadEvent(Level world, PollutionMechanism energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
