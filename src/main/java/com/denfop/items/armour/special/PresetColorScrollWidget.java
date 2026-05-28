package com.denfop.items.armour.special;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.IntConsumer;

public class PresetColorScrollWidget extends AbstractWidget {

    private static final int SWATCH = 16;
    private static final int GAP = 4;
    private static final int VISIBLE_ROWS = 2;

    private final List<Integer> colors;
    private final IntConsumer callback;

    private int selectedColor;
    private int scrollRow;

    public PresetColorScrollWidget(int x, int y, int width, int height,
                                   List<Integer> colors,
                                   int selectedColor,
                                   IntConsumer callback) {
        super(x, y, width, height, Component.empty());
        this.colors = colors;
        this.selectedColor = selectedColor & 0xFFFFFF;
        this.callback = callback;
    }

    public void setSelectedColor(int rgb) {
        this.selectedColor = rgb & 0xFFFFFF;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ColorPickerRenderUtil.drawInsetPanel(graphics, this.getX(), this.getY(), this.width, this.height, 0xFF11161D, 0xFF2D3542);

        int cols = Math.max(1, (this.width - 8) / (SWATCH + GAP));
        int totalRows = (int) Math.ceil(this.colors.size() / (double) cols);
        int maxScroll = Math.max(0, totalRows - VISIBLE_ROWS);
        this.scrollRow = Mth.clamp(this.scrollRow, 0, maxScroll);

        int startIndex = this.scrollRow * cols;
        int endIndex = Math.min(this.colors.size(), startIndex + cols * VISIBLE_ROWS);

        int startX = this.getX() + 4;
        int startY = this.getY() + 4;

        for (int index = startIndex; index < endIndex; index++) {
            int localIndex = index - startIndex;
            int col = localIndex % cols;
            int row = localIndex / cols;

            int x = startX + col * (SWATCH + GAP);
            int y = startY + row * (SWATCH + GAP);
            int rgb = this.colors.get(index) & 0xFFFFFF;

            ColorPickerRenderUtil.drawInsetPanel(graphics, x, y, SWATCH, SWATCH, 0xFF161B22, 0xFF2C3441);
            ColorPickerRenderUtil.drawCheckerboard(graphics, x + 1, y + 1, SWATCH - 2, SWATCH - 2);
            graphics.fill(x + 1, y + 1, x + SWATCH - 1, y + SWATCH - 1, 0xFF000000 | rgb);

            boolean hovered = mouseX >= x && mouseX < x + SWATCH && mouseY >= y && mouseY < y + SWATCH;
            if (hovered) {
                ColorPickerRenderUtil.drawBorder(graphics, x, y, SWATCH, SWATCH, 0xFFFFFFFF);
            } else if (rgb == this.selectedColor) {
                ColorPickerRenderUtil.drawBorder(graphics, x, y, SWATCH, SWATCH, 0xFF8DEBFF);
            }
        }

        if (maxScroll > 0) {
            int barX = this.getX() + this.width - 5;
            int barY = this.getY() + 4;
            int barH = this.height - 8;

            graphics.fill(barX, barY, barX + 2, barY + barH, 0xFF222935);

            int thumbH = Math.max(8, barH * VISIBLE_ROWS / totalRows);
            int thumbY = barY + Math.round((barH - thumbH) * (this.scrollRow / (float) maxScroll));
            graphics.fill(barX, thumbY, barX + 2, thumbY + thumbH, 0xFF8DEBFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active || !this.visible || button != 0 || !this.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        int cols = Math.max(1, (this.width - 8) / (SWATCH + GAP));
        int startIndex = this.scrollRow * cols;

        int localX = (int) mouseX - (this.getX() + 4);
        int localY = (int) mouseY - (this.getY() + 4);

        if (localX < 0 || localY < 0) {
            return false;
        }

        int col = localX / (SWATCH + GAP);
        int row = localY / (SWATCH + GAP);

        if (row < 0 || row >= VISIBLE_ROWS) {
            return false;
        }

        int inCellX = localX % (SWATCH + GAP);
        int inCellY = localY % (SWATCH + GAP);

        if (inCellX >= SWATCH || inCellY >= SWATCH) {
            return false;
        }

        int index = startIndex + row * cols + col;
        if (index < 0 || index >= this.colors.size()) {
            return false;
        }

        this.selectedColor = this.colors.get(index) & 0xFFFFFF;
        if (this.callback != null) {
            this.callback.accept(this.selectedColor);
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double delta) {
        if (!this.active || !this.visible || !this.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        int cols = Math.max(1, (this.width - 8) / (SWATCH + GAP));
        int totalRows = (int) Math.ceil(this.colors.size() / (double) cols);
        int maxScroll = Math.max(0, totalRows - VISIBLE_ROWS);

        if (maxScroll <= 0) {
            return false;
        }

        this.scrollRow = Mth.clamp(this.scrollRow - (int) Math.signum(delta), 0, maxScroll);
        return true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}