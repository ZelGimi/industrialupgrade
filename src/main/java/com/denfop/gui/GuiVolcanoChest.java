package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerVolcanoChest;
import net.minecraft.util.ResourceLocation;

public class GuiVolcanoChest extends GuiIU<ContainerVolcanoChest> {

    public GuiVolcanoChest(ContainerVolcanoChest guiContainer) {
        super(guiContainer);
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
