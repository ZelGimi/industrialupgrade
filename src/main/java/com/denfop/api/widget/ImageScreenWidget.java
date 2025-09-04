package com.denfop.api.widget;

import com.denfop.screen.ScreenIndustrialUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ImageScreenWidget extends ScreenWidget {


    public ImageScreenWidget(ScreenIndustrialUpgrade<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
        mouseX = gui.guiLeft();
        mouseY = gui.guiTop();
        gui.bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/common2.png"));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y, 0, 0, width - 3, height - 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 3, mouseY + this.y, 252, 0, 4, height - 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y - 2 + this.height, 0, 252, width, 4);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 3, mouseY + this.y - 2 + this.height, 252, 252, 4, 4);

        gui.bindTexture();
    }

}
