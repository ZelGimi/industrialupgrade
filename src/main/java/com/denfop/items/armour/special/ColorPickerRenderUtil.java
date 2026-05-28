package com.denfop.items.armour.special;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;

import static net.minecraft.client.gui.GuiComponent.fill;

public final class ColorPickerRenderUtil {

    private ColorPickerRenderUtil() {
    }

    public static void drawPanel(PoseStack poseStack, int x, int y, int width, int height, int fillColor, int borderColor) {
        fill(poseStack, x, y, x + width, y + height, fillColor);
        drawBorder(poseStack, x, y, width, height, borderColor);
    }

    public static void drawInsetPanel(PoseStack graphics, int x, int y, int width, int height, int fillColor, int borderColor) {
        fill(graphics, x, y, x + width, y + height, fillColor);
        drawBorder(graphics, x, y, width, height, borderColor);
        fill(graphics, x + 1, y + 1, x + width - 1, y + 2, 0x1FFFFFFF);
        fill(graphics, x + 1, y + 1, x + 2, y + height - 1, 0x14FFFFFF);
    }

    public static void drawBorder(PoseStack graphics, int x, int y, int width, int height, int color) {
        fill(graphics, x, y, x + width, y + 1, color);
        fill(graphics, x, y + height - 1, x + width, y + height, color);
        fill(graphics, x, y, x + 1, y + height, color);
        fill(graphics, x + width - 1, y, x + width, y + height, color);
    }

    public static void drawCheckerboard(PoseStack graphics, int x, int y, int width, int height) {
        final int size = 4;
        for (int yy = 0; yy < height; yy += size) {
            for (int xx = 0; xx < width; xx += size) {
                boolean odd = (((xx / size) + (yy / size)) & 1) == 0;
                int color = odd ? 0xFF2A2F38 : 0xFF20252E;
                fill(graphics, x + xx, y + yy, Math.min(x + xx + size, x + width), Math.min(y + yy + size, y + height), color);
            }
        }
    }

    public static void drawGradientRect(PoseStack poseStack, float x, float y, float width, float height,
                                        int topLeft, int topRight, int bottomRight, int bottomLeft) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Matrix4f matrix = poseStack.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        vertex(buffer, matrix, x, y + height, bottomLeft);
        vertex(buffer, matrix, x + width, y + height, bottomRight);
        vertex(buffer, matrix, x + width, y, topRight);
        vertex(buffer, matrix, x, y, topLeft);

        BufferUploader.drawWithShader(buffer.end());
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
        buffer.vertex(matrix, x, y, 0.0F).color(r, g, b, a).endVertex();
    }
}