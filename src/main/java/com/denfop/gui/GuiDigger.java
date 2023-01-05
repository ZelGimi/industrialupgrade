package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerDigger;
import net.minecraft.util.ResourceLocation;

public class GuiDigger extends GuiIU<ContainerDigger> {

    public GuiDigger(ContainerDigger guiContainer) {
        super(guiContainer);
        this.xSize = 206;
        this.ySize = 256;
        this.inventory.setY(172);
        this.addComponent(new GuiComponent(this, 50, 161, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiautodigger.png");

    }

}
