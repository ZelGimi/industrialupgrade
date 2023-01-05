package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerSinSolarPanel;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSintezator extends GuiIC2<ContainerSinSolarPanel> {

    private static ResourceLocation tex;

    static {
        GuiSintezator.tex = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUI_Sintezator_Slots.png");
    }

    private final ContainerSinSolarPanel container;

    public GuiSintezator(ContainerSinSolarPanel container) {
        super(container);
        this.container = container;
        this.allowUserInput = false;
        this.xSize = 194;
        this.ySize = 168;
    }

    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        final String storageString = Localization.translate("gui.SuperSolarPanel.storage") + ": ";
        final String maxOutputString = Localization.translate("gui.SuperSolarPanel.maxOutput") + ": ";
        final String generatingString = Localization.translate("gui.SuperSolarPanel.generating") + ": ";
        final String energyPerTickString = Localization.translate("gui.SuperSolarPanel.energyPerTick");
        String tierString = Localization.translate("gui.iu.tier") + ": ";
        String maxstorage_1 = ModUtils.getString(this.container.tileentity.maxStorage);
        String maxstorage_2 = ModUtils.getString(this.container.tileentity.storage);
        String tooltip;
        String output = ModUtils.getString(this.container.tileentity.production);
        this.fontRenderer.drawString(maxOutputString + output + (" " + energyPerTickString), 50, 32 - 10, 13487565);
        this.fontRenderer.drawString(tierString + this.container.tileentity.machineTire, 50, 32, 13487565);
        if (!this.container.tileentity.getmodulerf) {
            String generation = ModUtils.getString(this.container.tileentity.generating);
            String tooltip2 = generatingString + generation + " " + energyPerTickString;
            tooltip = storageString + maxstorage_2 + "/" + maxstorage_1;

            new AdvArea(this, 18, 24, 43, 38).withTooltip(tooltip).drawForeground(par1, par2);
            new AdvArea(this, 18, 40, 43, 58).withTooltip(tooltip2).drawForeground(par1, par2);

        } else {
            maxstorage_1 = ModUtils.getString(this.container.tileentity.maxStorage2);
            maxstorage_2 = ModUtils.getString(this.container.tileentity.storage2);
            tooltip = "RF: " + maxstorage_2 + "/" + maxstorage_1;

            String generation = ModUtils.getString(this.container.tileentity.generating * 4);
            new AdvArea(this, 18, 24, 43, 38).withTooltip(tooltip).drawForeground(par1, par2);
            String tooltip2 = generatingString + generation + " " + "RF/t";
            new AdvArea(this, 18, 40, 43, 58).withTooltip(tooltip2).drawForeground(par1, par2);

        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return tex;
    }

    protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(GuiSintezator.tex);
        final int h = (this.width - this.xSize) / 2;
        final int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        if (!this.container.tileentity.getmodulerf) {
            if (this.container.tileentity.storage > 0
                    || this.container.tileentity.storage <= this.container.tileentity.maxStorage) {
                final double l = this.container.tileentity.gaugeEnergyScaled(24);

                this.drawTexturedModalRect(h + 19, k + 24, 195, 0, (int) (l), 14);
            }
        } else {
            if (this.container.tileentity.storage2 > 0
                    || this.container.tileentity.storage2 <= this.container.tileentity.maxStorage2) {
                final double l = this.container.tileentity.gaugeEnergyScaled1(25);

                this.drawTexturedModalRect(h + 19, k + 24, 219, 0, (int) (l), 14);
            }
        }

        if (!this.container.tileentity.getmodulerf) {
            if (!this.container.tileentity.rain) {
                if (this.container.tileentity.sunIsUp) {
                    drawTexturedModalRect(h + 24, k + 42, 195, 15, 14, 14);
                } else {
                    drawTexturedModalRect(h + 24, k + 42, 210, 15, 14, 14);
                }
            } else {
                if (this.container.tileentity.sunIsUp) {
                    drawTexturedModalRect(h + 24, k + 42, 225, 15, 14, 14);
                } else {
                    drawTexturedModalRect(h + 24, k + 42, 240, 15, 14, 14);
                }
            }
        } else {
            if (!this.container.tileentity.rain) {
                if (this.container.tileentity.sunIsUp) {
                    drawTexturedModalRect(h + 24, k + 42, 195, 30, 14, 14);
                } else {
                    drawTexturedModalRect(h + 24, k + 42, 210, 30, 14, 14);
                }
            } else {
                if (this.container.tileentity.sunIsUp) {
                    drawTexturedModalRect(h + 24, k + 42, 225, 30, 14, 14);
                } else {
                    drawTexturedModalRect(h + 24, k + 42, 240, 30, 14, 14);
                }
            }
        }
    }

}
