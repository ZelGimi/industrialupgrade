package com.denfop.api.widget;

import com.denfop.screen.ScreenIndustrialUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SpaceMainBlueInterfaceWidget extends ScreenWidget {


    public SpaceMainBlueInterfaceWidget(ScreenIndustrialUpgrade<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY) {

        ScreenIndustrialUpgrade.bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/gui_space_main_blue.png"));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y, 0, 0, width - 5, height - 5);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 6, mouseY + this.y, 251, 0, 5, height - 5);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x, mouseY + this.y - 6 + this.height, 0, 250, width - 3, 6);
        gui.drawTexturedModalRect(poseStack, mouseX + this.x + width - 7, mouseY + this.y - 6 + this.height, 250, 250, 6, 6);

        gui.bindTexture();
    }

}
