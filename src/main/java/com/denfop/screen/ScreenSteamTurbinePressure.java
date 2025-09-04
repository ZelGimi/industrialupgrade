package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.containermenu.ContainerMenuSteamTurbinePressure;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenSteamTurbinePressure<T extends ContainerMenuSteamTurbinePressure> extends ScreenMain<ContainerMenuSteamTurbinePressure> {
    boolean hoverPlus = false;
    boolean hoverMinus = false;

    public ScreenSteamTurbinePressure(ContainerMenuSteamTurbinePressure guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (hoverPlus) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (hoverMinus) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        hoverMinus = false;
        hoverPlus = false;

        if (par1 >= 62 && par2 >= 37 && par1 <= 73 && par2 <= 48) {
            hoverPlus = true;
            new TooltipWidget(this, 62, 37, 12, 12).withTooltip("+1").drawForeground(poseStack, par1, par2);
        }
        if (par1 >= 103 && par2 >= 37 && par1 <= 103 + 11 && par2 <= 48) {
            hoverMinus = true;
            new TooltipWidget(this, 103, 37, 12, 12).withTooltip("-1").drawForeground(poseStack, par1, par2);
        }
        draw(poseStack, String.valueOf(this.container.base.getPressure()), 92 - getStringWidth(String.valueOf(this.container.base.getPressure())), 39, ModUtils.convertRGBcolorToInt(15,
                125, 205
        ));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        if (hoverPlus) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 62, this.guiTop + 37, 244, 13, 12, 12);

        }
        if (hoverMinus) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 103, this.guiTop + 37, 244, 0, 12, 12);

        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteamturbine_pressure.png");
    }

}
