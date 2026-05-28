package com.denfop.client.intro;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

/**
 * Small compatibility layer for Minecraft Forge 1.19.2.
 * <p>
 * The original intro UI was written against the newer GuiGraphics API.
 * Minecraft 1.19.2 still renders screens with PoseStack, so this wrapper
 * exposes only the methods used by the intro screens without changing the
 * rest of the UI architecture.
 */
public final class IntroGuiGraphics {

    private final PoseStack poseStack;

    public IntroGuiGraphics(PoseStack poseStack) {
        this.poseStack = poseStack;
    }

    public PoseStack pose() {
        return poseStack;
    }

    public void fill(int minX, int minY, int maxX, int maxY, int color) {
        GuiComponent.fill(poseStack, minX, minY, maxX, maxY, color);
    }

    public int drawString(Font font, String text, int x, int y, int color, boolean shadow) {
        if (shadow) {
            return font.drawShadow(poseStack, text, x, y, color);
        }
        return font.draw(poseStack, text, x, y, color);
    }

    public int drawString(Font font, FormattedCharSequence text, int x, int y, int color, boolean shadow) {
        if (shadow) {
            return font.drawShadow(poseStack, text, x, y, color);
        }
        return font.draw(poseStack, text, x, y, color);
    }

    public void blit(
            ResourceLocation texture,
            int x,
            int y,
            float uOffset,
            float vOffset,
            int width,
            int height,
            int textureWidth,
            int textureHeight
    ) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        GuiComponent.blit(poseStack, x, y, 0, uOffset, vOffset, width, height, textureWidth, textureHeight);
    }


    public void enableScissor(int x, int y, int endX, int endY) {
        Minecraft minecraft = Minecraft.getInstance();
        double scale = minecraft.getWindow().getGuiScale();
        int framebufferHeight = minecraft.getWindow().getHeight();

        int scissorX = (int) Math.floor(x * scale);
        int scissorY = (int) Math.floor(framebufferHeight - endY * scale);
        int scissorW = Math.max(0, (int) Math.ceil((endX - x) * scale));
        int scissorH = Math.max(0, (int) Math.ceil((endY - y) * scale));

        RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);
    }

    public void disableScissor() {
        RenderSystem.disableScissor();
    }
}
