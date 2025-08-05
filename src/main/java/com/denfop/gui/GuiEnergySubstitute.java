package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerRemover;
import com.denfop.container.ContainerSubstitute;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;


public class GuiEnergySubstitute<T extends ContainerSubstitute> extends GuiIU<ContainerSubstitute> {

    public final ContainerSubstitute container;
    boolean hoverFind;
    boolean hoverUpdate;
    public GuiEnergySubstitute(ContainerSubstitute container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.imageHeight=178;

    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (hoverFind){
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (hoverUpdate){
            new PacketUpdateServerTile(this.container.base, 1);
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        hoverFind = false;
        hoverUpdate = false;
        if (par1 >= 130 && par1 <= 155 && par2 >= 17 && par2 <= 39){
            new AdvArea(this,130,17,155,39).withTooltip(  Localization.translate("button.find_energypaths")).drawForeground(poseStack,par1,par2);
            hoverFind = true;
        }
        if (par1 >= 130 && par1 <= 155 && par2 >= 17+25 && par2 <= 39+25){
            new AdvArea(this,130,17+25,155,39+25).withTooltip(  Localization.translate("button" +
                    ".set_value_energypaths")).drawForeground(poseStack,par1,par2);
            hoverUpdate = true;
        }
    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(poseStack,partialTicks,mouseX,mouseY);
        this.drawTexturedModalRect(poseStack,this.guiLeft+130, this.guiTop+17, 200, 1, 22, 22);
        this.drawTexturedModalRect(poseStack,this.guiLeft+130, this.guiTop+17+25, 200, 26, 22, 22);
        if (hoverFind){
            this.drawTexturedModalRect(poseStack,this.guiLeft+130, this.guiTop+17, 223, 1, 22, 22);

        }
        if (hoverUpdate){
            this.drawTexturedModalRect(poseStack,this.guiLeft+130, this.guiTop+17+25, 223, 26, 22, 22);

        }
    }





    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_controllercable.png");
    }

}
