package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerHeatCirculationPump;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiHeatCirculationPump<T extends ContainerHeatCirculationPump> extends GuiIU<ContainerHeatCirculationPump> {

    public GuiHeatCirculationPump(ContainerHeatCirculationPump guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 186;
        this.imageHeight = 211;
    }


    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 82, this.guiTop + 56, 189, 4, 22, 22);

    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiheat4.png");
    }

}
