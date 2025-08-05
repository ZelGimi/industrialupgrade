package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerEarthChest;
import net.minecraft.resources.ResourceLocation;

public class GuiEarthChest<T extends ContainerEarthChest> extends GuiIU<ContainerEarthChest> {

    public GuiEarthChest(ContainerEarthChest guiContainer) {
        super(guiContainer);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
