package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerStorageCells;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


public class ScreenStorageCells<T extends ContainerStorageCells> extends ScreenMain<ContainerStorageCells> {

    public final ContainerStorageCells container;

    public ScreenStorageCells(ContainerStorageCells container1) {
        super(container1, EnumTypeStyle.STORAGE);
        this.container = container1;
        this.componentList.clear();
        this.imageHeight = 186;
    }


    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        if (container.base.maxValue != 0) {


            new AdvancedTooltipWidget(this, 137, 10, 146, 99).withTooltip(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(container.base.value) + "B" + "/" + ModUtils.getString(container.base.maxValue) + "B").drawForeground(poseStack, par1, par2);
        }

    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        poseStack.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        if (container.base.maxValue != 0) {
            double scale = Math.min(container.base.value * 1D / container.base.maxValue, 1);
            int chargeLevel = (int) (scale * 86);
            this.drawTexturedModalRect(
                    poseStack, guiLeft + 139,
                    guiTop + 11 - chargeLevel + 87,
                    248,
                    87 - chargeLevel,
                    6,
                    chargeLevel
            );
        }
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guistoragecells.png");
    }

}
