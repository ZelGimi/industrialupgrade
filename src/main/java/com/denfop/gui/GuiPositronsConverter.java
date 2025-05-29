package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerPositronsConverter;
import net.minecraft.resources.ResourceLocation;

public class GuiPositronsConverter<T extends ContainerPositronsConverter> extends GuiIU<ContainerPositronsConverter> {

    public GuiPositronsConverter(ContainerPositronsConverter guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 27, 48, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.qe)
        ));
        this.addComponent(new GuiComponent(this, 7, 25, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 80, 25, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.componentProgress)
        ));
        this.addComponent(new GuiComponent(this, 105, 22, EnumTypeComponent.POSITRONS,
                new Component<>(this.container.base.positrons)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
