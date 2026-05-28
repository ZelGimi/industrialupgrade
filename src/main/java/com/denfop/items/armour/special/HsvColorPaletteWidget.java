package com.denfop.items.armour.special;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class HsvColorPaletteWidget extends AbstractWidget {

    private final Listener listener;
    private float hue;
    private float saturation;
    private float value;
    private boolean dragging;
    public HsvColorPaletteWidget(int x, int y, int width, int height,
                                 float hue, float saturation, float value,
                                 Listener listener) {
        super(x, y, width, height, Component.empty());
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
        this.listener = listener;
    }

    public void setHue(float hue) {
        this.hue = Mth.clamp(hue, 0.0F, 1.0F);
    }

    public void setSaturationValue(float saturation, float value) {
        this.saturation = Mth.clamp(saturation, 0.0F, 1.0F);
        this.value = Mth.clamp(value, 0.0F, 1.0F);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ColorPickerRenderUtil.drawInsetPanel(graphics, this.getX(), this.getY(), this.width, this.height, 0xFF11161D, 0xFF2D3542);

        int innerX = this.getX() + 1;
        int innerY = this.getY() + 1;
        int innerW = this.width - 2;
        int innerH = this.height - 2;

        int baseColor = 0xFF000000 | (Mth.hsvToRgb(this.hue, 1.0F, 1.0F) & 0xFFFFFF);
        graphics.fill(innerX, innerY, innerX + innerW, innerY + innerH, baseColor);

        ColorPickerRenderUtil.drawHorizontalGradient(
                graphics.pose(),
                innerX,
                innerY,
                innerW,
                innerH,
                0xFFFFFFFF,
                0x00FFFFFF
        );

        ColorPickerRenderUtil.drawVerticalGradient(
                graphics.pose(),
                innerX,
                innerY,
                innerW,
                innerH,
                0x00000000,
                0xFF000000
        );

        int knobX = innerX + Math.round(this.saturation * (innerW - 1));
        int knobY = innerY + Math.round((1.0F - this.value) * (innerH - 1));

        graphics.fill(knobX - 2, knobY - 2, knobX + 3, knobY + 3, 0xFF000000);
        graphics.fill(knobX - 1, knobY - 1, knobX + 2, knobY + 2, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active || !this.visible || button != 0 || !this.isMouseOver(mouseX, mouseY)) {
            return false;
        }
        this.dragging = true;
        updateFromMouse(mouseX, mouseY);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!this.active || !this.visible || !this.dragging || button != 0) {
            return false;
        }
        updateFromMouse(mouseX, mouseY);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateFromMouse(double mouseX, double mouseY) {
        float localX = (float) ((mouseX - (this.getX() + 1)) / (double) (this.width - 2));
        float localY = (float) ((mouseY - (this.getY() + 1)) / (double) (this.height - 2));

        this.saturation = Mth.clamp(localX, 0.0F, 1.0F);
        this.value = 1.0F - Mth.clamp(localY, 0.0F, 1.0F);

        if (this.listener != null) {
            this.listener.onSaturationValueChanged(this.saturation, this.value);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public interface Listener {
        void onSaturationValueChanged(float saturation, float value);
    }
}