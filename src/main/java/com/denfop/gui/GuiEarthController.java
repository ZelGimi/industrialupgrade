package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerEarthAnalyzer;
import com.denfop.container.ContainerEarthController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiEarthController extends GuiIU<ContainerEarthController> {

    public GuiEarthController(ContainerEarthController guiContainer) {
        super(guiContainer);
    }
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("quarry.guide.earth_quarry"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for(int i =1;i < 10;i++){
                compatibleUpgrades.add(Localization.translate("quarry.guide.earth_quarry"+i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }
    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 30 && x <= 45 && y >= 30 && y <= 45) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        if (this.container.base.work) {
            if (this.container.base.indexChunk < this.container.base.max) {
                this.fontRenderer.drawString(
                        Localization.translate("earth_quarry.controller_work"), 30, 50,
                        ModUtils.convertRGBcolorToInt(56, 56, 56)
                );
            }
        }else if (this.container.base.indexChunk == this.container.base.max) {
            this.fontRenderer.drawString(Localization.translate("earth_quarry.send_work"), 60, 34,
                    ModUtils.convertRGBcolorToInt(56, 56, 56)
            );
        }
        handleUpgradeTooltip(par1, par2);
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
        this.drawTexturedModalRect(this.guiLeft + 30, this.guiTop + 30, 37, 52, 15, 15);
        if (this.container.base.work) {
            this.drawTexturedModalRect(this.guiLeft + 30, this.guiTop + 30, 37, 68, 16, 15);
        }
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft , this.guiTop, 0, 0, 10, 10);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
