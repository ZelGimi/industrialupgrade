package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerSteamTurbinePressure;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiSteamTurbinePressure<T extends ContainerSteamTurbinePressure> extends GuiIU<ContainerSteamTurbinePressure> {
    boolean hoverPlus = false;
    boolean hoverMinus = false;
    public GuiSteamTurbinePressure(ContainerSteamTurbinePressure guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (hoverPlus){
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (hoverMinus){
            new PacketUpdateServerTile(this.container.base, 1);
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        hoverMinus = false;
        hoverPlus = false;

        if (par1 >= 62 && par2 >= 37 && par1 <=73 && par2 <= 48) {
            hoverPlus = true;
            new Area(this,62,37,12,12).withTooltip("+1").drawForeground(poseStack,par1,par2);
        }
        if (par1 >= 103 && par2 >= 37 && par1 <=103+11 && par2 <= 48) {
            hoverMinus = true;
            new Area(this,103,37,12,12).withTooltip("-1").drawForeground(poseStack,par1,par2);
        }
        drawString(poseStack,String.valueOf(this.container.base.getPressure()),92 - getStringWidth(String.valueOf(this.container.base.getPressure())), 39, ModUtils.convertRGBcolorToInt(15,
                125, 205
        ));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        if (hoverPlus){
            this.drawTexturedModalRect( poseStack,this.guiLeft+62, this.guiTop+37, 244, 13,12, 12);

        }
        if (hoverMinus){
            this.drawTexturedModalRect( poseStack,this.guiLeft+103, this.guiTop+37, 244, 0,12, 12);

        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteamturbine_pressure.png");
    }

}
