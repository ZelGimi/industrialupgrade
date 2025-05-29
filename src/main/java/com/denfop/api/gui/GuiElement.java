package com.denfop.api.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.gui.GuiCore;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiElement<T extends GuiElement<T>> {

    public static final ResourceLocation commonTexture = new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png");
    public static final ResourceLocation commonTexture1 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/gui_progressbars.png"
    );
    public static final ResourceLocation commonTexture2 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/gui_progressbars1.png"
    );
    public static final ResourceLocation commonTexture3 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/steam_progressbars.png"
    );
    public static final ResourceLocation commonTexture4 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/bio_progressbars.png"
    );

    public static final ResourceLocation commonTexture5 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/guispace_progress.png"
    );
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

    public static void addLines(List<String> list, String str) {
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
        GuiCore.bindTexture(texture);
    }

    public static void bindCommonTexture() {
        GuiCore.bindTexture(commonTexture);
    }

    public static void bindCommonTexture1() {
        GuiCore.bindTexture(commonTexture1);
    }

    public static void bindCommonTexture2() {
        GuiCore.bindTexture(commonTexture2);
    }

    public static void bindCommonTexture3() {
        GuiCore.bindTexture(commonTexture3);
    }

    public static void bindCommonTexture4() {
        GuiCore.bindTexture(commonTexture4);
    }

    public static void bindCommonTexture5() {
        GuiCore.bindTexture(commonTexture5);
    }

    public static void bindBlockTexture() {
        GuiCore.bindTexture(TextureAtlas.LOCATION_BLOCKS);
    }

    public static TextureAtlas getBlockTextureMap() {
        return (TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS);
    }


    public boolean visible() {
        return true;
    }

    public GuiCore<?> getGui() {
        return gui;
    }

    public void setGui(final GuiCore<?> gui) {
        this.gui = gui;
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
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

    public void setHeight(final int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
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

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY) {
    }

    public void drawForeground(GuiGraphics poseStack, int mouseX, int mouseY) {
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

    public boolean onMouseDrag(int mouseX, int mouseY, MouseButton button, long timeFromLastClick) {
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

    protected final Container getBase() {
        return this.gui.getContainer().base;
    }


    public void addY(int height) {
        this.y += height;
    }


}
