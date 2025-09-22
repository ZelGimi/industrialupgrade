package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImageRocketPadScreen extends GuiElement {


    public ImageRocketPadScreen(GuiCore<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    public void drawBackground(int mouseX, int mouseY) {
        mouseX = gui.guiLeft;
        mouseY = gui.guiTop;
        gui.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/guirocket_pad.png"));
        GlStateManager.color(1, 1, 1, 1);
        gui.drawTexturedModalRect(mouseX + this.x, mouseY + this.y, 0, 0, width - 3, height - 1);
        gui.drawTexturedModalRect(mouseX + this.x + width - 3, mouseY + this.y, 252, 0, 4, height - 1);
        gui.drawTexturedModalRect(mouseX + this.x, mouseY + this.y - 2 + this.height, 0, 252, width, 4);
        gui.drawTexturedModalRect(mouseX + this.x + width - 3, mouseY + this.y - 2 + this.height, 252, 252, 4, 4);

        gui.bindTexture();
    }

}
