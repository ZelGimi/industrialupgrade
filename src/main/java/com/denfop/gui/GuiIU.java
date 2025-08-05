package com.denfop.gui;

import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBase;
import com.denfop.invslot.InvSlot;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiIU<T extends ContainerBase<? extends IAdvInventory>> extends GuiCore<T> {

    private final EnumTypeStyle style;
    public boolean isBlack = false;
    protected GuiComponent inventory;
    protected GuiComponent slots;
    protected List<InvSlot> invSlotList = new ArrayList<>();
    protected List<GuiComponent> componentList = new ArrayList<>();

    public GuiIU(final T container) {
        super(container);
        this.style = EnumTypeStyle.DEFAULT;
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );

        componentList.add(inventory);
        componentList.add(slots);
    }

    public GuiIU(final T container, EnumTypeStyle style) {
        super(container);
        this.style = style;
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );

        componentList.add(inventory);
        componentList.add(slots);
    }

    public GuiIU(final T container, EnumTypeComponent style) {
        super(container);
        this.style = getStyle(style);
        inventory = new GuiComponent(this, 7, 83, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );

        componentList.add(inventory);
        componentList.add(slots);
    }

    public EnumTypeComponent getComponent() {
        switch (this.style) {
            case ADVANCED:
                return EnumTypeComponent.ADVANCED;
            case IMPROVED:
                return EnumTypeComponent.IMPROVED;
            case PERFECT:
                return EnumTypeComponent.PERFECT;
            case PHOTONIC:
                return EnumTypeComponent.PHOTONIC;
            case STEAM:
                return EnumTypeComponent.STEAM_DEFAULT;
            case BIO:
                return EnumTypeComponent.BIO_DEFAULT;
            case SPACE:
                return EnumTypeComponent.SPACE_DEFAULT;
            default:
                return EnumTypeComponent.DEFAULT;
        }
    }

    public Font getFontRenderer() {
        return Minecraft.getInstance().font;
    }

    public EnumTypeStyle getStyle(EnumTypeComponent style) {
        switch (style) {
            case ADVANCED:
                return EnumTypeStyle.ADVANCED;
            case IMPROVED:
                return EnumTypeStyle.IMPROVED;
            case PERFECT:
                return EnumTypeStyle.PERFECT;
            case PHOTONIC:
                return EnumTypeStyle.PHOTONIC;
            case STEAM_DEFAULT:
                return EnumTypeStyle.STEAM;
            case BIO_DEFAULT:
                return EnumTypeStyle.BIO;
            case SPACE_DEFAULT:
                return EnumTypeStyle.SPACE;
            default:
                return EnumTypeStyle.DEFAULT;
        }
    }

    public EnumTypeStyle getStyle() {
        return this.style;
    }

    public void addComponent(GuiComponent component) {
        componentList.add(component);
    }

    public void removeComponent(int index) {
        componentList.remove(index);
    }

    public void removeComponent(GuiComponent component) {
        componentList.remove(component);
    }

    public void drawForeground(GuiGraphics poseStack, int mouseX, int mouseY) {
        componentList.forEach(guiComponent -> guiComponent.drawForeground(poseStack, mouseX, mouseY));
    }


    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        componentList.forEach(guiComponent -> guiComponent.buttonClicked(x, y));

    }

    protected void renderBg(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.renderBg(poseStack, partialTicks, mouseX, mouseY);
        drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        drawBackground(poseStack);
    }

    protected void drawBackground(GuiGraphics poseStack) {
        componentList.forEach(guiComponent -> guiComponent.drawBackground(poseStack, guiLeft(), guiTop()));

    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {

    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        poseStack.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
        String name = this.container.base.getDisplayName().getString();
        int textWidth = this.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }

        PoseStack pose = poseStack.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.imageWidth / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6) / scale);


        poseStack.drawString(Minecraft.getInstance().font, name, textX, textY, 4210752, false);
        pose.scale(1 / scale, 1 / scale, 1);

        pose.popPose();
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        this.drawForeground(poseStack, par1, par2);
    }

    public void updateTickInterface() {

    }
}
