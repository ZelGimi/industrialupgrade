package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.container.ContainerWaterSecurity;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ISecurity;
import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class GuiWaterSecurity<T extends ContainerWaterSecurity> extends GuiIU<ContainerWaterSecurity> {

    public GuiWaterSecurity(ContainerWaterSecurity guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer( poseStack, par1, par2);
        ISecurity security = (ISecurity) getContainer().base;
        String name = ((TileEntityMultiBlockElement) security).active.equals("") ? "none" :
                ((TileEntityMultiBlockElement) security).active;
        TileEntityMainController controller = (TileEntityMainController) ((TileEntityMultiBlockElement) security).getMain();
        if (controller != null) {
            String time = "";
            if (name.equals("error")) {
                time = controller.getRed_timer().getDisplay();
            }
            if (name.equals("unstable")) {
                time = controller.getYellow_timer().getDisplay();
            }
            new Area(this, 40, 36, 10, 27).withTooltip(Localization.translate("waterreactor.security." + name) + (!time.isEmpty()
                    ? ("\n" + time)
                    : time)).drawForeground(poseStack, par1, par2);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
        ISecurity security = (ISecurity) getContainer().base;
        bindTexture(new ResourceLocation(
                Constants.MOD_ID,
                "textures/gui/common.png"
        ));
        if (!((TileEntityMultiBlockElement) security).active.equals("")) {
            switch (((TileEntityMultiBlockElement) security).active) {
                case "none":
                    this.drawTexturedModalRect(poseStack,this.guiLeft + 40, this.guiTop + 36, 36, 1, 10, 27);
                    break;
                case "stable":
                    this.drawTexturedModalRect(poseStack,this.guiLeft + 40, this.guiTop + 36, 4, 1, 10, 27);
                    break;
                case "unstable":
                    this.drawTexturedModalRect(poseStack,this.guiLeft + 40, this.guiTop + 36, 21, 1, 10, 27);
                    break;
                case "error":
                    this.drawTexturedModalRect(poseStack,this.guiLeft + 40, this.guiTop + 36, 51, 1, 10, 27);
                    break;
            }
        } else {
            this.drawTexturedModalRect(poseStack,this.guiLeft + 40, this.guiTop + 36, 36, 1, 10, 27);

        }
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor4.png");
    }

}
