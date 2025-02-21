package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerAnalyzerChest;
import net.minecraft.util.ResourceLocation;


public class GuiAnalyzerChest extends GuiIU<ContainerAnalyzerChest> {

    public final ContainerAnalyzerChest container;

    public GuiAnalyzerChest(ContainerAnalyzerChest container1) {
        super(container1);
        this.container = container1;
        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));

    }


    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
