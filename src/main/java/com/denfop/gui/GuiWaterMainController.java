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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiWaterMainController extends GuiIU<ContainerWaterMainController> {

    private boolean visible;
    private boolean visible1;
    private boolean visible2;
    private boolean visible3;
    private boolean visible4;

    public GuiWaterMainController(ContainerWaterMainController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.ySize = 254;
        this.xSize = 223;
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
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(
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
                    .drawForeground(par1, par2);
        } else {
            new Area(this, 201, 10, 15, 76)
                    .withTooltip(Localization.translate("iu.reactor_info.upgrade1"))
                    .drawForeground(
                            par1,
                            par2
                    );
        }
        new Area(this, 26, 143, 120, 18)
                .withTooltip(Localization.translate("iu.reactor_info.heat") + ": " + ModUtils.getString(this.container.base.getHeat()) +
                        "/" + ModUtils.getString(this.container.base.getMaxHeat()) + "°C" + "\n" + Localization.translate(
                        "iu.reactor_info.stable_heat") + ": " + this.container.base.getStableMaxHeat() + "°C")
                .drawForeground(
                        par1,
                        par2
                );
        new AdvArea(this, 200, 88, 217, 105)
                .withTooltip(Localization.translate("iu.reactor_info.upgrade"))
                .drawForeground(
                        par1,
                        par2
                );
        new AdvArea(this, 158, 100, 185, 144)
                .withTooltip(Localization.translate("iu.reactor_info.pressure"))
                .drawForeground(
                        par1,
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
                        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
                            Slot slot = this.inventorySlots.inventorySlots.get(i1);
                            {
                                if (slot instanceof SlotInvSlot) {
                                    SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                                    if (slotInvSlot.invSlot == this.container.base.reactorsElements) {
                                        if (slotInvSlot.index == component.getY() * this.container.base.getWidth() + component.getX()) {
                                            if (this.container.base.heat_sensor) {
                                                this.fontRenderer.drawString(
                                                        String.valueOf((int) component.getHeat()),
                                                        slotInvSlot.xPos + 3,
                                                        slotInvSlot.yPos + 4,
                                                        ModUtils.convertRGBcolorToInt(195,
                                                                64, 0
                                                        )
                                                );
                                            }
                                            if (this.container.base.stable_sensor && component
                                                    .getItem()
                                                    .getType() != EnumTypeComponent.ROD) {
                                                this.fontRenderer.drawString(
                                                        String.valueOf(-1 * component.getDamage()),
                                                        slotInvSlot.xPos + 4 + (component.getDamage() > 0 ? -3 : 0),
                                                        slotInvSlot.yPos + 4,
                                                        ModUtils.convertRGBcolorToInt(14,
                                                                50, 86
                                                        )
                                                );
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

    public void drawItemStack(ItemStack stack, int x, int y, String altText) {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) {
            font = fontRenderer;
        }
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }

    private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
        return this.isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) i, (float) j, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        this.hoveredSlot = null;
        int k = 240;
        int l = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
            Slot slot = this.inventorySlots.inventorySlots.get(i1);
            if (this.container.base.heat_sensor || this.container.base.stable_sensor) {
                if (slot instanceof SlotInvSlot) {
                    SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                    if (slotInvSlot.invSlot == this.container.base.reactorsElements) {
                        continue;
                    }
                }
            }
            if (slot.isEnabled()) {
                this.drawSlot(slot);
            }

            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled()) {
                this.hoveredSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int j1 = slot.xPos;
                int k1 = slot.yPos;
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }

        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(
                this,
                mouseX,
                mouseY
        ));
        InventoryPlayer inventoryplayer = this.mc.player.inventory;
        ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;

        if (!itemstack.isEmpty()) {
            int j2 = 8;
            int k2 = this.draggedStack.isEmpty() ? 8 : 16;
            String s = null;

            if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.setCount(this.dragSplittingRemnant);

                if (itemstack.isEmpty()) {
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k2, s);
        }

        if (!this.returningStack.isEmpty()) {
            float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

            if (f >= 1.0F) {
                f = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }

            int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
            int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
            int l1 = this.touchUpX + (int) ((float) l2 * f);
            int i2 = this.touchUpY + (int) ((float) i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, (String) null);
        }

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        if (this.container.base.work) {
            drawTexturedModalRect(this.guiLeft + 160,
                    this.guiTop + 12
                    , 228, 100, 27, 27
            );
        }
        if (this.visible) {
            drawTexturedModalRect(this.guiLeft + 160,
                    this.guiTop + 12
                    , 228, 72, 27, 27
            );
        }
        if (this.visible1) {
            drawTexturedModalRect(this.guiLeft + 153,
                    this.guiTop + 121
                    , 241, 185, 14, 14
            );
        }
        if (this.visible2) {
            drawTexturedModalRect(this.guiLeft + 176,
                    this.guiTop + 121
                    , 241, 200, 14, 14
            );
        }
        if (this.visible3) {
            drawTexturedModalRect(this.guiLeft + 163,
                    this.guiTop + 41
                    , 235, 215, 20, 20
            );
        }
        if (this.visible4) {
            drawTexturedModalRect(this.guiLeft + 163,
                    this.guiTop + 63
                    , 235, 236, 20, 20
            );
        }
        switch (this.container.base.getLevelReactor()) {
            case 0:
                drawTexturedModalRect(this.guiLeft + 0,
                        this.guiTop + 7
                        , 241, 1, 14, 14
                );
                break;
            case 1:
                drawTexturedModalRect(this.guiLeft + 0,
                        this.guiTop + 20
                        , 241, 14, 14, 14
                );
                break;
            case 2:
                drawTexturedModalRect(this.guiLeft + 0,
                        this.guiTop + 33
                        , 241, 27, 14, 14
                );
                break;
            case 3:
                drawTexturedModalRect(this.guiLeft + 0,
                        this.guiTop + 46
                        , 241, 40, 14, 14
                );
                break;
            case 4:
                drawTexturedModalRect(this.guiLeft + 0,
                        this.guiTop + 59
                        , 241, 53, 14, 14
                );
                break;
        }
        if (this.container.base.typeWork == EnumTypeWork.LEVEL_INCREASE) {
            double bar = this.container.base.energy.getFillRatio();
            bar = Math.min(1, bar);
            drawTexturedModalRect(this.guiLeft + 204, (int) (this.guiTop + 84 - (bar * 70))
                    , 228, (int) (72 - (bar * 70)), 9, (int) (bar * 70));

        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png"));
        double bar = this.container.base.heat / this.container.base.getMaxHeat();
        bar = Math.min(bar, 1);
        drawTexturedModalRect(this.guiLeft + 26, this.guiTop + 143
                , 4, 218, (int) (bar * 120), 18);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));


        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 2, 0, 0, 10, 10);
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;


        if (x >= 200 && x <= 217 && y >= 88 && y <= 105) {
            new PacketUpdateServerTile(this.container.base, 3);
        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
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
