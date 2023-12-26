package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.api.reactors.EnumTypeWork;
import com.denfop.container.ContainerGasMainController;
import com.denfop.container.ContainerGraphiteReactor;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiGraphiteController extends GuiIU<ContainerGraphiteReactor> {

    public GuiGraphiteController(ContainerGraphiteReactor guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 248;
        this.ySize = 256;
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if(x >= 210 && y >= 70 && x <= 217&& y <=77){
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if(x >= 223 && y >= 120 && x <= 223+11&& y <=120+14){
            new PacketUpdateServerTile(this.container.base, 3);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.getLevelReactor()),226,30,
                ModUtils.convertRGBcolorToInt(15,
                        125,205));


        String name = this.container.base.security.name().toLowerCase().equals("") ? "none" :
                this.container.base.security.name().toLowerCase();

        if(this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE){
            new Area(this,220,40,17,80).withTooltip("Energy: " + ModUtils.getString(this.container.base.energy.getEnergy()) +
                    "/" + ModUtils.getString(this.container.base.energy.getCapacity())).drawForeground(par1, par2);
        }
        double size = (18 * (this.container.base.getWidth()) + (66 - 10 * ( this.container.base.enumFluidReactors.ordinal()))) /2D ;

        double size1 =
                (12) + (this.container.base.getHeight()) * 18;
        new Area(this, (int)size + 14,(int)size1 + 26,37,48).withTooltip("Radiation "+"\n"+ ModUtils.getString(this.container.base.getRad().getEnergy()) +
                "/" + ModUtils.getString(this.container.base.getRad().getCapacity())  + " ☢").drawForeground(par1,
                par2);
        String time = "" ;
        if(this.container.base.security == EnumTypeSecurity.ERROR)
            time = this.container.base.red_timer.getDisplay();
        if(this.container.base.security == EnumTypeSecurity.UNSTABLE)
            time = this.container.base.yellow_timer.getDisplay();
        new Area(this,190,134,32, 22).withTooltip(Localization.translate("iu.potion.radiation")+ ": " + ModUtils.getString(
                this.container.base.getReactor().getRadGeneration()) + "\n" + ((this.container.base.getLevelReactor() < this.container.base.getMaxLevelReactor()) ?
                Localization.translate("reactor.canupgrade") :  Localization.translate("reactor.notcanupgrade")) + "\n" + Localization.translate("gui.SuperSolarPanel.generating")+ ": " + ModUtils.getString(
                this.container.base.output) + " EF/t" + (!time.isEmpty() ? ("\n" + time) : time)).drawForeground(par1, par2);
        new Area(this,165,124,48-39, 149-116).withTooltip(Localization.translate("waterreactor.security."+name)).drawForeground(par1,
                par2);

        new Area(this,(int)(size / 2 - 10),(int)size1, 157, 26).withTooltip("Heat: " + ModUtils.getString(this.container.base.getHeat()) +
                "/" + ModUtils.getString(this.container.base.getMaxHeat())  + "°C"+ "\n"+"Stable Heat: " + this.container.base.getStableMaxHeat() + "°C").drawForeground(par1,
                par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 256,170);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + 170, 0, 170, 186,81);
        for (int i = 0; i < this.container.base.getWidth(); i++) {
            for (int j = 0; j <this.container.base.getHeight(); j++) {
                drawTexturedModalRect(this.guiLeft + (66 - 10 * ( this.container.base.enumFluidReactors.ordinal())) + i * 18,
                        this.guiTop + (10 ) + j * 18, 235
                        , 234, 18, 18);

            }
        }

        for (int i = 0; i < 4; i++) {

                drawTexturedModalRect(this.guiLeft + 189 + i * 18,
                        this.guiTop +169, 235
                        , 234, 18, 18);


        }
        drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 141
                ,237, 217, 16, 16);
        drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 12
                ,237, 217, 16, 16);
        drawTexturedModalRect(this.guiLeft + 225, this.guiTop + 12
                ,237, 217, 16, 16);
        drawTexturedModalRect(this.guiLeft + 225, this.guiTop + 141
                ,237, 217, 16, 16);
        double size = (18 * (this.container.base.getWidth()) + (66 - 10 * ( this.container.base.enumFluidReactors.ordinal()))) /2D ;

        double size1 =
                (12) + (this.container.base.getHeight()) * 18;
        drawTexturedModalRect((int) (this.guiLeft + size) + 14, (int) (this.guiTop + size1 + 26)
                ,190, 174, 37, 48);
        if(!this.container.base.work) {
            drawTexturedModalRect(this.guiLeft + 210, this.guiTop + 70
                    , 242, 209, 7, 7);
        }else{
            drawTexturedModalRect(this.guiLeft + 210, this.guiTop + 70
                    , 234, 209, 7, 7);
        }
        int rad = (int) (6 * this.container.base.getRad().getFillRatio());
        for(int i =0; i < 6;i++){
            if(i > rad - 1){
                drawTexturedModalRect((int) (this.guiLeft + size) + 14 + 4 + (i % 2) * 17, (int)  (this.guiTop + size1 + 29) + (i / 2) * 15
                        ,241, 173, 15, 12);
            }else{
                drawTexturedModalRect((int) (this.guiLeft + size) + 14 + 4 + (i % 2) * 17,(int)  (this.guiTop + size1 + 29)+ (i / 2) * 15
                        ,241, 187, 15, 12);
            }
        }
        drawTexturedModalRect(this.guiLeft + 190, this.guiTop + 134
                , 190, 224, 32, 22);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));
        if(this.container.base.typeWork == EnumTypeWork.WORK) {
            drawTexturedModalRect(this.guiLeft + 223, this.guiTop + 120
                    , 63, 52, 11, 14);

        }else{
            drawTexturedModalRect(this.guiLeft + 223, this.guiTop + 120
                    ,63, 68, 11, 14);
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));

        drawTexturedModalRect(this.guiLeft + 220, this.guiTop + 40
                , 109, 0, 17, 80);




        if(this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE){
            double bar = this.container.base.energy.getFillRatio();
            bar = Math.min(1,bar);
            drawTexturedModalRect(this.guiLeft + 222, (int) (this.guiTop + 82+35 - (bar * 74))
                    , 93, (int) (77 - (bar * 74)), 13, (int) (bar * 74));

        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));

        drawTexturedModalRect((int) (this.guiLeft + size / 2 - 10),
                (int) (this.guiTop + size1 )
                , 0, 58, 157, 26);
        double bar = this.container.base.heat / this.container.base.getMaxHeat();
        bar = Math.min(bar,1);
        drawTexturedModalRect((int) (this.guiLeft + size / 2 - 10) + 6,  (int) (this.guiTop + size1 ) + 6
                , 6, 90, (int) (bar * 145), 13);
        final EnumTypeSecurity security = this.container.base.security;
        if(security == EnumTypeSecurity.NONE){
            this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));
            drawTexturedModalRect(this.guiLeft + 165, this.guiTop + 124
                    , 39, 116, 48-39, 149-116);
        }
        if(security == EnumTypeSecurity.STABLE){
            this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));
            drawTexturedModalRect(this.guiLeft + 165, this.guiTop + 124
                    , 26, 116, 48-39, 149-116);
        }
        if(security == EnumTypeSecurity.UNSTABLE){
            this.mc.getTextureManager().bindTexture(getTexture());
            drawTexturedModalRect(this.guiLeft + 165, this.guiTop + 124
                    , 223, 223, 48-39, 149-116);
        }
        if(security == EnumTypeSecurity.ERROR){
            this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));
            drawTexturedModalRect(this.guiLeft + 165, this.guiTop + 124
                    , 12, 116, 48-39, 149-116);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_graphite4.png");
    }

}
