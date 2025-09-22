package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerNightTransformer;
import net.minecraft.util.ResourceLocation;

public class GuiNightTransformer extends GuiIU<ContainerNightTransformer> {

    public GuiNightTransformer(ContainerNightTransformer guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 10, 38, EnumTypeComponent.SOLARIUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.se)
        ));
        this.addComponent(new GuiComponent(this, 55, 64, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.qe)
        ));
        this.addComponent(new GuiComponent(this, 64, 39, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 95, 38, EnumTypeComponent.NIGHT_ENERGY_WEIGHT,
                new Component<>(this.container.base.ne)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
