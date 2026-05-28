package com.denfop.api.pollution.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

public final class PollutionWeatherRenderer {

    private static final int WEATHER_RADIUS = 11;
    private static final float RAIN_MIN_ALPHA = 0.025F;
    private static final float SNOW_MIN_ALPHA = 0.020F;
    private static final PollutionWeatherColumnCache COLUMN_CACHE = new PollutionWeatherColumnCache();

    private PollutionWeatherRenderer() {
    }

    public static void render(RenderLevelStageEvent event, PollutionVisualData state) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.level == null || minecraft.player == null) {
            COLUMN_CACHE.reset();
            return;
        }

        float weatherStrength = PollutionWeatherUtil.getPollutedPrecipitationStrength(
                minecraft,
                state,
                event.getPartialTick().getGameTimeDeltaTicks()
        );

        if (weatherStrength <= 0.02F) {
            return;
        }

        Camera camera = event.getCamera();
        Vec3 cameraPos = camera.getPosition();

        COLUMN_CACHE.ensureFresh(minecraft, WEATHER_RADIUS, minecraft.level.getGameTime());

        if (COLUMN_CACHE.getEntryCount() <= 0) {
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        event.getPoseStack().pushPose();
        event.getPoseStack().translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        Matrix4f matrix = event.getPoseStack().last().pose();

        boolean snow = PollutionWeatherUtil.isSnowAtPlayer(minecraft);
        if (snow) {
            renderAshSnow(event, matrix, state, weatherStrength, cameraPos);
        } else {
            renderToxicRain(event, matrix, state, weatherStrength, cameraPos);
        }

        event.getPoseStack().popPose();

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    private static void renderToxicRain(
            RenderLevelStageEvent event,
            Matrix4f matrix,
            PollutionVisualData state,
            float weatherStrength,
            Vec3 cameraPos
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        int color = PollutionWeatherUtil.getPrecipitationColor(state, false);

        int cameraY = Mth.floor(cameraPos.y);
        float renderTime = event.getRenderTick() + event.getPartialTick().getGameTimeDeltaTicks();
        float thunder = minecraft.level.getThunderLevel(event.getPartialTick().getGameTimeDeltaTicks());

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        PollutionWeatherColumnCache.ColumnEntry[] entries = COLUMN_CACHE.getEntries();
        int count = COLUMN_CACHE.getEntryCount();

        for (int i = 0; i < count; i++) {
            PollutionWeatherColumnCache.ColumnEntry entry = entries[i];

            if (shouldSkipColumn(entry, weatherStrength, state)) {
                continue;
            }

            float alpha = (RAIN_MIN_ALPHA
                    + weatherStrength * 0.18F
                    + entry.getRadialFactor() * 0.06F
                    + state.getAirInfluence() * 0.08F)
                    * (0.82F + entry.getSeed01() * 0.35F);
            if (alpha <= 0.003F) {
                continue;
            }

            int bottomY = Math.max(cameraY - WEATHER_RADIUS, entry.getTerrainY());
            int topY = Math.max(cameraY + WEATHER_RADIUS + 8, entry.getTerrainY() + 12);
            if (topY <= bottomY + 2) {
                continue;
            }

            float cycleHeight = (topY - bottomY) + 8.0F;
            float fallSpeed = 1.70F + entry.getSeed01b() * 1.10F + weatherStrength * 0.70F + thunder * 0.45F;
            float cycle = Mth.frac(renderTime * 0.055F * fallSpeed + entry.getSeed01() * 8.0F);

            float headY = topY - cycle * cycleHeight;
            float tailY = headY - (3.40F + weatherStrength * 3.20F + entry.getSeed01c() * 1.60F);

            if (headY < bottomY || tailY > topY) {
                continue;
            }

            float centerX = entry.getWorldX() + 0.5F + (entry.getSeed01() - 0.5F) * 0.28F;
            float centerZ = entry.getWorldZ() + 0.5F + (entry.getSeed01b() - 0.5F) * 0.28F;

            float windSignX = (entry.getDensitySeed() & 1) == 0 ? 1.0F : -1.0F;
            float windSignZ = (entry.getDensitySeed() & 2) == 0 ? 1.0F : -1.0F;

            float slantX = windSignX * (0.12F + weatherStrength * 0.20F + state.getAirInfluence() * 0.10F)
                    + Mth.sin(renderTime * 0.025F + entry.getSeed01() * 10.0F) * 0.07F;
            float slantZ = windSignZ * (0.04F + weatherStrength * 0.10F)
                    + Mth.cos(renderTime * 0.022F + entry.getSeed01b() * 9.0F) * 0.05F;

            float width = 0.018F + weatherStrength * 0.020F;

            addCrossRibbon(
                    builder,
                    matrix,
                    centerX,
                    headY,
                    centerZ,
                    centerX + slantX,
                    tailY,
                    centerZ + slantZ,
                    width,
                    color,
                    alpha,
                    alpha * 0.18F
            );
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }

    private static void renderAshSnow(
            RenderLevelStageEvent event,
            Matrix4f matrix,
            PollutionVisualData state,
            float weatherStrength,
            Vec3 cameraPos
    ) {
        int color = PollutionWeatherUtil.getPrecipitationColor(state, true);
        int cameraY = Mth.floor(cameraPos.y);
        float renderTime = event.getRenderTick() + event.getPartialTick().getGameTimeDeltaTicks();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        PollutionWeatherColumnCache.ColumnEntry[] entries = COLUMN_CACHE.getEntries();
        int count = COLUMN_CACHE.getEntryCount();

        for (int i = 0; i < count; i++) {
            PollutionWeatherColumnCache.ColumnEntry entry = entries[i];

            if (shouldSkipColumn(entry, weatherStrength, state)) {
                continue;
            }

            int bottomY = Math.max(cameraY - WEATHER_RADIUS, entry.getTerrainY());
            int topY = Math.max(cameraY + WEATHER_RADIUS + 6, entry.getTerrainY() + 10);
            if (topY <= bottomY + 2) {
                continue;
            }

            float cycleHeight = (topY - bottomY) + 4.0F;
            float fallSpeed = 0.50F + entry.getSeed01b() * 0.35F + weatherStrength * 0.28F;
            float cycle = Mth.frac(renderTime * 0.014F * fallSpeed + entry.getSeed01() * 8.0F);
            float centerY = topY - cycle * cycleHeight;

            if (centerY < bottomY) {
                continue;
            }

            float driftX = Mth.sin(renderTime * 0.018F + entry.getSeed01() * 12.0F)
                    * (0.12F + weatherStrength * 0.14F);
            float driftZ = Mth.cos(renderTime * 0.017F + entry.getSeed01b() * 13.0F)
                    * (0.12F + weatherStrength * 0.14F);

            float centerX = entry.getWorldX() + 0.5F + (entry.getSeed01() - 0.5F) * 0.55F + driftX;
            float centerZ = entry.getWorldZ() + 0.5F + (entry.getSeed01b() - 0.5F) * 0.55F + driftZ;

            float size = 0.06F + weatherStrength * 0.06F + entry.getSeed01c() * 0.03F;
            float alpha = (SNOW_MIN_ALPHA
                    + weatherStrength * 0.16F
                    + entry.getRadialFactor() * 0.05F
                    + state.getAirInfluence() * 0.07F)
                    * (0.80F + entry.getSeed01() * 0.35F);

            addCrossFlake(
                    builder,
                    matrix,
                    centerX,
                    centerY,
                    centerZ,
                    size,
                    color,
                    alpha
            );

            if (weatherStrength > 0.38F && entry.getSeed01c() > 0.45F) {
                addCrossFlake(
                        builder,
                        matrix,
                        centerX + 0.08F,
                        centerY - (0.22F + entry.getSeed01() * 0.24F),
                        centerZ - 0.05F,
                        size * 0.65F,
                        color,
                        alpha * 0.65F
                );
            }
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }

    private static boolean shouldSkipColumn(
            PollutionWeatherColumnCache.ColumnEntry entry,
            float weatherStrength,
            PollutionVisualData state
    ) {
        if (state.getAirInfluence() >= 0.22F) {
            return false;
        }

        if (weatherStrength >= 0.58F || state.getAirInfluence() >= 0.55F || state.getScreenInfluence() >= 0.45F) {
            return false;
        }

        float radial = entry.getRadialFactor();
        int seed = entry.getDensitySeed();

        if (weatherStrength < 0.18F) {
            if (radial < 0.70F && (seed & 1) != 0) {
                return true;
            }
            if (radial < 0.45F && seed != 0) {
                return true;
            }
        } else if (weatherStrength < 0.32F) {
            if (radial < 0.58F && (seed & 1) != 0) {
                return true;
            }
            if (radial < 0.30F && seed != 0) {
                return true;
            }
        } else if (weatherStrength < 0.48F) {
            if (radial < 0.24F && (seed & 1) != 0) {
                return true;
            }
        }

        return false;
    }

    private static void addCrossRibbon(
            BufferBuilder builder,
            Matrix4f matrix,
            float startX,
            float startY,
            float startZ,
            float endX,
            float endY,
            float endZ,
            float halfWidth,
            int rgb,
            float startAlpha,
            float endAlpha
    ) {
        int r = PollutionVisualColors.red(rgb);
        int g = PollutionVisualColors.green(rgb);
        int b = PollutionVisualColors.blue(rgb);
        int aTop = toAlpha(startAlpha);
        int aBottom = toAlpha(endAlpha);

        builder.addVertex(matrix, startX - halfWidth, startY, startZ).setColor(r, g, b, aTop);
        builder.addVertex(matrix, startX + halfWidth, startY, startZ).setColor(r, g, b, aTop);
        builder.addVertex(matrix, endX + halfWidth, endY, endZ).setColor(r, g, b, aBottom);
        builder.addVertex(matrix, endX - halfWidth, endY, endZ).setColor(r, g, b, aBottom);

        builder.addVertex(matrix, startX, startY, startZ - halfWidth).setColor(r, g, b, aTop);
        builder.addVertex(matrix, startX, startY, startZ + halfWidth).setColor(r, g, b, aTop);
        builder.addVertex(matrix, endX, endY, endZ + halfWidth).setColor(r, g, b, aBottom);
        builder.addVertex(matrix, endX, endY, endZ - halfWidth).setColor(r, g, b, aBottom);
    }

    private static void addCrossFlake(
            BufferBuilder builder,
            Matrix4f matrix,
            float centerX,
            float centerY,
            float centerZ,
            float halfSize,
            int rgb,
            float alpha
    ) {
        int r = PollutionVisualColors.red(rgb);
        int g = PollutionVisualColors.green(rgb);
        int b = PollutionVisualColors.blue(rgb);
        int a = toAlpha(alpha);

        builder.addVertex(matrix, centerX - halfSize, centerY + halfSize, centerZ).setColor(r, g, b, a);
        builder.addVertex(matrix, centerX + halfSize, centerY + halfSize, centerZ).setColor(r, g, b, a);
        builder.addVertex(matrix, centerX + halfSize, centerY - halfSize, centerZ).setColor(r, g, b, a);
        builder.addVertex(matrix, centerX - halfSize, centerY - halfSize, centerZ).setColor(r, g, b, a);

        builder.addVertex(matrix, centerX, centerY + halfSize, centerZ - halfSize).setColor(r, g, b, a);
        builder.addVertex(matrix, centerX, centerY + halfSize, centerZ + halfSize).setColor(r, g, b, a);
        builder.addVertex(matrix, centerX, centerY - halfSize, centerZ + halfSize).setColor(r, g, b, a);
        builder.addVertex(matrix, centerX, centerY - halfSize, centerZ - halfSize).setColor(r, g, b, a);
    }

    private static int toAlpha(float alpha) {
        return Mth.clamp(Math.round(alpha * 255.0F), 0, 255);
    }
}