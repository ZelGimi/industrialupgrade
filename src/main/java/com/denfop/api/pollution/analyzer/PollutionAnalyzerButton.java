package com.denfop.api.pollution.analyzer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class PollutionAnalyzerButton extends AbstractWidget {

    private final PressAction pressAction;
    private final int accentColor;
    private boolean selected;
    public PollutionAnalyzerButton(
            int x,
            int y,
            int width,
            int height,
            Component message,
            int accentColor,
            PressAction pressAction
    ) {
        super(x, y, width, height, message);
        this.pressAction = pressAction;
        this.accentColor = accentColor;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.active && this.visible && this.pressAction != null) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            this.pressAction.onPress(this);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hovered = this.isHoveredOrFocused();

        int outer = selected ? 0xFF6A88B9 : hovered ? 0xFF4E658C : 0xFF2A374C;
        int innerTop = selected ? 0xFF1E2A3B : hovered ? 0xFF182231 : 0xFF121A28;
        int innerBottom = selected ? 0xFF131D2B : hovered ? 0xFF101825 : 0xFF0D131E;
        int textColor = this.active ? 0xFFF1F7FF : 0xFF7E8A9A;

        graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, outer);
        graphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1, this.getY() + this.height - 1, innerBottom);
        graphics.fill(this.getX() + 2, this.getY() + 2, this.getX() + this.width - 2, this.getY() + this.height / 2, innerTop);

        graphics.fill(this.getX() + 3, this.getY() + this.height - 5, this.getX() + this.width - 3, this.getY() + this.height - 2,
                0xFF000000 | (accentColor & 0xFFFFFF));

        if (selected) {
            graphics.fill(this.getX() + 2, this.getY() + 2, this.getX() + this.width - 2, this.getY() + 4, 0x60FFFFFF);
        } else if (hovered) {
            graphics.fill(this.getX() + 2, this.getY() + 2, this.getX() + this.width - 2, this.getY() + 4, 0x30FFFFFF);
        }

        graphics.drawCenteredString(
                Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + this.width / 2,
                this.getY() + (this.height - 8) / 2,
                textColor
        );
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    public interface PressAction {
        void onPress(PollutionAnalyzerButton button);
    }
}