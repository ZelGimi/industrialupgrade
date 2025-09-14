package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuVolcanoChest;
import net.minecraft.resources.ResourceLocation;

public class ScreenVolcanoChest<T extends ContainerMenuVolcanoChest> extends ScreenMain<ContainerMenuVolcanoChest> {

    public ScreenVolcanoChest(ContainerMenuVolcanoChest guiContainer) {
        super(guiContainer);
    }

    protected ResourceLocation getTexture() {
        return  ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
