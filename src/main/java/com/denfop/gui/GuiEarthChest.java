package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerEarthChest;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiEarthChest extends GuiIU<ContainerEarthChest> {

    public GuiEarthChest(ContainerEarthChest guiContainer) {
        super(guiContainer);
    }
    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
