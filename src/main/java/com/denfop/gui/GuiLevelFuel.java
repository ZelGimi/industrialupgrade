package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TanksGauge;
import com.denfop.container.ContainerLevelFuel;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;

public class GuiLevelFuel extends GuiIU<ContainerLevelFuel> {

    public GuiLevelFuel(ContainerLevelFuel guiContainer) {
        super(guiContainer);

        this.componentList.clear();
        this.elements.add(TanksGauge.createNormal(this, 7, 7, guiContainer.base.getFluidTankInputList()));
        this.elements.add(TanksGauge.createNormal(this, 30, 7, guiContainer.base.getFluidTankCoolantList()));

        this.elements.add(TanksGauge.createNormal(this, 55, 7, guiContainer.base.getFluidTankOutputList()));
        this.elements.add(TanksGauge.createNormal(this, 78, 7, guiContainer.base.getFluidTankHotCoolantList()));
        this.addComponent(new GuiComponent(this, 35, 63, EnumTypeComponent.RAD,
                new Component<>(this.container.base.getRad())
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(Localization.translate("iu.potion.radiation") + ": " + ModUtils.getString(
                this.container.base.getReactor().getRadGeneration()) + "☢", 100, 7, ModUtils.convertRGBcolorToInt(19, 40, 73));
        this.fontRenderer.drawString(Localization.translate("gui.iu.tier") + ": " + ModUtils.getString(
                this.container.base.getLevelReactor()), 100, 19, ModUtils.convertRGBcolorToInt(19, 40, 73));
        this.fontRenderer.drawString(this.container.base.getLevelReactor() < this.container.base.getMaxLevelReactor() ?
                        Localization.translate("reactor.canupgrade") : Localization.translate("reactor.notcanupgrade"), 100, 31,
                ModUtils.convertRGBcolorToInt(19, 40, 73)
        );
        this.fontRenderer.drawString(Localization.translate("iu.minipanel.output") + ModUtils.getString(
                        this.container.base.output), 100, 43,
                ModUtils.convertRGBcolorToInt(19, 40, 73)
        );
        this.fontRenderer.drawString(Localization.translate("gui.SuperSolarPanel.generating") + ": " + ModUtils.getString(
                this.container.base.output) + " EF/t", 100, 56, ModUtils.convertRGBcolorToInt(19, 40, 73));

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor4.png");
    }

}
