package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TanksGauge;
import com.denfop.container.ContainerLevelFuel;
import com.denfop.gui.GuiIU;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiLevelFuel extends GuiIU<ContainerLevelFuel> {

    public GuiLevelFuel(ContainerLevelFuel guiContainer) {
        super(guiContainer);
        this.ySize=211;
        this.componentList.clear();
        this.xSize = 186;
        this.elements.add(TanksGauge.createNormal(this,8,45,guiContainer.base.getFluidTankInputList()));
        this.elements.add(TanksGauge.createNormal(this,30,45,guiContainer.base.getFluidTankCoolantList()));

        this.elements.add(TanksGauge.createNormal(this,140,45,guiContainer.base.getFluidTankOutputList()));
        this.elements.add(TanksGauge.createNormal(this,162,45,guiContainer.base.getFluidTankHotCoolantList()));
        this.addComponent(new GuiComponent(this, 70, 45, EnumTypeComponent.RAD,
                new Component<>(this.container.base.getRad())
        ));
    }
    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(Localization.translate("iu.potion.radiation")+ ": " + ModUtils.getString(
        this.container.base.getReactor().getRadGeneration()) + " ☢",52,60,ModUtils.convertRGBcolorToInt(15,125,205));
        this.fontRenderer.drawString(Localization.translate("gui.iu.tier")+ ": " + ModUtils.getString(
                this.container.base.getLevelReactor()) ,52,70,ModUtils.convertRGBcolorToInt(15,125,205));
        this.fontRenderer.drawString(this.container.base.getLevelReactor() < this.container.base.getMaxLevelReactor() ?
                Localization.translate("reactor.canupgrade") :  Localization.translate("reactor.notcanupgrade") ,52,80,
                ModUtils.convertRGBcolorToInt(15,125,205));
        this.fontRenderer.drawString(Localization.translate("iu.minipanel.output")+ ModUtils.getString(
                        this.container.base.output) ,52,90,
                ModUtils.convertRGBcolorToInt(15,125,205));
        this.fontRenderer.drawString(Localization.translate("gui.SuperSolarPanel.generating")+ ": " + ModUtils.getString(
                this.container.base.output) + " EF/t"  ,52,102,ModUtils.convertRGBcolorToInt(15,125,205));

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
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiwaterreactor4.png");
    }

}
