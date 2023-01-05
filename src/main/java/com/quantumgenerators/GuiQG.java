package com.quantumgenerators;

import com.denfop.gui.AdvArea;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;


public class GuiQG extends GuiIC2<ContainerQG> {

    public final ContainerQG container;

    public GuiQG(ContainerQG container1) {
        super(container1);
        this.container = container1;
        this.ySize = 195;
    }


    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        String name = Localization.translate(this.container.base.getName());
        int nmPos = (this.xSize - this.fontRenderer.getStringWidth(name)) / 2;
        this.fontRenderer.drawString(name, nmPos, 6, 4210752);
        String generatingString = Localization.translate("gui.SuperSolarPanel.generating") + ": ";
        this.fontRenderer.drawString(TextFormatting.GREEN + generatingString + ModUtils.getString(this.container.base.gen) + " " +
                        "QE/t", 36
                , 30, ModUtils.convertRGBcolorToInt(217, 217, 217));
        new AdvArea(this, 22, 64, 32, 74).withTooltip("+" + ModUtils.getString(1000)).drawForeground(mouseX, mouseY);
        new AdvArea(this, 40, 64, 60, 74).withTooltip("+" + ModUtils.getString(10000)).drawForeground(mouseX, mouseY);
        new AdvArea(this, 67, 64, 97, 74).withTooltip("+" + ModUtils.getString(100000)).drawForeground(mouseX, mouseY);
        new AdvArea(this, 105, 64, 145, 74).withTooltip("+" + ModUtils.getString(1000000)).drawForeground(mouseX, mouseY);
        new AdvArea(this, 22, 82, 32, 92).withTooltip("-" + ModUtils.getString(1000)).drawForeground(mouseX, mouseY);
        new AdvArea(this, 40, 82, 60, 92).withTooltip("-" + ModUtils.getString(10000)).drawForeground(mouseX, mouseY);
        new AdvArea(this, 67, 82, 97, 92).withTooltip("-" + ModUtils.getString(100000)).drawForeground(mouseX, mouseY);
        new AdvArea(this, 105, 82, 145, 92).withTooltip("-" + ModUtils.getString(1000000)).drawForeground(mouseX, mouseY);
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 22 && x <= 32 && y >= 64 && y <= 74) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
        }
        if (x >= 40 && x <= 60 && y >= 64 && y <= 74) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 1);
        }
        if (x >= 67 && x <= 97 && y >= 64 && y <= 74) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 2);
        }
        if (x >= 105 && x <= 145 && y >= 64 && y <= 74) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 3);
        }

        if (x >= 22 && x <= 32 && y >= 82 && y <= 92) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 4);
        }
        if (x >= 40 && x <= 60 && y >= 82 && y <= 92) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 5);
        }
        if (x >= 67 && x <= 97 && y >= 82 && y <= 92) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 6);
        }
        if (x >= 105 && x <= 145 && y >= 82 && y <= 92) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 7);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager().bindTexture(getTexture());


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_quantumgenerator.png");
    }

}
