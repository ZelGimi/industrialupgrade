package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.tiles.mechanism.steamboiler.TileEntitySteamExchangerBoiler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSteamBoilerExchanger extends GuiIU<ContainerDefaultMultiElement> {

    public GuiSteamBoilerExchanger(ContainerDefaultMultiElement guiContainer) {
        super(guiContainer);
        this.componentList.clear();

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.color(1, 1, 1, 1);
        bindTexture();
        if (((TileEntitySteamExchangerBoiler) container.base).isWork()) {
            this.drawTexturedModalRect(this.guiLeft + 129, this.guiTop + 13, 243, 1, 12, 64);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteamboilerexchanger.png");
    }

}
