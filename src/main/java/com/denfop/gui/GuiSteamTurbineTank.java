package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerSteamTurbineTank;
import net.minecraft.util.ResourceLocation;

public class GuiSteamTurbineTank extends GuiIU<ContainerSteamTurbineTank> {

    public GuiSteamTurbineTank(ContainerSteamTurbineTank guiContainer) {
        super(guiContainer);
        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        elements.add(TankGauge.createNormal(this, 80, 25, guiContainer.base.getTank()));
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
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
