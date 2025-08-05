package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerSafetyDoom;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiSafetyDoom<T extends ContainerSafetyDoom> extends GuiIU<ContainerSafetyDoom> {

    public GuiSafetyDoom(ContainerSafetyDoom guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 58, 63, EnumTypeComponent.RAD_1,
                new Component<>(this.container.base.rad)
        ));
        this.addComponent(new GuiComponent(this, 8, 62, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
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
