package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerAmpereGenerator;
import net.minecraft.resources.ResourceLocation;

public class GuiAmpereGenerator<T extends ContainerAmpereGenerator> extends GuiIU<ContainerAmpereGenerator> {

    public ContainerAmpereGenerator container;
    public String name;

    public GuiAmpereGenerator(ContainerAmpereGenerator guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());

        this.addComponent(new GuiComponent(this, 50, 40, EnumTypeComponent.ENERGY_WEIGHT_1,
                new Component<>(this.container.base.pressure)
        ));
        this.addComponent(new GuiComponent(this, 25, 40, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 10, 25, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");

    }


}
