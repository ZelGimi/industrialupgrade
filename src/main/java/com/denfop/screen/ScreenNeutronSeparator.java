package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuNeutronSeparator;
import net.minecraft.resources.ResourceLocation;

public class ScreenNeutronSeparator<T extends ContainerMenuNeutronSeparator> extends ScreenMain<ContainerMenuNeutronSeparator> {

    public ScreenNeutronSeparator(ContainerMenuNeutronSeparator guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 60, 55, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.qe)
        ));
        this.addComponent(new ScreenWidget(this, 72, 36, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(this.getContainer().base, 1, 100) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
