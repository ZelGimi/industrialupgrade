package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuFieldCleaner;
import net.minecraft.resources.ResourceLocation;

public class ScreenFieldCleaner<T extends ContainerMenuFieldCleaner> extends ScreenMain<ContainerMenuFieldCleaner> {

    public ScreenFieldCleaner(ContainerMenuFieldCleaner guiContainer) {
        super(guiContainer);
        this.addWidget(TankWidget.createNormal(this, 10, 20, this.container.base.tank));
        this.addComponent(new ScreenWidget(this, 98, 65, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
