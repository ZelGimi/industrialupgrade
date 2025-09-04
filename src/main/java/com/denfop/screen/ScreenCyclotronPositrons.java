package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuPositrons;
import net.minecraft.resources.ResourceLocation;

public class ScreenCyclotronPositrons<T extends ContainerMenuPositrons> extends ScreenMain<ContainerMenuPositrons> {

    public ScreenCyclotronPositrons(ContainerMenuPositrons guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 72, 40, EnumTypeComponent.POSITRONS,
                new WidgetDefault<>(this.container.base.getPositrons())
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
