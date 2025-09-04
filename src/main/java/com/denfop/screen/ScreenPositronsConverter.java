package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuPositronsConverter;
import net.minecraft.resources.ResourceLocation;

public class ScreenPositronsConverter<T extends ContainerMenuPositronsConverter> extends ScreenMain<ContainerMenuPositronsConverter> {

    public ScreenPositronsConverter(ContainerMenuPositronsConverter guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 27, 48, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.qe)
        ));
        this.addComponent(new ScreenWidget(this, 7, 25, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 80, 25, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        this.addComponent(new ScreenWidget(this, 105, 22, EnumTypeComponent.POSITRONS,
                new WidgetDefault<>(this.container.base.positrons)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
