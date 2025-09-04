package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuBattery;
import net.minecraft.resources.ResourceLocation;

public class ScreenBattery<T extends ContainerMenuBattery> extends ScreenMain<ContainerMenuBattery> {

    public ScreenBattery(ContainerMenuBattery guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 70, 35, EnumTypeComponent.FACTORY_PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
