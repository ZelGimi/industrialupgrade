package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerExchanger;
import com.denfop.container.ContainerGasTank;
import com.denfop.container.ContainerGraphiteTank;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiExchanger extends GuiIU<ContainerExchanger> {

    public GuiExchanger(ContainerExchanger guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 186;
        this.ySize = 238;
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if(x >= 87 && y >= 57 - 15 && x <= 98&& y <=57 - 4){
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if(x >= 87 && y >= 57+18+5 && x <= 98&& y <=57+18+16){
            new PacketUpdateServerTile(this.container.base, 1);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.getX()),83 - 10,57 + 4,
                ModUtils.convertRGBcolorToInt(15,
                        125,205));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 57, 189, 4, 18, 18);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));

        drawTexturedModalRect(this.guiLeft + 83 + 4, this.guiTop + 57 - 15
                , 0, 245, 11, 11);


        drawTexturedModalRect(this.guiLeft + 83 + 4, this.guiTop +57+18+5
                , 11, 245, 11, 11);
        this.mc.getTextureManager().bindTexture(this.getTexture());
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_graphite.png");
    }

}
