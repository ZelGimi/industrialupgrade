package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.containermenu.ContainerMenuRemover;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


public class ScreenEnergyRemover<T extends ContainerMenuRemover> extends ScreenMain<ContainerMenuRemover> {

    public final ContainerMenuRemover container;
    boolean hoverFind;
    boolean hoverUpdate;

    public ScreenEnergyRemover(ContainerMenuRemover container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.imageHeight = 178;

    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (hoverFind) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (hoverUpdate) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        hoverFind = false;
        hoverUpdate = false;
        if (par1 >= 130 && par1 <= 155 && par2 >= 17 && par2 <= 39) {
            new AdvancedTooltipWidget(this, 130, 17, 155, 39).withTooltip(Localization.translate("button.find_energypaths")).drawForeground(poseStack, par1, par2);
            hoverFind = true;
        }
        if (par1 >= 130 && par1 <= 155 && par2 >= 17 + 25 && par2 <= 39 + 25) {
            new AdvancedTooltipWidget(this, 130, 17 + 25, 155, 39 + 25).withTooltip(Localization.translate("button" +
                    ".set_value_energypaths")).drawForeground(poseStack, par1, par2);
            hoverUpdate = true;
        }
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 130, this.guiTop + 17, 200, 1, 22, 22);
        this.drawTexturedModalRect(poseStack, this.guiLeft + 130, this.guiTop + 17 + 25, 200, 26, 22, 22);
        if (hoverFind) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 130, this.guiTop + 17, 223, 1, 22, 22);

        }
        if (hoverUpdate) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 130, this.guiTop + 17 + 25, 223, 26, 22, 22);

        }
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_controllercable.png");
    }

}
