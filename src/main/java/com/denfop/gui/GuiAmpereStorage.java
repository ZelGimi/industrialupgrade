package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerAmpereStorage;
import net.minecraft.resources.ResourceLocation;

public class GuiAmpereStorage<T extends ContainerAmpereStorage> extends GuiIU<ContainerAmpereStorage> {

    public ContainerAmpereStorage container;
    public String name;

    public GuiAmpereStorage(ContainerAmpereStorage guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());

        this.addComponent(new GuiComponent(this, 7, 35, EnumTypeComponent.AMPERE,
                new Component<>(this.container.base.pressure)
        ));
    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_amperestorage.png");

    }


}
