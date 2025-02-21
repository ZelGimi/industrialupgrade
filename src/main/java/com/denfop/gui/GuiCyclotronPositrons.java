package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerPositrons;
import net.minecraft.util.ResourceLocation;

public class GuiCyclotronPositrons extends GuiIU<ContainerPositrons> {

    public GuiCyclotronPositrons(ContainerPositrons guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 72, 40, EnumTypeComponent.POSITRONS,
                new Component<>(this.container.base.getPositrons())
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
