package com.denfop.api.pollution.client;

import com.denfop.Constants;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class PollutionVisualForgeEvents {

    private PollutionVisualForgeEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {


        PollutionVisualController.tickClient(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        PollutionVisualData state = PollutionVisualController.getCurrent();
        if (!state.isActive()) {
            return;
        }

        FogType fogType = event.getCamera().getFluidInCamera();
        if (fogType == FogType.LAVA || fogType == FogType.POWDER_SNOW) {
            return;
        }

        float alpha = state.getFogInfluence();
        int targetColor = state.getFogColor();

        if (fogType == FogType.WATER) {
            alpha = Math.max(alpha, state.getBiomeInfluence() * 0.85F);
            targetColor = PollutionVisualColors.lerpColor(state.getFogColor(), state.getWaterTintColor(), 0.60F);
        } else {
            alpha = Math.max(alpha, state.getSkyInfluence() * 0.35F);

            float air = state.getAirInfluence();
            float pollutedWeather = PollutionWeatherUtil.getPollutedPrecipitationStrength(
                    Minecraft.getInstance(),
                    state,
                    1.0F
            );

            if (air > 0.01F) {
                alpha = Math.max(alpha, air * 0.95F);
                targetColor = PollutionVisualColors.lerpColor(targetColor, PollutionVisualColors.AIR_FOG, air * 0.82F);
            }

            if (pollutedWeather > 0.01F) {
                alpha = Math.max(alpha, pollutedWeather * 0.85F);
                targetColor = PollutionVisualColors.lerpColor(targetColor, PollutionVisualColors.AIR_FOG, pollutedWeather * 0.65F);
            }
        }

        event.setRed(Mth.lerp(alpha, event.getRed(), PollutionVisualColors.red01(targetColor)));
        event.setGreen(Mth.lerp(alpha, event.getGreen(), PollutionVisualColors.green01(targetColor)));
        event.setBlue(Mth.lerp(alpha, event.getBlue(), PollutionVisualColors.blue01(targetColor)));
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        PollutionVisualData state = PollutionVisualController.getCurrent();
        if (!state.isActive()) {
            return;
        }

        FogType fogType = event.getType();
        if (fogType == FogType.LAVA || fogType == FogType.POWDER_SNOW) {
            return;
        }

        float nearFactor = state.getFogNearFactor();
        float farFactor = state.getFogFarFactor();
        float absoluteFarCap = event.getFarPlaneDistance();

        if (fogType == FogType.WATER) {
            float waterAmount = Mth.clamp(
                    Math.max(state.getBiomeInfluence(), state.getSoilInfluence() * 0.9F),
                    0.0F,
                    1.0F
            );

            nearFactor = Mth.lerp(waterAmount, nearFactor, 0.58F);
            farFactor = Mth.lerp(waterAmount, farFactor, 0.22F);

            absoluteFarCap = Math.min(
                    absoluteFarCap,
                    Mth.lerp(waterAmount, event.getFarPlaneDistance(), 26.0F)
            );
        } else {
            float air = state.getAirInfluence();
            float soil = state.getSoilInfluence();
            float radiation = state.getRadiationInfluence();
            float pollutedWeather = PollutionWeatherUtil.getPollutedPrecipitationStrength(
                    Minecraft.getInstance(),
                    state,
                    1.0F
            );

            if (air > 0.01F) {
                nearFactor = Math.min(nearFactor, Mth.lerp(air, 1.0F, 0.54F));
                farFactor = Math.min(farFactor, Mth.lerp(air, 1.0F, 0.10F));

                absoluteFarCap = Math.min(
                        absoluteFarCap,
                        Mth.lerp(air, event.getFarPlaneDistance(), 56.0F)
                );
            }

            if (pollutedWeather > 0.01F) {
                nearFactor = Math.min(nearFactor, Mth.lerp(pollutedWeather, 1.0F, 0.74F));
                farFactor = Math.min(farFactor, Mth.lerp(pollutedWeather, 1.0F, 0.42F));

                absoluteFarCap = Math.min(
                        absoluteFarCap,
                        Mth.lerp(pollutedWeather, event.getFarPlaneDistance(), 34.0F)
                );
            }

            if (soil > 0.20F) {
                absoluteFarCap = Math.min(
                        absoluteFarCap,
                        Mth.lerp(soil, event.getFarPlaneDistance(), 92.0F)
                );
            }

            if (radiation > 0.25F) {
                absoluteFarCap = Math.min(
                        absoluteFarCap,
                        Mth.lerp(radiation, event.getFarPlaneDistance(), 72.0F)
                );
            }
        }

        float newNear = Math.max(0.25F, event.getNearPlaneDistance() * nearFactor);
        float newFar = Math.max(newNear + 6.0F, event.getFarPlaneDistance() * farFactor);

        newFar = Math.min(newFar, absoluteFarCap);
        newFar = Math.max(12.0F, newFar);
        newNear = Math.min(newNear, newFar - 4.0F);
        newNear = Math.max(0.25F, newNear);

        event.setNearPlaneDistance(newNear);
        event.setFarPlaneDistance(newFar);
        event.setFogShape(FogShape.SPHERE);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        PollutionVisualData state = PollutionVisualController.getCurrent();
        if (!state.isActive()) {
            return;
        }

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            PollutionSkyRenderer.render(event, state);
            return;
        }

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            PollutionWeatherRenderer.render(event, state);
        }
    }
}