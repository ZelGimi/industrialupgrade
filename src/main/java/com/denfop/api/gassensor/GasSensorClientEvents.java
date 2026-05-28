package com.denfop.api.gassensor;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "industrialupgrade", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GasSensorClientEvents {

    private GasSensorClientEvents() {
    }

    @SubscribeEvent
    public static void onGuiOverlay(RenderGuiOverlayEvent.Post event) {
        GasSensorOverlayRenderer.render(event.getPoseStack(), event.getWindow().getScreenWidth(), event.getWindow().getScreenHeight());
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        GasSensorClientCache.clear();
    }

    @SubscribeEvent
    public static void render(RenderLevelStageEvent event) {
        GasSensorOverlayRenderer.renderWorld(event);
    }
}