package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.tiles.mechanism.steamboiler.TileEntitySteamExchangerBoiler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiSteamBoilerExchanger<T extends ContainerDefaultMultiElement> extends GuiIU<ContainerDefaultMultiElement> {

    public GuiSteamBoilerExchanger(ContainerDefaultMultiElement guiContainer) {
        super(guiContainer);
        this.componentList.clear();

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        bindTexture();
        if (((TileEntitySteamExchangerBoiler) container.base).isWork()) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 129, this.guiTop + 13, 243, 1, 12, 64);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisteamboilerexchanger.png");
    }

}
