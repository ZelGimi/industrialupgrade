package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerTunerWireless;
import net.minecraft.resources.ResourceLocation;

public class GuiTunerWireless<T extends ContainerTunerWireless> extends GuiIU<ContainerTunerWireless> {

    public final ContainerTunerWireless container;

    public GuiTunerWireless(ContainerTunerWireless container1) {
        super(container1);
        this.container = container1;
        this.addElement(new CustomButton(this, 103, 21, 68, 17, container1.base, 0, Localization.translate("button.rf")));

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
