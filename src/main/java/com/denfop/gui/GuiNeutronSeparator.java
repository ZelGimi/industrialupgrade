package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerNeutronSeparator;
import net.minecraft.resources.ResourceLocation;

public class GuiNeutronSeparator<T extends ContainerNeutronSeparator> extends GuiIU<ContainerNeutronSeparator> {

    public GuiNeutronSeparator(ContainerNeutronSeparator guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 60, 55, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.qe)
        ));
        this.addComponent(new GuiComponent(this, 72, 36, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.getContainer().base, 1, 100) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
