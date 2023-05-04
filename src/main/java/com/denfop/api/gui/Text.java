package com.denfop.api.gui;

import com.denfop.gui.GuiIC2;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Text extends GuiElement<Text> {

    private final TextProvider.ITextProvider textProvider;
    private final Supplier<Integer> color;
    private final boolean shadow;
    private final boolean fixedHoverWidth;
    private final boolean fixedHoverHeight;
    private final int baseX;
    private final int baseY;
    private final boolean centerX;
    private final boolean centerY;

    private Text(
            GuiIC2<?> gui,
            int x,
            int y,
            int width,
            int height,
            TextProvider.ITextProvider textProvider,
            Supplier<Integer> color,
            boolean shadow,
            boolean fixedHoverWidth,
            boolean fixedHoverHeight,
            int baseX,
            int baseY,
            boolean centerX,
            boolean centerY
    ) {
        super(gui, x, y, width, height);
        this.textProvider = textProvider;
        this.color = color;
        this.shadow = shadow;
        this.fixedHoverWidth = fixedHoverWidth;
        this.fixedHoverHeight = fixedHoverHeight;
        this.baseX = baseX;
        this.baseY = baseY;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public static Text create(GuiIC2<?> gui, int x, int y, String text, int color, boolean shadow) {
        return create(gui, x, y, TextProvider.of(text), color, shadow);
    }

    public static Text create(GuiIC2<?> gui, int x, int y, TextProvider.ITextProvider textProvider, int color, boolean shadow) {
        return create(gui, x, y, textProvider, color, shadow, false, false);
    }

    public static Text create(
            GuiIC2<?> gui,
            int x,
            int y,
            String text,
            int color,
            boolean shadow,
            boolean centerX,
            boolean centerY
    ) {
        return create(gui, x, y, TextProvider.of(text), color, shadow, centerX, centerY);
    }

    public static Text create(
            GuiIC2<?> gui,
            int x,
            int y,
            TextProvider.ITextProvider textProvider,
            int color,
            boolean shadow,
            boolean centerX,
            boolean centerY
    ) {
        return create(gui, x, y, -1, -1, textProvider, color, shadow, centerX, centerY);
    }

    public static Text create(
            GuiIC2<?> gui,
            int x,
            int y,
            int width,
            int height,
            TextProvider.ITextProvider textProvider,
            int color,
            boolean shadow,
            boolean centerX,
            boolean centerY
    ) {
        return create(gui, x, y, width, height, textProvider, color, shadow, 0, 0, centerX, centerY);
    }

    public static Text create(
            GuiIC2<?> gui,
            int x,
            int y,
            int width,
            int height,
            TextProvider.ITextProvider textProvider,
            int color,
            boolean shadow,
            int xOffset,
            int yOffset,
            boolean centerX,
            boolean centerY
    ) {
        return create(
                gui,
                x,
                y,
                width,
                height,
                textProvider,
                Suppliers.ofInstance(color),
                shadow,
                xOffset,
                yOffset,
                centerX,
                centerY
        );
    }

    public static Text createRightAligned(
            GuiIC2<?> gui,
            int x,
            int y,
            int width,
            int height,
            TextProvider.ITextProvider textProvider,
            int color,
            boolean shadow,
            int xOffset,
            int yOffset,
            boolean centerX,
            boolean centerY
    ) {
        return create(
                gui,
                x,
                y,
                width,
                height,
                textProvider,
                Suppliers.ofInstance(color),
                shadow,
                xOffset - getWidth(gui, textProvider),
                yOffset,
                centerX,
                centerY
        );
    }

    public static Text create(
            GuiIC2<?> gui,
            int x,
            int y,
            int width,
            int height,
            TextProvider.ITextProvider textProvider,
            Supplier<Integer> color,
            boolean shadow,
            int xOffset,
            int yOffset,
            boolean centerX,
            boolean centerY
    ) {
        boolean fixedHoverWidth;
        if (width < 0) {
            fixedHoverWidth = false;
            width = getWidth(gui, textProvider);
        } else {
            fixedHoverWidth = true;
        }

        boolean fixedHoverHeight;
        if (height < 0) {
            fixedHoverHeight = false;
            height = 8;
        } else {
            fixedHoverHeight = true;
        }

        int baseX = x + xOffset;
        int baseY = y + yOffset;
        if (centerX) {
            if (fixedHoverWidth) {
                baseX += width / 2;
            } else {
                x -= width / 2;
            }
        }

        if (centerY) {
            if (fixedHoverHeight) {
                baseY += (height + 1) / 2;
            } else {
                y -= height / 2;
            }
        }

        return new Text(
                gui,
                x,
                y,
                width,
                height,
                textProvider,
                color,
                shadow,
                fixedHoverWidth,
                fixedHoverHeight,
                baseX,
                baseY,
                centerX,
                centerY
        );
    }

    private static int getWidth(GuiIC2<?> gui, TextProvider.ITextProvider textProvider) {
        String text = textProvider.get(gui.getContainer().base, TextProvider.emptyTokens());
        return text.isEmpty() ? 0 : Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

    public void drawBackground(int mouseX, int mouseY) {
        String text = this.textProvider.get(this.getBase(), this.getTokens());
        int textWidth;
        byte textHeight;
        if (text.isEmpty()) {
            textHeight = 0;
            textWidth = 0;
        } else {
            textWidth = this.gui.getStringWidth(text);
            textHeight = 8;
        }

        int textX = this.baseX;
        if (this.centerX) {
            textX -= textWidth / 2;
        }

        int textY = this.baseY;
        if (this.centerY) {
            textY -= textHeight / 2;
        }

        if (!this.fixedHoverWidth) {
            this.x = textX;
            this.width = textWidth;
        }

        if (!this.fixedHoverHeight) {
            this.y = textY;
            this.height = textHeight;
        }

        super.drawBackground(mouseX, mouseY);
        if (!text.isEmpty()) {
            this.gui.drawString(textX, textY, text, this.color.get(), this.shadow);
        }

    }

    public enum TextAlignment {
        Start,
        Center,
        End;

        private static final Map<String, Text.TextAlignment> map = getMap();
        public final String name;

        TextAlignment() {
            this.name = this.name().toLowerCase(Locale.ENGLISH);
        }

        public static Text.TextAlignment get(String name) {
            return map.get(name);
        }

        private static Map<String, Text.TextAlignment> getMap() {
            Text.TextAlignment[] values = values();
            Map<String, Text.TextAlignment> ret = new HashMap(values.length);
            Text.TextAlignment[] var2 = values;
            int var3 = values.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Text.TextAlignment style = var2[var4];
                ret.put(style.name, style);
            }

            return ret;
        }
    }

}
