package com.denfop.api.radiationsystem;


import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event) {
        if (event.player.level().dimension() != Level.OVERWORLD || event.player.level().isClientSide || event.phase == TickEvent.Phase.START) {
            return;
        }
        RadiationSystem.rad_system.work(event.player);

    }

    @SubscribeEvent
    public void tick(TickEvent.LevelTickEvent event) {
        if (event.level.dimension() != Level.OVERWORLD || event.level.isClientSide || event.phase == TickEvent.Phase.START) {
            return;
        }
        RadiationSystem.rad_system.workDecay(event.level);
    }

}
