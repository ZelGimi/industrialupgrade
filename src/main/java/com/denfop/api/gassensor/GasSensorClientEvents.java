package com.denfop.api.gassensor;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = "industrialupgrade", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class GasSensorClientEvents {

    private GasSensorClientEvents() {
    }

    @SubscribeEvent
    public static void onGuiOverlay(RenderGuiLayerEvent.Post event) {
        GasSensorOverlayRenderer.render(event.getGuiGraphics());
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