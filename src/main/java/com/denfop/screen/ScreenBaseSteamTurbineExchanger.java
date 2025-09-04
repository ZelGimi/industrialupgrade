package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuBaseSteamTurbineExchanger;
import net.minecraft.resources.ResourceLocation;

public class ScreenBaseSteamTurbineExchanger<T extends ContainerMenuBaseSteamTurbineExchanger> extends ScreenMain<ContainerMenuBaseSteamTurbineExchanger> {

    public ScreenBaseSteamTurbineExchanger(ContainerMenuBaseSteamTurbineExchanger guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteamturbine_exchanger.png");
    }

}
