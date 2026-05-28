package com.denfop.api.gassensor;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = "industrialupgrade", bus = EventBusSubscriber.Bus.GAME)
public final class GasSensorServerEvents {

    private GasSensorServerEvents() {
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

        if (ServerLifecycleHooks.getCurrentServer() == null) {
            return;
        }

        GasSensorScannerManager.tick(ServerLifecycleHooks.getCurrentServer());
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() != null) {
            GasSensorScannerManager.clear(event.getEntity().getUUID());
        }
    }
}