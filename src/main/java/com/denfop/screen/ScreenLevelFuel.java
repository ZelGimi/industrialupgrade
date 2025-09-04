package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TanksWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.containermenu.ContainerMenuLevelFuel;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenLevelFuel<T extends ContainerMenuLevelFuel> extends ScreenMain<ContainerMenuLevelFuel> {

    public ScreenLevelFuel(ContainerMenuLevelFuel guiContainer) {
        super(guiContainer);
        this.imageWidth = 255;
        this.componentList.clear();
        this.elements.add(TanksWidget.createNormal(this, 7, 7, guiContainer.base.getFluidTankInputList()));
        this.elements.add(TanksWidget.createNormal(this, 30, 7, guiContainer.base.getFluidTankCoolantList()));

        this.elements.add(TanksWidget.createNormal(this, 55, 7, guiContainer.base.getFluidTankOutputList()));
        this.elements.add(TanksWidget.createNormal(this, 78, 7, guiContainer.base.getFluidTankHotCoolantList()));
        this.addComponent(new ScreenWidget(this, 35, 63, EnumTypeComponent.RAD,
                new WidgetDefault<>(this.container.base.getRad())
        ));
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        draw(poseStack, Localization.translate("effect.industrialupgrade.radiation") + ": " + ModUtils.getString(
                this.container.base.getReactor().getRadGeneration()) + "☢", 100, 7, ModUtils.convertRGBcolorToInt(19, 40, 73));
        draw(poseStack, Localization.translate("gui.iu.tier") + ": " + ModUtils.getString(
                this.container.base.getLevelReactor()), 100, 19, ModUtils.convertRGBcolorToInt(19, 40, 73));
        draw(poseStack, this.container.base.getLevelReactor() < this.container.base.getMaxLevelReactor() ?
                        Localization.translate("reactor.canupgrade") : Localization.translate("reactor.notcanupgrade"), 100, 31,
                ModUtils.convertRGBcolorToInt(19, 40, 73)
        );
        draw(poseStack, Localization.translate("iu.minipanel.output") + ModUtils.getString(
                        this.container.base.output), 100, 43,
                ModUtils.convertRGBcolorToInt(19, 40, 73)
        );
        draw(poseStack, Localization.translate("gui.SuperSolarPanel.generating") + ": " + ModUtils.getString(
                this.container.base.output) + " EF/t", 100, 56, ModUtils.convertRGBcolorToInt(19, 40, 73));

    }


    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor5.png");
    }

}
