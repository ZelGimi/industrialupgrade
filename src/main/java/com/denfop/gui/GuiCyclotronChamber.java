package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerCyclotronChamber;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiCyclotronChamber<T extends ContainerCyclotronChamber> extends GuiIU<ContainerCyclotronChamber> {

    public GuiCyclotronChamber(ContainerCyclotronChamber guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft + 80, this.guiTop + 44, 237, 1, 18, 18);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
