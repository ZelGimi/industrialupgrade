package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerRadioactiveOreHandler;
import net.minecraft.resources.ResourceLocation;

public class GuiRadioactiveOreHandler<T extends ContainerRadioactiveOreHandler> extends GuiIU<ContainerRadioactiveOreHandler> {

    public GuiRadioactiveOreHandler(ContainerRadioactiveOreHandler guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 68, 34, EnumTypeComponent.LASER_PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
        this.addComponent(new GuiComponent(this, 60, 55, EnumTypeComponent.RAD,
                new Component<>((this.container.base).componentRadiation)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
