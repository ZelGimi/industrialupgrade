package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuNightConverter;
import net.minecraft.resources.ResourceLocation;

public class ScreenNightConverter<T extends ContainerMenuNightConverter> extends ScreenMain<ContainerMenuNightConverter> {

    public ScreenNightConverter(ContainerMenuNightConverter guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 120, 35, EnumTypeComponent.NIGHT_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.ne)
        ));
        this.addComponent(new ScreenWidget(this, 71, 35, EnumTypeComponent.NIGHT_PROCESS,
                new WidgetDefault<>(this.container.base.progress)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
