package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuPeatGenerator;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;

public class ScreenPeatGenerator<T extends ContainerMenuPeatGenerator> extends ScreenMain<ContainerMenuPeatGenerator> {


    public ContainerMenuPeatGenerator container;
    public String name;

    public ScreenPeatGenerator(ContainerMenuPeatGenerator container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.name = Localization.translate(this.container.base.getName());
        this.addComponent(new ScreenWidget(this, 66, 36, EnumTypeComponent.FIRE,
                new WidgetDefault<>(new ComponentProgress(this.container.base, 1, (short) 1) {
                    @Override
                    public double getBar() {
                        return container.base.gaugeFuelScaled(12) / 12D;
                    }

                })
        ));
        this.addComponent(new ScreenWidget(this, 86, 36, EnumTypeComponent.ENERGY_WEIGHT_1,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
