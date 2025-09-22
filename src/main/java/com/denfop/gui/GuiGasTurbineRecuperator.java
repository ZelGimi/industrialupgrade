package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerGasTurbineRecuperator;
import net.minecraft.util.ResourceLocation;

public class GuiGasTurbineRecuperator extends GuiIU<ContainerGasTurbineRecuperator> {

    public GuiGasTurbineRecuperator(ContainerGasTurbineRecuperator guiContainer) {
        super(guiContainer);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
