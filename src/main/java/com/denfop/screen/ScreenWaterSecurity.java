package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.water.ISecurity;
import com.denfop.blockentity.reactors.water.controller.BlockEntityMainController;
import com.denfop.containermenu.ContainerMenuWaterSecurity;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenWaterSecurity<T extends ContainerMenuWaterSecurity> extends ScreenMain<ContainerMenuWaterSecurity> {

    public ScreenWaterSecurity(ContainerMenuWaterSecurity guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        ISecurity security = (ISecurity) getContainer().base;
        String name = ((BlockEntityMultiBlockElement) security).active.equals("") ? "none" :
                ((BlockEntityMultiBlockElement) security).active;
        BlockEntityMainController controller = (BlockEntityMainController) ((BlockEntityMultiBlockElement) security).getMain();
        if (controller != null) {
            String time = "";
            if (name.equals("error")) {
                time = controller.getRed_timer().getDisplay();
            }
            if (name.equals("unstable")) {
                time = controller.getYellow_timer().getDisplay();
            }
            new TooltipWidget(this, 40, 36, 10, 27).withTooltip(Localization.translate("waterreactor.security." + name) + (!time.isEmpty()
                    ? ("\n" + time)
                    : time)).drawForeground(poseStack, par1, par2);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        ISecurity security = (ISecurity) getContainer().base;
        bindTexture(new ResourceLocation(
                Constants.MOD_ID,
                "textures/gui/common.png"
        ));
        if (!((BlockEntityMultiBlockElement) security).active.equals("")) {
            switch (((BlockEntityMultiBlockElement) security).active) {
                case "none":
                    this.drawTexturedModalRect(poseStack, this.guiLeft + 40, this.guiTop + 36, 36, 1, 10, 27);
                    break;
                case "stable":
                    this.drawTexturedModalRect(poseStack, this.guiLeft + 40, this.guiTop + 36, 4, 1, 10, 27);
                    break;
                case "unstable":
                    this.drawTexturedModalRect(poseStack, this.guiLeft + 40, this.guiTop + 36, 21, 1, 10, 27);
                    break;
                case "error":
                    this.drawTexturedModalRect(poseStack, this.guiLeft + 40, this.guiTop + 36, 51, 1, 10, 27);
                    break;
            }
        } else {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 40, this.guiTop + 36, 36, 1, 10, 27);

        }
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor4.png");
    }

}
