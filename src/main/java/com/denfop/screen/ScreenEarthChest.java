package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuEarthChest;
import net.minecraft.resources.ResourceLocation;

public class ScreenEarthChest<T extends ContainerMenuEarthChest> extends ScreenMain<ContainerMenuEarthChest> {

    public ScreenEarthChest(ContainerMenuEarthChest guiContainer) {
        super(guiContainer);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
