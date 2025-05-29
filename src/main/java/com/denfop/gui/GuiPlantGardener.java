package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerPlantGardener;
import net.minecraft.resources.ResourceLocation;

public class GuiPlantGardener<T extends ContainerPlantGardener> extends GuiIU<ContainerPlantGardener> {

    public GuiPlantGardener(ContainerPlantGardener guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 98, 65, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
