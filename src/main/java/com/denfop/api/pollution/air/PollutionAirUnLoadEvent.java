package com.denfop.api.pollution.air;

import com.denfop.api.pollution.PollutionMechanism;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class PollutionAirUnLoadEvent extends LevelEvent {

    public final PollutionMechanism tile;


    public PollutionAirUnLoadEvent(Level world, PollutionMechanism energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
