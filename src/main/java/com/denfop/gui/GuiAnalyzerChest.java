package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerAnalyzerChest;
import net.minecraft.util.ResourceLocation;


public class GuiAnalyzerChest extends GuiCore<ContainerAnalyzerChest> {

    public final ContainerAnalyzerChest container;

    public GuiAnalyzerChest(ContainerAnalyzerChest container1) {
        super(container1);
        this.container = container1;
    }


    protected void drawForegroundLayer(int par1, int par2) {


    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guianalyzerchest.png");
    }

}
