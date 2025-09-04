package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuStamp;
import net.minecraft.resources.ResourceLocation;

public class ScreenStamp<T extends ContainerMenuStamp> extends ScreenMain<ContainerMenuStamp> {

    public ScreenStamp(ContainerMenuStamp guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 66, 32, EnumTypeComponent.STAMP_PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));

    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
