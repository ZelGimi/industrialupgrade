package com.denfop.api.pollution.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

public final class PollutionSkyRenderer {

    private PollutionSkyRenderer() {
    }

    public static void render(RenderLevelStageEvent event, PollutionVisualData state) {
        if (!state.isActive()) {
            return;
        }

        if (state.getSkyInfluence() <= 0.01F && state.getHorizonHaze() <= 0.01F) {
            return;
        }

        Camera camera = event.getCamera();
        Vec3 cameraPos = camera.getPosition();
        float partialTick = event.getPartialTick().getGameTimeDeltaTicks();
        float pulse = PollutionVisualController.getPulse(partialTick);

        int upperColor = state.getSkyColor();
        int smogColor = PollutionVisualColors.lerpColor(state.getFogColor(), state.getSkyColor(), 0.35F + state.getCloudVeil() * 0.25F);
        int lowerColor = PollutionVisualColors.lerpColor(state.getFogColor(), state.getOverlayColor(), 0.25F);

        float upperAlpha = 0.04F + state.getSkyInfluence() * 0.16F;
        float cloudAlpha = 0.03F + state.getCloudVeil() * 0.18F;
        float lowerAlpha = 0.02F + state.getHorizonHaze() * 0.06F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        double baseX = cameraPos.x;
        double baseY = cameraPos.y;
        double baseZ = cameraPos.z;

        event.getPoseStack().pushPose();
        event.getPoseStack().translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        Matrix4f matrix = event.getPoseStack().last().pose();

        drawHorizontalLayer(
                matrix,
                baseX,
                baseY + 120.0D + Mth.sin((event.getRenderTick() + partialTick) * 0.02F) * (2.0F + state.getShimmerStrength() * 3.0F),
                baseZ,
                420.0F,
                upperColor,
                upperAlpha
        );

        drawHorizontalLayer(
                matrix,
                baseX + Mth.sin((event.getRenderTick() + partialTick) * 0.007F) * 22.0F,
                baseY + 142.0D,
                baseZ + Mth.cos((event.getRenderTick() + partialTick) * 0.006F) * 22.0F,
                520.0F,
                smogColor,
                cloudAlpha
        );

        drawHorizontalLayer(
                matrix,
                baseX,
                baseY + 64.0D,
                baseZ,
                300.0F,
                lowerColor,
                lowerAlpha
        );

        if (state.getRadiationInfluence() > 0.18F) {
            int anomalyColor = PollutionVisualColors.lerpColor(PollutionVisualColors.RADIATION_SKY, state.getSkyColor(), 0.45F);
            float anomalyAlpha = 0.01F + state.getRadiationInfluence() * 0.08F * (0.85F + 0.15F * pulse);

            drawHorizontalLayer(
                    matrix,
                    baseX + Mth.cos((event.getRenderTick() + partialTick) * 0.021F) * 12.0F,
                    baseY + 176.0D + Mth.sin((event.getRenderTick() + partialTick) * 0.031F) * 4.0F,
                    baseZ + Mth.sin((event.getRenderTick() + partialTick) * 0.018F) * 12.0F,
                    280.0F,
                    anomalyColor,
                    anomalyAlpha
            );
        }

        event.getPoseStack().popPose();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void drawHorizontalLayer(Matrix4f matrix, double centerX, double y, double centerZ, float halfSize, int rgb, float alpha) {
        if (alpha <= 0.001F) {
            return;
        }

        int a = Math.max(0, Math.min(255, Math.round(alpha * 255.0F)));
        int r = PollutionVisualColors.red(rgb);
        int g = PollutionVisualColors.green(rgb);
        int b = PollutionVisualColors.blue(rgb);

        float minX = (float) (centerX - halfSize);
        float maxX = (float) (centerX + halfSize);
        float minZ = (float) (centerZ - halfSize);
        float maxZ = (float) (centerZ + halfSize);
        float fy = (float) y;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        builder.addVertex(matrix, minX, fy, minZ).setColor(r, g, b, a);
        builder.addVertex(matrix, minX, fy, maxZ).setColor(r, g, b, a);
        builder.addVertex(matrix, maxX, fy, maxZ).setColor(r, g, b, a);
        builder.addVertex(matrix, maxX, fy, minZ).setColor(r, g, b, a);
        BufferUploader.drawWithShader(builder.buildOrThrow());
    }
}