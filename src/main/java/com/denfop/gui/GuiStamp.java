package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerStamp;
import net.minecraft.resources.ResourceLocation;

public class GuiStamp<T extends ContainerStamp> extends GuiIU<ContainerStamp> {

    public GuiStamp(ContainerStamp guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 66, 32, EnumTypeComponent.STAMP_PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));

    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
