package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerCyclotronCryogen;
import net.minecraft.resources.ResourceLocation;

public class GuiCyclotronCryogen<T extends ContainerCyclotronCryogen> extends GuiIU<ContainerCyclotronCryogen> {

    public GuiCyclotronCryogen(ContainerCyclotronCryogen guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addElement(TankGauge.createNormal(this, this.imageWidth / 2 - 10, 20, guiContainer.base.getCryogenTank()));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
