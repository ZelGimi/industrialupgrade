package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerSafe;
import net.minecraft.util.ResourceLocation;

public class GuiSafe extends GuiIU<ContainerSafe> {

    public GuiSafe(ContainerSafe guiContainer) {
        super(guiContainer);
        this.ySize = 232;
        this.inventory.setY(150);
        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guibags.png");
    }

}
