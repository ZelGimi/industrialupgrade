package com.denfop.gui;

import com.denfop.IUCore;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.IClickHandler;
import com.denfop.api.gui.IKeyboardDependent;
import com.denfop.api.gui.MouseButton;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.container.ContainerBase;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.api.upgrade.UpgradeRegistry;
import ic2.core.init.Localization;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public abstract class GuiIC2<T extends ContainerBase<? extends IInventory>> extends GuiContainer {

    public static final int textHeight = 8;
    protected final T container;
    protected final List<GuiElement<?>> elements;
    private final Queue<GuiIC2.Tooltip> queuedTooltips;
    private boolean fixKeyEvents;
    private boolean tick;
    private boolean background;
    private boolean mouseClick;
    private boolean mouseDrag;
    private boolean mouseRelease;
    private boolean mouseScroll;
    private boolean key;

    public GuiIC2(T container) {
        this(container, 176, 166);
    }

    public GuiIC2(T container, int ySize) {
        this(container, 176, ySize);
    }

    public GuiIC2(T container, int xSize, int ySize) {
        super(container);
        this.fixKeyEvents = false;
        this.tick = false;
        this.background = false;
        this.mouseClick = false;
        this.mouseDrag = false;
        this.mouseRelease = false;
        this.mouseScroll = false;
        this.key = false;
        this.queuedTooltips = new ArrayDeque<>();
        this.elements = new ArrayList<>();
        this.container = container;
        this.ySize = ySize;
        this.xSize = xSize;
    }

    private static List<ItemStack> getCompatibleUpgrades(IUpgradableBlock block) {
        List<ItemStack> ret = new ArrayList<>();
        Set<UpgradableProperty> properties = block.getUpgradableProperties();

        for (final ItemStack stack : UpgradeRegistry.getUpgrades()) {
            IUpgradeItem item = (IUpgradeItem) stack.getItem();
            if (item.isSuitableFor(stack, properties)) {
                ret.add(stack);
            }
        }

        return ret;
    }

    public T getContainer() {
        return this.container;
    }

    public void initGui() {
        super.initGui();

        for (final GuiElement<?> guiElement : this.elements) {
            if (guiElement instanceof IKeyboardDependent) {
                Keyboard.enableRepeatEvents(true);
                this.fixKeyEvents = true;
                break;
            }
        }

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    public void updateScreen() {
        super.updateScreen();
        if (this.tick) {

            for (final GuiElement<?> guiElement : this.elements) {
                if (guiElement.isEnabled()) {
                    guiElement.tick();
                }
            }
        }

    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        if (this.container.base instanceof IUpgradableBlock) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }

        if (this.background) {

            for (final GuiElement<?> element : this.elements) {
                if (element.isEnabled()) {
                    element.drawBackground(mouseX, mouseY);
                }
            }
        }

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2, 6, name, 4210752, false);
    }

    protected final void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.drawForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
        this.flushTooltips();
    }

    protected void drawForegroundLayer(int mouseX, int mouseY) {
        if (this.container.base instanceof IUpgradableBlock) {
            this.handleUpgradeTooltip(mouseX, mouseY);
        }

        for (final GuiElement<?> element : this.elements) {
            if (element.isEnabled()) {
                element.drawForeground(mouseX, mouseY);
            }
        }

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("ic2.generic.text.upgrade"));

            for (final ItemStack stack : getCompatibleUpgrades((IUpgradableBlock) this.container.base)) {
                text.add(stack.getDisplayName());
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.mouseScroll) {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth - this.guiLeft;
            int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1 - this.guiTop;
            int scrollDelta = Mouse.getEventDWheel();
            ScrollDirection direction;
            if (scrollDelta != 0) {
                direction = scrollDelta < 0 ? ScrollDirection.down : ScrollDirection.up;
            } else {
                direction = ScrollDirection.stopped;
            }

            for (final GuiElement<?> element : this.elements) {
                if (element.isEnabled() && element.contains(mouseX, mouseY)) {
                    element.onMouseScroll(mouseX, mouseY, direction);
                }
            }
        }

    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean handled = false;
        if (this.mouseClick) {
            MouseButton button = MouseButton.get(mouseButton);
            if (button != null) {
                mouseX -= this.guiLeft;
                mouseY -= this.guiTop;

                for (final GuiElement<?> element : this.elements) {
                    if (element.isEnabled()) {
                        handled |= element.onMouseClick(mouseX, mouseY, button, element.contains(mouseX, mouseY));
                    }
                }

                if (!handled) {
                    mouseX += this.guiLeft;
                    mouseY += this.guiTop;
                } else {
                    this.mouseHandled = true;
                }
            }
        }

        if (!handled) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }

    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        boolean handled = false;
        if (this.mouseDrag) {
            MouseButton button = MouseButton.get(clickedMouseButton);
            if (button != null) {
                mouseX -= this.guiLeft;
                mouseY -= this.guiTop;

                for (final GuiElement<?> element : this.elements) {
                    if (element.isEnabled()) {
                        handled |= element.onMouseDrag(
                                mouseX,
                                mouseY,
                                button,
                                timeSinceLastClick,
                                element.contains(mouseX, mouseY)
                        );
                    }
                }

                if (!handled) {
                    mouseX += this.guiLeft;
                    mouseY += this.guiTop;
                } else {
                    this.mouseHandled = true;
                }
            }
        }

        if (!handled) {
            super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }

    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        boolean handled = false;
        if (this.mouseRelease) {
            MouseButton button = MouseButton.get(state);
            if (button != null) {
                mouseX -= this.guiLeft;
                mouseY -= this.guiTop;

                for (final GuiElement<?> element : this.elements) {
                    if (element.isEnabled()) {
                        handled |= element.onMouseRelease(mouseX, mouseY, button, element.contains(mouseX, mouseY));
                    }
                }

                if (!handled) {
                    mouseX += this.guiLeft;
                    mouseY += this.guiTop;
                } else {
                    this.mouseHandled = true;
                }
            }
        }

        if (!handled) {
            super.mouseReleased(mouseX, mouseY, state);
        }

    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        boolean handled = false;
        if (this.key) {

            for (final GuiElement<?> element : this.elements) {
                if (element.isEnabled()) {
                    handled |= element.onKeyTyped(typedChar, keyCode);
                }
            }

            this.keyHandled = handled;
        }

        if (!handled) {
            super.keyTyped(typedChar, keyCode);
        }

    }

    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.fixKeyEvents) {
            Keyboard.enableRepeatEvents(false);
        }

    }

    public void drawTexturedRect(double x, double y, double width, double height, double texX, double texY) {
        this.drawTexturedRect(x, y, width, height, texX, texY, false);
    }

    public void drawTexturedRect(double x, double y, double width, double height, double texX, double texY, boolean mirrorX) {
        this.drawTexturedRect(
                x,
                y,
                width,
                height,
                texX / 256.0D,
                texY / 256.0D,
                (texX + width) / 256.0D,
                (texY + height) / 256.0D,
                mirrorX
        );
    }

    public void drawTexturedRect(
            double x,
            double y,
            double width,
            double height,
            double uS,
            double vS,
            double uE,
            double vE,
            boolean mirrorX
    ) {
        x += this.guiLeft;
        y += this.guiTop;
        double xE = x + width;
        double yE = y + height;
        if (mirrorX) {
            double tmp = uS;
            uS = uE;
            uE = tmp;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y, this.zLevel).tex(uS, vS).endVertex();
        worldrenderer.pos(x, yE, this.zLevel).tex(uS, vE).endVertex();
        worldrenderer.pos(xE, yE, this.zLevel).tex(uE, vE).endVertex();
        worldrenderer.pos(xE, y, this.zLevel).tex(uE, vS).endVertex();
        tessellator.draw();
    }

    public void drawSprite(
            double x,
            double y,
            double width,
            double height,
            TextureAtlasSprite sprite,
            int color,
            double scale,
            boolean fixRight,
            boolean fixBottom
    ) {
        if (sprite == null) {
            sprite = this.mc.getTextureMapBlocks().getMissingSprite();
        }

        x += this.guiLeft;
        y += this.guiTop;
        scale *= 16.0D;
        double spriteUS = sprite.getMinU();
        double spriteVS = sprite.getMinV();
        double spriteWidth = (double) sprite.getMaxU() - spriteUS;
        double spriteHeight = (double) sprite.getMaxV() - spriteVS;
        int a = color >>> 24 & 255;
        int r = color >>> 16 & 255;
        int g = color >>> 8 & 255;
        int b = color & 255;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

        double maxWidth;
        for (double xS = x; xS < x + width; xS += maxWidth) {
            double uS;
            if (xS == x && fixRight && (maxWidth = width % scale) > 0.0D) {
                uS = spriteUS + spriteWidth * (1.0D - maxWidth / scale);
            } else {
                maxWidth = scale;
                uS = spriteUS;
            }

            double xE = Math.min(xS + maxWidth, x + width);
            double uE = uS + (xE - xS) / scale * spriteWidth;

            double maxHeight;
            for (double yS = y; yS < y + height; yS += maxHeight) {
                double vS;
                if (yS == y && fixBottom && (maxHeight = height % scale) > 0.0D) {
                    vS = spriteVS + spriteHeight * (1.0D - maxHeight / scale);
                } else {
                    maxHeight = scale;
                    vS = spriteVS;
                }

                double yE = Math.min(yS + maxHeight, y + height);
                double vE = vS + (yE - yS) / scale * spriteHeight;
                buffer.pos(xS, yS, this.zLevel).tex(uS, vS).color(r, g, b, a).endVertex();
                buffer.pos(xS, yE, this.zLevel).tex(uS, vE).color(r, g, b, a).endVertex();
                buffer.pos(xE, yE, this.zLevel).tex(uE, vE).color(r, g, b, a).endVertex();
                buffer.pos(xE, yS, this.zLevel).tex(uE, vS).color(r, g, b, a).endVertex();
            }
        }

        tessellator.draw();
    }

    public void drawItem(int x, int y, ItemStack stack) {
        this.itemRender.renderItemIntoGUI(stack, this.guiLeft + x, this.guiTop + y);
    }

    public void drawItemStack(int x, int y, ItemStack stack) {
        this.drawItem(x, y, stack);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, this.guiLeft + x, this.guiTop + y, null);
    }

    public void drawColoredRect(int x, int y, int width, int height, int color) {
        x += this.guiLeft;
        y += this.guiTop;
        drawRect(x, y, x + width, y + height, color);
    }

    public int drawString(int x, int y, String text, int color, boolean shadow) {
        return this.fontRenderer.drawString(
                text,
                (float) (this.guiLeft + x),
                (float) (this.guiTop + y),
                color,
                shadow
        ) - this.guiLeft;
    }

    public void drawXCenteredString(int x, int y, String text, int color, boolean shadow) {
        this.drawCenteredString(x, y, text, color, shadow, true, false);
    }

    public void drawXYCenteredString(int x, int y, String text, int color, boolean shadow) {
        this.drawCenteredString(x, y, text, color, shadow, true, true);
    }

    public void drawCenteredString(int x, int y, String text, int color, boolean shadow, boolean centerX, boolean centerY) {
        if (centerX) {
            x -= this.getStringWidth(text) / 2;
        }

        if (centerY) {
            y -= 4;
        }

        this.fontRenderer.drawString(text, this.guiLeft + x, this.guiTop + y, color);
    }

    public int getStringWidth(String text) {
        return this.fontRenderer.getStringWidth(text);
    }

    public String trimStringToWidth(String text, int width) {
        return this.fontRenderer.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidthReverse(String text, int width) {
        return this.fontRenderer.trimStringToWidth(text, width, true);
    }

    public void drawTooltip(int x, int y, List<String> text) {
        this.queuedTooltips.add(new GuiIC2.Tooltip(text, x, y));
    }

    public void drawTooltip(int x, int y, ItemStack stack) {
        assert !StackUtil.isEmpty(stack);

        this.renderToolTip(stack, x, y);
    }

    protected void flushTooltips() {

        for (final Tooltip tooltip : this.queuedTooltips) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawHoveringText(tooltip.text, tooltip.x, tooltip.y);
            GlStateManager.disableLighting();
        }

        this.queuedTooltips.clear();
    }

    protected void addElement(GuiElement<?> element) {
        this.elements.add(element);
        if (!this.tick || !this.background || !this.mouseClick || !this.mouseDrag || !this.mouseRelease || !this.mouseScroll || !this.key) {
            GuiElement.Subscriptions subs = element.getSubscriptions();
            if (!this.tick) {
                this.tick = subs.tick;
            }

            if (!this.background) {
                this.background = subs.background;
            }

            if (!this.mouseClick) {
                this.mouseClick = subs.mouseClick;
            }

            if (!this.mouseDrag) {
                this.mouseDrag = subs.mouseDrag;
            }

            if (!this.mouseRelease) {
                this.mouseRelease = subs.mouseRelease;
            }

            if (!this.mouseScroll) {
                this.mouseScroll = subs.mouseScroll;
            }

            if (!this.key) {
                this.key = subs.key;
            }
        }

    }

    protected final void bindTexture() {
        this.mc.getTextureManager().bindTexture(this.getTexture());
    }

    protected IClickHandler createEventSender(final int event) {
        if (this.container.base instanceof TileEntity) {
            return button -> IUCore.network.get(false).initiateClientTileEntityEvent((TileEntity) GuiIC2.this.container.base
                    , event);
        } else {
            throw new IllegalArgumentException("not applicable for " + this.container.base);
        }
    }

    protected abstract ResourceLocation getTexture();

    private static class Tooltip {

        final int x;
        final int y;
        final List<String> text;

        Tooltip(List<String> text, int x, int y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }

    }

}
