package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerApothecaryBee;
import net.minecraft.resources.ResourceLocation;

public class GuiApothecaryBee<T extends ContainerApothecaryBee> extends GuiIU<ContainerApothecaryBee> {

    public GuiApothecaryBee(ContainerApothecaryBee guiContainer) {
        super(guiContainer);

        this.addComponent(new GuiComponent(this, 98, 45, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }



    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
