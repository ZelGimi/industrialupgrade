package com.denfop.api.pollution;


import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class PollutionSoilUnLoadEvent extends LevelEvent {

    public final IPollutionMechanism tile;


    public PollutionSoilUnLoadEvent(Level world, IPollutionMechanism energyTile1) {
        super(world);
        this.tile = energyTile1;
    }

}
