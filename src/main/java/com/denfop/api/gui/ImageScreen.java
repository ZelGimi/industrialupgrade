package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ImageScreen extends GuiElement<ImageScreen> {


    public ImageScreen(GuiCore<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY) {
        mouseX = gui.guiLeft();
        mouseY = gui.guiTop();
        gui.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/common2.png"));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y, 0, 0, width - 3, height - 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 3, mouseY + this.y, 252, 0, 4, height - 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y - 2 + this.height, 0, 252, width, 4);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 3, mouseY + this.y - 2 + this.height, 252, 252, 4, 4);

        gui.bindTexture();
    }

}
