package com.denfop.api.pollution;


import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event) {
        if (event.player.getLevel().dimension() != Level.OVERWORLD || event.player.getLevel().isClientSide || event.phase == TickEvent.Phase.START) {
            return;
        }
        PollutionManager.pollutionManager.work(event.player);

    }

    @SubscribeEvent
    public void tick(TickEvent.LevelTickEvent event) {
        if (event.level.dimension() != Level.OVERWORLD || event.level.isClientSide || event.phase == TickEvent.Phase.START) {
            return;
        }
        PollutionManager.pollutionManager.tick(event.level);
    }

    @SubscribeEvent
    public void tick(PollutionAirLoadEvent event) {
        if (((Level) event.getLevel()).dimension() != Level.OVERWORLD || ((Level) event.getLevel()).isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.addAirPollutionMechanism(event.tile);

    }

    @SubscribeEvent
    public void tick(PollutionAirUnLoadEvent event) {
        if (((Level) event.getLevel()).dimension() != Level.OVERWORLD || ((Level) event.getLevel()).isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.removeAirPollutionMechanism(event.tile);

    }

    @SubscribeEvent
    public void tick(PollutionSoilLoadEvent event) {
        if (((Level) event.getLevel()).dimension() != Level.OVERWORLD || ((Level) event.getLevel()).isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.addSoilPollutionMechanism(event.tile);

    }

    @SubscribeEvent
    public void tick(PollutionSoilUnLoadEvent event) {
        if (((Level) event.getLevel()).dimension() != Level.OVERWORLD || ((Level) event.getLevel()).isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.removeSoilPollutionMechanism(event.tile);

    }

}
