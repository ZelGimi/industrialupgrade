package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImageResearchTableInterface extends GuiElement {


    public ImageResearchTableInterface(GuiCore<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    public void drawBackground(int mouseX, int mouseY) {
        mouseX = gui.guiLeft;
        mouseY = gui.guiTop;
        gui.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/guiresearch_table.png"));
        GlStateManager.color(1, 1, 1, 1);
        gui.drawTexturedModalRect(mouseX + this.x, mouseY + this.y, 0, 0, width - 5, height - 5);
        gui.drawTexturedModalRect(mouseX + this.x + width - 6, mouseY + this.y, 251, 0, 6, height - 5);
        gui.drawTexturedModalRect(mouseX + this.x, mouseY + this.y - 6 + this.height, 0, 250, width - 3, 6);
        gui.drawTexturedModalRect(mouseX + this.x + width - 7, mouseY + this.y - 6 + this.height, 250, 250, 7, 5);

        gui.bindTexture();
    }

}
