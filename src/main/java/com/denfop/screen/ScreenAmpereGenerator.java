package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EmptyWidget;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuAmpereGenerator;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;

public class ScreenAmpereGenerator<T extends ContainerMenuAmpereGenerator> extends ScreenMain<ContainerMenuAmpereGenerator> {

    public ContainerMenuAmpereGenerator container;
    public String name;

    public ScreenAmpereGenerator(ContainerMenuAmpereGenerator guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());

        this.addComponent(new ScreenWidget(this, 50, 40, EnumTypeComponent.ENERGY_WEIGHT_1,
                new WidgetDefault<>(this.container.base.pressure)
        ));
        this.addComponent(new ScreenWidget(this, 25, 40, EnumTypeComponent.FLUID_PART1,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 10, 25, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");

    }


}
