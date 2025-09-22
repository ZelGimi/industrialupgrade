package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TextBook extends GuiElement {

    private final String textProvider;
    private final Supplier<Integer> color;
    private final boolean shadow;
    private final boolean fixedHoverWidth;
    private final boolean fixedHoverHeight;
    private final int baseX;
    private final int baseY;
    private final boolean centerX;
    private final boolean centerY;

    private TextBook(
            GuiCore<?> gui,
            int x,
            int y,
            int width,
            int height,
            String textProvider,
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


    public static TextBook create(
            GuiCore<?> gui,
            int x,
            int y,
            String textProvider,
            int color,
            boolean shadow
    ) {
        return create(gui, x, y, textProvider, color, shadow, false, false);
    }


    public static TextBook create(
            GuiCore<?> gui,
            int x,
            int y,
            String textProvider,
            int color,
            boolean shadow,
            boolean centerX,
            boolean centerY
    ) {
        return create(gui, x, y, -1, -1, textProvider, color, shadow, centerX, centerY);
    }

    public static TextBook create(
            GuiCore<?> gui,
            int x,
            int y,
            int width,
            int height,
            String textProvider,
            int color,
            boolean shadow,
            boolean centerX,
            boolean centerY
    ) {
        return create(gui, x, y, width, height, textProvider, color, shadow, 0, 0, centerX, centerY);
    }

    public static TextBook create(
            GuiCore<?> gui,
            int x,
            int y,
            int width,
            int height,
            String textProvider,
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

    public static TextBook create(
            GuiCore<?> gui,
            int x,
            int y,
            int width,
            int height,
            String textProvider,
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

        return new TextBook(
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

    private static int getWidth(GuiCore<?> gui, String textProvider) {
        String text;
        text = textProvider;
        return text.isEmpty() ? 0 : Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

    public static List<String> splitEqually(String text) {
        final boolean isUkrOrrus = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode().equals(
                "ru_ru") || (Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode().equals(
                "uk_ua"));
        int size = isUkrOrrus ? 43 : 33;
        String[] ret = text.split(" ");
        List<String> ret1 = new ArrayList<>();
        StringBuilder k = new StringBuilder();
        for (String res : ret) {
            if ((k.length() + res.length() + 1) < size && !res.equals("\n")) {
                k.append(" ").append(res);

            } else {
                ret1.add(k.toString().trim());
                k = new StringBuilder();
                k.append(res);
            }
        }
        if (k.length() > 0) {
            ret1.add(k.toString().trim());
        }
        return ret1;
    }

    public Supplier<Integer> getColor() {
        return color;
    }

    public boolean isShadow() {
        return shadow;
    }

    public String getText() {
        return this.textProvider;
    }

    public void drawBackground(int mouseX, int mouseY) {
        String text = this.textProvider;
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
        final List<String> textList = splitEqually(text);
        int y = 0;
        for (String text1 : textList) {
            if (!text1.isEmpty()) {

                drawText(text1, textX, textY + y, 0.75);
                y += 9;
            }
        }

    }

    private int calculateX(int originalX, String text, double scale) {

        return originalX + getGui().width / 8;
    }

    private int calculateY(int originalY, String text, double scale) {
        return originalY + getGui().height / 16;
    }

    public void drawText(String text, float posX, float posY, double scale) {
        GL11.glPushMatrix();


        GL11.glScaled(scale, scale, scale);
        this.gui.drawString(calculateX((int) posX, text, scale), calculateY((int) posY, text, scale), text, this.color.get(),
                this.shadow
        );

        GL11.glPopMatrix();
    }

    public enum TextAlignment {
        Start,
        Center,
        End;

        private static final Map<String, TextBook.TextAlignment> map = getMap();
        public final String name;

        TextAlignment() {
            this.name = this.name().toLowerCase(Locale.ENGLISH);
        }

        public static TextBook.TextAlignment get(String name) {
            return map.get(name);
        }

        private static Map<String, TextBook.TextAlignment> getMap() {
            TextBook.TextAlignment[] values = values();
            Map<String, TextBook.TextAlignment> ret = new HashMap(values.length);
            TextBook.TextAlignment[] var2 = values;
            int var3 = values.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                TextBook.TextAlignment style = var2[var4];
                ret.put(style.name, style);
            }

            return ret;
        }
    }

}
