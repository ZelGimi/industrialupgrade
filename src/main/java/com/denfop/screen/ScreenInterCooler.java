package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuInterCooler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenInterCooler<T extends ContainerMenuInterCooler> extends ScreenMain<ContainerMenuInterCooler> {

    public ScreenInterCooler(ContainerMenuInterCooler guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 186;
        this.imageHeight = 211;
    }


    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 83, this.guiTop + 48, 188, 3, 22, 22);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guigasreactor5.png");
    }

}
