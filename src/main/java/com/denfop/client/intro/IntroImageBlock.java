package com.denfop.client.intro;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class IntroImageBlock extends IntroMarkdownBlock {

    private static final int MIN_PLACEHOLDER_HEIGHT = 110;
    private static final int MIN_RENDER_HEIGHT = 80;
    private static final int MAX_RENDER_HEIGHT = 8192;

    private final String altText;
    private final String source;

    public IntroImageBlock(String altText, String source) {
        this.altText = altText == null ? "" : altText.trim();
        this.source = source.trim();
    }

    public String getAltText() {
        return altText;
    }

    public String getSource() {
        return source;
    }

    @Override
    public int getHeight(int width, Font font, IntroImageManager imageManager) {
        return resolveMetrics(width, imageManager).height();
    }

    @Override
    public void render(
            GuiGraphics guiGraphics,
            Font font,
            IntroImageManager imageManager,
            int x,
            int y,
            int width,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        RenderMetrics metrics = resolveMetrics(width, imageManager);
        IntroRemoteImage image = imageManager.get(source);

        int drawX = x;
        int drawY = y;

        if (image.isReady()) {
            IntroDrawUtil.drawFittedImage(guiGraphics, image, drawX, drawY, metrics.width(), metrics.height());
            return;
        }

        IntroDrawUtil.drawPanel(
                guiGraphics,
                drawX,
                drawY,
                metrics.width(),
                metrics.height(),
                0xFF1A2330,
                0xFF3C506D
        );

        String status = image.isFailed()
                ? "Image failed to load / no internet / unavailable"
                : "Loading image...";

        int textX = drawX + 8;
        int textY = drawY + 10;
        int textW = Math.max(40, metrics.width() - 16);

        textY += IntroDrawUtil.drawWrappedText(guiGraphics, font, status, textX, textY, textW, 0xFFFFC66D) + 6;

        if (!altText.isBlank()) {
            IntroDrawUtil.drawWrappedText(guiGraphics, font, altText, textX, textY, textW, 0xFFDDE6F4);
        }
    }

    private RenderMetrics resolveMetrics(int availableWidth, IntroImageManager imageManager) {
        IntroRemoteImage image = imageManager.get(source);

        if (!image.isReady()) {
            int placeholderHeight = Math.max(
                    MIN_PLACEHOLDER_HEIGHT,
                    (int) Math.round(availableWidth * 9.0D / 16.0D)
            );
            return new RenderMetrics(availableWidth, placeholderHeight);
        }

        int texW = Math.max(1, image.getWidth());
        int texH = Math.max(1, image.getHeight());

        int drawW = availableWidth;
        int drawH = (int) Math.round(drawW * (double) texH / texW);
        drawH = Mth.clamp(drawH, MIN_RENDER_HEIGHT, MAX_RENDER_HEIGHT);

        return new RenderMetrics(drawW, drawH);
    }

    private record RenderMetrics(int width, int height) {
    }
}