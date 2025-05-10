package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerNightConverter;
import net.minecraft.resources.ResourceLocation;

public class GuiNightConverter<T extends ContainerNightConverter> extends GuiIU<ContainerNightConverter> {

    public GuiNightConverter(ContainerNightConverter guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 120, 35, EnumTypeComponent.NIGHT_ENERGY_WEIGHT,
                new Component<>(this.container.base.ne)
        ));
        this.addComponent(new GuiComponent(this, 71, 35, EnumTypeComponent.NIGHT_PROCESS,
                new Component<>(this.container.base.progress)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
