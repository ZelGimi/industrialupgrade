package com.denfop.api.pollution;


import com.denfop.api.pollution.air.PollutionAirLoadEvent;
import com.denfop.api.pollution.air.PollutionAirUnLoadEvent;
import com.denfop.api.pollution.soil.PollutionSoilLoadEvent;
import com.denfop.api.pollution.soil.PollutionSoilUnLoadEvent;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class EventHandler {

    @SubscribeEvent
    public void tick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().dimension() != Level.OVERWORLD || event.getEntity().level().isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.work(event.getEntity());

    }

    @SubscribeEvent
    public void tick(LevelTickEvent.Post event) {
        if (event.getLevel().dimension() != Level.OVERWORLD || event.getLevel().isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.tick(event.getLevel());
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
