package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.container.ContainerGeoGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiGeoGenerator<T extends ContainerGeoGenerator> extends GuiIU<ContainerGeoGenerator> {

    public ContainerGeoGenerator container;
    public String name;

    public GuiGeoGenerator(ContainerGeoGenerator container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.name = Localization.translate((container.base).getName());
        this.addElement(TankGauge.createNormal(this, 62, 20, (container.base).fluidTank));
        this.addComponent(new GuiComponent(this, 34, 40, EnumTypeComponent.FLUID_PART2,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 84, 40, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 104, 38, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.getEnergy())
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
