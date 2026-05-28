package com.denfop.client.intro;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.List;

public final class IntroDrawUtil {

    private IntroDrawUtil() {
    }

    public static void drawPanel(GuiGraphics guiGraphics, int x, int y, int width, int height, int fillColor, int borderColor) {
        guiGraphics.fill(x, y, x + width, y + height, fillColor);
        guiGraphics.fill(x, y, x + width, y + 1, borderColor);
        guiGraphics.fill(x, y + height - 1, x + width, y + height, borderColor);
        guiGraphics.fill(x, y, x + 1, y + height, borderColor);
        guiGraphics.fill(x + width - 1, y, x + width, y + height, borderColor);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static void drawGeneratedBadge(GuiGraphics guiGraphics, Font font, String label, int x, int y, int size) {
        int hash = label.hashCode();
        int base = 0xFF202A3A | (hash & 0x003F3F3F);
        int accent = 0xFF4F87FF | ((hash >> 3) & 0x000F0F0F);

        drawPanel(guiGraphics, x, y, size, size, base, accent);

        String initials = initials(label);
        int textWidth = font.width(initials);
        int textX = x + (size - textWidth) / 2;
        int textY = y + (size - font.lineHeight) / 2;
        guiGraphics.drawString(font, initials, textX, textY, 0xFFFFFFFF, false);
    }

    public static String initials(String label) {
        String[] parts = label.trim().split("\\s+");
        if (parts.length == 0) {
            return "?";
        }
        if (parts.length == 1) {
            String s = parts[0];
            return s.substring(0, Math.min(2, s.length())).toUpperCase();
        }
        String first = parts[0].isEmpty() ? "" : parts[0].substring(0, 1);
        String second = parts[1].isEmpty() ? "" : parts[1].substring(0, 1);
        return (first + second).toUpperCase();
    }

    public static void drawFittedImageBiased(
            GuiGraphics guiGraphics,
            IntroRemoteImage image,
            int x,
            int y,
            int boxWidth,
            int boxHeight,
            float verticalBias
    ) {
        if (!image.isReady() || image.getTiles().isEmpty() || boxWidth <= 0 || boxHeight <= 0) {
            return;
        }

        float scale = Math.min(boxWidth / (float) image.getWidth(), boxHeight / (float) image.getHeight());

        int finalWidth = Math.max(1, Math.round(image.getWidth() * scale));
        int finalHeight = Math.max(1, Math.round(image.getHeight() * scale));

        int freeX = boxWidth - finalWidth;
        int freeY = boxHeight - finalHeight;

        int drawX = x + freeX / 2;

        verticalBias = Mth.clamp(verticalBias, 0.0F, 1.0F);
        int drawY = y + Math.round(freeY * verticalBias);

        RenderSystem.enableBlend();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(drawX, drawY, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);

        for (IntroRemoteImage.Tile tile : image.getTiles()) {
            guiGraphics.blit(
                    tile.getTextureLocation(),
                    tile.getX(),
                    tile.getY(),
                    0.0F,
                    0.0F,
                    tile.getWidth(),
                    tile.getHeight(),
                    tile.getWidth(),
                    tile.getHeight()
            );
        }

        guiGraphics.pose().popPose();
    }

    public static void drawStretchedImage(
            GuiGraphics guiGraphics,
            IntroRemoteImage image,
            int x,
            int y,
            int boxWidth,
            int boxHeight
    ) {
        if (!image.isReady() || image.getTiles().isEmpty() || boxWidth <= 0 || boxHeight <= 0) {
            return;
        }

        float scaleX = boxWidth / (float) image.getWidth();
        float scaleY = boxHeight / (float) image.getHeight();

        RenderSystem.enableBlend();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0.0F);
        guiGraphics.pose().scale(scaleX, scaleY, 1.0F);

        for (IntroRemoteImage.Tile tile : image.getTiles()) {
            guiGraphics.blit(
                    tile.getTextureLocation(),
                    tile.getX(),
                    tile.getY(),
                    0.0F,
                    0.0F,
                    tile.getWidth(),
                    tile.getHeight(),
                    tile.getWidth(),
                    tile.getHeight()
            );
        }

        guiGraphics.pose().popPose();
    }

