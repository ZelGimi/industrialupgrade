package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ImageInterfaceWidget;
import com.denfop.containermenu.ContainerMenuAnalyzerChest;
import net.minecraft.resources.ResourceLocation;

public class ScreenAnalyzerChest<T extends ContainerMenuAnalyzerChest> extends ScreenMain<ContainerMenuAnalyzerChest> {

    public final ContainerMenuAnalyzerChest container;

    public ScreenAnalyzerChest(ContainerMenuAnalyzerChest container1) {
        super(container1);
        this.container = container1;
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
