package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerCyclotronQuantum;
import net.minecraft.resources.ResourceLocation;

public class GuiCyclotronQuantum<T extends ContainerCyclotronQuantum> extends GuiIU<ContainerCyclotronQuantum> {

    public GuiCyclotronQuantum(ContainerCyclotronQuantum guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 72, 40, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.getQuantum())
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
