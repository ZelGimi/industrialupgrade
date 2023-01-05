package com.denfop.api.radiationsystem;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }
        RadiationSystem.rad_system.work(event.player);
    }

}
