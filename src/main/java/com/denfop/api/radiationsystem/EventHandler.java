package com.denfop.api.radiationsystem;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event) {
        if (event.player.getEntityWorld().isRemote || event.phase == TickEvent.Phase.START) {
            return;
        }
        RadiationSystem.rad_system.work(event.player);

    }
    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event){
        if (event.world.provider.getDimension() != 0 || event.phase == TickEvent.Phase.START) {
            return;
        }
        RadiationSystem.rad_system.workDecay(event.world);
    }
}
