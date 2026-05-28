package com.denfop.items.armour.special;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public final class ColorPickerRenderUtil {

    private ColorPickerRenderUtil() {
    }

    public static void drawPanel(GuiGraphics graphics, int x, int y, int width, int height, int fillColor, int borderColor) {
        graphics.fill(x, y, x + width, y + height, fillColor);
        drawBorder(graphics, x, y, width, height, borderColor);
    }

    public static void drawInsetPanel(GuiGraphics graphics, int x, int y, int width, int height, int fillColor, int borderColor) {
        graphics.fill(x, y, x + width, y + height, fillColor);
        drawBorder(graphics, x, y, width, height, borderColor);
        graphics.fill(x + 1, y + 1, x + width - 1, y + 2, 0x1FFFFFFF);
        graphics.fill(x + 1, y + 1, x + 2, y + height - 1, 0x14FFFFFF);
    }

    public static void drawBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + 1, color);
        graphics.fill(x, y + height - 1, x + width, y + height, color);
        graphics.fill(x, y, x + 1, y + height, color);
        graphics.fill(x + width - 1, y, x + width, y + height, color);
    }

    public static void drawCheckerboard(GuiGraphics graphics, int x, int y, int width, int height) {
        final int size = 4;
        for (int yy = 0; yy < height; yy += size) {
            for (int xx = 0; xx < width; xx += size) {
                boolean odd = (((xx / size) + (yy / size)) & 1) == 0;
                int color = odd ? 0xFF2A2F38 : 0xFF20252E;
                graphics.fill(x + xx, y + yy, Math.min(x + xx + size, x + width), Math.min(y + yy + size, y + height), color);
            }
        }
    }

    public static void drawGradientRect(PoseStack poseStack, float x, float y, float width, float height,
                                        int topLeft, int topRight, int bottomRight, int bottomLeft) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Matrix4f matrix = poseStack.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        vertex(buffer, matrix, x, y + height, bottomLeft);
        vertex(buffer, matrix, x + width, y + height, bottomRight);
        vertex(buffer, matrix, x + width, y, topRight);
        vertex(buffer, matrix, x, y, topLeft);

        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.disableBlend();
    }

    public static void drawHorizontalGradient(PoseStack poseStack, float x, float y, float width, float height, int leftColor, int rightColor) {
        drawGradientRect(poseStack, x, y, width, height, leftColor, rightColor, rightColor, leftColor);
    }

    public static void drawVerticalGradient(PoseStack poseStack, float x, float y, float width, float height, int topColor, int bottomColor) {
        drawGradientRect(poseStack, x, y, width, height, topColor, topColor, bottomColor, bottomColor);
    }

    private static void vertex(BufferBuilder buffer, Matrix4f matrix, float x, float y, int argb) {
        float a = ((argb >> 24) & 0xFF) / 255.0F;
        float r = ((argb >> 16) & 0xFF) / 255.0F;
        float g = ((argb >> 8) & 0xFF) / 255.0F;
        float b = (argb & 0xFF) / 255.0F;
        buffer.addVertex(matrix, x, y, 0.0F).setColor(r, g, b, a);
    }
}