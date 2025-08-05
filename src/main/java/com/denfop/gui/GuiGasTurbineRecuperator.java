package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerGasTurbineRecuperator;
import net.minecraft.resources.ResourceLocation;

public class GuiGasTurbineRecuperator<T extends ContainerGasTurbineRecuperator> extends GuiIU<ContainerGasTurbineRecuperator> {

    public GuiGasTurbineRecuperator(ContainerGasTurbineRecuperator guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guigas_turbine_recuperator.png");
    }

}
