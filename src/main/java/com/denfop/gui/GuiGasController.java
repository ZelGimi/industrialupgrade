package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.api.reactors.EnumTypeWork;
import com.denfop.container.ContainerGasMainController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiGasController extends GuiIU<ContainerGasMainController> {

    public GuiGasController(ContainerGasMainController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 256;
        this.ySize = 256;
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if(x >= 199 && y >= 163 && x <= 205&& y <=168){
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if(x >= 223 && y >= 170 && x <= 223+11&& y <=170+14){
            new PacketUpdateServerTile(this.container.base, 3);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.getLevelReactor()),226,75,
                ModUtils.convertRGBcolorToInt(15,
                        125,205));
        new Area(this,13,140, 159, 30).withTooltip("Heat: " + ModUtils.getString(this.container.base.getHeat()) +
                "/" + ModUtils.getString(this.container.base.getMaxHeat())  + "°C"+ "\n"+"Stable Heat: " + this.container.base.getStableMaxHeat() + "°C").drawForeground(par1,
                par2);
        new AdvArea(this,219,189,238,212).withTooltip("Radiation "+"\n"+ ModUtils.getString(this.container.base.getRad().getEnergy()) +
                "/" + ModUtils.getString(this.container.base.getRad().getCapacity())  + " ☢").drawForeground(par1,
                par2);

        String name = this.container.base.security.name().toLowerCase().equals("") ? "none" :
                this.container.base.security.name().toLowerCase();
        new Area(this,200,191,5, 11).withTooltip(Localization.translate("waterreactor.security."+name)).drawForeground(par1,
                par2);
        if(this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE){
            new Area(this,220,85,17,80).withTooltip("Energy: " + ModUtils.getString(this.container.base.energy.getEnergy()) +
                    "/" + ModUtils.getString(this.container.base.energy.getCapacity())).drawForeground(par1, par2);
        }
        String time = "" ;
        if(this.container.base.security == EnumTypeSecurity.ERROR)
            time = this.container.base.red_timer.getDisplay();
        if(this.container.base.security == EnumTypeSecurity.UNSTABLE)
            time = this.container.base.yellow_timer.getDisplay();

        new AdvArea(this,191,121,213,137).withTooltip(Localization.translate("iu.potion.radiation")+ ": " + ModUtils.getString(
                this.container.base.getReactor().getRadGeneration()) + "\n" + ((this.container.base.getLevelReactor() < this.container.base.getMaxLevelReactor()) ?
                Localization.translate("reactor.canupgrade") :  Localization.translate("reactor.notcanupgrade")) + "\n" + Localization.translate("gui.SuperSolarPanel.generating")+ ": " + ModUtils.getString(
                this.container.base.output) + " EF/t" + (!time.isEmpty() ? ("\n" + time) : time)).drawForeground(par1, par2);


    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 248,224);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + 224, 0, 224, 186,31);
        for (int i = 0; i < this.container.base.getWidth(); i++) {
            for (int j = 0; j <this.container.base.getHeight(); j++) {
                drawTexturedModalRect(this.guiLeft + (66 - 10 * ( this.container.base.enumFluidReactors.ordinal())) + i * 20,
                        this.guiTop + (15 + 1 * (3 - this.container.base.enumFluidReactors.ordinal())) + j * 20, 236
                        , 236, 20, 20);

            }
        }

        for (int i = 0; i < 4; i++) {

                drawTexturedModalRect(this.guiLeft + 185 + i * 19,
                        this.guiTop +229, 236
                        , 236, 20, 20);


        }
        if(this.container.base.work){
            drawTexturedModalRect(this.guiLeft + 200, this.guiTop + 164
                    , 204, 244, 5, 4);
        }
        switch (this.container.base.security){
            case NONE:
                drawTexturedModalRect(this.guiLeft + 200, this.guiTop + 191
                        , 193, 230, 5, 11);
                break;
            case ERROR:
                drawTexturedModalRect(this.guiLeft + 200, this.guiTop + 191
                        , 200, 230, 5, 11);
                break;
            case STABLE:
                drawTexturedModalRect(this.guiLeft + 200, this.guiTop + 191
                        , 214, 230, 5, 11);
                break;
            case UNSTABLE:
                drawTexturedModalRect(this.guiLeft + 200, this.guiTop + 191
                        , 207, 230, 5, 11);
                break;
        }
        double rad = 6 * this.container.base.getRad().getEnergy() / this.container.base.getRad().getCapacity();
        for(int i =0; i < rad;i++){
            drawTexturedModalRect(this.guiLeft + 222  + (i % 3) * 5, this.guiTop + 193 + (i / 3) * 13
                    , 193, 244, 4, 4);
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));
        if(this.container.base.typeWork == EnumTypeWork.WORK) {
            drawTexturedModalRect(this.guiLeft + 223, this.guiTop + 170
                    , 63, 52, 11, 14);

        }else{
            drawTexturedModalRect(this.guiLeft + 223, this.guiTop + 170
                    ,63, 68, 11, 14);
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));

        drawTexturedModalRect(this.guiLeft + 220, this.guiTop + 85
                , 109, 0, 17, 80);
        if(this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE){
            double bar = this.container.base.energy.getFillRatio();
            bar = Math.min(1,bar);
            drawTexturedModalRect(this.guiLeft + 222, (int) (this.guiTop + 123+40 - (bar * 74))
                    , 93, (int) (77 - (bar * 74)), 13, (int) (bar * 74));

        }
        drawTexturedModalRect(this.guiLeft + 13, this.guiTop + 140 - 2 * (3 - this.container.base.enumFluidReactors.ordinal())
                , 97, 114, 159, 30);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));

        double bar = this.container.base.heat / this.container.base.getMaxHeat();
        bar = Math.min(bar,1);
        drawTexturedModalRect(this.guiLeft + 17, this.guiTop + 143 - 2 * (3 - this.container.base.enumFluidReactors.ordinal())
                , 1, 20, (int) (bar * 153), 24);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigraphitereactor5.png");
    }

}
