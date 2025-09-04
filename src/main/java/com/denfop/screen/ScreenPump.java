package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuPump;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenPump<T extends ContainerMenuPump> extends ScreenMain<ContainerMenuPump> {

    public final ContainerMenuPump container;

    public ScreenPump(ContainerMenuPump container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.addWidget(TankWidget.createNormal(this, 70, 16, container.base.fluidTank));
        this.addComponent(new ScreenWidget(this, 10, 27, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));
        this.addComponent(new ScreenWidget(this, 36, 35, EnumTypeComponent.PROCESS2,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        this.addComponent(new ScreenWidget(this, 93, 36, EnumTypeComponent.FLUID_PART3,
                new WidgetDefault<>(new EmptyWidget())
        ));
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
