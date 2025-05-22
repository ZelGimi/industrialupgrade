package com.denfop.api.pollution;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event) {
        if (event.player.getEntityWorld().provider.getDimension() != 0 || event.player.getEntityWorld().isRemote || event.phase == TickEvent.Phase.START) {
            return;
        }
        PollutionManager.pollutionManager.work(event.player);

    }

    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.world.provider.getDimension() != 0 || event.world.isRemote || event.phase == TickEvent.Phase.START) {
            return;
        }
        PollutionManager.pollutionManager.tick(event.world);
    }

    @SubscribeEvent
    public void tick(PollutionAirLoadEvent event) {
        if (event.getWorld().provider.getDimension() != 0 || event.getWorld().isRemote) {
            return;
        }
        PollutionManager.pollutionManager.addAirPollutionMechanism(event.tile);

    }

    @SubscribeEvent
    public void tick(PollutionAirUnLoadEvent event) {
        if (event.getWorld().provider.getDimension() != 0 || event.getWorld().isRemote) {
            return;
        }
        PollutionManager.pollutionManager.removeAirPollutionMechanism(event.tile);

    }

    @SubscribeEvent
    public void tick(PollutionSoilLoadEvent event) {
        if (event.getWorld().provider.getDimension() != 0 || event.getWorld().isRemote) {
            return;
        }
        PollutionManager.pollutionManager.addSoilPollutionMechanism(event.tile);

    }

    @SubscribeEvent
    public void tick(PollutionSoilUnLoadEvent event) {
        if (event.getWorld().provider.getDimension() != 0 || event.getWorld().isRemote) {
            return;
        }
        PollutionManager.pollutionManager.removeSoilPollutionMechanism(event.tile);

    }

}
