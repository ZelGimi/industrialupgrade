package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TankWidget;
import com.denfop.containermenu.ContainerMenuGasTank;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenGasMainTank<T extends ContainerMenuGasTank> extends ScreenMain<ContainerMenuGasTank> {

    public ScreenGasMainTank(ContainerMenuGasTank guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        elements.add(TankWidget.createNormal(this, 80, 25, guiContainer.base.tank));
        this.imageWidth = 186;
        this.imageHeight = 211;
    }


    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigasreactor5.png");
    }

}
