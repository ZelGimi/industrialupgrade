package com.denfop.api.pollution.radiation;


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
        RadiationSystem.rad_system.work(event.getEntity());

    }

    @SubscribeEvent
    public void tick(LevelTickEvent.Post event) {
        if (event.getLevel().dimension() != Level.OVERWORLD || event.getLevel().isClientSide) {
            return;
        }
        RadiationSystem.rad_system.workDecay(event.getLevel());
    }

}
