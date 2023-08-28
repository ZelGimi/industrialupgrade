package com.denfop.api.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.gui.GuiCore;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GuiElement<T extends GuiElement<T>> {

    public static final ResourceLocation commonTexture = new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png");
    public static final ResourceLocation commonTexture1 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/gui_progressbars.png"
    );

    protected static final int hoverColor = -2130706433;
    private static final Map<Class<?>, GuiElement.Subscriptions> SUBSCRIPTIONS = new HashMap();
    protected GuiCore<?> gui;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    private Supplier<String> tooltipProvider;

    protected GuiElement(GuiCore<?> gui, int x, int y, int width, int height) {
        if (width < 0) {
            throw new IllegalArgumentException("negative width");
        } else if (height < 0) {
            throw new IllegalArgumentException("negative height");
        } else {
            this.gui = gui;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    static void addLines(List<String> list, String str) {
        int startPos;
        int pos;
        for (startPos = 0; (pos = str.indexOf(10, startPos)) != -1; startPos = pos + 1) {
            list.add(processText(str.substring(startPos, pos)));
        }

        if (startPos == 0) {
            list.add(processText(str));
        } else {
            list.add(processText(str.substring(startPos)));
        }

    }

    protected static String processText(String text) {
        return Localization.translate(text);
    }

    protected static void bindTexture(ResourceLocation texture) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

    public static void bindCommonTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(commonTexture);
    }

    public static void bindCommonTexture1() {
        Minecraft.getMinecraft().renderEngine.bindTexture(commonTexture1);
    }

    protected static void bindBlockTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    protected static TextureMap getBlockTextureMap() {
        return Minecraft.getMinecraft().getTextureMapBlocks();
    }

    private static GuiElement.Method hasMethod(Class<?> cls, String name, Class<?>... params) {
        try {
            return !cls.getDeclaredMethod(name, params).isAnnotationPresent(GuiElement.SkippedMethod.class)
                    ? GuiElement.Method.PRESENT
                    : GuiElement.Method.SKIPPED;
        } catch (NoSuchMethodException var4) {
            return GuiElement.Method.MISSING;
        }
    }

    public GuiCore<?> getGui() {
        return gui;
    }

    public void setGui(final GuiCore<?> gui) {
        this.gui = gui;
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }


    public T withTooltip(String tooltip) {
        return this.withTooltip(Suppliers.ofInstance(tooltip));
    }

    public T withTooltip(Supplier<String> tooltipProvider) {
        this.tooltipProvider = tooltipProvider;
        return (T) this;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public Supplier<String> getTooltipProvider() {
        return tooltipProvider;
    }

    public void tick() {
    }

    public void drawBackground(int mouseX, int mouseY) {
    }

    public void drawForeground(int mouseX, int mouseY) {
        if (this.contains(mouseX, mouseY) && !this.suppressTooltip(mouseX, mouseY)) {
            List<String> lines = this.getToolTip();
            if (this.tooltipProvider != null) {
                String tooltip = this.tooltipProvider.get();
                if (tooltip != null && !tooltip.isEmpty()) {
                    addLines(lines, tooltip);
                }
            }

            if (!lines.isEmpty()) {
                this.gui.drawTooltip(mouseX, mouseY, lines);
            }
        }

    }

    public boolean onMouseClick(int mouseX, int mouseY, MouseButton button, boolean onThis) {
        return onThis && this.onMouseClick(mouseX, mouseY, button);
    }

    protected boolean onMouseClick(int mouseX, int mouseY, MouseButton button) {
        return false;
    }

    public boolean onMouseDrag(int mouseX, int mouseY, MouseButton button, long timeFromLastClick, boolean onThis) {
        return onThis && this.onMouseDrag(mouseX, mouseY, button, timeFromLastClick);
    }

    protected boolean onMouseDrag(int mouseX, int mouseY, MouseButton button, long timeFromLastClick) {
        return false;
    }

    public boolean onMouseRelease(int mouseX, int mouseY, MouseButton button, boolean onThis) {
        return onThis && this.onMouseRelease(mouseX, mouseY, button);
    }

    protected boolean onMouseRelease(int mouseX, int mouseY, MouseButton button) {
        return false;
    }

    public void onMouseScroll(int mouseX, int mouseY, ScrollDirection direction) {
    }

    public boolean onKeyTyped(char typedChar, int keyCode) {
        return false;
    }

    protected boolean suppressTooltip(int mouseX, int mouseY) {
        return false;
    }

    protected List<String> getToolTip() {
        return new ArrayList<>();
    }

    protected final IInventory getBase() {
        return this.gui.getContainer().base;
    }

    public final GuiElement.Subscriptions getSubscriptions() {
        Class<?> cls = this.getClass();
        GuiElement.Subscriptions subscriptions = SUBSCRIPTIONS.get(cls);
        if (subscriptions == null) {
            GuiElement.Method tick = GuiElement.Method.MISSING;
            GuiElement.Method background = GuiElement.Method.MISSING;
            GuiElement.Method mouseClick = GuiElement.Method.MISSING;
            GuiElement.Method mouseDrag = GuiElement.Method.MISSING;
            GuiElement.Method mouseRelease = GuiElement.Method.MISSING;
            GuiElement.Method mouseScroll = GuiElement.Method.MISSING;

            GuiElement.Method key;
            for (key = GuiElement.Method.MISSING; cls != GuiElement.class && (!tick.hasSeen() || !background.hasSeen() || !mouseClick.hasSeen() || !mouseDrag.hasSeen() || !mouseRelease.hasSeen() || !mouseScroll.hasSeen() || !key.hasSeen()); cls = cls.getSuperclass()) {
                if (!tick.hasSeen()) {
                    tick = hasMethod(cls, "tick");
                }

                if (!background.hasSeen()) {
                    background = hasMethod(cls, "drawBackground", Integer.TYPE, Integer.TYPE);
                }

                if (!mouseClick.hasSeen()) {
                    mouseClick = hasMethod(cls, "onMouseClick", Integer.TYPE, Integer.TYPE, MouseButton.class);
                }

                if (!mouseClick.hasSeen()) {
                    mouseClick = hasMethod(cls, "onMouseClick", Integer.TYPE, Integer.TYPE, MouseButton.class, Boolean.TYPE);
                }

                if (!mouseDrag.hasSeen()) {
                    mouseDrag = hasMethod(cls, "onMouseDrag", Integer.TYPE, Integer.TYPE, MouseButton.class, Long.TYPE);
                }

                if (!mouseDrag.hasSeen()) {
                    mouseDrag = hasMethod(
                            cls,
                            "onMouseDrag",
                            Integer.TYPE,
                            Integer.TYPE,
                            MouseButton.class,
                            Long.TYPE,
                            Boolean.TYPE
                    );
                }

                if (!mouseRelease.hasSeen()) {
                    mouseRelease = hasMethod(cls, "onMouseRelease", Integer.TYPE, Integer.TYPE, MouseButton.class);
                }

                if (!mouseRelease.hasSeen()) {
                    mouseRelease = hasMethod(cls, "onMouseRelease", Integer.TYPE, Integer.TYPE, MouseButton.class, Boolean.TYPE);
                }

                if (!mouseScroll.hasSeen()) {
                    mouseScroll = hasMethod(cls, "onMouseScroll", Integer.TYPE, Integer.TYPE, ScrollDirection.class);
                }

                if (!key.hasSeen()) {
                    key = hasMethod(cls, "onKeyTyped", Character.TYPE, Integer.TYPE);
                }
            }

            subscriptions = new GuiElement.Subscriptions(
                    tick.isPresent(),
                    background.isPresent(),
                    mouseClick.isPresent(),
                    mouseDrag.isPresent(),
                    mouseRelease.isPresent(),
                    mouseScroll.isPresent(),
                    key.isPresent()
            );
            SUBSCRIPTIONS.put(this.getClass(), subscriptions);
        }

        return subscriptions;
    }

    public void addY(int height) {
        this.y += height;
    }

    private enum Method {
        PRESENT,
        SKIPPED,
        MISSING;

        Method() {
        }

        boolean hasSeen() {
            return this != MISSING;
        }

        boolean isPresent() {
            return this == PRESENT;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    protected @interface SkippedMethod {

    }

    public static final class Subscriptions {

        public final boolean tick;
        public final boolean background;
        public final boolean mouseClick;
        public final boolean mouseDrag;
        public final boolean mouseRelease;
        public final boolean mouseScroll;
        public final boolean key;

        Subscriptions(
                boolean tick,
                boolean background,
                boolean mouseClick,
                boolean mouseDrag,
                boolean mouseRelease,
                boolean mouseScroll,
                boolean key
        ) {
            this.tick = tick;
            this.background = background;
            this.mouseClick = mouseClick;
            this.mouseDrag = mouseDrag;
            this.mouseRelease = mouseRelease;
            this.mouseScroll = mouseScroll;
            this.key = key;
        }

        public String toString() {
            return String.format(
                    "tick: %s, background: %s, mouseClick: %s, mouseDrag: %s, mouseRelease: %s, mouseScroll: %s, key: %s",
                    this.tick,
                    this.background,
                    this.mouseClick,
                    this.mouseDrag,
                    this.mouseRelease,
                    this.mouseScroll,
                    this.key
            );
        }

    }

}
