package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerHeatMachine;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.gui.GuiElement;
import ic2.core.gui.TankGauge;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiHeatMachine extends GuiIC2<ContainerHeatMachine> {

    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
    public ContainerHeatMachine container;
    public String name;

    public GuiHeatMachine(ContainerHeatMachine guiContainer, boolean b) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());
        if (this.container.base.hasFluid) {
            this.addElement(TankGauge.createNormal(this, 96, 22, (container.base).fluidTank));
        }

    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 53 && x <= 63 && y >= 54 && y <= 64) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
        }
        if (x >= 73 && x <= 83 && y >= 54 && y <= 64) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 1);
        }

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        String temp = this.container.base.getTemperature() + "°C" + "/" + this.container.base.getMaxTemperature() + "°C";
        new AdvArea(this, 53, 42, 83, 53).withTooltip(temp).drawForeground(par1, par2);
        if (!this.container.base.hasFluid) {
            String tooltip2 =
                    ModUtils.getString(Math.min(
                            this.container.base.energy.getEnergy(),
                            this.container.base.energy.getCapacity()
                    )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                            "EU";
            new AdvArea(this, 113, 21, 124, 70)
                    .withTooltip(tooltip2)
                    .drawForeground(par1, par2);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);
        int temperature = 0;
        if (this.container.base.getMaxTemperature() > 0) {
            temperature = 38 * this.container.base.getTemperature() / this.container.base.getMaxTemperature();
        }
        if (temperature > 0) {
            drawTexturedModalRect(this.guiLeft + 53, this.guiTop + 42, 176, 104, temperature + 1, 11);
        }
        if (!this.container.base.hasFluid) {
            int chargeLevel = (int) (48.0F * this.container.base.energy.getFillRatio());

            if (chargeLevel > 0) {
                drawTexturedModalRect(xOffset + 113, yOffset + 22 + 48 - chargeLevel, 176,
                        115 + 48 - chargeLevel, 12, chargeLevel
                );
            }
        }
        x -= this.guiLeft;
        y -= this.guiTop;
        for (final GuiElement<?> guiElement : this.elements) {
            if (guiElement.isEnabled()) {
                guiElement.drawBackground(x, y);
            }
        }


    }

    @Override
    protected ResourceLocation getTexture() {
        if (this.container.base.hasFluid) {
            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidheater.png");
        } else {
            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guienergyheater.png");
        }

    }


}
