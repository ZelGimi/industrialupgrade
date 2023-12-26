package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.TankGauge;
import com.denfop.container.ContainerExchanger;
import com.denfop.container.ContainerGasTank;
import com.denfop.container.ContainerGraphiteTank;
import com.denfop.container.ContainerHeatCirculationPump;
import com.denfop.container.ContainerHeatPump;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiHeatCirculationPump extends GuiIU<ContainerHeatCirculationPump> {

    public GuiHeatCirculationPump(ContainerHeatCirculationPump guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 186;
        this.ySize = 211;
    }



    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);

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

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat2.png");
    }

}
