package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerCyclotronCoolant;
import net.minecraft.resources.ResourceLocation;

public class GuiCyclotronCoolant<T extends ContainerCyclotronCoolant> extends GuiIU<ContainerCyclotronCoolant> {

    public GuiCyclotronCoolant(ContainerCyclotronCoolant guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addElement(TankGauge.createNormal(this, this.imageWidth / 2 - 10, 20, guiContainer.base.getCoolantTank()));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
