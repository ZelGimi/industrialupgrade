package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerHeatTank;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiHeatMainTank<T extends ContainerHeatTank> extends GuiIU<ContainerHeatTank> {

    public GuiHeatMainTank(ContainerHeatTank guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        elements.add(TankGauge.createNormal(this, 80, 25, guiContainer.base.tank));
        this.imageWidth = 186;
        this.imageHeight = 212;
    }


    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect( poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat4.png");
    }

}
