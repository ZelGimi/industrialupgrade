package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.api.reactors.EnumTypeWork;
import com.denfop.container.ContainerGasMainController;
import com.denfop.container.ContainerGraphiteReactor;
import com.denfop.container.ContainerHeatReactor;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiHeatController extends GuiIU<ContainerHeatReactor> {

    public GuiHeatController(ContainerHeatReactor guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 250;
        this.ySize = 256;
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if(x >= 223 && y >= 120 && x <= 223+11&& y <=120+14){
            new PacketUpdateServerTile(this.container.base, 3);
        }
        if(x >= 210 && y >= 70 && x <= 217&& y <=77){
            new PacketUpdateServerTile(this.container.base, 0);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(String.valueOf(this.container.base.getLevelReactor()),230,30,
                ModUtils.convertRGBcolorToInt(15,
                        125,205));
        if(this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE){
            new Area(this,224,40,17,80).withTooltip("Energy: " + ModUtils.getString(this.container.base.energy.getEnergy()) +
                    "/" + ModUtils.getString(this.container.base.energy.getCapacity())).drawForeground(par1, par2);
        }
        double size =  (10 * (this.container.base.getWidth()) + (66 - 10 * ( this.container.base.enumFluidReactors.ordinal()))) /2D ;

        double size1 =
                (12) + (this.container.base.getHeight()) * 18;
        new Area(this,(int)(size / 2 - 10),(int)size1, 162, 24).withTooltip("Heat: " + ModUtils.getString(this.container.base.getHeat()) +
                "/" + ModUtils.getString(this.container.base.getMaxHeat())  + "°C"+ "\n"+"Stable Heat: " + this.container.base.getStableMaxHeat() + "°C").drawForeground(par1,
                par2);
        String name = this.container.base.security.name().toLowerCase().equals("") ? "none" :
                this.container.base.security.name().toLowerCase();
        new Area(this,210,86,48-39, 149-116).withTooltip(Localization.translate("waterreactor.security."+name)).drawForeground(par1,
                par2);
        new Area(this, 180,70,14, 27).withTooltip("Radiation "+"\n"+ ModUtils.getString(this.container.base.getRad().getEnergy()) +
                "/" + ModUtils.getString(this.container.base.getRad().getCapacity())  + " ☢").drawForeground(par1,
                par2);
        String time = "" ;
        if(this.container.base.security == EnumTypeSecurity.ERROR)
            time = this.container.base.red_timer.getDisplay();
        if(this.container.base.security == EnumTypeSecurity.UNSTABLE)
            time = this.container.base.yellow_timer.getDisplay();
        new Area(this,185,120,225-187, 95-56).withTooltip(Localization.translate("iu.potion.radiation")+ ": " + ModUtils.getString(
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
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 256,165);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + 165, 0, 165, 186,87);
      for (int i = 0; i < this.container.base.getWidth(); i++) {
            for (int j = 0; j <this.container.base.getHeight(); j++) {
                drawTexturedModalRect(this.guiLeft + (65 - 10 * ( this.container.base.enumFluidReactors.ordinal())) + i * 18,
                        this.guiTop + (9 ) + j * 18, 231
                        , 234, 20, 20);

            }
        }

        for (int i = 0; i < 4; i++) {

                drawTexturedModalRect(this.guiLeft +    186 + i * 18,
                        this.guiTop + 165, 231
                        , 234, 20, 20);


        }
        double size =
                (10 * (this.container.base.getWidth()) + (66 - 10 * ( this.container.base.enumFluidReactors.ordinal()))) /2D ;

        double size1 =
                (12) + (this.container.base.getHeight()) * 18;
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));

        drawTexturedModalRect(this.guiLeft + 224, this.guiTop + 40
                , 109, 0, 17, 80);




        if(this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE){
            double bar = this.container.base.energy.getFillRatio();
            bar = Math.min(1,bar);
            drawTexturedModalRect(this.guiLeft + 226, (int) (this.guiTop + 83+35 - (bar * 74))
                    , 93, (int) (77 - (bar * 74)), 13, (int) (bar * 74));

        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));

        drawTexturedModalRect((int) (this.guiLeft + size / 2 - 10),
                (int) (this.guiTop + size1 )
                , 1, 186, 162, 24);
        double bar = this.container.base.heat / this.container.base.getMaxHeat();
        bar = Math.min(bar,1);
        drawTexturedModalRect((int) (this.guiLeft + size / 2 - 10) + 6,  (int) (this.guiTop + size1 ) + 3
                , 7, 161, (int) (bar * 151), 19);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));
        if(this.container.base.typeWork == EnumTypeWork.WORK) {
            drawTexturedModalRect(this.guiLeft + 227, this.guiTop + 120
                    , 63, 52, 11, 14);

        }else{
            drawTexturedModalRect(this.guiLeft + 227, this.guiTop + 120
                    ,63, 68, 11, 14);
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat1.png"));

        if(!this.container.base.work) {
            drawTexturedModalRect(this.guiLeft + 210, this.guiTop + 70
                    , 227, 28, 14, 14);
        }else{
            drawTexturedModalRect(this.guiLeft + 210, this.guiTop + 70
                    , 242, 28, 14, 14);
        }
        drawTexturedModalRect(this.guiLeft + 180, this.guiTop + 70
                , 188, 28, 14, 27);

        drawTexturedModalRect(this.guiLeft + 185, this.guiTop + 120
                , 188, 57, 225-187, 95-56);

        int rad = (int) (6 * this.container.base.getRad().getFillRatio());
        for(int i =0; i < 6;i++){
            if(i <= rad - 1){
                drawTexturedModalRect(this.guiLeft + 180 + 3 + (i % 2) * 4,
                        this.guiTop + 70 + 15 + (i / 2) * 3
                        ,208  + (i % 2) * 4 , 43 + (i / 2) * 3, 4, 4);
            }
        }
        final EnumTypeSecurity security = this.container.base.security;
        if(security == EnumTypeSecurity.NONE){
            this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));
            drawTexturedModalRect(this.guiLeft + 210, this.guiTop + 86
                    , 39, 116, 48-39, 149-116);
        }
        if(security == EnumTypeSecurity.STABLE){
            this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));
            drawTexturedModalRect(this.guiLeft + 210, this.guiTop + 86
                    , 26, 116, 48-39, 149-116);
        }
        if(security == EnumTypeSecurity.UNSTABLE){
            this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_graphite4.png"));

            drawTexturedModalRect(this.guiLeft + 210, this.guiTop + 86
                    , 223, 223, 48-39, 149-116);
        }
        if(security == EnumTypeSecurity.ERROR){
            this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));
            drawTexturedModalRect(this.guiLeft + 210, this.guiTop + 86
                    , 12, 116, 48-39, 149-116);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat3.png");
    }

}
