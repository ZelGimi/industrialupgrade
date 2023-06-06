package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerSolar;
import net.minecraft.util.ResourceLocation;

public class GuiSolar extends GuiIU<ContainerSolar> {

    public GuiSolar(ContainerSolar guiContainer) {
        super(guiContainer);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());

        if (this.container.base.sunIsUp && this.container.base.skyIsVisible && !this.container.base.rain) {
            drawTexturedModalRect(xoffset + 80, yoffset + 44, 176, 0, 14, 14);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisolargenerator.png");
    }

}
