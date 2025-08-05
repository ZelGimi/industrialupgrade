package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gasvein.TypeGas;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerGasWellAnalyzer;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.gaswell.TileEntityGasWellAnalyzer;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class GuiGasWellAnalyzer<T extends ContainerGasWellAnalyzer> extends GuiIU<ContainerGasWellAnalyzer> {
    boolean hoverController = false;
    public GuiGasWellAnalyzer(ContainerGasWellAnalyzer guiContainer) {
        super(guiContainer);
        componentList.clear();

    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (hoverController){
            new PacketUpdateServerTile(container.base, 0);
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        hoverController = false;
        if (par1 >= 78 && par2 >= 33 && par1 <= 98 && par2 <= 53){
            hoverController = true;
            new AdvArea(this,78,33,98,53).withTooltip(((TileEntityGasWellAnalyzer) this.container.base).work ? Localization.translate(
                    "turn_off") :
                    Localization.translate("turn_on")).drawForeground(poseStack,par1,par2);
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
            draw(poseStack, Localization.translate("earth_quarry.send_work"), 8, 60,    ModUtils.convertRGBcolorToInt(13, 229, 34));

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
        bindTexture();
        if (((TileEntityGasWellAnalyzer) this.container.base).work){
            this.drawTexturedModalRect(poseStack, this.guiLeft + 78, this.guiTop + 33, 235, 64, 21, 21);

        }
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(poseStack,partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigaswell_analyzer.png");
    }

}
