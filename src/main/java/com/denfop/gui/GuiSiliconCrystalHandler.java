package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.container.ContainerSiliconCrystalHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSiliconCrystalHandler extends GuiIU<ContainerSiliconCrystalHandler> {

    public GuiSiliconCrystalHandler(ContainerSiliconCrystalHandler guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 130, 55, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(this.container.base.timer)
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        GlStateManager.scale(0.7, 0.7, 0.7);
        this.fontRenderer.drawString(
                Localization.translate("iu.coal_concentration") + " " + (int) ((this.container.base.col / 90D) * 100) + "%",
                10,
                30,
                4210752
        );

        GlStateManager.scale(1 / 0.7, 1 / 0.7, 1 / 0.7);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
