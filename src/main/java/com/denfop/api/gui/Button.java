package com.denfop.api.gui;

import com.denfop.gui.GuiIC2;
import com.google.common.base.Supplier;
import ic2.core.init.Localization;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

public abstract class Button<T extends Button<T>> extends GuiElement<T> {

    private static final int iconSize = 16;
    private final IClickHandler handler;
    private Supplier<String> textProvider;
    private Supplier<ItemStack> iconProvider;

    protected Button(GuiIC2<?> gui, int x, int y, int width, int height, IClickHandler handler) {
        super(gui, x, y, width, height);
        this.handler = handler;
    }

    public T withText(final String text) {
        return this.withText(new Supplier<String>() {
            public String get() {
                return text;
            }
        });
    }

    public T withText(Supplier<String> textProvider) {
        this.textProvider = textProvider;
        return (T) this;
    }

    public T withIcon(Supplier<ItemStack> iconProvider) {
        this.iconProvider = iconProvider;
        return (T) this;
    }

    protected int getTextColor(int mouseX, int mouseY) {
        return 14540253;
    }

    public void drawBackground(int mouseX, int mouseY) {
        if (this.textProvider != null) {
            String text = this.textProvider.get();
            if (text != null && !text.isEmpty()) {
                text = Localization.translate(text);
                this.gui.drawXYCenteredString(
                        this.x + this.width / 2,
                        this.y + this.height / 2,
                        text,
                        this.getTextColor(mouseX, mouseY),
                        true
                );
            }
        } else if (this.iconProvider != null) {
            ItemStack stack = this.iconProvider.get();
            if (stack != null) {
                stack.getItem();
                RenderHelper.enableGUIStandardItemLighting();
                this.gui.drawItem(this.x + (this.width - 16) / 2, this.y + (this.height - 16) / 2, stack);
                RenderHelper.disableStandardItemLighting();
            }
        }

    }

    protected boolean onMouseClick(int mouseX, int mouseY, MouseButton button) {
        this.gui.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.handler.onClick(button);
        return false;
    }

}
