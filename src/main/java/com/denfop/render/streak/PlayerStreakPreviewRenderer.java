package com.denfop.render.streak;

import com.denfop.items.armour.special.ColorPickerRenderUtil;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_TEX_COLOR;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

public final class PlayerStreakPreviewRenderer {

    private static final Vector3f PREVIEW_TRANSLATION = new Vector3f();

    private PlayerStreakPreviewRenderer() {
    }

    public static void renderPreview(GuiGraphics graphics,
                                     int x, int y, int width, int height,
                                     Player player,
                                     PlayerStreakInfo info,
                                     float partialTicks,
                                     int mouseX,
                                     int mouseY) {

        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF0D1016);
        drawPreviewBackground(graphics, x, y, width, height);

        int centerX = x + width / 2;
        int entityY = y + height - 6;
        int scale = Math.min(58, Math.max(40, height - 34));

        float mouseOffsetX = (float) centerX - mouseX;
        float mouseOffsetY = (float) (y + height / 2) - mouseY;

        float yawFactor = (float) Math.atan(mouseOffsetX / 80.0F);
        float pitchFactor = (float) Math.atan(mouseOffsetY / 100.0F);

        float attachX = centerX - scale * 0.18F - yawFactor * 8.0F;
        float attachY = entityY - scale * 1.06F + pitchFactor * 2.0F;

        renderStraightRibbon(
                graphics.pose(),
                x,
                y,
                width,
                height,
                attachX,
                attachY,
                scale,
                info,
                partialTicks
        );

