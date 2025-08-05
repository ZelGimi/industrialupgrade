package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerBrewingPlant;
import net.minecraft.resources.ResourceLocation;

public class GuiBrewingPlant<T extends ContainerBrewingPlant> extends GuiIU<ContainerBrewingPlant> {

    public GuiBrewingPlant(ContainerBrewingPlant guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 71, 34, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
