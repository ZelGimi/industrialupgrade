package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuPlantFertilizer;
import net.minecraft.resources.ResourceLocation;

public class ScreenPlantFertilizer<T extends ContainerMenuPlantFertilizer> extends ScreenMain<ContainerMenuPlantFertilizer> {

    public ScreenPlantFertilizer(ContainerMenuPlantFertilizer guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 98, 45, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
