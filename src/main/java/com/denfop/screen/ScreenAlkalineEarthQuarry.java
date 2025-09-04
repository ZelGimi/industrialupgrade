package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuAlkalineEarthQuarry;
import net.minecraft.resources.ResourceLocation;

public class ScreenAlkalineEarthQuarry<T extends ContainerMenuAlkalineEarthQuarry> extends ScreenMain<ContainerMenuAlkalineEarthQuarry> {

    public ScreenAlkalineEarthQuarry(ContainerMenuAlkalineEarthQuarry guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 55, 45, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.timer)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
