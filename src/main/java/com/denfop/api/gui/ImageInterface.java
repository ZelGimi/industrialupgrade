package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImageInterface extends GuiElement<ImageInterface> {


    public ImageInterface(GuiCore<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    public void drawBackground(int mouseX, int mouseY) {
        mouseX = gui.guiLeft;
        mouseY = gui.guiTop;
        gui.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/common3.png"));
        GlStateManager.color(1, 1, 1, 1);
        gui.drawTexturedModalRect(mouseX + this.x, mouseY + this.y, 0, 0, width - 4, height - 4);
        gui.drawTexturedModalRect(mouseX + this.x + width - 4, mouseY + this.y, 252, 0, 4, height - 4);
        gui.drawTexturedModalRect(mouseX + this.x, mouseY + this.y - 4 + this.height, 0, 252, width, 4);
        gui.drawTexturedModalRect(mouseX + this.x + width - 4, mouseY + this.y - 4 + this.height, 252, 252, 4, 4);

        gui.bindTexture();
    }

}
