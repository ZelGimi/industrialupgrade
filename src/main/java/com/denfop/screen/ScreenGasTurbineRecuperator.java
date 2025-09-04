package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuGasTurbineRecuperator;
import net.minecraft.resources.ResourceLocation;

public class ScreenGasTurbineRecuperator<T extends ContainerMenuGasTurbineRecuperator> extends ScreenMain<ContainerMenuGasTurbineRecuperator> {

    public ScreenGasTurbineRecuperator(ContainerMenuGasTurbineRecuperator guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guigas_turbine_recuperator.png");
    }

}
