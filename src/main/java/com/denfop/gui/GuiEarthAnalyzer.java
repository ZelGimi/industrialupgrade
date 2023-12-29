package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerEarthAnalyzer;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiEarthAnalyzer extends GuiIU<ContainerEarthAnalyzer> {

    public GuiEarthAnalyzer(ContainerEarthAnalyzer guiContainer) {
        super(guiContainer);
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if(x >= 30 && x <= 45 && y >= 30 &&y <= 45 )
            new PacketUpdateServerTile(this.container.base, 0);
    }
    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        if(this.container.base.isAnalyzed() && !this.container.base.fullAnalyzed()){
            this.fontRenderer.drawString(Localization.translate("earth_quarry.analyze"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56));
        }else   if(this.container.base.fullAnalyzed() && this.container.base.isAnalyzed()){
            this.fontRenderer.drawString(Localization.translate("earth_quarry.analyze1"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56));
        } else if (this.container.base.fullAnalyzed() && !this.container.base.isAnalyzed()) {
            this.fontRenderer.drawString(Localization.translate("earth_quarry.full_analyze"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));
        this.drawTexturedModalRect(this.guiLeft + 30, this.guiTop + 30, 37, 52,15, 15);
        if(this.container.base.isAnalyzed() && !this.container.base.fullAnalyzed()){
            this.drawTexturedModalRect(this.guiLeft + 30, this.guiTop + 30, 37, 68,17, 15);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }
}
