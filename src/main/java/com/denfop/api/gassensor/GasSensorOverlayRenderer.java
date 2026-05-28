package com.denfop.api.gassensor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Locale;

public final class GasSensorOverlayRenderer {

    private static final int HUD_BG = 0xD0120904;
    private static final int HUD_BORDER = 0xFFE39B3A;
    private static final int HUD_BORDER_DARK = 0xFF5B3510;
    private static final int HUD_TEXT = 0xFFF3E7D1;
    private static final int HUD_TEXT_DIM = 0xFFD6C19D;

    private static final int MARKER_MAIN = 0xFFE39B3A;
    private static final int MARKER_SOFT = 0x99F2BE5A;
    private static final int MARKER_WHITE = 0xE6FFF8EE;

    private GasSensorOverlayRenderer() {
    }

    public static void render(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) {
            return;
        }

        GasSensorVeinEntry followed = GasSensorClientCache.getFollowedVein();
        if (followed == null) {
            return;
        }

        MarkerTarget target = resolveMarkerTarget(minecraft, followed);
        if (target == null) {
            return;
        }

        int panelW = 230;
        int panelH = 44;
        int x = (graphics.guiWidth() - panelW) / 2;
        int y = 8;

        graphics.fill(x, y, x + panelW, y + panelH, HUD_BG);
        graphics.fill(x, y, x + panelW, y + 1, HUD_BORDER);
        graphics.fill(x, y + panelH - 1, x + panelW, y + panelH, HUD_BORDER_DARK);
        graphics.fill(x, y, x + 1, y + panelH, HUD_BORDER);
        graphics.fill(x + panelW - 1, y, x + panelW, y + panelH, HUD_BORDER_DARK);

