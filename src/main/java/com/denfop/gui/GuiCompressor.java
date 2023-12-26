package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerCompressor;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiCompressor extends GuiIU<ContainerCompressor> {

    public GuiCompressor(ContainerCompressor guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 186;
        this.ySize = 211;
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if(x >= 90 && x <= 100 && y >= 38 && y <= 48)
            new PacketUpdateServerTile(this.container.base, 0);
        if(x >= 90 && x <= 100 && y >= 80 && y <= 90)
            new PacketUpdateServerTile(this.container.base, 1);
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.getPressure()),93,61, ModUtils.convertRGBcolorToInt(15,
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
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));

        drawTexturedModalRect(this.guiLeft + 90, this.guiTop +38
                , 0, 245, 11, 11);


        drawTexturedModalRect(this.guiLeft + 90, this.guiTop +80
                , 11, 245, 11, 11);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigraphitereactor.png");
    }
}
