package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerGasTurbineRecuperator;
import net.minecraft.resources.ResourceLocation;

public class GuiGasTurbineRecuperator<T extends ContainerGasTurbineRecuperator> extends GuiIU<ContainerGasTurbineRecuperator> {

    public GuiGasTurbineRecuperator(ContainerGasTurbineRecuperator guiContainer) {
        super(guiContainer);
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
