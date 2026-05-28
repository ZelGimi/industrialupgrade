package com.denfop.client.intro;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class IntroHeadingBlock extends IntroMarkdownBlock {

    private final int level;
    private final String text;

    public IntroHeadingBlock(int level, String text) {
        this.level = Math.max(1, Math.min(level, 6));
        this.text = text;
    }

    private float getScale() {
        return switch (level) {
            case 1 -> 1.65F;
            case 2 -> 1.45F;
            case 3 -> 1.25F;
            case 4 -> 1.12F;
            default -> 1.0F;
        };
    }

    @Override
    public int getHeight(int width, Font font, IntroImageManager imageManager) {
        float scale = getScale();
        int wrapped = font.split(net.minecraft.network.chat.Component.literal(text), Mth.floor(width / scale)).size();
        return Mth.ceil(wrapped * font.lineHeight * scale) + 12;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, IntroImageManager imageManager, int x, int y, int width, int mouseX, int mouseY, float partialTick) {
        IntroDrawUtil.drawScaledWrappedText(guiGraphics, font, text, x, y, width, 0xFFEAF2FF, getScale());
    }
}