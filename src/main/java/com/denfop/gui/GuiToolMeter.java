package com.denfop.gui;

import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.IClickHandler;
import com.denfop.container.ContainerMeter;
import ic2.core.init.Localization;
import ic2.core.util.Util;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiToolMeter extends GuiIU<ContainerMeter> {

    public GuiToolMeter(ContainerMeter container) {
        super(container);
        this.ySize = 217;
        this.componentList.clear();
        this.addElement((new CustomButton(
                this,
                112,
                55,
                20,
                20,
                this.createModeSetter(ContainerMeter.Mode.EnergyIn)
        )).withTooltip("ic2.itemToolMEter.mode.switch\nic2.itemToolMEter.mode.EnergyIn"));
        this.addElement((new CustomButton(
                this,
                132,
                55,
                20,
                20,
                this.createModeSetter(ContainerMeter.Mode.EnergyOut)
        )).withTooltip("ic2.itemToolMEter.mode.switch\nic2.itemToolMEter.mode.EnergyOut"));
        this.addElement((new CustomButton(
                this,
                112,
                75,
                20,
                20,
                this.createModeSetter(ContainerMeter.Mode.EnergyGain)
        )).withTooltip("ic2.itemToolMEter.mode.switch\nic2.itemToolMEter.mode.EnergyGain"));
        this.addElement((new CustomButton(this, 132, 75, 20, 20, this.createModeSetter(ContainerMeter.Mode.Voltage))).withTooltip(
                "ic2.itemToolMEter.mode.switch\nic2.itemToolMEter.mode.Voltage"));
    }

    private IClickHandler createModeSetter(final ContainerMeter.Mode mode) {
        return button -> GuiToolMeter.this.container.setMode(mode);
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 26 && y >= 111 && x <= 83 && y <= 123) {
            this.container.reset();
        }

    }

    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        String unit = this.container.getMode() == ContainerMeter.Mode.Voltage
                ? "ic2.generic.text.v"
                : "ic2.generic.text.EUt";
        unit = Localization.translate(unit);
        this.fontRenderer.drawString(Localization.translate("ic2.itemToolMEter.mode"), 115, 43, 2157374);
        this.fontRenderer.drawString(Localization.translate("ic2.itemToolMEter.avg"), 15, 41, 2157374);
        this.fontRenderer.drawString("" + Util.toSiString(this.container.getResultAvg(), 6) + unit, 15, 51, 2157374);
        this.fontRenderer.drawString(Localization.translate("ic2.itemToolMEter.max/min"), 15, 64, 2157374);
        this.fontRenderer.drawString("" + Util.toSiString(this.container.getResultMax(), 6) + unit, 15, 74, 2157374);
        this.fontRenderer.drawString("" + Util.toSiString(this.container.getResultMin(), 6) + unit, 15, 84, 2157374);
        this.fontRenderer.drawString(Localization.translate(
                "ic2.itemToolMEter.cycle",
                this.container.getResultCount() / 20
        ), 15, 100, 2157374);
        this.fontRenderer.drawString(Localization.translate("ic2.itemToolMEter.mode.reset"), 39, 114, 2157374);
        switch (this.container.getMode()) {
            case EnergyIn:
                this.fontRenderer.drawString(Localization.translate("ic2.itemToolMEter.mode.EnergyIn"), 105, 100, 2157374);
                break;
            case EnergyOut:
                this.fontRenderer.drawString(Localization.translate("ic2.itemToolMEter.mode.EnergyOut"), 105, 100, 2157374);
                break;
            case EnergyGain:
                this.fontRenderer.drawString(Localization.translate("ic2.itemToolMEter.mode.EnergyGain"), 105, 100, 2157374);
                break;
            case Voltage:
                this.fontRenderer.drawString(Localization.translate("ic2.itemToolMEter.mode.Voltage"), 105, 100, 2157374);
        }

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.bindTexture();
        switch (this.container.getMode()) {
            case EnergyIn:
                this.drawTexturedRect(112.0, 55.0, 40.0, 40.0, 176.0, 0.0);
                break;
            case EnergyOut:
                this.drawTexturedRect(112.0, 55.0, 40.0, 40.0, 176.0, 40.0);
                break;
            case EnergyGain:
                this.drawTexturedRect(112.0, 55.0, 40.0, 40.0, 176.0, 120.0);
                break;
            case Voltage:
                this.drawTexturedRect(112.0, 55.0, 40.0, 40.0, 176.0, 80.0);
        }

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation("ic2", "textures/gui/GUIToolEUMeter.png");
    }

}
