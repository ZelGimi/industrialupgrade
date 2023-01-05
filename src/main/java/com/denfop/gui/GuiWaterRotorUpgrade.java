package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerWaterRotorUpgrade;
import ic2.core.GuiIC2;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiWaterRotorUpgrade extends GuiIC2<ContainerWaterRotorUpgrade> {

    public final ContainerWaterRotorUpgrade container;
    public final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guiwaterrotorupgrade.png");
    public final ResourceLocation rotors_gui = new ResourceLocation(Constants.TEXTURES, "textures/gui/guirotors.png");

    public GuiWaterRotorUpgrade(ContainerWaterRotorUpgrade guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.ySize = 206;
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.mc.getTextureManager().bindTexture(background);

        if (!this.container.base.rotor_slot.isEmpty()) {
            int j = (this.width - this.xSize) / 2;
            int k = (this.height - this.ySize) / 2;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            setTexture(rotors_gui);
            drawTexturedModalRect(j + 71, k + 5, 32 * (this.container.base.getRotor().getIndex() % 8),
                    55 * (this.container.base.getRotor().getIndex() / 8), 32, 54
            );
            drawTexturedModalRect(j + 71, k + 57, 32 * (this.container.base.getRotor().getIndex() % 8),
                    55 * (this.container.base.getRotor().getIndex() / 8), 32, 54
            );
            drawTexturedModalRect(j + 33, k + 43, 55 * (this.container.base.getRotor().getIndex() % 4),
                    112 + 33 * (this.container.base.getRotor().getIndex() / 4), 54, 32
            );
            drawTexturedModalRect(j + 85, k + 43, 55 * (this.container.base.getRotor().getIndex() % 4),
                    112 + 33 * (this.container.base.getRotor().getIndex() / 4), 54, 32
            );
            setTexture(background);
            drawTexturedModalRect(j + 77, k + 6, 230,
                    33, 18, 18
            );
            drawTexturedModalRect(j + 34, k + 49, 230,
                    33, 18, 18
            );
            drawTexturedModalRect(j + 77, k + 49, 212,
                    33, 18, 18
            );
            drawTexturedModalRect(j + 120, k + 49, 230,
                    33, 18, 18
            );
            drawTexturedModalRect(j + 77, k + 92, 230,
                    33, 18, 18
            );

        }

    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    void setTexture(ResourceLocation resourceLocation) {
        this.mc.getTextureManager().bindTexture(resourceLocation);
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

}
