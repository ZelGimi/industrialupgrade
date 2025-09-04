package com.denfop.api.widget;

import com.denfop.screen.ScreenIndustrialUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ImageInterfaceWidget extends ScreenWidget {


    public ImageInterfaceWidget(ScreenIndustrialUpgrade<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
        mouseX = gui.guiLeft();
        mouseY = gui.guiTop();
        ScreenIndustrialUpgrade.bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/common3.png"));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y, 0, 0, width - 4, height - 4);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 4, mouseY + this.y, 252, 0, 4, height - 4);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y - 4 + this.height, 0, 252, width, 4);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 4, mouseY + this.y - 4 + this.height, 252, 252, 4, 4);

        gui.bindTexture();
    }

}
