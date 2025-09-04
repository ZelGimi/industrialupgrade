package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EmptyWidget;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuNightTransformer;
import net.minecraft.resources.ResourceLocation;

public class ScreenNightTransformer<T extends ContainerMenuNightTransformer> extends ScreenMain<ContainerMenuNightTransformer> {

    public ScreenNightTransformer(ContainerMenuNightTransformer guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 10, 38, EnumTypeComponent.SOLARIUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.se)
        ));
        this.addComponent(new ScreenWidget(this, 55, 64, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.qe)
        ));
        this.addComponent(new ScreenWidget(this, 64, 39, EnumTypeComponent.FLUID_PART1,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 95, 38, EnumTypeComponent.NIGHT_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.ne)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
