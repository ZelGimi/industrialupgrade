package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuWaterRotorUpgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenWaterRotorUpgrade<T extends ContainerMenuWaterRotorUpgrade> extends ScreenMain<ContainerMenuWaterRotorUpgrade> {

    public final ContainerMenuWaterRotorUpgrade container;
    public final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guiwater_rotor_upgrade.png");
    public final ResourceLocation rotors_gui = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotors.png");

    public ScreenWaterRotorUpgrade(ContainerMenuWaterRotorUpgrade guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.componentList.clear();

    }


    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        if (!this.container.base.rotor_slot.isEmpty()) {
            RenderSystem.setShaderColor(this.container.base.getRotor().getColor().getRed() / 255f, this.container.base.getRotor().getColor().getGreen() / 255f, this.container.base.getRotor().getColor().getBlue() / 255f, 1);
            drawTexturedModalRect(poseStack, guiLeft + 49, guiTop + 5, 177,
                    81, 78, 78
            );

            RenderSystem.setShaderColor(1, 1, 1, 1);
            drawTexturedModalRect(poseStack, guiLeft + 49, guiTop + 5, 177,
                    0, 78, 78
            );
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    void setTexture(ResourceLocation resourceLocation) {
        bindTexture(resourceLocation);
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

}
