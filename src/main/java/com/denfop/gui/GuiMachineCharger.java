package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerMachineCharger;
import net.minecraft.util.ResourceLocation;

public class GuiMachineCharger extends GuiIU<ContainerMachineCharger> {

    public GuiMachineCharger(ContainerMachineCharger guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, this.xSize / 2 - 20, 40, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.getEnergy())
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
