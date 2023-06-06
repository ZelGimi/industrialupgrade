package com.denfop.items.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class MyFontRenderer extends FontRenderer {

    private static final ResourceLocation FONT_TEXTURE = new ResourceLocation("textures/font/ascii.png");


    public MyFontRenderer(Minecraft mc) {
        super(mc.gameSettings, FONT_TEXTURE, mc.getTextureManager(), false);
    }

    public MyFontRenderer(FontRenderer fontRenderer) {
        super(
                Minecraft.getMinecraft().gameSettings,
                fontRenderer.locationFontTexture,
                fontRenderer.renderEngine,
                fontRenderer.getUnicodeFlag()
        );
    }

    public int getCharWidth(char character) {
        return super.getCharWidth(character);
    }


    // Другие переопределенные методы FontRenderer...

}
