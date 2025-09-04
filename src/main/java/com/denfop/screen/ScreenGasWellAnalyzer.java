package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.vein.gas.TypeGas;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.blockentity.gaswell.BlockEntityGasWellAnalyzer;
import com.denfop.containermenu.ContainerMenuGasWellAnalyzer;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenGasWellAnalyzer<T extends ContainerMenuGasWellAnalyzer> extends ScreenMain<ContainerMenuGasWellAnalyzer> {
    boolean hoverController = false;

    public ScreenGasWellAnalyzer(ContainerMenuGasWellAnalyzer guiContainer) {
        super(guiContainer);
        componentList.clear();

    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (hoverController) {
            new PacketUpdateServerTile(container.base, 0);
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        hoverController = false;
        if (par1 >= 78 && par2 >= 33 && par1 <= 98 && par2 <= 53) {
            hoverController = true;
            new AdvancedTooltipWidget(this, 78, 33, 98, 53).withTooltip(((BlockEntityGasWellAnalyzer) this.container.base).work ? Localization.translate(
                    "turn_off") :
                    Localization.translate("turn_on")).drawForeground(poseStack, par1, par2);
        }
        if (this.container.base.vein != null && !container.base.vein.isFind()) {
            if (this.container.base.progress < 1200) {
                draw(poseStack,
                        (this.container.base.progress * 100 / 1200) + "%",
                        8, 60,
                        ModUtils.convertRGBcolorToInt(13, 229, 34)
                );


            }
        } else if (this.container.base.vein != null && container.base.vein.isFind() && container.base.vein.getType() == TypeGas.NONE) {
            draw(poseStack,
                    Localization.translate("earth_quarry.error"),
                    8, 60,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
        } else if (this.container.base.vein != null && container.base.vein.isFind() && container.base.vein.getType() != TypeGas.NONE) {
            draw(poseStack, Localization.translate("earth_quarry.send_work"), 8, 60, ModUtils.convertRGBcolorToInt(13, 229, 34));

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture();
        if (((BlockEntityGasWellAnalyzer) this.container.base).work) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 78, this.guiTop + 33, 235, 64, 21, 21);

        }
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigaswell_analyzer.png");
    }

}
