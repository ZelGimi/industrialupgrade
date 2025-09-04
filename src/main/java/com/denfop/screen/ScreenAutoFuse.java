package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuAutoFuse;
import net.minecraft.resources.ResourceLocation;

public class ScreenAutoFuse<T extends ContainerMenuAutoFuse> extends ScreenMain<ContainerMenuAutoFuse> {

    public ScreenAutoFuse(ContainerMenuAutoFuse guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 70, 60, EnumTypeComponent.RAD,
                new WidgetDefault<>((this.container.base).rad_energy)
        ));

    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
