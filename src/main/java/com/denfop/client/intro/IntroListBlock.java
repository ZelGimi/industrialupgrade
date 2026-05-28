package com.denfop.client.intro;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class IntroListBlock extends IntroMarkdownBlock {

    private final List<String> items;

    public IntroListBlock(List<String> items) {
        this.items = List.copyOf(items);
    }

    @Override
    public int getHeight(int width, Font font, IntroImageManager imageManager) {
        int total = 0;
        int innerWidth = Math.max(20, width - 12);
        for (String item : items) {
            total += font.split(Component.literal(item), innerWidth).size() * font.lineHeight;
        }
        return total + 6;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, IntroImageManager imageManager, int x, int y, int width, int mouseX, int mouseY, float partialTick) {
        int yy = y;
        int textX = x + 12;
        int innerWidth = Math.max(20, width - 12);

        for (String item : items) {
            guiGraphics.drawString(font, "•", x, yy, 0xFF7EB6FF, false);
            List<net.minecraft.util.FormattedCharSequence> lines = font.split(Component.literal(item), innerWidth);
            for (net.minecraft.util.FormattedCharSequence line : lines) {
                guiGraphics.drawString(font, line, textX, yy, 0xFFD4DCEA, false);
                yy += font.lineHeight;
            }
        }
    }
}