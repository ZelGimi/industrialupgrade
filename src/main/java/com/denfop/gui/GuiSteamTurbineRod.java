package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerSteamTurbineRod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSteamTurbineRod extends GuiIU<ContainerSteamTurbineRod> {


    public GuiSteamTurbineRod(ContainerSteamTurbineRod guiContainer) {
        super(guiContainer);
        this.xSize = 206;
        this.ySize = 256;
        this.inventory.setY(172);
        this.elements.add(new ImageInterface(this, 0, 0, this.xSize, this.ySize));

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");

    }


    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

}
