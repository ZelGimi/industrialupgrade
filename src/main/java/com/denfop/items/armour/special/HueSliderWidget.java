package com.denfop.items.armour.special;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class HueSliderWidget extends AbstractWidget {

    private final Listener listener;
    private float hue;
    private boolean dragging;
    public HueSliderWidget(int x, int y, int width, int height, float hue, Listener listener) {
        super(x, y, width, height, Component.empty());
        this.hue = hue;
        this.listener = listener;
    }

    public void setHue(float hue) {
        this.hue = Mth.clamp(hue, 0.0F, 1.0F);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ColorPickerRenderUtil.drawInsetPanel(graphics, this.getX(), this.getY(), this.width, this.height, 0xFF11161D, 0xFF2D3542);

        int innerX = this.getX() + 1;
        int innerY = this.getY() + 1;
        int innerW = this.width - 2;
        int innerH = this.height - 2;

        for (int i = 0; i < innerH; i++) {
            float t = i / (float) Math.max(1, innerH - 1);
            int color = 0xFF000000 | (Mth.hsvToRgb(t, 1.0F, 1.0F) & 0xFFFFFF);
            graphics.fill(innerX, innerY + i, innerX + innerW, innerY + i + 1, color);
        }

        int knobY = innerY + Math.round(this.hue * (innerH - 1));
        graphics.fill(this.getX() - 1, knobY - 1, this.getX() + this.width + 1, knobY + 2, 0xFF000000);
        graphics.fill(this.getX(), knobY, this.getX() + this.width, knobY + 1, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active || !this.visible || button != 0 || !this.isMouseOver(mouseX, mouseY)) {
            return false;
        }
        this.dragging = true;
        updateFromMouse(mouseY);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!this.active || !this.visible || !this.dragging || button != 0) {
            return false;
        }
        updateFromMouse(mouseY);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateFromMouse(double mouseY) {
        float localY = (float) ((mouseY - (this.getY() + 1)) / (double) (this.height - 2));
        this.hue = Mth.clamp(localY, 0.0F, 1.0F);
        if (this.listener != null) {
            this.listener.onHueChanged(this.hue);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public interface Listener {
        void onHueChanged(float hue);
    }
}