    public static void drawCoverImage(
            GuiGraphics guiGraphics,
            IntroRemoteImage image,
            int x,
            int y,
            int boxWidth,
            int boxHeight
    ) {
        if (!image.isReady() || image.getTiles().isEmpty() || boxWidth <= 0 || boxHeight <= 0) {
            return;
        }

        float scale = Math.max(boxWidth / (float) image.getWidth(), boxHeight / (float) image.getHeight());

        int finalWidth = Math.max(1, Math.round(image.getWidth() * scale));
        int finalHeight = Math.max(1, Math.round(image.getHeight() * scale));

        int drawX = x + (boxWidth - finalWidth) / 2;
        int drawY = y + (boxHeight - finalHeight) / 2;

        RenderSystem.enableBlend();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(drawX, drawY, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);

        for (IntroRemoteImage.Tile tile : image.getTiles()) {
            guiGraphics.blit(
                    tile.getTextureLocation(),
                    tile.getX(),
                    tile.getY(),
                    0.0F,
                    0.0F,
                    tile.getWidth(),
                    tile.getHeight(),
                    tile.getWidth(),
                    tile.getHeight()
            );
        }

        guiGraphics.pose().popPose();
    }

    public static void renderImageOrFallback(
            GuiGraphics guiGraphics,
            Font font,
            IntroImageManager imageManager,
            String label,
            String source,
            int x,
            int y,
            int width,
            int height
    ) {
        IntroRemoteImage image = imageManager.get(source);
        if (image.isReady()) {
            drawFittedImage(guiGraphics, image, x, y, width, height);
            return;
        }

        drawGeneratedBadge(guiGraphics, font, label, x, y, Math.min(width, height));
    }

    public static void renderChipIcon(
            GuiGraphics guiGraphics,
            Font font,
            IntroImageManager imageManager,
            String iconSource,
            String label,
            int x,
            int y,
            int size
    ) {
        if (iconSource != null && !iconSource.isBlank()) {
            IntroRemoteImage image = imageManager.get(iconSource);
            if (image.isReady()) {
                drawFittedImage(guiGraphics, image, x, y, size, size);
                return;
            }
        }

        drawGeneratedBadge(guiGraphics, font, label, x, y, size);
    }

    public static void drawFittedImage(
            GuiGraphics guiGraphics,
            IntroRemoteImage image,
            int x,
            int y,
            int boxWidth,
            int boxHeight
    ) {
        if (!image.isReady() || image.getTiles().isEmpty() || boxWidth <= 0 || boxHeight <= 0) {
            return;
        }

        float scale = boxWidth / (float) image.getWidth();
        float scale1 = boxHeight / (float) image.getHeight();
        int finalWidth = Math.max(1, Math.round(image.getWidth() * scale));
        int finalHeight = Math.max(1, Math.round(image.getHeight() * scale));

        int drawX = x + (boxWidth - finalWidth) / 2;
        int drawY = y + (boxHeight - finalHeight) / 2;

        RenderSystem.enableBlend();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(drawX, drawY, 0.0F);
        guiGraphics.pose().scale(scale, scale1, 1.0F);

        for (IntroRemoteImage.Tile tile : image.getTiles()) {
            guiGraphics.blit(
                    tile.getTextureLocation(),
                    tile.getX(),
                    tile.getY(),
                    0.00f, 0.00f,
                    tile.getWidth(),
                    tile.getHeight(),
                    tile.getWidth(),
                    tile.getHeight()
            );
        }

        guiGraphics.pose().popPose();
    }

    public static int drawWrappedText(
            GuiGraphics guiGraphics,
            Font font,
            String text,
            int x,
            int y,
            int width,
            int color
    ) {
        List<net.minecraft.util.FormattedCharSequence> lines = font.split(Component.literal(text), width);
        int yy = y;
        for (net.minecraft.util.FormattedCharSequence line : lines) {
            guiGraphics.drawString(font, line, x, yy, color, false);
            yy += font.lineHeight;
        }
        return yy - y;
    }

    public static int drawScaledWrappedText(
            GuiGraphics guiGraphics,
            Font font,
            String text,
            int x,
            int y,
            int width,
            int color,
            float scale
    ) {
        int wrapWidth = Math.max(10, Mth.floor(width / scale));
        List<net.minecraft.util.FormattedCharSequence> lines = font.split(Component.literal(text), wrapWidth);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);

        int localY = 0;
        for (net.minecraft.util.FormattedCharSequence line : lines) {
            guiGraphics.drawString(font, line, 0, localY, color, false);
            localY += font.lineHeight;
        }

        guiGraphics.pose().popPose();
        return Mth.ceil(localY * scale);
    }

    public static int getWrappedHeight(Font font, String text, int width) {
        return font.split(Component.literal(text), Math.max(1, width)).size() * font.lineHeight;
    }
}