        double dx = target.x() - minecraft.player.getX();
        double dy = target.y() - minecraft.player.getY();
        double dz = target.z() - minecraft.player.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        float angleToTarget = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0D);
        float relativeYaw = Mth.wrapDegrees(angleToTarget - minecraft.player.getYRot());

        int cx = x + 18;
        int cy = y + 22;

        drawCompassBackground(graphics, cx, cy);
        drawCompassNeedle(graphics, cx, cy, relativeYaw);

        String name = Component.translatable(followed.getTranslationKey()).getString();
        String dist = String.format(Locale.ROOT, "%.1f m", distance);

        graphics.drawString(minecraft.font, trim(name, 168), x + 38, y + 11, HUD_TEXT, false);
        graphics.drawString(minecraft.font, dist, x + 38, y + 25, HUD_TEXT_DIM, false);
    }

    public static void renderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null || minecraft.options.hideGui) {
            return;
        }

        GasSensorVeinEntry followed = GasSensorClientCache.getFollowedVein();
        if (followed == null) {
            return;
        }

        MarkerTarget target = resolveMarkerTarget(minecraft, followed);
        if (target == null) {
            return;
        }

        Camera camera = event.getCamera();
        Vec3 cameraPos = camera.getPosition();

        float partialTick = event.getPartialTick().getGameTimeDeltaTicks();
        float pulse = 0.5F + 0.5F * Mth.sin((minecraft.level.getGameTime() + partialTick) * 0.18F);

        float beamHalf = 0.045F + pulse * 0.018F;
        float crossHalf = 0.28F + pulse * 0.10F;
        float topY = (float) target.y() + 1.15F + pulse * 0.20F;
        float baseY = (float) target.y() + 0.02F;
        float cx = (float) target.x();
        float cz = (float) target.z();

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        drawVerticalBeam(matrix, cx, baseY, cz, beamHalf, topY, MARKER_SOFT);
        drawGroundCross(matrix, cx, baseY, cz, crossHalf, 0.032F, MARKER_MAIN);
        drawDiamond(matrix, cx, topY + 0.08F, cz, 0.10F + pulse * 0.03F, MARKER_WHITE);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    private static MarkerTarget resolveMarkerTarget(Minecraft minecraft, GasSensorVeinEntry vein) {
        double bestDistSq = Double.MAX_VALUE;
        double bestX = vein.getBlockX() + 0.5D;
        double bestY = vein.getBlockY() + 0.5D;
        double bestZ = vein.getBlockZ() + 0.5D;

        List<GasSensorVeinEntry.PreviewCell> cells = vein.getPreviewCells();
        if (cells != null && !cells.isEmpty()) {
            for (GasSensorVeinEntry.PreviewCell cell : cells) {
                double x = vein.getBlockX() + cell.relativeX() + 0.5D;
                double y = vein.getBlockY() + cell.relativeY() + 0.5D;
                double z = vein.getBlockZ() + cell.relativeZ() + 0.5D;

                double dx = x - minecraft.player.getX();
                double dy = y - minecraft.player.getY();
                double dz = z - minecraft.player.getZ();
                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq < bestDistSq) {
                    bestDistSq = distSq;
                    bestX = x;
                    bestY = y;
                    bestZ = z;
                }
            }
        }

        return new MarkerTarget(bestX, bestY, bestZ);
    }

    private static String trim(String value, int px) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.font.plainSubstrByWidth(value, px);
    }

    private static void drawCompassBackground(GuiGraphics graphics, int cx, int cy) {
        graphics.fill(cx - 10, cy - 10, cx + 11, cy + 11, 0x40281408);
        graphics.fill(cx - 1, cy - 9, cx + 1, cy - 6, 0xFFFFFFFF);
        graphics.fill(cx - 1, cy + 6, cx + 1, cy + 9, 0xFFB78A3C);
        graphics.fill(cx - 9, cy - 1, cx - 6, cy + 1, 0xFFB78A3C);
        graphics.fill(cx + 6, cy - 1, cx + 9, cy + 1, 0xFFB78A3C);
        graphics.fill(cx - 1, cy - 1, cx + 1, cy + 1, 0xFFFFFFFF);
    }

    private static void drawCompassNeedle(GuiGraphics graphics, int cx, int cy, float relativeYaw) {
        float rad = (float) Math.toRadians(relativeYaw);
        float len = 7.5F;

        int tipX = cx + Math.round(Mth.sin(rad) * len);
        int tipY = cy - Math.round(Mth.cos(rad) * len);

        int tailX = cx - Math.round(Mth.sin(rad) * 4.0F);
        int tailY = cy + Math.round(Mth.cos(rad) * 4.0F);

        drawPointLine(graphics, tailX, tailY, tipX, tipY, MARKER_MAIN);
        graphics.fill(tipX - 2, tipY - 2, tipX + 3, tipY + 3, MARKER_WHITE);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void drawPointLine(GuiGraphics graphics, int x0, int y0, int x1, int y1, int color) {
        int steps = Math.max(Math.abs(x1 - x0), Math.abs(y1 - y0));
        if (steps <= 0) {
            graphics.fill(x0, y0, x0 + 1, y0 + 1, color);
            return;
        }

        for (int i = 0; i <= steps; i++) {
            float t = i / (float) steps;
            int x = Math.round(Mth.lerp(t, x0, x1));
            int y = Math.round(Mth.lerp(t, y0, y1));
            graphics.fill(x, y, x + 2, y + 2, color);
        }
    }

    private static void drawVerticalBeam(Matrix4f matrix, float cx, float y0, float cz, float half, float y1, int color) {
        float zHalf = half * 0.45F;
        float xHalf = half * 0.45F;

        drawQuad(
                matrix,
                cx - half, y0, cz - zHalf,
                cx + half, y1, cz + zHalf,
                color
        );

        drawQuad(
                matrix,
                cx - xHalf, y0, cz - half,
                cx + xHalf, y1, cz + half,
                color
        );
    }

    private static void drawGroundCross(Matrix4f matrix, float cx, float y, float cz, float half, float thickness, int color) {
        drawFlatQuad(
                matrix,
                cx - half, y, cz - thickness,
                cx + half, y, cz + thickness,
                color
        );

        drawFlatQuad(
                matrix,
                cx - thickness, y, cz - half,
                cx + thickness, y, cz + half,
                color
        );
    }

    private static void drawDiamond(Matrix4f matrix, float cx, float cy, float cz, float radius, int color) {
        int a = (color >>> 24) & 0xFF;
        int r = (color >>> 16) & 0xFF;
        int g = (color >>> 8) & 0xFF;
        int b = color & 0xFF;

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        builder.addVertex(matrix, cx, cy + radius, cz).setColor(r, g, b, a);
        builder.addVertex(matrix, cx - radius, cy, cz).setColor(r, g, b, 0);
        builder.addVertex(matrix, cx + radius, cy, cz).setColor(r, g, b, 0);

        builder.addVertex(matrix, cx, cy - radius, cz).setColor(r, g, b, a);
        builder.addVertex(matrix, cx - radius, cy, cz).setColor(r, g, b, 0);
        builder.addVertex(matrix, cx + radius, cy, cz).setColor(r, g, b, 0);

        builder.addVertex(matrix, cx, cy, cz + radius).setColor(r, g, b, a);
        builder.addVertex(matrix, cx, cy - radius, cz).setColor(r, g, b, 0);
        builder.addVertex(matrix, cx, cy + radius, cz).setColor(r, g, b, 0);

        builder.addVertex(matrix, cx, cy, cz - radius).setColor(r, g, b, a);
        builder.addVertex(matrix, cx, cy - radius, cz).setColor(r, g, b, 0);
        builder.addVertex(matrix, cx, cy + radius, cz).setColor(r, g, b, 0);

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }

    private static void drawQuad(Matrix4f matrix, float x0, float y0, float z0, float x1, float y1, float z1, int color) {
        int a = (color >>> 24) & 0xFF;
        int r = (color >>> 16) & 0xFF;
        int g = (color >>> 8) & 0xFF;
        int b = color & 0xFF;

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        builder.addVertex(matrix, x0, y0, z0).setColor(r, g, b, a);
        builder.addVertex(matrix, x0, y1, z0).setColor(r, g, b, a);
        builder.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a);
        builder.addVertex(matrix, x1, y0, z1).setColor(r, g, b, a);

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }

    private static void drawFlatQuad(Matrix4f matrix, float x0, float y, float z0, float x1, float y2, float z1, int color) {
        int a = (color >>> 24) & 0xFF;
        int r = (color >>> 16) & 0xFF;
        int g = (color >>> 8) & 0xFF;
        int b = color & 0xFF;

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        builder.addVertex(matrix, x0, y, z0).setColor(r, g, b, a);
        builder.addVertex(matrix, x0, y2, z1).setColor(r, g, b, a);
        builder.addVertex(matrix, x1, y2, z1).setColor(r, g, b, a);
        builder.addVertex(matrix, x1, y, z0).setColor(r, g, b, a);

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }

    private record MarkerTarget(double x, double y, double z) {
    }
}