package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuGeoGenerator;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenGeoGenerator<T extends ContainerMenuGeoGenerator> extends ScreenMain<ContainerMenuGeoGenerator> {

    public ContainerMenuGeoGenerator container;
    public String name;

    public ScreenGeoGenerator(ContainerMenuGeoGenerator container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.name = Localization.translate((container.base).getName());
        this.addWidget(TankWidget.createNormal(this, 62, 20, (container.base).fluidTank));
        this.addComponent(new ScreenWidget(this, 34, 40, EnumTypeComponent.FLUID_PART2,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 84, 40, EnumTypeComponent.FLUID_PART1,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 104, 38, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.getEnergy())
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
