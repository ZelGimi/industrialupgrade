package com.denfop.api.gassensor;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = "industrialupgrade", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GasSensorServerEvents {

    private GasSensorServerEvents() {
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

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