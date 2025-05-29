package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerMachineCharger;
import net.minecraft.resources.ResourceLocation;

public class GuiMachineCharger<T extends ContainerMachineCharger> extends GuiIU<ContainerMachineCharger> {

    public GuiMachineCharger(ContainerMachineCharger guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, this.imageWidth / 2 - 20, 40, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.getEnergy())
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
