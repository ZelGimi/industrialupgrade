package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
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
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSolarPanels extends GuiCore<ContainerSolarPanels> {


    public final TileSolarPanel tileentity;
    private ResourceLocation res;

    public GuiSolarPanels(ContainerSolarPanels container) {
        super(container);
        this.tileentity = container.tileentity;
        this.xSize = 194;
        this.ySize = 238;

    }


    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        String formatPanelName = Localization.translate("blockAdministatorSolarPanel.name");
        if (tileentity.getPanels() != null) {
            formatPanelName = Localization.translate(tileentity.getPanels().name1);
        }
        int nmPos = (this.xSize - this.fontRenderer.getStringWidth(formatPanelName)) / 2;
        this.fontRenderer.drawString(formatPanelName, nmPos, 6, 7718655);

        String storageString = Localization.translate("gui.SuperSolarPanel.storage") + ": ";
        String maxOutputString = Localization.translate("gui.SuperSolarPanel.maxOutput") + ": ";
        String generatingString = Localization.translate("gui.SuperSolarPanel.generating") + ": ";
        String energyPerTickString = Localization.translate("gui.SuperSolarPanel.energyPerTick");
        String energyPerTickString1 = Localization.translate("gui.SuperSolarPanel.energyPerTick1");
        String tierString = Localization.translate("gui.iu.tier") + ": ";
        String ModulesString = Localization.translate("iu.genday");
        String ModulesString1 = Localization.translate("iu.gennight");
        String ModulesString2 = Localization.translate("iu.storage");
        String ModulesString3 = Localization.translate("iu.output");
        String ModulesString4 = Localization.translate("iu.tier1");
        String ModulesString5 = Localization.translate("iu.tier2");
        String ModulesString6 = Localization.translate("iu.moduletype1");
        String ModulesString61 = Localization.translate("iu.moduletype2");
        String ModulesString62 = Localization.translate("iu.moduletype3");
        String ModulesString63 = Localization.translate("iu.moduletype4");
        String ModulesString64 = Localization.translate("iu.moduletype5");
        String ModulesString65 = Localization.translate("iu.moduletype6");
        String ModulesString66 = Localization.translate("iu.moduletype7");
        String ModulesString8 = Localization.translate("iu.modulewirelles");
        String ModulesString10 = Localization.translate("iu.modulewirelles2");

        String Time1 = Localization.translate("iu.time");
        String Time4 = Localization.translate("iu.time1");
        String Time5 = Localization.translate("iu.time2");
        String Time6 = Localization.translate("iu.time3");
        String Time7 = Localization.translate("iu.time4");
        String maxstorage_1 = ModUtils.getString(this.tileentity.maxStorage);
        String maxstorage_2 = ModUtils.getString(this.tileentity.storage);


        if ((this.tileentity.maxStorage / this.tileentity.p) != 1) {
            this.fontRenderer.drawString(
                    ModulesString2 + ModUtils.getString(((this.tileentity.maxStorage / this.tileentity.p) - 1) * 100) + "%",
                    15,
                    182 - 2,
                    13487565
            );
        }


        if ((this.tileentity.production / this.tileentity.u) != 1) {
            this.fontRenderer.drawString(
                    ModulesString3 + ModUtils.getString(((this.tileentity.production / this.tileentity.u) - 1) * 100) + "%",
                    15,
                    175 - 2,
                    13487565
            );
        }
        String generation = ModUtils.getString(this.tileentity.generating);
        String generation1 = ModUtils.getString(this.tileentity.generating * 4);

        String tooltip2 = generatingString + generation + " " + energyPerTickString;


        String tooltip = storageString + maxstorage_2 + "/" + maxstorage_1;
        if (this.tileentity.solarType != 0) {

            if (this.tileentity.solarType == 1) {
                this.fontRenderer.drawString(ModulesString6, 15, 196 - 2, 13487565);
            }
            if (this.tileentity.solarType == 2) {
                this.fontRenderer.drawString(ModulesString61, 15, 196 - 2, 13487565);
            }
            if (this.tileentity.solarType == 3) {
                this.fontRenderer.drawString(ModulesString62, 15, 196 - 2, 13487565);
            }
            if (this.tileentity.solarType == 4) {
                this.fontRenderer.drawString(ModulesString63, 15, 196 - 2, 13487565);
            }
            if (this.tileentity.solarType == 5) {
                this.fontRenderer.drawString(ModulesString64, 15, 196 - 2, 13487565);
            }
            if (this.tileentity.solarType == 6) {
                this.fontRenderer.drawString(ModulesString65, 15, 196 - 2, 13487565);
            }
            if (this.tileentity.solarType == 7) {
                this.fontRenderer.drawString(ModulesString66, 15, 196 - 2, 13487565);
            }

        }

        this.fontRenderer.drawString(

                maxOutputString + ModUtils.getString(this.tileentity.production) + " " + energyPerTickString,
                50,
                26 - 4 - 12 + 8 - 6 + 4,
                13487565
        );

        this.fontRenderer.drawString(Localization.translate("pollutioninformation"), 50,
                30 + 7, 13487565
        );
        String temptime = Localization.translate("pollutionpnale");


        if ((this.tileentity.genDay / this.tileentity.k) != 1 && this.tileentity.sunIsUp) {
            this.fontRenderer.drawString(
                    ModulesString + ModUtils.getString(((this.tileentity.genDay / this.tileentity.k) - 1) * 100) + "%",
                    15,
                    189 - 2,
                    13487565
            );
        }
        if ((this.tileentity.genNight / this.tileentity.m) != 1 && !this.tileentity.sunIsUp) {
            this.fontRenderer.drawString(
                    ModulesString1 + ModUtils.getString(((this.tileentity.genNight / this.tileentity.m) - 1) * 100) + "%",
                    15,
                    189 - 2,
                    13487565
            );
        }
        this.fontRenderer.drawString(tierString + ModUtils.getString(this.tileentity.tier), 50, 46 - 4 - 12 - 8 + 10 - 6,
                13487565
        );
        switch (this.tileentity.timer.getIndexWork()) {
            case 0:
                temptime = Time1 + this.tileentity.timer.getTime() + Time4;
                break;
            case 1:
                temptime = Time1 + this.tileentity.timer.getTime() + Time5;
                break;
            case 2:
                temptime = Time1 + this.tileentity.timer.getTime() + Time6;
                break;
            case -1:
                temptime = Time7;
                break;
        }
        double temp = this.tileentity.tier - this.tileentity.o;
        if (temp > 0) {
            this.fontRenderer.drawString(ModulesString4 + ModUtils.getString(temp), 15, 209 - 2 + 6 + 6, 13487565);
        } else if (temp < 0) {

            this.fontRenderer.drawString(ModulesString5 + ModUtils.getString(temp), 15, 209 - 2 + 6 + 6, 13487565);
        }
        handleUpgradeTooltip(mouseX, mouseY);

        new Area(this, 18, 40, 43 - 18, 58 - 40).withTooltip(tooltip2).drawForeground(mouseX, mouseY);


        new Area(this, 50, 37, 144 - 50, 42 - 30 + 7).withTooltip(temptime).drawForeground(mouseX, mouseY);

        new Area(this, 18, 24, 43 - 18, 38 - 24).withTooltip(tooltip).drawForeground(mouseX, mouseY);
        if (this.tileentity.wireless) {
            this.fontRenderer.drawString(ModulesString8, 15, 209 - 2, 13487565);

        } else {
            this.fontRenderer.drawString(ModulesString10, 15, 209 - 2, 13487565);

        }
//par1 - this.guiLeft, par2 - this.guiTop, temptime, 50, 30, 144, 42
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

    private void DrawModel(int h, int k) {

        if (!this.tileentity.rain) {

            if (this.tileentity.sunIsUp || (this.tileentity.solarType == 3 || this.tileentity.solarType == 4)) {
                drawTexturedModalRect(h + 24, k + 42, 195, 15, 14, 14);

            } else {
                drawTexturedModalRect(h + 24, k + 42, 210, 15, 14, 14);
            }
        } else {
            if (this.tileentity.sunIsUp || (this.tileentity.solarType == 3 || this.tileentity.solarType == 4)) {
                drawTexturedModalRect(h + 24, k + 42, 225, 15, 14, 14);
            } else {
                drawTexturedModalRect(h + 24, k + 42, 240, 15, 14, 14);
            }
        }


    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        setResourceLocation(this.tileentity.getType().texture);
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(h, k, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getResourceLocation());

        if (this.tileentity.storage > 0) {
            double l = this.tileentity.gaugeEnergyScaled(24.0F);
            drawTexturedModalRect(h + 18, k + 24, 194, 0, (int) (l + 1), 14);
        }

        if (this.tileentity.skyIsVisible || (this.tileentity.solarType == 3 || this.tileentity.solarType == 4)) {
            DrawModel(h, k);
        }
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIAdvancedSolarPanel.png");
    }

    private ResourceLocation getResourceLocation() {
        return res;
    }

    private void setResourceLocation(ResourceLocation res) {
        this.res = res;
        this.mc.getTextureManager().bindTexture(res);
    }

}
