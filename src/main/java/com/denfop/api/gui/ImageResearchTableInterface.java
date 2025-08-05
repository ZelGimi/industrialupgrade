package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ImageResearchTableInterface extends GuiElement<ImageResearchTableInterface> {


    public ImageResearchTableInterface(GuiCore<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY) {
        mouseX = gui.guiLeft();
        mouseY = gui.guiTop();
        GuiCore.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guiresearch_table.png"));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y, 0, 0, width - 5, height - 5);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 6, mouseY + this.y, 251, 0, 6, height - 5);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y - 6 + this.height, 0, 250, width - 3, 6);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 7, mouseY + this.y - 6 + this.height, 250, 250, 7, 5);

        gui.bindTexture();
    }

}
