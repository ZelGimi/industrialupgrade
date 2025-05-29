package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerReCirculationPump;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiReCirculationPump<T extends ContainerReCirculationPump> extends GuiIU<ContainerReCirculationPump> {

    public GuiReCirculationPump(ContainerReCirculationPump guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 186;
        this.imageHeight = 211;
    }



    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect( poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        this.drawTexturedModalRect( poseStack,this.guiLeft + 83, this.guiTop + 48, 188, 3, 22, 22);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigasreactor5.png");
    }

}
