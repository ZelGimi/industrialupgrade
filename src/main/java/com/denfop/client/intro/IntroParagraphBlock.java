package com.denfop.client.intro;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class IntroParagraphBlock extends IntroMarkdownBlock {

    private final String text;

    public IntroParagraphBlock(String text) {
        this.text = text;
    }

    @Override
    public int getHeight(int width, Font font, IntroImageManager imageManager) {
        return font.split(Component.literal(text), width).size() * font.lineHeight + 10;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, IntroImageManager imageManager, int x, int y, int width, int mouseX, int mouseY, float partialTick) {
        IntroDrawUtil.drawWrappedText(guiGraphics, font, text, x, y, width, 0xFFD4DCEA);
    }
}