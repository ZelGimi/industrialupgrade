package com.powerutils;


import com.denfop.utils.ModUtils;
import ic2.api.energy.EnergyNet;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiTEConverter extends GuiIC2<ContainerTEConverter> {

    private static final ResourceLocation background = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/ec3_gui.png"
    );
    private final ContainerTEConverter container;
    private final TileEntityTEConverter tileentity;

    public GuiTEConverter(ContainerTEConverter container1) {
        super(container1);
        this.ySize = 176;
        this.container = container1;
        this.tileentity = container1.base;

    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

        if (x >= 43 && x <= 58 && y >= 77 && y <= 87) {
            if (this.container.base.rf) {
                IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
            }
        } else if (x >= 59 && x <= 73 && y >= 77 && y <= 87) {
            if (!this.container.base.rf) {
                IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
            }

        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    @Override
    protected void drawForegroundLayer(int par1, int par2) {
        int nmPos = (this.xSize - this.fontRenderer.getStringWidth(Localization.translate(this.container.base.getName()))) / 2;
        this.fontRenderer.drawString(Localization.translate(this.container.base.getName()), nmPos, 6, 7718655);
        this.fontRenderer.drawString(
                "EU: " + ModUtils.getString(this.tileentity.energy.getEnergy()) + "/" + ModUtils.getString(
                        this.tileentity.capacity),
                9,
                20,
                13487565
        );
        this.fontRenderer.drawString("TE: " + ModUtils.getString(this.tileentity.energy2) + "/" + ModUtils.getString(this.tileentity.maxStorage2),
                9, 30, 13487565
        );
        if (this.tileentity.rf) {
            this.fontRenderer.drawString(
                    "MAX IN: " + ModUtils.getString(EnergyNet.instance.getPowerFromTier(this.tileentity.tier)) + " EU/t",
                    9,
                    40,
                    13487565
            );
            this.fontRenderer.drawString("IN: " + ModUtils.getString(this.tileentity.differenceenergy1) + " EU/t", 9, 50,
                    13487565
            );
            this.fontRenderer.drawString("OUT: " + ModUtils.getString(this.tileentity.differenceenergy) + " TE/t", 9, 60,
                    13487565
            );
        } else {
            this.fontRenderer.drawString(
                    "MAX OUT: " + ModUtils.getString(EnergyNet.instance.getPowerFromTier(this.tileentity.tier)) + " EU/t",
                    9,
                    40,
                    13487565
            );
            this.fontRenderer.drawString("IN: " + ModUtils.getString(this.tileentity.differenceenergy1) + " TE/t", 9, 50,
                    13487565
            );
            this.fontRenderer.drawString("OUT: " + ModUtils.getString(this.tileentity.differenceenergy) + " EU/t", 9, 60,
                    13487565
            );
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
        if (this.container.base.rf) {
            drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

        }
        if (!this.tileentity.rf) {
            if (isPointInRegion(43, 76, 31, 12, mouseX, mouseY)) {
                drawTexturedModalRect(j + 43, k + 76, 194, 44, 15, 11);
            } else {
                drawTexturedModalRect(j + 43, k + 76, 177, 44, 15, 11);
            }
            drawTexturedModalRect(j + 6, k + 72, 177, 4, 36, 19);
            drawTexturedModalRect(j + 116, k + 70, 177, 102, 16, 17);
        } else {
            if (isPointInRegion(43, 76, 31, 12, mouseX, mouseY)) {
                drawTexturedModalRect(j + 59, k + 76, 194, 44, 15, 11);
            } else {
                drawTexturedModalRect(j + 59, k + 76, 177, 44, 15, 11);
            }
            drawTexturedModalRect(j + 75, k + 72, 177, 24, 36, 19);
            drawTexturedModalRect(j + 131, k + 70, 194, 102, 16, 17);
        }
        if (this.tileentity.energy.getEnergy() > 0) {
            int gaugeFullHeight = 39;
            int gaugeScaledHeight = this.tileentity.gaugeICEnergyScaled(gaugeFullHeight);
            drawTexturedModalRect(j + 119, k + 68 - gaugeScaledHeight, 177, 97 - gaugeScaledHeight, 11, gaugeScaledHeight);
        }
        if (this.tileentity.energy2 > 0) {
            int gaugeFullHeight = 39;
            int gaugeScaledHeight = this.tileentity.gaugeTEEnergyScaled(gaugeFullHeight);
            drawTexturedModalRect(j + 133, k + 68 - gaugeScaledHeight, 189, 97 - gaugeScaledHeight, 11, gaugeScaledHeight);
        }
    }

}
