package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ImageSpaceInterface3 extends GuiElement<ImageSpaceInterface3> {


    public ImageSpaceInterface3(GuiCore<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
        mouseX = gui.guiLeft();
        mouseY = gui.guiTop();
        GuiCore.bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/gui_space_main_green.png"));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y, 0, 0, width - 5, height - 5);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 6, mouseY + this.y, 251, 0, 5, height - 5);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y - 6 + this.height, 0, 250, width - 3, 6);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 7, mouseY + this.y - 6 + this.height, 250, 250, 6, 6);

        gui.bindTexture();
    }

}
