package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerBaseSteamTurbineExchanger;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiBaseSteamTurbineExchanger extends GuiIU<ContainerBaseSteamTurbineExchanger> {

    public GuiBaseSteamTurbineExchanger(ContainerBaseSteamTurbineExchanger guiContainer) {
        super(guiContainer);
        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));

    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

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
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/пгшьфсршту.png");
    }

}