        renderPlayerFront(
                graphics,
                player,
                centerX,
                entityY,
                scale,
                yawFactor,
                pitchFactor
        );
    }

    public static int resolvePreviewRgb(PlayerStreakInfo info, float partialTicks) {
        float time = getTime(partialTicks);
        return StreakRenderHelper.resolvePackedRgb(info, time);
    }

    private static void drawPreviewBackground(GuiGraphics graphics, int x, int y, int width, int height) {
        ColorPickerRenderUtil.drawVerticalGradient(
                graphics.pose(),
                x + 1,
                y + 1,
                width - 2,
                height - 2,
                0xFF111722,
                0xFF0A0D12
        );

        int floorY = y + height - 22;
        graphics.fill(x + 10, floorY, x + width - 10, floorY + 1, 0x332BC8FF);

        for (int i = 0; i < 5; i++) {
            int lineY = y + 20 + i * 20;
            graphics.fill(x + 8, lineY, x + width - 8, lineY + 1, 0x10FFFFFF);
        }
    }

    private static void renderPlayerFront(GuiGraphics graphics,
                                          Player player,
                                          int centerX,
                                          int entityY,
                                          int scale,
                                          float yawFactor,
                                          float pitchFactor) {

        float oldBodyRot = player.yBodyRot;
        float oldYRot = player.getYRot();
        float oldXRot = player.getXRot();
        float oldYHeadRotO = player.yHeadRotO;
        float oldYHeadRot = player.yHeadRot;

        Quaternionf basePose = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf pitchPose = Axis.XP.rotationDegrees(pitchFactor * 20.0F);
        basePose.mul(pitchPose);

        Quaternionf cameraOrientation = new Quaternionf(pitchPose).conjugate();

        try {
            player.yBodyRot = 180.0F + yawFactor * 20.0F;
            player.setYRot(180.0F + yawFactor * 40.0F);
            player.setXRot(-pitchFactor * 20.0F);
            player.yHeadRot = player.getYRot();
            player.yHeadRotO = player.getYRot();

            InventoryScreen.renderEntityInInventory(
                    graphics,
                    centerX,
                    entityY,
                    scale, PREVIEW_TRANSLATION,
                    basePose,
                    cameraOrientation,
                    player
            );
        } finally {
            player.yBodyRot = oldBodyRot;
            player.setYRot(oldYRot);
            player.setXRot(oldXRot);
            player.yHeadRotO = oldYHeadRotO;
            player.yHeadRot = oldYHeadRot;
        }
    }

    private static void renderStraightRibbon(PoseStack poseStack,
                                             int x,
                                             int y,
                                             int width,
                                             int height,
                                             float attachX,
                                             float attachY,
                                             float scale,
                                             PlayerStreakInfo info,
                                             float partialTicks) {
        float time = getTime(partialTicks);
        float[] rgba = StreakRenderHelper.resolveColor(info, time, 1.0F);

        int red = Mth.clamp(Math.round(rgba[0] * 255.0F), 0, 255);
        int green = Mth.clamp(Math.round(rgba[1] * 255.0F), 0, 255);
        int blue = Mth.clamp(Math.round(rgba[2] * 255.0F), 0, 255);

        float startX = x + 10.0F;
        float startY = attachY - scale * 0.22F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        ScreenIndustrialUpgrade.bindTexture(StreakRenderHelper.TEXTURE);

        drawStraightRibbonLayer(
                poseStack,
                x, y, width, height,
                startX, startY,
                attachX, attachY,
                red, green, blue,
                scale,
                1.8F,
                0.28F,
                time
        );

        drawStraightRibbonLayer(
                poseStack,
                x, y, width, height,
                startX, startY,
                attachX, attachY,
                red, green, blue,
                scale,
                1.0F,
                0.90F,
                time
        );

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void drawStraightRibbonLayer(PoseStack poseStack,
                                                int previewX,
                                                int previewY,
                                                int previewWidth,
                                                int previewHeight,
                                                float startX,
                                                float startY,
                                                float endX,
                                                float endY,
                                                int red,
                                                int green,
                                                int blue,
                                                float scale,
                                                float thicknessMultiplier,
                                                float alphaMultiplier,
                                                float time) {
        final int segments = 18;

        float dxFull = endX - startX;
        float dyFull = endY - startY;
        float len = Mth.sqrt(dxFull * dxFull + dyFull * dyFull);
        if (len < 0.0001F) {
            return;
        }

        float nx = -dyFull / len;
        float ny = dxFull / len;


        BufferBuilder buffer = Tesselator.getInstance().begin(QUADS, POSITION_TEX_COLOR);
        Matrix4f matrix = poseStack.last().pose();

        for (int i = 0; i < segments - 1; i++) {
            float t0 = i / (float) (segments - 1);
            float t1 = (i + 1) / (float) (segments - 1);

            float x0 = Mth.lerp(t0, startX, endX);
            float y0 = Mth.lerp(t0, startY, endY);

            float x1 = Mth.lerp(t1, startX, endX);
            float y1 = Mth.lerp(t1, startY, endY);

            float thickness0 = Mth.lerp(t0, scale * 0.28F, scale * 0.08F) * thicknessMultiplier;
            float thickness1 = Mth.lerp(t1, scale * 0.28F, scale * 0.08F) * thicknessMultiplier;

            float alpha0 = (0.45F + (1.0F - t0) * 0.55F) * alphaMultiplier;
            float alpha1 = (0.45F + (1.0F - t1) * 0.55F) * alphaMultiplier;

            int a0 = Mth.clamp(Math.round(alpha0 * 255.0F), 0, 255);
            int a1 = Mth.clamp(Math.round(alpha1 * 255.0F), 0, 255);

            float l0x = x0 - nx * thickness0;
            float l0y = y0 - ny * thickness0;
            float r0x = x0 + nx * thickness0;
            float r0y = y0 + ny * thickness0;

            float l1x = x1 - nx * thickness1;
            float l1y = y1 - ny * thickness1;
            float r1x = x1 + nx * thickness1;
            float r1y = y1 + ny * thickness1;

            if (allOutside(previewX, previewY, previewWidth, previewHeight, l0x, l0y, r0x, r0y, l1x, l1y, r1x, r1y)) {
                continue;
            }

            float u0 = t0 * 2.0F + time * 0.01F;
            float u1 = t1 * 2.0F + time * 0.01F;

            buffer.addVertex(matrix, l0x, l0y, 0.0F).setUv(u0, 1.0F).setColor(red, green, blue, a0);
            buffer.addVertex(matrix, l1x, l1y, 0.0F).setUv(u1, 1.0F).setColor(red, green, blue, a1);
            buffer.addVertex(matrix, r1x, r1y, 0.0F).setUv(u1, 0.0F).setColor(red, green, blue, a1);
            buffer.addVertex(matrix, r0x, r0y, 0.0F).setUv(u0, 0.0F).setColor(red, green, blue, a0);
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private static boolean allOutside(int x, int y, int width, int height,
                                      float ax, float ay,
                                      float bx, float by,
                                      float cx, float cy,
                                      float dx, float dy) {
        return outside(ax, ay, x, y, width, height)
                && outside(bx, by, x, y, width, height)
                && outside(cx, cy, x, y, width, height)
                && outside(dx, dy, x, y, width, height);
    }

    private static boolean outside(float px, float py, int x, int y, int width, int height) {
        return px < x + 1 || px > x + width - 1 || py < y + 1 || py > y + height - 1;
    }

    private static float getTime(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            return mc.level.getGameTime() + partialTicks;
        }
        return (Util.getMillis() % 100000L) / 50.0F;
    }
}