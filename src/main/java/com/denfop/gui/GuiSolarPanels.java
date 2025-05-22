package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSolarPanels extends GuiIU<ContainerSolarPanels> {


    public final TileSolarPanel tileentity;

    public GuiSolarPanels(ContainerSolarPanels container) {
        super(container);
        this.componentList.clear();
        this.tileentity = container.tileentity;
        this.xSize = 198;
        this.ySize = 232;
        this.componentList.add(new GuiComponent(this, 63, 96, 18, 18,
                new Component<>(new ComponentButton(tileentity, 1000, "") {
                    @Override
                    public void ClickEvent() {
                        super.ClickEvent();
                        tileentity.twoContainer = true;
                    }
                })
        ));

    }


    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        String formatPanelName = Localization.translate("blockAdministatorSolarPanel.name");
        if (tileentity.getPanels() != null) {
            formatPanelName = Localization.translate(container.base.getName());
        }
        int nmPos = (this.xSize - this.fontRenderer.getStringWidth(formatPanelName)) / 2 + 10;
        this.fontRenderer.drawString(formatPanelName, nmPos, 15, 7718655);

        String storageString = Localization.translate("gui.SuperSolarPanel.storage") + ": ";

        new AdvArea(this, 63, 97, 80, 113).withTooltip(Localization.translate("iu.panel_upgrade.info0")).drawForeground(
                mouseX,
                mouseY
        );


        String ModulesString6 = Localization.translate("iu.moduletype1");
        String ModulesString61 = Localization.translate("iu.moduletype2");
        String ModulesString62 = Localization.translate("iu.moduletype3");
        String ModulesString63 = Localization.translate("iu.moduletype4");
        String ModulesString64 = Localization.translate("iu.moduletype5");
        String ModulesString65 = Localization.translate("iu.moduletype6");
        String ModulesString66 = Localization.translate("iu.moduletype7");

        String Time1 = Localization.translate("iu.time");
        String Time4 = Localization.translate("iu.time1");
        String Time5 = Localization.translate("iu.time2");
        String Time6 = Localization.translate("iu.time3");
        String Time7 = Localization.translate("iu.time4");
        String maxstorage_1 = ModUtils.getString(this.tileentity.maxStorage);
        String maxstorage_2 = ModUtils.getString(this.tileentity.storage);


        String tooltip = storageString + maxstorage_2 + "/" + maxstorage_1;
        if (this.tileentity.solarType != 0) {

            if (this.tileentity.solarType == 1) {
                new AdvArea(this, 167, 45, 191, 68).withTooltip(ModulesString6).drawForeground(mouseX, mouseY);
            }
            if (this.tileentity.solarType == 2) {
                new AdvArea(this, 167, 45, 191, 68).withTooltip(ModulesString61).drawForeground(mouseX, mouseY);
            }
            if (this.tileentity.solarType == 3) {
                new AdvArea(this, 167, 45, 191, 68).withTooltip(ModulesString62).drawForeground(mouseX, mouseY);
            }
            if (this.tileentity.solarType == 4) {
                new AdvArea(this, 167, 45, 191, 68).withTooltip(ModulesString63).drawForeground(mouseX, mouseY);
            }
            if (this.tileentity.solarType == 5) {
                new AdvArea(this, 167, 45, 191, 68).withTooltip(ModulesString64).drawForeground(mouseX, mouseY);
            }
            if (this.tileentity.solarType == 6) {
                new AdvArea(this, 167, 45, 191, 68).withTooltip(ModulesString65).drawForeground(mouseX, mouseY);
            }
            if (this.tileentity.solarType == 7) {
                new AdvArea(this, 167, 45, 191, 68).withTooltip(ModulesString66).drawForeground(mouseX, mouseY);
            }

        }


        String temptime = Localization.translate("pollutionpnale");


        switch (this.tileentity.timer.getIndexWork()) {
            case 0:
                temptime = Time1 + this.tileentity.timer.getTime() + "\n" + Time4;
                break;
            case 1:
                temptime = Time1 + this.tileentity.timer.getTime() + "\n" + Time5;
                break;
            case 2:
                temptime = Time1 + this.tileentity.timer.getTime() + "\n" + Time6;
                break;
            case -1:
                temptime = Time7;
                break;
        }

        handleUpgradeTooltip(mouseX, mouseY);
        handleUpgradeTooltip1(mouseX, mouseY);

        new Area(this, 181, 72, 10, 22).withTooltip(temptime).drawForeground(mouseX, mouseY);

        new Area(this, 28, 51, 84, 17).withTooltip(tooltip).drawForeground(mouseX, mouseY);


    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.panelinformation"));
            List<String> compatibleUpgrades = ListInformationUtils.panelinform;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    private void handleUpgradeTooltip1(int mouseX, int mouseY) {
        if (mouseX >= 57 && mouseX <= 86 && mouseY >= 81 && mouseY <= 94) {
            List<String> text = new LinkedList<>();
            String maxOutputString = Localization.translate("gui.SuperSolarPanel.maxOutput") + ": ";
            String generatingString = Localization.translate("gui.SuperSolarPanel.generating") + ": ";
            String energyPerTickString = Localization.translate("gui.SuperSolarPanel.energyPerTick");
            String generation = ModUtils.getString(this.tileentity.generating);
            String tooltip2 = generatingString + generation + " " + energyPerTickString;
            String tierString = Localization.translate("gui.iu.tier") + ": ";
            text.add(tooltip2);
            text.add(maxOutputString + ModUtils.getString(this.tileentity.output) + " " + energyPerTickString);
            text.add(tierString + ModUtils.getString(this.tileentity.tier));
            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture();
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(h, k, 0, 0, 10, 10);
        this.bindTexture();

        if (this.tileentity.storage > 0) {
            double l = this.tileentity.gaugeEnergyScaled(84.0F);
            drawTexturedModalRect(h + 29, k + 52, 18, 238, (int) l, 16);
        }
        if (this.tileentity.sunIsUp) {
            drawTexturedModalRect(h + 154, k + 45, 222, 14, (int) 12, 12);
            drawTexturedModalRect(h + 154, k + 58, 222, 1, (int) 12, 12);
            if (this.tileentity.rain) {
                drawTexturedModalRect(h + 141, k + 45, 209, 1, (int) 12, 12);
            }
        } else {
            drawTexturedModalRect(h + 154, k + 45, 222, 1, (int) 12, 12);
            drawTexturedModalRect(h + 154, k + 58, 222, 14, (int) 12, 12);
            if (this.tileentity.rain) {
                drawTexturedModalRect(h + 141, k + 58, 209, 14, (int) 12, 12);
            }
        }
        int pollution =
                (int) Math.min(21F * (this.container.base.pollution.getAllTime() - this.container.base.pollution.getTime())
                        / (this.container.base.pollution.getAllTime() * 1D), 75F);
        if (pollution > 0) {
            drawTexturedModalRect(h + 182, k + 73 + 21 - pollution, 236,
                    2 + 21 - pollution, 8, pollution
            );
        }
        if (this.tileentity.wirelessComponent.isHasUpdate()) {
            drawTexturedModalRect(h + 154, k + 84, 222, 14, (int) 12, 12);
        } else {
            drawTexturedModalRect(h + 154, k + 84, 222, 1, (int) 12, 12);
        }
        if (this.tileentity.pollution.isActive()) {
            drawTexturedModalRect(h + 154, k + 71, 222, 14, (int) 12, 12);
        } else {
            drawTexturedModalRect(h + 154, k + 71, 222, 1, (int) 12, 12);
        }
        if (this.tileentity.solarType != 0) {

            if (this.tileentity.solarType == 1) {
                drawTexturedModalRect(h + 167, k + 45, 209, 80, (int) 25, 25);
            }
            if (this.tileentity.solarType == 2) {
                drawTexturedModalRect(h + 167, k + 45, 209, 184, (int) 25, 25);
            }
            if (this.tileentity.solarType == 3) {
                drawTexturedModalRect(h + 167, k + 45, 209, 132, (int) 25, 25);
            }
            if (this.tileentity.solarType == 4) {
                drawTexturedModalRect(h + 167, k + 45, 209, 158, (int) 25, 25);
            }
            if (this.tileentity.solarType == 5) {
                drawTexturedModalRect(h + 167, k + 45, 209, 54, (int) 25, 25);
            }
            if (this.tileentity.solarType == 6) {
                drawTexturedModalRect(h + 167, k + 45, 209, 28, (int) 25, 25);
            }
            if (this.tileentity.solarType == 7) {
                drawTexturedModalRect(h + 167, k + 45, 209, 106, (int) 25, 25);
            }

        }
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisolarpanel.png");
    }


}
