package com.denfop.client.intro;

import net.minecraft.client.gui.Font;

public abstract class IntroMarkdownBlock {

    public abstract int getHeight(int width, Font font, IntroImageManager imageManager);

    public abstract void render(
            IntroGuiGraphics guiGraphics,
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