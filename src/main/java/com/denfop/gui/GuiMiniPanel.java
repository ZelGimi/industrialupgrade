package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerMiniPanels;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GuiMiniPanel extends GuiCore<ContainerMiniPanels> {

    private final TileEntityMiniPanels tileentity;

    public GuiMiniPanel(ContainerMiniPanels guiContainer) {
        super(guiContainer);
        this.tileentity = container.getTileEntity();
        this.xSize = 194;
        this.ySize = 224;
    }

    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        int nmPos = (this.xSize - this.fontRenderer.getStringWidth(Localization.translate(this.tileentity.getName()))) / 2;
        this.fontRenderer.drawString(Localization.translate(this.tileentity.getName()), nmPos, 6, 7718655);
        String storageString = Localization.translate("gui.SuperSolarPanel.storage") + ": ";
        String maxOutputString = Localization.translate("gui.SuperSolarPanel.maxOutput") + ": ";
        String generatingString = Localization.translate("gui.SuperSolarPanel.generating") + ": ";
        String energyPerTickString = Localization.translate("gui.SuperSolarPanel.energyPerTick");
        String Time1 = Localization.translate("iu.time");
        String Time4 = Localization.translate("iu.time1");
        String Time5 = Localization.translate("iu.time2");
        String Time6 = Localization.translate("iu.time3");
        String Time7 = Localization.translate("iu.time4");
        String maxstorage_1 = ModUtils.getString(this.tileentity.component.getCapacity());
        String maxstorage_2 = ModUtils.getString(this.tileentity.component.getStorage());
        String generation = ModUtils.getString(this.tileentity.generating);
        String tooltip2 = generatingString + generation + " " + energyPerTickString;
        String tooltip = storageString + maxstorage_2 + "/" + maxstorage_1;
        this.fontRenderer.drawString(

                maxOutputString + ModUtils.getString(this.tileentity.component.getProdution()) + " " + energyPerTickString,
                20,
                122,
                13487565
        );
        this.fontRenderer.drawString(

                Localization.translate("iu.wind_tier") + ModUtils.getString(this.tileentity.getCoreLevel()),
                135,
                122,
                13487565
        );
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
        new AdvArea(this, 140, 19, 150, 93).withTooltip(temptime).drawForeground(mouseX, mouseY);
        new Area(this, 28, 99, 43 - 18, 58 - 40).withTooltip(tooltip2).drawForeground(mouseX, mouseY);
        new Area(this, 66, 94, 53 - 18, 42 - 24).withTooltip(tooltip).drawForeground(mouseX, mouseY);
        if (!this.tileentity.invSlotGlass.isEmpty()) {
            new AdvArea(this, 12, 19, 22, 93).withTooltip(Localization.translate("iu.minipanel.stable_place") + Math.max(
                    0,
                    100 - (int) this.tileentity.load
            ) + " %").drawForeground(mouseX, mouseY);
        }
        if (this.tileentity.component.getBonusCapacity() != 0) {
            new Area(this, 154, 20, 16, 16)
                    .withTooltip(Localization.translate("iu.minipanel.bonus") + (int) (this.tileentity.component.getBonusCapacity() * 100) + " %")
                    .drawForeground(
                            mouseX,
                            mouseY
                    );
        }
        if (this.tileentity.component.getBonusProdution() != 0) {
            new Area(this, 154, 40, 16, 16)
                    .withTooltip(Localization.translate("iu.minipanel.bonus") + (int) (this.tileentity.component.getBonusProdution() * 100) + " %")
                    .drawForeground(
                            mouseX,
                            mouseY
                    );
        }
        if (this.tileentity.bonusGeneration != 0) {
            new Area(this, 154, 60, 16, 16)
                    .withTooltip(Localization.translate("iu.minipanel.bonus") + (int) (this.tileentity.bonusGeneration * 100) + " %")
                    .drawForeground(
                            mouseX,
                            mouseY
                    );
        }
        if (this.tileentity.pollution.isActive()) {
            new ItemStackImage(this, 170, 93, this.tileentity.pollution::getStack).drawForeground(mouseX, mouseY);
        }

        for (int i = 0; i < this.tileentity.invSlotGlass.size(); i++) {
            final List<TileEntityMiniPanels.EnumState> list = this.tileentity.listStable.get(i);
            if (list.size() == 0) {
                continue;
            }
            if (i == 0) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 65, 26, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );
                final TileEntityMiniPanels.EnumState second = list.get(1);
                new Area(this, 52, 39, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + second.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );
            }

            if (i == 1) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 91, 26, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );
                final TileEntityMiniPanels.EnumState second = list.get(1);
                new Area(this, 78, 39, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + second.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );

            }
            if (i == 2) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 104, 39, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );


            }
            if (i == 3) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 65, 52, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );
                final TileEntityMiniPanels.EnumState second = list.get(1);
                new Area(this, 52, 65, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + second.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );


            }
            if (i == 4) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 91, 52, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );
                final TileEntityMiniPanels.EnumState second = list.get(1);
                new Area(this, 78, 65, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + second.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );


            }
            if (i == 5) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 105, 65, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );


            }
            if (i == 6) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 65, 78, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );


            }
            if (i == 7) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 91, 78, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                mouseX,
                                mouseY
                        );


            }
        }

    }

    private void DrawModel(int h, int k) {
        if (!this.tileentity.rain) {
            if (this.tileentity.sunIsUp) {
                drawTexturedModalRect(h + 28, k + 99, 195, 15, 14, 14);

            } else {
                drawTexturedModalRect(h + 28, k + 99, 210, 15, 14, 14);
            }
        } else {
            if (this.tileentity.sunIsUp) {
                drawTexturedModalRect(h + 28, k + 99, 225, 15, 14, 14);
            } else {
                drawTexturedModalRect(h + 28, k + 99, 240, 15, 14, 14);
            }
        }


    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        if (this.tileentity.skyIsVisible) {
            DrawModel(h, k);
        }
        if (this.tileentity.component.getEnergy() > 0) {
            double l = Math.min(1, this.tileentity.component.getFillRatio()) * 24;
            drawTexturedModalRect(h + 69, k + 97, 194, 0, (int) (l + 1), 14);
        }

        int stable_level = (int) Math.min(75F * this.container.base.load
                / 100D, 75F);
        if (stable_level > 0) {
            drawTexturedModalRect(h + 12, k + 19 + 75 - stable_level, 207,
                    122 + 75 - stable_level, 11, stable_level
            );
        }
        int pollution = (int) Math.min(75F * (this.container.base.pollution.getAllTime() - this.container.base.pollution.getTime())
                / (this.container.base.pollution.getAllTime() * 1D), 75F);
        if (pollution > 0) {
            drawTexturedModalRect(h + 140, k + 19 + 75 - pollution, 223,
                    122 + 75 - pollution, 11, pollution
            );
        }

        ResourceLocation res = FluidName.fluidchlorum.getInstance().getStill();
        res = new ResourceLocation(res.getResourceDomain(), "textures/" + res.getResourcePath() +
                ".png");
        ResourceLocation res1 = FluidName.fluiddizel.getInstance().getStill();
        res1 = new ResourceLocation(res1.getResourceDomain(), "textures/" + res1.getResourcePath() +
                ".png");
        ResourceLocation res2 = FluidName.fluidbromine.getInstance().getStill();
        res2 = new ResourceLocation(res2.getResourceDomain(), "textures/" + res2.getResourcePath() +
                ".png");
        for (int i = 0; i < this.tileentity.invSlotGlass.size(); i++) {
            final List<TileEntityMiniPanels.EnumState> list = this.tileentity.listStable.get(i);
            if (list.size() == 0) {
                continue;
            }
            if (i == 0) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 65), (k + 26), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 65), (k + 26), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 65), (k + 26), 0, 0, 8, 8);
                }
                final TileEntityMiniPanels.EnumState second = list.get(1);
                if (second == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 52), (k + 39), 0, 0, 8, 8);
                } else if (second == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 52), (k + 39), 0, 0, 8, 8);
                } else if (second == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 52), (k + 39), 0, 0, 8, 8);
                }
            }

            if (i == 1) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 91), (k + 26), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 91), (k + 26), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 91), (k + 26), 0, 0, 8, 8);
                }
                final TileEntityMiniPanels.EnumState second = list.get(1);
                if (second == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 78), (k + 39), 0, 0, 8, 8);
                } else if (second == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 78), (k + 39), 0, 0, 8, 8);
                } else if (second == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 78), (k + 39), 0, 0, 8, 8);
                }
            }
            if (i == 2) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 104), (k + 39), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 104), (k + 39), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 104), (k + 39), 0, 0, 8, 8);
                }

            }
            if (i == 3) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 65), (k + 52), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 65), (k + 52), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 65), (k + 52), 0, 0, 8, 8);
                }
                final TileEntityMiniPanels.EnumState second = list.get(1);
                if (second == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 52), (k + 65), 0, 0, 8, 8);
                } else if (second == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 52), (k + 65), 0, 0, 8, 8);
                } else if (second == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 52), (k + 65), 0, 0, 8, 8);
                }

            }
            if (i == 4) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 91), (k + 52), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 91), (k + 52), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 91), (k + 52), 0, 0, 8, 8);
                }
                final TileEntityMiniPanels.EnumState second = list.get(1);
                if (second == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 78), (k + 65), 0, 0, 8, 8);
                } else if (second == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 78), (k + 65), 0, 0, 8, 8);
                } else if (second == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 78), (k + 65), 0, 0, 8, 8);
                }

            }
            if (i == 5) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 104), (k + 65), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 104), (k + 65), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 104), (k + 65), 0, 0, 8, 8);
                }

            }
            if (i == 6) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 65), (k + 78), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 65), (k + 78), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 65), (k + 78), 0, 0, 8, 8);
                }

            }
            if (i == 7) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {
                    this.mc.getTextureManager().bindTexture(res);
                    drawTexturedModalRect((h + 91), (k + 78), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {
                    this.mc.getTextureManager().bindTexture(res1);
                    drawTexturedModalRect((h + 91), (k + 78), 0, 0, 8, 8);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {
                    this.mc.getTextureManager().bindTexture(res2);
                    drawTexturedModalRect((h + 91), (k + 78), 0, 0, 8, 8);
                }

            }
        }
        if (this.tileentity.pollution.isActive()) {
            new ItemStackImage(this, 170, 93, this.tileentity.pollution::getStack).drawBackground(h, k);
        }

        this.bindTexture();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75F, 0.75F, 0.75F);
        drawTexturedModalRect((h + 8) / 0.75F, (k + 85) / 0.75F, 196, 49, 16, 16);
        drawTexturedModalRect((h + 146) / 0.75F, (k + 89) / 0.75F, 196, 66, 16, 16);

        GlStateManager.popMatrix();
        if (tileentity.component.getBonusCapacity() > 0) {
            new ItemStackImage(this, 154, 20, this.tileentity.invSlotStorage::get).drawBackground(h, k);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.15F, 0.15F, 1F);
            this.mc.getTextureManager().bindTexture(getTexture());
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            drawTexturedModalRect((h + 163) / 0.15F, (k + 28) / 0.15F, 192, 199, 250 - 192, 255 - 199);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        if (tileentity.component.getBonusProdution() > 0) {
            new ItemStackImage(this, 154, 40, this.tileentity.invSlotOutput::get).drawBackground(h, k);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.15F, 0.15F, 1F);
            this.mc.getTextureManager().bindTexture(getTexture());
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            drawTexturedModalRect((h + 163) / 0.15F, (k + 46) / 0.15F, 192, 199, 250 - 192, 255 - 199);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        if (tileentity.bonusGeneration > 0) {
            new ItemStackImage(this, 154, 60, this.tileentity.invSlotGlass::get).drawBackground(h, k);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.15F, 0.15F, 1F);
            this.mc.getTextureManager().bindTexture(getTexture());
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            drawTexturedModalRect((h + 163) / 0.15F, (k + 66) / 0.15F, 192, 199, 250 - 192, 255 - 199);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_mini_panels.png");

    }

}
