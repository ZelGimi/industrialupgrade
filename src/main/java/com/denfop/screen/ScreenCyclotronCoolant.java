package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TankWidget;
import com.denfop.containermenu.ContainerMenuCyclotronCoolant;
import net.minecraft.resources.ResourceLocation;

public class ScreenCyclotronCoolant<T extends ContainerMenuCyclotronCoolant> extends ScreenMain<ContainerMenuCyclotronCoolant> {

    public ScreenCyclotronCoolant(ContainerMenuCyclotronCoolant guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addWidget(TankWidget.createNormal(this, this.imageWidth / 2 - 10, 20, guiContainer.base.getCoolantTank()));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
