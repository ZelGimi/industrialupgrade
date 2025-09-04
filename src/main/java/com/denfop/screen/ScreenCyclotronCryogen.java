package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TankWidget;
import com.denfop.containermenu.ContainerMenuCyclotronCryogen;
import net.minecraft.resources.ResourceLocation;

public class ScreenCyclotronCryogen<T extends ContainerMenuCyclotronCryogen> extends ScreenMain<ContainerMenuCyclotronCryogen> {

    public ScreenCyclotronCryogen(ContainerMenuCyclotronCryogen guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addWidget(TankWidget.createNormal(this, this.imageWidth / 2 - 10, 20, guiContainer.base.getCryogenTank()));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
