package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.api.reactors.EnumTypeWork;
import com.denfop.api.reactors.LogicComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerHeatReactor;
import com.denfop.container.SlotInvSlot;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.reactors.heat.controller.TileEntityMainController;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiHeatController<T extends ContainerHeatReactor> extends GuiIU<ContainerHeatReactor> {

    private boolean visible;
    private boolean visible1;
    private boolean visible2;
    private boolean visible3;

    public GuiHeatController(ContainerHeatReactor guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 225;
        this.imageHeight = 254;
        this.componentList.add(new GuiComponent(this, 160, 13, 186 - 160, 38 - 12,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityMainController) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityMainController) this.getEntityBlock()).work;
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 0, 75, 182 - 163, 60 - 41,
                new Component<>(new ComponentButton(this.container.base, -1) {
                    @Override
                    public String getText() {
                        return Localization.translate("iu.reactor_heat");
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 0, 97, 182 - 163, 60 - 41,
                new Component<>(new ComponentButton(this.container.base, -2) {
                    @Override
                    public String getText() {
                        return Localization.translate("iu.reactor_stable");
                    }
                })
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 158 && mouseX <= 187 && mouseY >= 138 && mouseY <= 151) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("reactor.guide.heat_reactor"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 22; i++) {
                compatibleUpgrades.add(Localization.translate("reactor.guide.heat_reactor" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 260, mouseY - 30, text);
        }
    }

    public void renderSlot(PoseStack p_97800_, Slot p_97801_) {
        if (this.container.base.heat_sensor || this.container.base.stable_sensor) {
            if (p_97801_ instanceof SlotInvSlot) {
                SlotInvSlot slotInvSlot = (SlotInvSlot) p_97801_;
                if (slotInvSlot.invSlot == this.container.base.reactorsElements) {
                    return;
                }
            }
        }
        super.renderSlot(p_97800_,p_97801_);
    }
    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 200 && y >= 88 && x <= 217 && y <= 105) {
            new PacketUpdateServerTile(this.container.base, 3);
        }

    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer( poseStack,par1, par2);
        if (!this.container.base.work) {
            this.visible = par1 >= 160 && par1 <= 186 && par2 >= 13 && par2 <= 39;
        } else {
            this.visible = false;
        }
        this.visible1 = par1 >= 0 && par1 <= 19 && par2 >= 75 && par2 <= 94;
        this.visible2 = par1 >= 0 && par1 <= 19 && par2 >= 97 && par2 <= 116;
        this.visible3 = par1 >= 158 && par1 <= 187 && par2 >= 138 && par2 <= 151;
        if (this.container.base.heat_sensor || this.container.base.stable_sensor) {
            if (this.container.base.work) {
                if (this.container.base.getReactor() != null) {
                    for (LogicComponent component : this.container.base.reactor.getListComponent()) {
                        for (int i1 = 0; i1 < this.container.slots.size(); ++i1) {
                            Slot slot = this.container.slots.get(i1);
                            {
                                if (slot instanceof SlotInvSlot) {
                                    SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                                    if (slotInvSlot.invSlot == this.container.base.reactorsElements) {
                                        if (slotInvSlot.index == component.getY() * this.container.base.getWidth() + component.getX()) {
                                            if (this.container.base.heat_sensor) {
                                                PoseStack pose = poseStack;
                                                pose.pushPose();
                                                pose.translate(slotInvSlot.x + 22 / 2  - getStringWidth(    String.valueOf((int) component.getHeat())) / 2,slotInvSlot.y + 5,0);
                                                pose.scale(0.75f,0.75f,1f);
                                                drawString(poseStack,
                                                        String.valueOf((int) component.getHeat()),
                                                        0,
                                                        0,
                                                        ModUtils.convertRGBcolorToInt(195,
                                                                64, 0
                                                        )
                                                );
                                                pose.popPose();
                                            }
                                            if (this.container.base.stable_sensor && component
                                                    .getItem()
                                                    .getType() != EnumTypeComponent.ROD) {
                                                PoseStack pose = poseStack;
                                                pose.pushPose();
                                                pose.translate(slotInvSlot.x + 22 / 2  - getStringWidth(     String.valueOf(-1 * component.getDamage())) / 2,slotInvSlot.y + 5,0);
                                                pose.scale(0.75f,0.75f,1f);
                                                drawString(poseStack,
                                                        String.valueOf(-1 * component.getDamage()),
                                                        0,
                                                        0,
                                                        ModUtils.convertRGBcolorToInt(14,
                                                                50, 86
                                                        )
                                                );
                                                pose.popPose();

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        String time = "";
        if (this.container.base.security == EnumTypeSecurity.ERROR) {
            time = this.container.base.red_timer.getDisplay();
        }
        if (this.container.base.security == EnumTypeSecurity.UNSTABLE) {
            time = this.container.base.yellow_timer.getDisplay();
        }
        new AdvArea(this, 158, 76, 188, 98).withTooltip("Radiation: " + ModUtils.getString(this.container.base
                .getRad()
                .getEnergy()) +
                "/" + ModUtils.getString(this.container.base.getRad().getCapacity()) + " ☢" + "\n" + Localization.translate("iu" +
                ".potion.radiation") + ": " + ModUtils.getString(
                this.container.base
                        .getReactor()
                        .getRadGeneration()) + " ☢/t \n" + ((this.container.base.getLevelReactor() < this.container.base.getMaxLevelReactor())
                ?
                Localization.translate("reactor.canupgrade")
                : Localization.translate("reactor.notcanupgrade")) + "\n" + Localization.translate(
                "gui.SuperSolarPanel.generating") + ": " + ModUtils.getString(
                this.container.base.output) + " EF/t" + (!time.isEmpty() ? ("\n" + time) : time)).drawForeground( poseStack,par1, par2);
        String name = this.container.base.security.name().toLowerCase().equals("") ? "none" :
                this.container.base.security.name().toLowerCase();
        new AdvArea(this, 158, 43, 188, 73).withTooltip(Localization.translate("waterreactor.security." + name)).drawForeground(
                poseStack,  par1,
                par2
        );
        new AdvArea(this, 21, 139, 148, 162)
                .withTooltip(Localization.translate("iu.reactor_info.heat") + ": " + ModUtils.getString(this.container.base.getHeat()) +
                        "/" + ModUtils.getString(this.container.base.getMaxHeat()) + "°C" + "\n" + Localization.translate(
                        "iu.reactor_info.stable_heat") + ": " + this.container.base.getStableMaxHeat() + "°C")
                .drawForeground(
                        poseStack,  par1,
                        par2
                );
        new AdvArea(this, 200, 88, 217, 105)
                .withTooltip(Localization.translate("iu.reactor_info.upgrade"))
                .drawForeground(
                        poseStack, par1,
                        par2
                );
        if (this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE) {
            new AdvArea(this, 201, 10, 215, 85)
                    .withTooltip(Localization.translate("iu.reactor_info.energy") + ": " + ModUtils.getString(this.container.base.energy.getEnergy()) +
                            "/" + ModUtils.getString(this.container.base.energy.getCapacity()))
                    .drawForeground( poseStack,par1, par2);
        } else {
            new AdvArea(this, 201, 10, 215, 85)
                    .withTooltip(Localization.translate("iu.reactor_info.upgrade1"))
                    .drawForeground( poseStack,par1, par2);
        }
        handleUpgradeTooltip(par1, par2);
    }



    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect( poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        if (this.visible) {
            drawTexturedModalRect( poseStack,this.guiLeft + 160,
                    this.guiTop + 13
                    , 228, 159, 27, 27
            );
        }
        if (this.visible1) {
            drawTexturedModalRect( poseStack,this.guiLeft + 0,
                    this.guiTop + 75
                    , 235, 215, 20, 20
            );
        }
        if (this.visible2) {
            drawTexturedModalRect( poseStack,this.guiLeft + 0,
                    this.guiTop + 97
                    , 235, 236, 20, 20
            );
        }
        if (this.visible3) {
            drawTexturedModalRect( poseStack,this.guiLeft + 158,
                    this.guiTop + 138
                    , 225, 121, 30, 14
            );
        }
        if (this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE) {
            drawTexturedModalRect( poseStack,this.guiLeft + 201,
                    this.guiTop + 89
                    , 239, 104, 16, 16
            );
        }
        switch (this.container.base.getLevelReactor()) {
            case 0:
                drawTexturedModalRect( poseStack,this.guiLeft + 0,
                        this.guiTop + 7
                        , 241, 1, 14, 14
                );
                break;
            case 1:
                drawTexturedModalRect( poseStack,this.guiLeft + 0,
                        this.guiTop + 20
                        , 241, 14, 14, 14
                );
                break;
            case 2:
                drawTexturedModalRect( poseStack,this.guiLeft + 0,
                        this.guiTop + 33
                        , 241, 27, 14, 14
                );
                break;
            case 3:
                drawTexturedModalRect( poseStack,this.guiLeft + 0,
                        this.guiTop + 46
                        , 241, 40, 14, 14
                );
                break;
            case 4:
                drawTexturedModalRect( poseStack,this.guiLeft + 0,
                        this.guiTop + 59
                        , 241, 53, 14, 14
                );
                break;
        }
        if (this.container.base.work) {
            drawTexturedModalRect( poseStack,this.guiLeft + 160, this.guiTop + 13
                    , 228, 187, 27, 27);
        }
        if (this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE) {
            double bar = this.container.base.energy.getFillRatio();
            bar = Math.min(1, bar);
            drawTexturedModalRect( poseStack,this.guiLeft + 204, (int) (this.guiTop + 84 - (bar * 70))
                    , 228, (int) (72 - (bar * 70)), 9, (int) (bar * 70));

        }
        switch (this.container.base.security) {
            case NONE:
                break;
            case ERROR:
            case UNSTABLE:
                drawTexturedModalRect( poseStack,this.guiLeft + 165, this.guiTop + 50
                        , 238, 68, 5, 11);
                break;
            case STABLE:
                drawTexturedModalRect( poseStack,this.guiLeft + 165, this.guiTop + 50
                        , 238, 86, 17, 17);
                break;
        }
       bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common1.png"));

        double bar = this.container.base.heat / this.container.base.getMaxHeat();
        bar = Math.min(bar, 1);
        drawTexturedModalRect( poseStack,this.guiLeft + 26, this.guiTop + 143
                , 6, 165, (int) (bar * 118), 16);
    }

    @Override
    protected ResourceLocation getTexture() {
        switch (this.container.base.getMaxLevelReactor()) {
            case 1:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat.png");
            case 2:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat1.png");
            case 3:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat2.png");
            case 4:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat3.png");

        }
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat4.png");
    }

}
