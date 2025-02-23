package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerAutoOpenBox;
import net.minecraft.util.ResourceLocation;

public class GuiAutoOpenBox extends GuiIU<ContainerAutoOpenBox> {

    public GuiAutoOpenBox(ContainerAutoOpenBox guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 154, 65, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
