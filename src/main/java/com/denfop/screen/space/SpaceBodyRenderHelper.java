package com.denfop.screen.space;

import com.denfop.screen.ScreenIndustrialUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public final class SpaceBodyRenderHelper {

    private SpaceBodyRenderHelper() {
    }

    public static void renderCubeBody(GuiGraphics graphics,
                                      ResourceLocation texture,
                                      float x,
                                      float y,
                                      float z,
                                      float radius,
                                      float rotY,
                                      float rotX,
                                      boolean selected,
                                      boolean hovered) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, z);
        pose.mulPose(Axis.XP.rotationDegrees(rotX));
        pose.mulPose(Axis.YP.rotationDegrees(rotY));
        pose.scale(radius, radius, radius);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        ScreenIndustrialUpgrade.bindTexture(texture);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        Matrix4f matrix = pose.last().pose();

        int alpha = 255;
        int boost = selected ? 55 : (hovered ? 28 : 0);

        face(buffer, matrix, -1, -1, -1, 1, -1, -1, 1, 1, -1, -1, 1, -1, 190 + boost, 190 + boost, 190 + boost, alpha); // front
        face(buffer, matrix, -1, -1, 1, -1, 1, 1, 1, 1, 1, 1, -1, 1, 140 + boost, 140 + boost, 140 + boost, alpha); // back
        face(buffer, matrix, -1, -1, -1, -1, 1, -1, -1, 1, 1, -1, -1, 1, 165 + boost, 165 + boost, 165 + boost, alpha); // left
        face(buffer, matrix, 1, -1, -1, 1, -1, 1, 1, 1, 1, 1, 1, -1, 215 + boost, 215 + boost, 215 + boost, alpha); // right
        face(buffer, matrix, -1, 1, -1, 1, 1, -1, 1, 1, 1, -1, 1, 1, 235 + boost, 235 + boost, 235 + boost, alpha); // top
        face(buffer, matrix, -1, -1, -1, -1, -1, 1, 1, -1, 1, 1, -1, -1, 125 + boost, 125 + boost, 125 + boost, alpha); // bottom

        tess.end();

        if (selected || hovered) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            BufferBuilder line = tess.getBuilder();
            line.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            int r = selected ? 0 : 110;
            int g = 255;
            int b = selected ? 40 : 255;
            cubeOutline(line, matrix, 1.15f, r, g, b, 255);
            tess.end();
        }

        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
        pose.popPose();
    }

    public static void renderRing(GuiGraphics graphics,
                                  float x,
                                  float y,
                                  float radiusX,
                                  float radiusY,
                                  boolean selected) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        PoseStack pose = graphics.pose();
        Matrix4f matrix = pose.last().pose();

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.getBuilder();
        buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        int segments = 64;
        int red = selected ? 80 : 255;
        int green = selected ? 255 : 220;
        int blue = selected ? 80 : 180;

        for (int i = 0; i <= segments; i++) {
            float a = (float) (Math.PI * 2.0 * i / segments);
            float px = x + (float) Math.cos(a) * radiusX;
            float py = y + (float) Math.sin(a) * radiusY;
            buffer.vertex(matrix, px, py, 0).color(red, green, blue, 180).endVertex();
        }

        tess.end();
        RenderSystem.disableBlend();
    }

    private static void face(BufferBuilder buffer,
                             Matrix4f matrix,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float x3, float y3, float z3,
                             float x4, float y4, float z4,
                             int r, int g, int b, int a) {
        buffer.vertex(matrix, x1, y1, z1).uv(0, 0).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z2).uv(1, 0).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x3, y3, z3).uv(1, 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x4, y4, z4).uv(0, 1).color(r, g, b, a).endVertex();
    }

    private static void cubeOutline(BufferBuilder buffer, Matrix4f matrix, float s, int r, int g, int b, int a) {
        v(buffer, matrix, -s, -s, -s, r, g, b, a, s, -s, -s);
        v(buffer, matrix, s, -s, -s, r, g, b, a, s, s, -s);
        v(buffer, matrix, s, s, -s, r, g, b, a, -s, s, -s);
        v(buffer, matrix, -s, s, -s, r, g, b, a, -s, -s, -s);

        v(buffer, matrix, -s, -s, s, r, g, b, a, s, -s, s);
        v(buffer, matrix, s, -s, s, r, g, b, a, s, s, s);
        v(buffer, matrix, s, s, s, r, g, b, a, -s, s, s);
        v(buffer, matrix, -s, s, s, r, g, b, a, -s, -s, s);

        v(buffer, matrix, -s, -s, -s, r, g, b, a, -s, -s, s);
        v(buffer, matrix, s, -s, -s, r, g, b, a, s, -s, s);
        v(buffer, matrix, s, s, -s, r, g, b, a, s, s, s);
        v(buffer, matrix, -s, s, -s, r, g, b, a, -s, s, s);
    }

    private static void v(BufferBuilder buffer,
                          Matrix4f matrix,
                          float x1, float y1, float z1,
                          int r, int g, int b, int a,
                          float x2, float y2, float z2) {
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).endVertex();
    }
}