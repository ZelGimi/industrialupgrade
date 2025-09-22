package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerSinSolarPanel;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSintezator extends GuiCore<ContainerSinSolarPanel> {

    private static ResourceLocation tex;

    static {
        GuiSintezator.tex = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUI_Sintezator_Slots.png");
    }

    private final ContainerSinSolarPanel container;

    public GuiSintezator(ContainerSinSolarPanel container) {
        super(container);
        this.container = container;
        this.allowUserInput = false;
        this.xSize = 175;
        this.ySize = 174;
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
        this.fontRenderer.drawString(maxOutputString + output + (" " + energyPerTickString), 54, 40, 13487565);
        this.fontRenderer.drawString(tierString + this.container.tileentity.machineTire, 54, 20, 13487565);

        String generation = ModUtils.getString(this.container.tileentity.generating);
        String tooltip2 = generatingString + generation + " " + energyPerTickString;
        tooltip = storageString + maxstorage_2 + "/" + maxstorage_1;

        new AdvArea(this, 13, 17, 49, 32).withTooltip(tooltip).drawForeground(par1, par2);
        new AdvArea(this, 26, 42, 35, 51).withTooltip(tooltip2).drawForeground(par1, par2);

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

        if (this.container.tileentity.storage > 0
                || this.container.tileentity.storage <= this.container.tileentity.maxStorage) {
            final double l = this.container.tileentity.gaugeEnergyScaled(37);

            this.drawTexturedModalRect(h + 13, k + 17, 177, 3, (int) (l), 16);
        }


        if (!this.container.tileentity.rain) {
            if (this.container.tileentity.sunIsUp) {
                drawTexturedModalRect(h + 26, k + 42, 177, 35, 10, 10);
            } else {
                drawTexturedModalRect(h + 26, k + 42, 187, 35, 10, 10);
            }
        } else {
            if (this.container.tileentity.sunIsUp) {
                drawTexturedModalRect(h + 26, k + 42, 198, 35, 10, 10);
            } else {
                drawTexturedModalRect(h + 26, k + 42, 208, 35, 10, 10);
            }
        }
    }

}
