package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerAutoFuse;
import net.minecraft.resources.ResourceLocation;

public class GuiAutoFuse<T extends ContainerAutoFuse> extends GuiIU<ContainerAutoFuse> {

    public GuiAutoFuse(ContainerAutoFuse guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 70, 60, EnumTypeComponent.RAD,
                new Component<>((this.container.base).rad_energy)
        ));

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
