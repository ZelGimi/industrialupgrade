package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerDigger;
import net.minecraft.util.ResourceLocation;

public class GuiDigger extends GuiIU<ContainerDigger> {

    public GuiDigger(ContainerDigger guiContainer) {
        super(guiContainer);
        this.xSize = 206;
        this.ySize = 256;
        this.inventory.setY(172);
        this.inventory.setX(this.inventory.getX() + 18);
        this.addComponent(new GuiComponent(this, 60, 105, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.elements.add(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachinepng");

    }

}
