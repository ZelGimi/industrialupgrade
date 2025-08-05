package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerPigFarm;
import net.minecraft.resources.ResourceLocation;

public class GuiPigFarm<T extends ContainerPigFarm> extends GuiIU<ContainerPigFarm> {

    public GuiPigFarm(ContainerPigFarm guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 98, 65, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
