package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuSteamTurbineRod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenSteamTurbineRod<T extends ContainerMenuSteamTurbineRod> extends ScreenMain<ContainerMenuSteamTurbineRod> {


    public ScreenSteamTurbineRod(ContainerMenuSteamTurbineRod guiContainer) {
        super(guiContainer);
        this.componentList.clear();

    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guisteamturbine_blades.png");

    }


    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

}
