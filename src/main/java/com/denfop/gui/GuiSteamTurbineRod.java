package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerSteamTurbineRod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiSteamTurbineRod<T extends ContainerSteamTurbineRod> extends GuiIU<ContainerSteamTurbineRod> {


    public GuiSteamTurbineRod(ContainerSteamTurbineRod guiContainer) {
        super(guiContainer);
        this.imageWidth = 206;
        this.imageHeight = 256;
        this.inventory.setY(172);
        this.elements.add(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");

    }



    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
        this.bindTexture();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

}
