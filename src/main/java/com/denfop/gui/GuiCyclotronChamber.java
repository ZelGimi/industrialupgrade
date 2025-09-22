package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerCyclotronChamber;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiCyclotronChamber extends GuiIU<ContainerCyclotronChamber> {

    public GuiCyclotronChamber(ContainerCyclotronChamber guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.color(1, 1, 1, 1);
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 44, 237, 1, 18, 18);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
