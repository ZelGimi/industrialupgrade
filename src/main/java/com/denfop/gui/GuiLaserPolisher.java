package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerLaserPolisher;
import net.minecraft.resources.ResourceLocation;

public class GuiLaserPolisher<T extends ContainerLaserPolisher> extends GuiIU<ContainerLaserPolisher> {

    public GuiLaserPolisher(ContainerLaserPolisher guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 68, 34, EnumTypeComponent.LASER_PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
