package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerAlkalineEarthQuarry;
import net.minecraft.resources.ResourceLocation;

public class GuiAlkalineEarthQuarry<T extends ContainerAlkalineEarthQuarry> extends GuiIU<ContainerAlkalineEarthQuarry> {

    public GuiAlkalineEarthQuarry(ContainerAlkalineEarthQuarry guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 55, 45, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.timer)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
