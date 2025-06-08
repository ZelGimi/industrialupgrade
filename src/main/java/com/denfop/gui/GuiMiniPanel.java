package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.container.ContainerMiniPanels;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class GuiMiniPanel<T extends ContainerMiniPanels> extends GuiCore<ContainerMiniPanels> {

    private final TileEntityMiniPanels tileentity;

    public GuiMiniPanel(ContainerMiniPanels guiContainer) {
        super(guiContainer);
        this.tileentity = container.getTileEntity();
        this.imageWidth = 202;
        this.imageHeight = 232;
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int mouseX, int mouseY) {
        super.drawForegroundLayer(poseStack,mouseX, mouseY);
        int nmPos = (this.imageWidth - this.getStringWidth(Localization.translate(this.tileentity.getName()))) / 2;
     draw(poseStack,Localization.translate(this.tileentity.getName()), nmPos, 6, 7718655);
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
       draw(poseStack,

                maxOutputString,
                5,
                120,
                13487565
        );
        draw(poseStack,

                ModUtils.getString(this.tileentity.component.getProdution()) + " EF",
                25 - this.getStringWidth(ModUtils.getString(this.tileentity.component.getProdution())),
                129,
                13487565
        );
       draw(poseStack,

                Localization.translate("iu.wind_tier"),
                167,
                120,
                13487565
        );
       draw(poseStack,

                ModUtils.getString(this.tileentity.getCoreLevel()),
                180,
                129,
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
        new AdvArea(this, 168, 19, 183, 111).withTooltip(temptime).drawForeground(poseStack,mouseX, mouseY);
        new AdvArea(this, 96, 135, 105, 143).withTooltip(tooltip2).drawForeground(poseStack,mouseX, mouseY);
        new AdvArea(this, 80, 111, 122, 132).withTooltip(tooltip).drawForeground(poseStack,mouseX, mouseY);
        if (!this.tileentity.invSlotGlass.isEmpty()) {
            new AdvArea(this, 18, 19, 32, 111).withTooltip(Localization.translate("iu.minipanel.stable_place") + Math.max(
                    0,
                    100 - (int) this.tileentity.load
            ) + " %").drawForeground(poseStack,mouseX, mouseY);
        }
        if (this.tileentity.component.getBonusCapacity() != 0) {
            new Area(this, 184, 20, 16, 16)
                    .withTooltip(Localization.translate("iu.minipanel.bonus") + (int) (this.tileentity.component.getBonusCapacity() * 100) + " %")
                    .drawForeground(
                            poseStack,  mouseX,
                            mouseY
                    );
        }
        if (this.tileentity.component.getBonusProdution() != 0) {
            new Area(this, 184, 40, 16, 16)
                    .withTooltip(Localization.translate("iu.minipanel.bonus") + (int) (this.tileentity.component.getBonusProdution() * 100) + " %")
                    .drawForeground(
                            poseStack,  mouseX,
                            mouseY
                    );
        }
        if (this.tileentity.bonusGeneration != 0) {
            new Area(this, 184, 60, 16, 16)
                    .withTooltip(Localization.translate("iu.minipanel.bonus") + (int) (this.tileentity.bonusGeneration * 100) + " %")
                    .drawForeground(
                            poseStack,  mouseX,
                            mouseY
                    );
        }
        if (this.tileentity.pollution.isActive()) {
            new ItemStackImage(this, 184, 93, this.tileentity.pollution::getStack).drawForeground(poseStack,mouseX, mouseY);
        }

        for (int i = 0; i < this.tileentity.invSlotGlass.size(); i++) {
            final List<TileEntityMiniPanels.EnumState> list = this.tileentity.listStable.get(i);
            if (list.size() == 0) {
                continue;
            }
            if (i == 0) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 87, 34, 8, 8)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                poseStack,mouseX,
                                mouseY
                        );
                final TileEntityMiniPanels.EnumState second = list.get(1);
                new Area(this, 74, 51, 9, 6)
                        .withTooltip(Localization.translate("iu.minipanel." + second.name().toLowerCase()))
                        .drawForeground(
                                poseStack,mouseX,
                                mouseY
                        );

            }

            if (i == 1) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 110, 38, 6, 9)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                poseStack, mouseX,
                                mouseY
                        );
                final TileEntityMiniPanels.EnumState second = list.get(1);
                new Area(this, 97, 51, 9, 6)
                        .withTooltip(Localization.translate("iu.minipanel." + second.name().toLowerCase()))
                        .drawForeground(
                                poseStack, mouseX,
                                mouseY
                        );

            }
            if (i == 2) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 120, 51, 9, 6)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                poseStack,mouseX,
                                mouseY
                        );


            }
            if (i == 3) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 87, 61, 6, 9)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                poseStack, mouseX,
                                mouseY
                        );
                final TileEntityMiniPanels.EnumState second = list.get(1);
                new Area(this, 74, 74, 9, 6)
                        .withTooltip(Localization.translate("iu.minipanel." + second.name().toLowerCase()))
                        .drawForeground(
                                poseStack,  mouseX,
                                mouseY
                        );


            }
            if (i == 4) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 110, 61, 6, 9)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                poseStack, mouseX,
                                mouseY
                        );
                final TileEntityMiniPanels.EnumState second = list.get(1);
                new Area(this, 97, 74, 9, 6)
                        .withTooltip(Localization.translate("iu.minipanel." + second.name().toLowerCase()))
                        .drawForeground(
                                poseStack,mouseX,
                                mouseY
                        );


            }
            if (i == 5) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 120, 74, 9, 6)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                poseStack,mouseX,
                                mouseY
                        );


            }
            if (i == 6) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 87, 84, 6, 9)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                poseStack,mouseX,
                                mouseY
                        );


            }
            if (i == 7) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                new Area(this, 110, 84, 6, 9)
                        .withTooltip(Localization.translate("iu.minipanel." + first.name().toLowerCase()))
                        .drawForeground(
                                poseStack, mouseX,
                                mouseY
                        );


            }
        }

    }

    private void DrawModel(GuiGraphics poseStack, int h, int k) {
        if (!this.tileentity.rain) {
            if (this.tileentity.sunIsUp) {
                drawTexturedModalRect(poseStack,h + 96, k + 134, 213, 97, 10, 10);

            } else {
                drawTexturedModalRect(poseStack,h + 96, k + 134, 223, 97, 10, 10);
            }
        } else {
            if (this.tileentity.sunIsUp) {
                drawTexturedModalRect(poseStack,h + 96, k + 134, 234, 97, 10, 10);
            } else {
                drawTexturedModalRect(poseStack,h + 96, k + 134, 244, 97, 10, 10);
            }
        }


    }

    protected void renderBg(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());
        int h = guiLeft;
        int k = guiTop;
        drawTexturedModalRect(poseStack, h, k, 0, 0, this.imageWidth, 147);
        drawTexturedModalRect(poseStack,h, k + 147, 0, 147, 190, this.imageHeight - 146);
        if (this.tileentity.skyIsVisible) {
            DrawModel(poseStack,h, k);
        }
        if (this.tileentity.component.getEnergy() > 0) {
            double l = Math.min(1, this.tileentity.component.getFillRatio()) * 37;
            drawTexturedModalRect(poseStack,h + 83, k + 114, 214, 112, (int) (l), 15);
        }

        int stable_level = (int) Math.min(85 * this.container.base.load
                / 100D, 85);
        if (stable_level > 0) {
            drawTexturedModalRect(poseStack,h + 22, k + 23 + 85 - stable_level, 217,
                    6 + 85 - stable_level, 7, stable_level
            );
        }
        int pollution = (int) Math.min(85 * (this.container.base.pollution.getAllTime() - this.container.base.pollution.getTime())
                / (this.container.base.pollution.getAllTime() * 1D), 85);
        if (pollution > 0) {
            drawTexturedModalRect(poseStack,h + 173, k + 23 + 85 - pollution, 234,
                    6 + 85 - pollution, 7, pollution
            );
        }


        for (int i = 0; i < this.tileentity.invSlotGlass.size(); i++) {
            final List<TileEntityMiniPanels.EnumState> list = this.tileentity.listStable.get(i);
            if (list.size() == 0) {
                continue;
            }
            if (i == 0) {
                final TileEntityMiniPanels.EnumState first = list.get(1);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,(h + 74), (k + 51), 220, 135, 9, 6);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,(h + 74), (k + 51), 220, 144, 9, 6);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,(h + 74), (k + 51), 220, 153, 9, 6);
                }
                final TileEntityMiniPanels.EnumState second = list.get(0);
                if (second == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,(h + 87), (k + 38), 213, 135, 6, 9);
                } else if (second == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,(h + 87), (k + 38), 213, 144, 6, 9);
                } else if (second == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,(h + 87), (k + 38), 213, 153, 6, 9);
                }
            }

            if (i == 1) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,(h + 110), (k + 38), 213, 135, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,(h + 110), (k + 38), 213, 144, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,(h + 110), (k + 38), 213, 153, 6, 9);
                }
                final TileEntityMiniPanels.EnumState second = list.get(1);
                if (second == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,(h + 97), (k + 51), 220, 135, 9, 6);
                } else if (second == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,(h + 97), (k + 51), 220, 144, 9, 6);
                } else if (second == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,(h + 97), (k + 51), 220, 153, 9, 6);
                }
            }
            if (i == 2) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,(h + 120), (k + 51), 220, 135, 9, 6);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,(h + 120), (k + 51), 220, 144, 9, 6);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,(h + 120), (k + 51), 220, 153, 9, 6);
                }

            }
            if (i == 3) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,(h + 87), (k + 61), 213, 135, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,(h + 87), (k + 61), 213, 144, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,(h + 87), (k + 61), 213, 153, 6, 9);
                }
                final TileEntityMiniPanels.EnumState second = list.get(1);
                if (second == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,h + 74, k + 74, 220, 135, 9, 6);
                } else if (second == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,h + 74, k + 74, 220, 144, 9, 6);
                } else if (second == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,h + 74, k + 74, 220, 153, 9, 6);
                }

            }
            if (i == 4) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,(h + 110), (k + 61), 213, 135, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,(h + 110), (k + 61), 213, 144, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,(h + 110), (k + 61), 213, 153, 6, 9);
                }
                final TileEntityMiniPanels.EnumState second = list.get(1);
                if (second == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,h + 97, k + 74, 220, 135, 9, 6);
                } else if (second == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,h + 97, k + 74, 220, 144, 9, 6);
                } else if (second == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,h + 97, k + 74, 220, 153, 9, 6);
                }

            }
            if (i == 5) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,h + 120, k + 74, 220, 135, 9, 6);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,h + 120, k + 74, 220, 144, 9, 6);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,h + 120, k + 74, 220, 153, 9, 6);
                }

            }
            if (i == 6) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,(h + 87), (k + 84), 213, 135, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,(h + 87), (k + 84), 213, 144, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,(h + 87), (k + 84), 213, 153, 6, 9);
                }

            }
            if (i == 7) {
                final TileEntityMiniPanels.EnumState first = list.get(0);
                if (first == TileEntityMiniPanels.EnumState.STABLE) {

                    drawTexturedModalRect(poseStack,(h + 110), (k + 84), 213, 135, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.NORMAL) {

                    drawTexturedModalRect(poseStack,(h + 110), (k + 84), 213, 144, 6, 9);
                } else if (first == TileEntityMiniPanels.EnumState.UNSTABLE) {

                    drawTexturedModalRect(poseStack,(h + 110), (k + 84), 213, 153, 6, 9);
                }

            }
        }
        if (this.tileentity.pollution.isActive()) {
            new ItemStackImage(this, 184, 93, this.tileentity.pollution::getStack).drawBackground(poseStack,h, k);
        }

        this.bindTexture();
        PoseStack pose = poseStack.pose();
        pose.pushPose();
        pose.scale(0.75F, 0.75F, 0.75F);
        drawTexturedModalRect(poseStack, (int) ((h + 25) / 0.75F), (int) ((k + 105) / 0.75F), 237, 134, 16, 16);
        drawTexturedModalRect(poseStack, (int) ((h + 179) / 0.75F), (int) ((k + 105) / 0.75F), 237, 151, 16, 16);

        pose.popPose();
        if (tileentity.component.getBonusCapacity() > 0) {
            new ItemStackImage(this, 184, 20,() -> this.tileentity.invSlotStorage.get(0)).drawBackground(poseStack,h, k);
            pose.pushPose();
            pose.scale(0.15F, 0.15F, 1F);
            bindTexture(getTexture());
            RenderSystem.disableDepthTest();
            drawTexturedModalRect(poseStack, (int) ((h + 193) / 0.15F), (int) ((k + 28) / 0.15F), 192, 199, 250 - 192, 255 - 199);
            RenderSystem.enableDepthTest();
            pose.popPose();
        }
        if (tileentity.component.getBonusProdution() > 0) {
            new ItemStackImage(this, 184, 40,() -> this.tileentity.invSlotOutput.get(0)).drawBackground(poseStack,h, k);
            pose.pushPose();
            pose.scale(0.15F, 0.15F, 1F);
            bindTexture(getTexture());
            RenderSystem.disableDepthTest();
            drawTexturedModalRect(poseStack, (int) ((h + 193) / 0.15F), (int) ((k + 46) / 0.15F), 192, 199, 250 - 192, 255 - 199);
            RenderSystem.enableDepthTest();
            pose.popPose();
        }
        if (tileentity.bonusGeneration > 0) {
            new ItemStackImage(this, 184, 60,() -> this.tileentity.invSlotGlass.get(0)).drawBackground(poseStack,h, k);
            pose.pushPose();
            pose.scale(0.15F, 0.15F, 1F);
            bindTexture(getTexture());
            RenderSystem.disableDepthTest();
            drawTexturedModalRect(poseStack, (int) ((h + 193) / 0.15F), (int) ((k + 66) / 0.15F), 192, 199, 250 - 192, 255 - 199);
            RenderSystem.enableDepthTest();
            pose.popPose();
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_mini_panels.png");

    }

}
