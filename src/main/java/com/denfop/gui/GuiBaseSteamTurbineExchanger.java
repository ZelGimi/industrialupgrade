package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.container.ContainerBaseSteamTurbineExchanger;
import net.minecraft.resources.ResourceLocation;

public class GuiBaseSteamTurbineExchanger<T extends ContainerBaseSteamTurbineExchanger> extends GuiIU<ContainerBaseSteamTurbineExchanger> {

    public GuiBaseSteamTurbineExchanger(ContainerBaseSteamTurbineExchanger guiContainer) {
        super(guiContainer);
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));

    }




    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/пгшьфсршту.png");
    }

}
