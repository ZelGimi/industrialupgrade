package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuNuclearWasteRecycler;
import net.minecraft.resources.ResourceLocation;

public class ScreenNuclearWasteRecycler<T extends ContainerMenuNuclearWasteRecycler> extends ScreenMain<ContainerMenuNuclearWasteRecycler> {

    public ScreenNuclearWasteRecycler(ContainerMenuNuclearWasteRecycler guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 130, 55, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 70, 35, EnumTypeComponent.RADIATION_PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        this.addComponent(new ScreenWidget(this, 58, 60, EnumTypeComponent.RAD,
                new WidgetDefault<>(this.container.base.rad)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
