package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuMachineCharger;
import net.minecraft.resources.ResourceLocation;

public class ScreenMachineCharger<T extends ContainerMenuMachineCharger> extends ScreenMain<ContainerMenuMachineCharger> {

    public ScreenMachineCharger(ContainerMenuMachineCharger guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, this.imageWidth / 2 - 20, 40, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.getEnergy())
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
