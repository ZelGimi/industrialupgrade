package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerSafe;
import net.minecraft.resources.ResourceLocation;

public class GuiSafe<T extends ContainerSafe> extends GuiIU<ContainerSafe> {

    public GuiSafe(ContainerSafe guiContainer) {
        super(guiContainer);
        this.imageHeight = 232;
        this.inventory.setY(150);
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guibags.png");
    }

}
