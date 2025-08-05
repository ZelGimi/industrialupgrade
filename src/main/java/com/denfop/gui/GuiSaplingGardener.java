package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerSaplingGardener;
import net.minecraft.resources.ResourceLocation;

public class GuiSaplingGardener<T extends ContainerSaplingGardener> extends GuiIU<ContainerSaplingGardener> {

    public GuiSaplingGardener(ContainerSaplingGardener guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 98, 45, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
