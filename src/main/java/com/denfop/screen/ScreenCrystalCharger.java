package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuCrystalCharger;
import net.minecraft.resources.ResourceLocation;

public class ScreenCrystalCharger<T extends ContainerMenuCrystalCharger> extends ScreenMain<ContainerMenuCrystalCharger> {

    public ScreenCrystalCharger(ContainerMenuCrystalCharger guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 10, 25, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.ampere)
        ));
        this.addComponent(new ScreenWidget(this, 68, 34, EnumTypeComponent.LASER_PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
