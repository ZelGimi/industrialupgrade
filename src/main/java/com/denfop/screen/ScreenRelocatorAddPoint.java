package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.items.relocator.ItemStackRelocator;
import com.denfop.items.relocator.Point;
import com.denfop.network.packet.PacketAddRelocatorPoint;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ScreenRelocatorAddPoint<T extends ContainerMenuBase<ItemStackRelocator>> extends ScreenMain<ContainerMenuBase<ItemStackRelocator>> {

    public EditBox textField;
    boolean hoverAdd;

    public ScreenRelocatorAddPoint(ContainerMenuBase<ItemStackRelocator> guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 176;
        this.imageHeight = 77;


    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (hoverAdd) {
            new PacketAddRelocatorPoint(minecraft.player, new Point(textField.getValue(), minecraft.player.blockPosition()));

        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return this.textField.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
    }

    @Override
    protected void init() {
        super.init();
        this.textField = new EditBox(this.font, this.leftPos + 46, this.topPos + 28, 132 - 43, 12, Component.literal(""));
        this.textField.setMaxLength(50);
        this.textField.setValue("");
        this.textField.setFocused(true);
        this.textField.setBordered(false);
        this.textField.setCanLoseFocus(false);
        this.addWidget(this.textField);
        this.addRenderableWidget(this.textField);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.textField.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics poseStack, int mouseX, int mouseY) {
        super.renderLabels(poseStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        if (hoverAdd) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 79, this.guiTop + 40, 237, 0, 19, 20);

        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        hoverAdd = false;
        if (par1 >= 79 && par2 >= 40 && par1 <= 97 && par2 <= 59)
            hoverAdd = true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == this.minecraft.options.keyInventory.getKey().getValue()) {
            this.textField.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers) || this.textField.keyPressed(keyCode, scanCode, modifiers);
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guirelocator_add.png");
    }

}
