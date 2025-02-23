package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerHeatCirculationPump;
import net.minecraft.util.ResourceLocation;

public class GuiHeatCirculationPump extends GuiIU<ContainerHeatCirculationPump> {

    public GuiHeatCirculationPump(ContainerHeatCirculationPump guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 186;
        this.ySize = 211;
    }


    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 82, this.guiTop + 56, 189, 4, 22, 22);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat4.png");
    }

}
