package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuAmpereStorage;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;

public class ScreenAmpereStorage<T extends ContainerMenuAmpereStorage> extends ScreenMain<ContainerMenuAmpereStorage> {

    public ContainerMenuAmpereStorage container;
    public String name;

    public ScreenAmpereStorage(ContainerMenuAmpereStorage guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());

        this.addComponent(new ScreenWidget(this, 7, 35, EnumTypeComponent.AMPERE,
                new WidgetDefault<>(this.container.base.pressure)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_amperestorage.png");

    }


}
