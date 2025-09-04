package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuHeatSocket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenHeatSocket<T extends ContainerMenuHeatSocket> extends ScreenMain<ContainerMenuHeatSocket> {

    public ScreenHeatSocket(ContainerMenuHeatSocket guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 72, 40, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.getEnergy())
        ));
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
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat4.png");
    }

}
