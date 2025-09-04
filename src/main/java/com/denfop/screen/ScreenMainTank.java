package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TankWidget;
import com.denfop.containermenu.ContainerMenuWaterTank;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenMainTank<T extends ContainerMenuWaterTank> extends ScreenMain<ContainerMenuWaterTank> {

    public ScreenMainTank(ContainerMenuWaterTank guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        elements.add(TankWidget.createNormal(this, 40, 25, guiContainer.base.tank));
    }


    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor4.png");
    }

}
