package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerGasWellTank;
import net.minecraft.resources.ResourceLocation;

public class GuiGasWellTank<T extends ContainerGasWellTank> extends GuiIU<ContainerGasWellTank> {

    public GuiGasWellTank(ContainerGasWellTank guiContainer) {
        super(guiContainer);
        this.addElement(TankGauge.createNormal(this, this.imageWidth / 2 - 10, 20, guiContainer.base.getTank()));
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
