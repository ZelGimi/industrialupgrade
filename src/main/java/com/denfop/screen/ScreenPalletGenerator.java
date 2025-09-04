package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuPalletGenerator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenPalletGenerator<T extends ContainerMenuPalletGenerator> extends ScreenMain<ContainerMenuPalletGenerator> {

    public ScreenPalletGenerator(ContainerMenuPalletGenerator guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 58, 63, EnumTypeComponent.RAD_1,
                new WidgetDefault<>(this.container.base.rad)
        ));
        this.addComponent(new ScreenWidget(this, 7, 64, EnumTypeComponent.ENERGY_WEIGHT_1,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft + 49, this.guiTop + 17, 179, 30, 18, 18);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 49, this.guiTop + 35, 179, 30, 18, 18);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 67, this.guiTop + 17, 179, 30, 18, 18);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 67, this.guiTop + 35, 179, 30, 18, 18);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 85, this.guiTop + 17, 179, 30, 18, 18);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 85, this.guiTop + 35, 179, 30, 18, 18);
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guipallet.png");
    }

}
