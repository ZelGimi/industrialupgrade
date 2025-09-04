package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ImageInterfaceWidget;
import com.denfop.containermenu.ContainerMenuSafe;
import net.minecraft.resources.ResourceLocation;

public class ScreenSafe<T extends ContainerMenuSafe> extends ScreenMain<ContainerMenuSafe> {

    public ScreenSafe(ContainerMenuSafe guiContainer) {
        super(guiContainer);
        this.imageHeight = 232;
        this.inventory.setY(150);
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guibags.png");
    }

}
