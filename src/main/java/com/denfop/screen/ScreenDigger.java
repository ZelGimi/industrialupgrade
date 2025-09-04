package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ImageInterfaceWidget;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuDigger;
import net.minecraft.resources.ResourceLocation;

public class ScreenDigger<T extends ContainerMenuDigger> extends ScreenMain<ContainerMenuDigger> {

    public ScreenDigger(ContainerMenuDigger guiContainer) {
        super(guiContainer);
        this.imageWidth = 206;
        this.imageHeight = 256;
        this.inventory.setY(172);
        this.inventory.setX(this.inventory.getX() + 18);
        this.addComponent(new ScreenWidget(this, 60, 105, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.elements.add(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");

    }

}
