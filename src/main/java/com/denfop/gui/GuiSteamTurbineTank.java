package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerSteamTurbineTank;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiSteamTurbineTank<T extends ContainerSteamTurbineTank> extends GuiIU<ContainerSteamTurbineTank> {

    public GuiSteamTurbineTank(ContainerSteamTurbineTank guiContainer) {
        super(guiContainer);
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
        elements.add(TankGauge.createNormal(this, 80, 25, guiContainer.base.getTank()));
    }


    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect( poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
