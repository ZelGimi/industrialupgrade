package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerAnalyzerChest;
import net.minecraft.resources.ResourceLocation;

public class GuiAnalyzerChest<T extends ContainerAnalyzerChest> extends GuiIU<ContainerAnalyzerChest> {

    public final ContainerAnalyzerChest container;

    public GuiAnalyzerChest(ContainerAnalyzerChest container1) {
        super(container1);
        this.container = container1;
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
