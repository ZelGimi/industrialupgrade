package com.denfop.gui;

import com.denfop.IUCore;
import com.denfop.api.gui.Area;
import com.denfop.container.ContainerSolarPanels;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSolarPanels extends GuiIC2<ContainerSolarPanels> {


    public final TileEntitySolarPanel tileentity;
    private ResourceLocation res;

    public GuiSolarPanels(ContainerSolarPanels container) {
        super(container);
        this.tileentity = container.tileentity;
        this.xSize = 194;
        this.ySize = 238;

    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 70 && x <= 123 && y >= 40 && y <= 56) {
            IUCore.network.get(false).initiateClientTileEntityEvent(this.tileentity, 0);
        }


    }

    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        String formatPanelName = Localization.translate("blockAdministatorSolarPanel.name");
        if (tileentity.getPanels() != null) {
            formatPanelName = Localization.translate(tileentity.getPanels().name1);
        }
        int nmPos = (this.xSize - this.fontRenderer.getStringWidth(formatPanelName)) / 2;
        this.fontRenderer.drawString(formatPanelName, nmPos, 4, 7718655);
        if (tileentity.getmodulerf) {
            if (tileentity.rf) {
                this.fontRenderer.drawString("RF -> EU", 81, 44, 13487565);
            } else {
                this.fontRenderer.drawString("EU -> RF", 81, 44, 13487565);
            }
        } else {
            this.fontRenderer.drawString("EU -> RF", 81, 44, 13487565);
        }

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
        String ModulesString7 = Localization.translate("iu.rfmodule");
        String ModulesString71 = Localization.translate("iu.rfmodule1");
        String rfstorageString = Localization.translate("iu.rfstorage");
        String ModulesString8 = Localization.translate("iu.modulewirelles");
        String ModulesString10 = Localization.translate("iu.modulewirelles2");
        String Time = ModUtils.getString(ModUtils
                .Time(this.tileentity.time)
                .get(0)) + Localization.translate("iu.hour") + ModUtils.getString(ModUtils
                .Time(this.tileentity.time)
                .get(1)) + Localization.translate("iu.minutes") + ModUtils.getString(ModUtils
                .Time(this.tileentity.time)
                .get(2)) + Localization.translate("iu.seconds");
        String Time2 = ModUtils.getString(ModUtils
                .Time(this.tileentity.time1)
                .get(0)) + Localization.translate("iu.hour") + ModUtils.getString(ModUtils
                .Time(this.tileentity.time1)
                .get(1)) + Localization.translate("iu.minutes") + ModUtils.getString(ModUtils
                .Time(this.tileentity.time1)
                .get(2)) + Localization.translate("iu.seconds");
        String Time3 = ModUtils.getString(ModUtils
                .Time(this.tileentity.time2)
                .get(0)) + Localization.translate("iu.hour") + ModUtils.getString(ModUtils
                .Time(this.tileentity.time2)
                .get(1)) + Localization.translate("iu.minutes") + ModUtils.getString(ModUtils
                .Time(this.tileentity.time2)
                .get(2)) + Localization.translate("iu.seconds");

        String Time1 = Localization.translate("iu.time");
        String Time4 = Localization.translate("iu.time1");
        String Time5 = Localization.translate("iu.time2");
        String Time6 = Localization.translate("iu.time3");
        String Time7 = Localization.translate("iu.time4");
        String maxstorage_1 = ModUtils.getString(this.tileentity.maxStorage);
        String maxstorage_2 = ModUtils.getString(this.tileentity.storage);
        // TODO

        String rf = ModUtils.getString(this.tileentity.storage2);
        String rf1 = ModUtils.getString(this.tileentity.maxStorage2);
        String tooltip1 = rfstorageString + rf + "/" + rf1;

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
        String tooltip3 = generatingString + generation1 + " " + energyPerTickString1;


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
        if (tileentity.getmodulerf) {
            if (!this.tileentity.rf) {
                this.fontRenderer.drawString(

                        maxOutputString + ModUtils.getString(this.tileentity.production) + " " + energyPerTickString,
                        50,
                        26 - 4 - 12 + 8 - 6,
                        13487565
                );
            } else {
                this.fontRenderer.drawString(

                        maxOutputString + ModUtils.getString(this.tileentity.production * 4) + " " + "RF/t",
                        50,
                        26 - 4 - 12 + 8 - 6,
                        13487565
                );
            }
        } else {
            this.fontRenderer.drawString(

                    maxOutputString + ModUtils.getString(this.tileentity.production) + " " + energyPerTickString,
                    50,
                    26 - 4 - 12 + 8 - 6,
                    13487565
            );
        }
        this.fontRenderer.drawString(Localization.translate("pollutioninformation"), 50,
                30, 13487565
        );
        String temptime = Localization.translate("pollutionpnale");

        if (this.tileentity.getmodulerf) {
            if (this.tileentity.rf) {
                this.fontRenderer.drawString(ModulesString7, 15, 203 - 2, 13487565);
            }
            if (!this.tileentity.rf) {
                this.fontRenderer.drawString(ModulesString71, 15, 203 - 2, 13487565);
            }

        }

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
        this.fontRenderer.drawString(tierString + ModUtils.getString(this.tileentity.tier), 50, 46 - 4 - 12 - 8 + 5 - 6,
                13487565
        );
        if (this.tileentity.time > 0) {
            temptime = Time1 + Time + Time4;
        }
        if (this.tileentity.time1 > 0 && this.tileentity.time <= 0) {
            temptime = Time1 + Time2 + Time5;
        }
        if (this.tileentity.time2 > 0 && this.tileentity.time1 <= 0 && this.tileentity.time <= 0) {
            temptime = Time1 + Time3 + Time6;
        } else if (this.tileentity.time2 <= 0 && this.tileentity.time1 <= 0 && this.tileentity.time <= 0) {
            temptime = Time7;
        }
        double temp = this.tileentity.tier - this.tileentity.o;
        if (temp > 0) {
            this.fontRenderer.drawString(ModulesString4 + ModUtils.getString(temp), 15, 209 - 2 + 6 + 6, 13487565);
        } else if (temp < 0) {

            this.fontRenderer.drawString(ModulesString5 + ModUtils.getString(temp), 15, 209 - 2 + 6 + 6, 13487565);
        }
        handleUpgradeTooltip(mouseX, mouseY);
        if (this.tileentity.getmodulerf) {

            if (!this.tileentity.rf) {
                new Area(this, 18, 40, 43 - 18, 58 - 40).withTooltip(tooltip2).drawForeground(mouseX, mouseY);
            } else {
                new Area(this, 160, 40, 185 - 160, 58 - 40).withTooltip(tooltip3).drawForeground(mouseX, mouseY);

            }
        } else {
            new Area(this, 18, 40, 43 - 18, 58 - 40).withTooltip(tooltip2).drawForeground(mouseX, mouseY);

        }
        new Area(this, 50, 30, 144 - 50, 42 - 30).withTooltip(temptime).drawForeground(mouseX, mouseY);

        new Area(this, 18, 24, 43 - 18, 38 - 24).withTooltip(tooltip).drawForeground(mouseX, mouseY);
        new Area(this, 155, 24, 180 - 155, 38 - 24).withTooltip(tooltip1).drawForeground(mouseX, mouseY);
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
        if (this.tileentity.getmodulerf) {
            if (!this.tileentity.rf) {

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

            } else {

                if (!this.tileentity.rain) {
                    if (this.tileentity.sunIsUp || (this.tileentity.solarType == 3 || this.tileentity.solarType == 4)) {
                        drawTexturedModalRect(h + 160, k + 42, 195, 46, 14, 14);
                    } else {
                        drawTexturedModalRect(h + 160, k + 42, 210, 46, 14, 14);
                    }
                } else {
                    if (this.tileentity.sunIsUp || (this.tileentity.solarType == 3 || this.tileentity.solarType == 4)) {
                        drawTexturedModalRect(h + 160, k + 42, 225, 46, 14, 14);
                    } else {
                        drawTexturedModalRect(h + 160, k + 42, 240, 46, 14, 14);
                    }
                }
            }

        } else {
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
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        setResourceLocation(this.tileentity.getType().texture);
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
        drawTexturedModalRect(h, k, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getResourceLocation());

        if (this.tileentity.storage > 0) {
            double l = this.tileentity.gaugeEnergyScaled(24.0F);
            drawTexturedModalRect(h + 18, k + 24, 194, 0, (int) (l + 1), 14);
        }
        if (this.tileentity.storage2 > 0 || this.tileentity.storage2 <= this.tileentity.maxStorage2) {
            float l = this.tileentity.gaugeEnergyScaled2(24.0F);

            drawTexturedModalRect(h + 19 + 72 + 40 + 23 + 1, k + 24, 219, 0, (int) (l), 14);
        }
        if (this.tileentity.getmodulerf) {

            if (!this.tileentity.rf) {
                drawTexturedModalRect(h + 40, k + 41, 195, 30, 15, 15);
            } else {
                drawTexturedModalRect(h + 142, k + 42, 210, 30, 15, 15);
            }

        }
        if (this.tileentity.skyIsVisible || (this.tileentity.solarType == 3 || this.tileentity.solarType == 4)) {
            DrawModel(h, k);
        }
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation("industrialupgrade", "textures/gui/GUIAdvancedSolarPanel.png");
    }

    private ResourceLocation getResourceLocation() {
        return res;
    }

    private void setResourceLocation(ResourceLocation res) {
        this.res = res;
        this.mc.getTextureManager().bindTexture(res);
    }

}
