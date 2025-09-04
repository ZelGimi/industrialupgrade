package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TankWidget;
import com.denfop.containermenu.ContainerMenuGraphiteTank;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenGraphiteMainTank<T extends ContainerMenuGraphiteTank> extends ScreenMain<ContainerMenuGraphiteTank> {

    public ScreenGraphiteMainTank(ContainerMenuGraphiteTank guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        elements.add(TankWidget.createNormal(this, 80, 25, guiContainer.base.tank));
        this.imageWidth = 186;
        this.imageHeight = 212;
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigraphite4.png");
    }

}
