package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.containermenu.ContainerMenuSinSolarPanel;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenSintezator<T extends ContainerMenuSinSolarPanel> extends ScreenIndustrialUpgrade<ContainerMenuSinSolarPanel> {

    private static ResourceLocation tex;

    static {
        ScreenSintezator.tex = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUI_Sintezator_Slots.png".toLowerCase());
    }

    private final ContainerMenuSinSolarPanel container;

    public ScreenSintezator(ContainerMenuSinSolarPanel container) {
        super(container);
        this.container = container;
        this.imageWidth = 175;
        this.imageHeight = 174;
    }

    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        final String storageString = Localization.translate("gui.SuperSolarPanel.storage") + ": ";
        final String maxOutputString = Localization.translate("gui.SuperSolarPanel.maxOutput") + ": ";
        final String generatingString = Localization.translate("gui.SuperSolarPanel.generating") + ": ";
        final String energyPerTickString = Localization.translate("gui.SuperSolarPanel.energyPerTick");
        String tierString = Localization.translate("gui.iu.tier") + ": ";
        String maxstorage_1 = ModUtils.getString(this.container.tileentity.maxStorage);
        String maxstorage_2 = ModUtils.getString(this.container.tileentity.storage);
        String tooltip;
        String output = ModUtils.getString(this.container.tileentity.production);
        this.font.draw(poseStack, maxOutputString + output + (" " + energyPerTickString), 54, 40, 13487565);
        this.font.draw(poseStack, tierString + this.container.tileentity.machineTire, 54, 20, 13487565);

        String generation = ModUtils.getString(this.container.tileentity.generating);
        String tooltip2 = generatingString + generation + " " + energyPerTickString;
        tooltip = storageString + maxstorage_2 + "/" + maxstorage_1;

        new AdvancedTooltipWidget(this, 13, 17, 49, 32).withTooltip(tooltip).drawForeground(poseStack, par1, par2);
        new AdvancedTooltipWidget(this, 26, 42, 35, 51).withTooltip(tooltip2).drawForeground(poseStack, par1, par2);

    }

    @Override
    protected ResourceLocation getTexture() {
        return tex;
    }

    protected void renderBg(PoseStack poseStack, final float f, final int i, final int j) {

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        bindTexture(ScreenSintezator.tex);
        final int h = guiLeft;
        final int k = guiTop;
        this.drawTexturedModalRect(poseStack, h, k, 0, 0, this.imageWidth, this.imageHeight);

        if (this.container.tileentity.storage > 0
                || this.container.tileentity.storage <= this.container.tileentity.maxStorage) {
            final double l = this.container.tileentity.gaugeEnergyScaled(37);

            this.drawTexturedModalRect(poseStack, h + 13, k + 17, 177, 3, (int) (l), 16);
        }


        if (!this.container.tileentity.rain) {
            if (this.container.tileentity.sunIsUp) {
                drawTexturedModalRect(poseStack, h + 26, k + 42, 177, 35, 10, 10);
            } else {
                drawTexturedModalRect(poseStack, h + 26, k + 42, 187, 35, 10, 10);
            }
        } else {
            if (this.container.tileentity.sunIsUp) {
                drawTexturedModalRect(poseStack, h + 26, k + 42, 198, 35, 10, 10);
            } else {
                drawTexturedModalRect(poseStack, h + 26, k + 42, 208, 35, 10, 10);
            }
        }
    }

}
