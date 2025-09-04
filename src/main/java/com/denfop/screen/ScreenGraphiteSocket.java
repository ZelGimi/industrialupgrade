package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuGraphiteSocket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenGraphiteSocket<T extends ContainerMenuGraphiteSocket> extends ScreenMain<ContainerMenuGraphiteSocket> {

    public ScreenGraphiteSocket(ContainerMenuGraphiteSocket guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 72, 40, EnumTypeComponent.ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.getEnergy())
        ));
        this.imageWidth = 187;
        this.imageHeight = 212;
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guigraphite4.png");
    }

}
