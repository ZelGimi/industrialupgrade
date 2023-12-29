package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.reactors.EnumTypeWork;
import com.denfop.container.ContainerWaterMainController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiWaterMainController extends GuiIU<ContainerWaterMainController> {

    public GuiWaterMainController(ContainerWaterMainController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.ySize = 259;
        this.xSize = 187;
    }
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 5 && mouseY <= 17) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("reactor.guide.water_reactor"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for(int i =1;i < 15;i++){
                compatibleUpgrades.add(Localization.translate("reactor.guide.water_reactor"+i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 40, mouseY + 10, text);
        }
    }
    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.getPressure()),13,51,ModUtils.convertRGBcolorToInt(15,
                125,205));
        this.fontRenderer.drawString(String.valueOf(this.container.base.getLevelReactor()),160,130,
                ModUtils.convertRGBcolorToInt(15,
                125,205));
        if(this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE){
            new Area(this,154,45,17,80).withTooltip("Energy: " + ModUtils.getString(this.container.base.energy.getEnergy()) +
                    "/" + ModUtils.getString(this.container.base.energy.getCapacity())).drawForeground(par1, par2);
        }
        new Area(this,26,145 - 8 * (3 - this.container.base.enumFluidReactors.ordinal()), 123, 25).withTooltip("Heat: " + ModUtils.getString(this.container.base.getHeat()) +
                "/" + ModUtils.getString(this.container.base.getMaxHeat())  + "°C"+ "\n"+"Stable Heat: " + this.container.base.getStableMaxHeat() + "°C").drawForeground(par1,
                par2);

        handleUpgradeTooltip(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        for (int i = 0; i < this.container.base.getWidth(); i++) {
            for (int j = 0; j < this.container.base.getHeight(); j++) {
                drawTexturedModalRect(this.guiLeft + (28 + 10 * (3 - this.container.base.enumFluidReactors.ordinal())) + i * 20,
                        this.guiTop + (23 + 10 * (3 - this.container.base.enumFluidReactors.ordinal())) + j * 20, 188
                        , 3, 20, 20);

            }
        }
        for (int i = 0; i <4; i++) {
                drawTexturedModalRect(this.guiLeft + 186,
                        this.guiTop + 38 + i * 18, 188
                        , 3, 20, 20);


        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));
        drawTexturedModalRect(this.guiLeft + 26, this.guiTop + 145 - 8 * (3 - this.container.base.enumFluidReactors.ordinal())
                , 0, 190, 123, 25);
        double bar = this.container.base.heat / this.container.base.getMaxHeat();
        bar = Math.min(bar,1);
        drawTexturedModalRect(this.guiLeft + 30, this.guiTop + 148 - 8 * (3 - this.container.base.enumFluidReactors.ordinal())
                , 4, 218, (int) (bar * 116), 17);
        drawTexturedModalRect(this.guiLeft + 154, this.guiTop + 45
                , 109, 0, 17, 80);
        if(this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE){
             bar = this.container.base.energy.getFillRatio();
            bar = Math.min(1,bar);
            drawTexturedModalRect(this.guiLeft + 156, (int) (this.guiTop + 123 - (bar * 74))
                    , 93, (int) (77 - (bar * 74)), 13, (int) (bar * 74));

        }

        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));

        drawTexturedModalRect(this.guiLeft + 10, this.guiTop +38
                , 0, 245, 11, 11);


        drawTexturedModalRect(this.guiLeft + 10, this.guiTop +60
                , 11, 245, 11, 11);
        if(!this.container.base.work) {
            drawTexturedModalRect(this.guiLeft + 9, this.guiTop + 80
                    , 38, 53, 16, 14);

        }else{
            drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 79
                    , 37, 68, 16, 15);
        }
        if(this.container.base.typeWork == EnumTypeWork.WORK) {
            drawTexturedModalRect(this.guiLeft + 11, this.guiTop + 100
                    , 63, 52, 11, 14);

        }else{
            drawTexturedModalRect(this.guiLeft + 11, this.guiTop + 100
                    ,63, 68, 11, 14);
        }

        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft +3 , this.guiTop+5, 0, 0, 10, 10);
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if(x >= 10 && x <= 20 && y >= 38 && y <= 48)
            new PacketUpdateServerTile(this.container.base, 1);
        if(x >= 10 && x <= 20 && y >= 60 && y <= 70)
            new PacketUpdateServerTile(this.container.base, 2);
        if(x >= 10 && x <= 24 && y >= 80 && y <= 94)
            new PacketUpdateServerTile(this.container.base, 0);
        if(x >= 11 && x <= 21 && y >= 100 && y <= 114)
            new PacketUpdateServerTile(this.container.base, 3);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiwaterreactor5.png");
    }

}
