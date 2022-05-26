package com.simplequarries;

import com.denfop.gui.AdvArea;
import com.denfop.utils.ListInformation;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GUIBaseQuarry extends GuiIC2<ContainerBaseQuarry> {

    public final ContainerBaseQuarry container;

    public GUIBaseQuarry(ContainerBaseQuarry container1) {
        super(container1);
        this.container = container1;
        this.xSize=229;
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 147 && mouseX <= 158 && mouseY >= 27 && mouseY <= 76) {
            List<String> text = new ArrayList<>();
            text.add( Localization.translate("gui.SuperSolarPanel.storage") + ": " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/"+ ModUtils.getString(this.container.base.energy.getCapacity()) );
            List<String> compatibleUpgrades = getStringList();
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }
    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 179 && x <= 189 && y >= 29 && y <= 39) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
        }
        if (x >= 206 && x <= 216 && y >= 29 && y <= 39) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 1);
        }
        if (x >= 179 && x <= 189 && y >= 64 && y <= 74) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 2);
        }
        if (x >= 206 && x <= 216 && y >= 64 && y <= 74) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 3);
        }
        if (x >= 146 && x <= 160 && y >= 5 && y <= 23) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 4);
        }
    }
        private List<String> getStringList() {
        List<String> lst = new ArrayList<>();
        lst.add("Consume: "+ this.container.base.energyconsume + "EU/t");
        lst.add("X: "+ this.container.base.blockpos.getX());
        lst.add("Y: "+ this.container.base.blockpos.getY());
        lst.add("Z: "+ this.container.base.blockpos.getZ());
        return  lst;
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
         handleUpgradeTooltip(mouseX, mouseY);
         this.fontRenderer.drawString(TextFormatting.GREEN+""+ this.container.base.min_y, 187
                , 12,  ModUtils.convertRGBcolorToInt(217, 217, 217));
        this.fontRenderer.drawString(TextFormatting.GREEN+""+ this.container.base.max_y, 187
                , 48,  ModUtils.convertRGBcolorToInt(217, 217, 217));
        new AdvArea(this,172,27,175,75).withTooltip(ModUtils.getString(this.container.base.exp_storage)+"/"+ModUtils.getString(this.container.base.exp_max_storage)).drawForeground(mouseX
                , mouseY);
        new AdvArea(this,146,5,160,23).withTooltip(Localization.translate("sq.add_experience")).drawForeground(mouseX
                , mouseY);

        new AdvArea(this,179,29,189,39).withTooltip("+1").drawForeground(mouseX
                , mouseY);
        new AdvArea(this,179,64,189,74).withTooltip("+1").drawForeground(mouseX
                , mouseY);
        new AdvArea(this,206,29,216,39).withTooltip("-1").drawForeground(mouseX
                , mouseY);
        new AdvArea(this,206,64,216,74).withTooltip("-1").drawForeground(mouseX
                , mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(h, k, 0, 0, this.xSize, 83);
        drawTexturedModalRect(h, k+83, 0, 83, 176, this.ySize-83);

        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (48.0F * this.container.base.energy.getEnergy()
                / this.container.base.energy.getCapacity());
        int exp = (int) (48.0F * this.container.base.exp_storage
                / this.container.base.exp_max_storage);

        if (exp > 0) {
            drawTexturedModalRect(h + 173, k + 28 + 48 - chargeLevel, 194,
                    119+48 - chargeLevel, 12, chargeLevel
            );
        }
        if (chargeLevel > 0) {
            drawTexturedModalRect(h + 140 + 1 + 5+1, k + 28 + 48 - chargeLevel, 176,
                    119+48 - chargeLevel, 12, chargeLevel
            );
        }
        int y1 = 5;
        for(int i = 0;i < this.container.base.index;i++){
            drawTexturedModalRect(h + 5, k + y1, 173,
                    167, 18, 18
            );
            y1+=18;
        }


    }


    public String getName() {
        return container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(SQConstants.MOD_ID, "textures/gui/GUIQuantumQuerry.png");
    }

}
