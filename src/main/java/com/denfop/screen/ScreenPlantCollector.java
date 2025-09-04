package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuPlantCollector;
import net.minecraft.resources.ResourceLocation;

public class ScreenPlantCollector<T extends ContainerMenuPlantCollector> extends ScreenMain<ContainerMenuPlantCollector> {

    public ScreenPlantCollector(ContainerMenuPlantCollector guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 98, 65, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
