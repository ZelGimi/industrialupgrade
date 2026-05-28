package com.denfop.api.pollution.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;

public enum PollutionScreenOverlay implements LayeredDraw.Layer {
    INSTANCE;

    private static void renderBaseVeil(GuiGraphics guiGraphics, int width, int height, PollutionVisualData state, float pulse) {
        float screen = state.getScreenInfluence();
        float radiationPulse = state.getRadiationInfluence() * (0.75F + 0.25F * pulse);
        int alpha = Math.min(255, Math.round((screen * 24.0F) + (radiationPulse * 18.0F)));
        if (alpha <= 0) {
            return;
        }

        guiGraphics.fill(0, 0, width, height, PollutionVisualColors.argb(alpha, state.getOverlayColor()));
    }

    private static void renderEdgeVignette(GuiGraphics guiGraphics, int width, int height, PollutionVisualData state, float pulse) {
        float influence = state.getScreenInfluence();
        int rgb = PollutionVisualColors.lerpColor(0x101010, state.getOverlayColor(), 0.35F);
        int layers = 22 + (int) (influence * 22.0F);

        for (int i = 0; i < layers; i++) {
            float t = 1.0F - (i / (float) layers);
            float alphaF = influence * t * t * (0.34F + state.getRadiationInfluence() * 0.10F) * (0.92F + 0.08F * pulse);
            int alpha = Math.min(255, Math.round(alphaF * 255.0F));
            if (alpha <= 0) {
                continue;
            }

            int inset = i * 2;
            if ((width - inset) <= inset || (height - inset) <= inset) {
                break;
            }

            int color = PollutionVisualColors.argb(alpha, rgb);
            guiGraphics.fill(inset, inset, width - inset, inset + 2, color);
            guiGraphics.fill(inset, height - inset - 2, width - inset, height - inset, color);
            guiGraphics.fill(inset, inset + 2, inset + 2, height - inset - 2, color);
            guiGraphics.fill(width - inset - 2, inset + 2, width - inset, height - inset - 2, color);
        }
    }

    private static void renderNoiseFilm(GuiGraphics guiGraphics, int width, int height, PollutionVisualData state, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        long time = minecraft.level != null ? minecraft.level.getGameTime() : 0L;

        float intensity = state.getScreenInfluence();
        int dots = 35 + (int) (intensity * 120.0F);
        int baseColor = PollutionVisualColors.lerpColor(state.getOverlayColor(), 0xFFFFFF, 0.15F);

        for (int i = 0; i < dots; i++) {
            long hash = mix(time * 31L + i * 1315423911L + (long) (partialTick * 100.0F));
            int x = Math.floorMod((int) hash, Math.max(1, width));
            int y = Math.floorMod((int) (hash >> 17), Math.max(1, height));
            int size = 1 + Math.floorMod((int) (hash >> 29), 2);

            int maxAlpha = 4 + Math.round(intensity * 18.0F) + Math.round(state.getRadiationInfluence() * 10.0F);
            int alpha = 2 + Math.floorMod((int) (hash >> 41), Math.max(1, maxAlpha));
            int color = PollutionVisualColors.argb(alpha, baseColor);

            guiGraphics.fill(x, y, Math.min(width, x + size), Math.min(height, y + size), color);
        }
    }

    private static void renderScanlines(GuiGraphics guiGraphics, int width, int height, PollutionVisualData state, float pulse) {
        float strength = state.getScreenInfluence() * (0.05F + state.getAirInfluence() * 0.05F + state.getRadiationInfluence() * 0.07F);
        int alpha = Math.min(255, Math.round(strength * 255.0F * (0.75F + 0.25F * pulse)));
        if (alpha <= 0) {
            return;
        }

        int color = PollutionVisualColors.argb(alpha, PollutionVisualColors.lerpColor(0x0D0D0D, state.getOverlayColor(), 0.12F));
        int offset = (Minecraft.getInstance().player != null ? Minecraft.getInstance().player.tickCount : 0) & 3;

        for (int y = offset; y < height; y += 4) {
            guiGraphics.fill(0, y, width, y + 1, color);
        }
    }

    private static void renderAnomalyBands(GuiGraphics guiGraphics, int width, int height, PollutionVisualData state, float pulse) {
        float radiation = state.getRadiationInfluence();
        if (radiation < 0.22F) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        long time = minecraft.level != null ? minecraft.level.getGameTime() : 0L;

        for (int i = 0; i < 3; i++) {
            long hash = mix(time * 73L + i * 9973L);
            int bandWidth = 6 + Math.floorMod((int) (hash >> 11), 20);
            int x = Math.floorMod((int) hash, Math.max(1, width + bandWidth)) - (bandWidth / 2);
            int startX = Math.max(0, x);
            int endX = Math.min(width, x + bandWidth);
            if (endX <= startX) {
                continue;
            }

            int alpha = Math.min(255, Math.round((8.0F + radiation * 24.0F) * (0.60F + 0.40F * pulse)));
            int color = PollutionVisualColors.argb(alpha, PollutionVisualColors.RADIATION_OVERLAY);
            guiGraphics.fill(startX, 0, endX, height, color);
        }
    }

    private static long mix(long value) {
        value ^= (value >>> 33);
        value *= 0xff51afd7ed558ccdL;
        value ^= (value >>> 33);
        value *= 0xc4ceb9fe1a85ec53L;
        value ^= (value >>> 33);
        return value;
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        PollutionVisualData state = PollutionVisualController.getCurrent();

        if (minecraft.player == null || minecraft.level == null || minecraft.options.hideGui) {
            return;
        }

        if (!state.isActive() || state.getScreenInfluence() <= 0.01F) {
            return;
        }

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);

        float pulse = PollutionVisualController.getPulse(partialTick);
        renderBaseVeil(guiGraphics, screenWidth, screenHeight, state, pulse);
        renderEdgeVignette(guiGraphics, screenWidth, screenHeight, state, pulse);
        renderNoiseFilm(guiGraphics, screenWidth, screenHeight, state, partialTick);
        renderScanlines(guiGraphics, screenWidth, screenHeight, state, pulse);
        renderAnomalyBands(guiGraphics, screenWidth, screenHeight, state, pulse);
    }
}