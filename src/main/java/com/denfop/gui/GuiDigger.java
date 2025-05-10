package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerDigger;
import net.minecraft.resources.ResourceLocation;

public class GuiDigger<T extends ContainerDigger> extends GuiIU<ContainerDigger> {

    public GuiDigger(ContainerDigger guiContainer) {
        super(guiContainer);
        this.imageWidth = 206;
        this.imageHeight = 256;
        this.inventory.setY(172);
        this.inventory.setX(this.inventory.getX() + 18);
        this.addComponent(new GuiComponent(this, 60, 105, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.elements.add(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");

    }

}
