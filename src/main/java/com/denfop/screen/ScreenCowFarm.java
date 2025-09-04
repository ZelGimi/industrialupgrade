package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuCowFarm;
import net.minecraft.resources.ResourceLocation;

public class ScreenCowFarm<T extends ContainerMenuCowFarm> extends ScreenMain<ContainerMenuCowFarm> {

    public ScreenCowFarm(ContainerMenuCowFarm guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 18, 20, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
