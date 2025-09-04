package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuCyclotronChamber;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenCyclotronChamber<T extends ContainerMenuCyclotronChamber> extends ScreenMain<ContainerMenuCyclotronChamber> {

    public ScreenCyclotronChamber(ContainerMenuCyclotronChamber guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft + 80, this.guiTop + 44, 237, 1, 18, 18);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
