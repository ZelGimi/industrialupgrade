package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ButtonWidget;
import com.denfop.containermenu.ContainerMenuTunerWireless;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;

public class ScreenTunerWireless<T extends ContainerMenuTunerWireless> extends ScreenMain<ContainerMenuTunerWireless> {

    public final ContainerMenuTunerWireless container;

    public ScreenTunerWireless(ContainerMenuTunerWireless container1) {
        super(container1);
        this.container = container1;
        this.addWidget(new ButtonWidget(this, 103, 21, 68, 17, container1.base, 0, Localization.translate("button.rf")));

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
