package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuSafetyDoom;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenSafetyDoom<T extends ContainerMenuSafetyDoom> extends ScreenMain<ContainerMenuSafetyDoom> {

    public ScreenSafetyDoom(ContainerMenuSafetyDoom guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 58, 63, EnumTypeComponent.RAD_1,
                new WidgetDefault<>(this.container.base.rad)
        ));
        this.addComponent(new ScreenWidget(this, 8, 62, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guisafety.png");
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        if (this.container.base.full) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 81, this.guiTop + 33, 192, 64, 16, 16);
        } else {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 81, this.guiTop + 33, 192, 81, 16, 16);
        }
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

}
