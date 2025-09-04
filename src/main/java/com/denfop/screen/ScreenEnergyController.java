package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.api.widget.ImageScreenWidget;
import com.denfop.containermenu.ContainerMenuController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


public class ScreenEnergyController<T extends ContainerMenuController> extends ScreenMain<ContainerMenuController> {

    public final ContainerMenuController container;
    boolean hoverFind;
    boolean hoverUpdate;

    public ScreenEnergyController(ContainerMenuController container1) {
        super(container1);
        this.container = container1;

        this.addWidget(new ImageScreenWidget(this, 7, 32, 115, 15));

        this.imageHeight = 176;
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
        draw(poseStack, Localization.translate("iu.energy_controller_info") + (this.container.base.size), 66 - getStringWidth(Localization.translate("iu.energy_controller_info") + (this.container.base.size)) / 2, 37,
                2157374
        );
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_controllercable.png"));
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
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
