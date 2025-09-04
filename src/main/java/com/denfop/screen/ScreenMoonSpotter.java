package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuMoonSpotter;
import net.minecraft.resources.ResourceLocation;

public class ScreenMoonSpotter<T extends ContainerMenuMoonSpotter> extends ScreenMain<ContainerMenuMoonSpotter> {

    public ScreenMoonSpotter(ContainerMenuMoonSpotter guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 71, 35, EnumTypeComponent.NIGHT_PROCESS,
                new WidgetDefault<>(this.container.base.timer)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
