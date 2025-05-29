package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ImageScreen;
import com.denfop.api.gui.MouseButton;
import com.denfop.container.ContainerBase;
import com.denfop.items.relocator.ItemStackRelocator;
import com.denfop.items.relocator.Point;
import com.denfop.network.packet.PacketAddRelocatorPoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class GuiRelocatorAddPoint<T extends ContainerBase<ItemStackRelocator>> extends GuiIU<ContainerBase<ItemStackRelocator>> {

    public EditBox textField;

    public GuiRelocatorAddPoint(ContainerBase<ItemStackRelocator> guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 100;
        this.imageHeight = 50;
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, imageHeight));
        this.addElement(new ImageScreen(this, 8, 6, 82, 14));
        this.addElement(new CustomButton(this, 8, 26, 84, 14, null, 0, Localization.translate("button.write")) {
            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    Minecraft.getInstance().getSoundManager().play(
                            SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                    );
                    new PacketAddRelocatorPoint(minecraft.player, new Point(textField.getValue(), minecraft.player.blockPosition()));
                }
                return true;
            }
        });

    }

    @Override
    protected void init() {
        super.init();
        this.textField = new EditBox(this.font, this.leftPos + 14, this.topPos + 10, 76, 10, Component.literal(""));
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == this.minecraft.options.keyInventory.getKey().getValue()) {
            this.textField.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers) || this.textField.keyPressed(keyCode, scanCode, modifiers);
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
