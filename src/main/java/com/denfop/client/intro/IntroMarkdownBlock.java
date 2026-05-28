package com.denfop.client.intro;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public abstract class IntroMarkdownBlock {

    public abstract int getHeight(int width, Font font, IntroImageManager imageManager);

    public abstract void render(
            GuiGraphics guiGraphics,
            Font font,
            IntroImageManager imageManager,
            int x,
            int y,
            int width,
            int mouseX,
            int mouseY,
            float partialTick
    );
}