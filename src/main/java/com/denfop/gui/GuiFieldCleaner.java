package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerFieldCleaner;
import net.minecraft.resources.ResourceLocation;

public class GuiFieldCleaner<T extends ContainerFieldCleaner> extends GuiIU<ContainerFieldCleaner> {

    public GuiFieldCleaner(ContainerFieldCleaner guiContainer) {
        super(guiContainer);
        this.addElement(TankGauge.createNormal(this, 10, 20, this.container.base.tank));
        this.addComponent(new GuiComponent(this, 98, 65, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
