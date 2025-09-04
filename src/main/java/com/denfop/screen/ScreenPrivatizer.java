package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ButtonWidget;
import com.denfop.containermenu.ContainerMenuPrivatizer;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;

public class ScreenPrivatizer<T extends ContainerMenuPrivatizer> extends ScreenMain<ContainerMenuPrivatizer> {

    public final ContainerMenuPrivatizer container;

    public ScreenPrivatizer(ContainerMenuPrivatizer container1) {
        super(container1);
        this.container = container1;
        this.addWidget(new ButtonWidget(this, 103, 21, 68, 17, container1.base, 0, Localization.translate("button.write")));

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
