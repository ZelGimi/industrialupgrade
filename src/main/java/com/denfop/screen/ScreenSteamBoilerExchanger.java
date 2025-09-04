package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.blockentity.mechanism.steamboiler.BlockEntitySteamExchangerBoiler;
import com.denfop.containermenu.ContainerMenuDefaultMultiElement;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenSteamBoilerExchanger<T extends ContainerMenuDefaultMultiElement> extends ScreenMain<ContainerMenuDefaultMultiElement> {

    public ScreenSteamBoilerExchanger(ContainerMenuDefaultMultiElement guiContainer) {
        super(guiContainer);
        this.componentList.clear();

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        bindTexture();
        if (((BlockEntitySteamExchangerBoiler) container.base).isWork()) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 129, this.guiTop + 13, 243, 1, 12, 64);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteamboilerexchanger.png");
    }

}
