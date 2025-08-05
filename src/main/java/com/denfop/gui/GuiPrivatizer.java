package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerPrivatizer;
import net.minecraft.resources.ResourceLocation;

public class GuiPrivatizer<T extends ContainerPrivatizer> extends GuiIU<ContainerPrivatizer> {

    public final ContainerPrivatizer container;

    public GuiPrivatizer(ContainerPrivatizer container1) {
        super(container1);
        this.container = container1;
        this.addElement(new CustomButton(this, 103, 21, 68, 17, container1.base, 0, Localization.translate("button.write")));

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
