package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuRadioactiveOreHandler;
import net.minecraft.resources.ResourceLocation;

public class ScreenRadioactiveOreHandler<T extends ContainerMenuRadioactiveOreHandler> extends ScreenMain<ContainerMenuRadioactiveOreHandler> {

    public ScreenRadioactiveOreHandler(ContainerMenuRadioactiveOreHandler guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 68, 34, EnumTypeComponent.LASER_PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        this.addComponent(new ScreenWidget(this, 60, 55, EnumTypeComponent.RAD,
                new WidgetDefault<>((this.container.base).componentRadiation)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
