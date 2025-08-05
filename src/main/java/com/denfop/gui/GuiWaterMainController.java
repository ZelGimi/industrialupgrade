package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.EnumTypeWork;
import com.denfop.api.reactors.LogicComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerWaterMainController;
import com.denfop.container.SlotInvSlot;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiWaterMainController<T extends ContainerWaterMainController> extends GuiIU<ContainerWaterMainController> {

    private boolean visible;
    private boolean visible1;
    private boolean visible2;
    private boolean visible3;
    private boolean visible4;

    public GuiWaterMainController(ContainerWaterMainController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageHeight = 254;
        this.imageWidth = 223;
        this.addComponent(new GuiComponent(this, 149, 117, 170 - 149, 138 - 117,
                new Component<>(new ComponentButton(this.container.base, 1) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 172, 117, 170 - 149, 138 - 117,
                new Component<>(new ComponentButton(this.container.base, 2) {
                    @Override
                    public String getText() {
                        return "-1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 163, 41, 182 - 163, 60 - 41,
                new Component<>(new ComponentButton(this.container.base, -1) {
                    @Override
                    public String getText() {
                        return Localization.translate("iu.reactor_heat");
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 163, 63, 182 - 163, 60 - 41,
                new Component<>(new ComponentButton(this.container.base, -2) {
                    @Override
                    public String getText() {
                        return Localization.translate("iu.reactor_stable");
                    }
                })
        ));
        this.componentList.add(new GuiComponent(this, 160, 12, 186 - 160, 38 - 12,
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

    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
    }
    public void renderSlot(GuiGraphics p_97800_, Slot p_97801_) {
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
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 15 && mouseX <= 30 && mouseY >= 2 && mouseY <= 14) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("reactor.guide.water_reactor"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 15; i++) {
                compatibleUpgrades.add(Localization.translate("reactor.guide.water_reactor" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 40, mouseY + 10, text);
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer( poseStack, par1, par2);
       draw( poseStack,
                String.valueOf(this.container.base.getPressure()),
                171,
                103,
                ModUtils.convertRGBcolorToInt(14,
                        50, 86
                )
        );

        if (this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE) {
            new Area(this, 201, 10, 15, 76)
                    .withTooltip(Localization.translate("iu.reactor_info.energy") + ": " + ModUtils.getString(this.container.base.energy.getEnergy()) +
                            "/" + ModUtils.getString(this.container.base.energy.getCapacity()))
                    .drawForeground( poseStack, par1, par2);
        } else {
            new Area(this, 201, 10, 15, 76)
                    .withTooltip(Localization.translate("iu.reactor_info.upgrade1"))
                    .drawForeground(
                            poseStack,  par1,
                            par2
                    );
        }
        new Area(this, 26, 143, 120, 18)
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
                        poseStack,  par1,
                        par2
                );
        new AdvArea(this, 158, 100, 185, 124)
                .withTooltip(Localization.translate("iu.reactor_info.pressure"))
                .drawForeground(
                        poseStack,    par1,
                        par2
                );
        handleUpgradeTooltip(par1, par2);
        if (!this.container.base.work) {
            this.visible = par1 >= 160 && par1 <= 186 && par2 >= 12 && par2 <= 38;
        } else {
            this.visible = false;
        }
        this.visible1 = par1 >= 149 && par1 <= 170 && par2 >= 117 && par2 <= 138;
        this.visible2 = par1 >= 172 && par1 <= 193 && par2 >= 117 && par2 <= 138;
        this.visible3 = par1 >= 163 && par1 <= 182 && par2 >= 41 && par2 <= 60;
        this.visible4 = par1 >= 163 && par1 <= 182 && par2 >= 63 && par2 <= 82;
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
                                                PoseStack pose = poseStack.pose();
                                                pose.pushPose();
                                                pose.translate(slotInvSlot.x + 22 / 2  - getStringWidth(    String.valueOf((int) component.getHeat())) / 2,slotInvSlot.y + 5,0);
                                                pose.scale(0.75f,0.75f,1f);
                                                draw(poseStack,
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
                                                PoseStack pose = poseStack.pose();
                                                pose.pushPose();
                                                pose.translate(slotInvSlot.x + 22 / 2  - getStringWidth(     String.valueOf(-1 * component.getDamage())) / 2,slotInvSlot.y + 5,0);
                                                pose.scale(0.75f,0.75f,1f);
                                                draw(poseStack,
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
    }



    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer( poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        if (this.container.base.work) {
            drawTexturedModalRect( poseStack, this.guiLeft + 160,
                    this.guiTop + 12
                    , 228, 100, 27, 27
            );
        }
        if (this.visible) {
            drawTexturedModalRect( poseStack, this.guiLeft + 160,
                    this.guiTop + 12
                    , 228, 72, 27, 27
            );
        }
        if (this.visible1) {
            drawTexturedModalRect( poseStack, this.guiLeft + 153,
                    this.guiTop + 121
                    , 241, 185, 14, 14
            );
        }
        if (this.visible2) {
            drawTexturedModalRect( poseStack, this.guiLeft + 176,
                    this.guiTop + 121
                    , 241, 200, 14, 14
            );
        }
        if (this.visible3) {
            drawTexturedModalRect( poseStack, this.guiLeft + 163,
                    this.guiTop + 41
                    , 235, 215, 20, 20
            );
        }
        if (this.visible4) {
            drawTexturedModalRect( poseStack, this.guiLeft + 163,
                    this.guiTop + 63
                    , 235, 236, 20, 20
            );
        }
        switch (this.container.base.getLevelReactor()) {
            case 0:
                drawTexturedModalRect( poseStack, this.guiLeft + 0,
                        this.guiTop + 7
                        , 241, 1, 14, 14
                );
                break;
            case 1:
                drawTexturedModalRect( poseStack, this.guiLeft + 0,
                        this.guiTop + 20
                        , 241, 14, 14, 14
                );
                break;
            case 2:
                drawTexturedModalRect( poseStack, this.guiLeft + 0,
                        this.guiTop + 33
                        , 241, 27, 14, 14
                );
                break;
            case 3:
                drawTexturedModalRect( poseStack, this.guiLeft + 0,
                        this.guiTop + 46
                        , 241, 40, 14, 14
                );
                break;
            case 4:
                drawTexturedModalRect( poseStack, this.guiLeft + 0,
                        this.guiTop + 59
                        , 241, 53, 14, 14
                );
                break;
        }
        if (this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE) {
            double bar = this.container.base.energy.getFillRatio();
            bar = Math.min(1, bar);
            drawTexturedModalRect( poseStack, this.guiLeft + 204, (int) (this.guiTop + 84 - (bar * 70))
                    , 228, (int) (72 - (bar * 70)), 9, (int) (bar * 70));

        }
       bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));
        double bar = this.container.base.heat / this.container.base.getMaxHeat();
        bar = Math.min(bar, 1);
        drawTexturedModalRect( poseStack, this.guiLeft + 26, this.guiTop + 143
                , 4, 218, (int) (bar * 120), 18);

        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));


        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect( poseStack, this.guiLeft + 15, this.guiTop + 2, 0, 0, 10, 10);
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;


        if (x >= 200 && x <= 217 && y >= 88 && y <= 105) {
            new PacketUpdateServerTile(this.container.base, 3);
        }
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect( poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected ResourceLocation getTexture() {
        switch (this.container.base.getMaxLevelReactor()) {
            case 1:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor.png");
            case 2:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor1.png");
            case 3:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor2.png");
            case 4:
                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guifluidreactor3.png");

        }
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiwaterreactor5.png");
    }

}
