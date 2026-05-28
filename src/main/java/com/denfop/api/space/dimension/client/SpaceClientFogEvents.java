package com.denfop.api.space.dimension.client;

import com.denfop.IUCore;
import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = IUCore.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class SpaceClientFogEvents {

    private SpaceClientFogEvents() {
    }

    @SubscribeEvent
    public static void onComputeFogColor(final ViewportEvent.ComputeFogColor event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        SpaceDimensionProfile profile = SpaceBodyProfiles.byDimensionPath(minecraft.level.dimension().location().getPath());
        if (profile == null) {
            return;
        }

        event.setRed((float) profile.fogColor().x);
        event.setGreen((float) profile.fogColor().y);
        event.setBlue((float) profile.fogColor().z);
    }

    @SubscribeEvent
    public static void onRenderFog(final ViewportEvent.RenderFog event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || event.getType() != FogType.NONE) {
            return;
        }
        SpaceDimensionProfile profile = SpaceBodyProfiles.byDimensionPath(minecraft.level.dimension().location().getPath());
        if (profile == null) {

            return;
        }
        float far = event.getFarPlaneDistance() * profile.fogDistanceFactor();
        float near = Math.max(0.0F, far * (profile.atmosphereDensity() > 0.55F ? 0.12F : 0.03F));
        event.setNearPlaneDistance(near);
        event.setFarPlaneDistance(far);
        event.setCanceled(true);
    }
}
