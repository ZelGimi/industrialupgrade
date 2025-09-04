package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuCyclotronQuantum;
import net.minecraft.resources.ResourceLocation;

public class ScreenCyclotronQuantum<T extends ContainerMenuCyclotronQuantum> extends ScreenMain<ContainerMenuCyclotronQuantum> {

    public ScreenCyclotronQuantum(ContainerMenuCyclotronQuantum guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 72, 40, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.getQuantum())
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guicyclotron2.png");
    }

}
