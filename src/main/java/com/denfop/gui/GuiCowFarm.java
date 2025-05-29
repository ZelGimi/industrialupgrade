package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerCowFarm;
import net.minecraft.resources.ResourceLocation;

public class GuiCowFarm<T extends ContainerCowFarm> extends GuiIU<ContainerCowFarm> {

    public GuiCowFarm(ContainerCowFarm guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 18, 20, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
