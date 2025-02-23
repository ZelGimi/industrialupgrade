package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.GuiVerticalSliderList;
import com.denfop.api.gui.MouseButton;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.upgrades.UpgradeRegistry;
import com.denfop.container.ContainerBase;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public abstract class GuiCore<T extends ContainerBase<? extends IInventory>> extends GuiContainer {

    public final T container;
    protected final List<GuiElement<?>> elements;
    protected final Queue<GuiCore.Tooltip> queuedTooltips;

    public GuiCore(T container) {
        this(container, 176, 166);
    }

    public GuiCore(T container, int ySize) {
        this(container, 176, ySize);
    }

    public GuiCore(T container, int xSize, int ySize) {
        super(container);
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

    }

    protected void drawSlot(Slot slotIn) {
        int i = slotIn.xPos;
        int j = slotIn.yPos;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
        ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
        String s = null;

        if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        } else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty()) {
            if (this.dragSplittingSlots.size() == 1) {
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(
                        this.dragSplittingSlots,
                        this.dragSplittingLimit,
                        itemstack,
                        slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount()
                );
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

                if (itemstack.getCount() > k) {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            } else {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }

        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        if (itemstack.isEmpty() && slotIn.isEnabled()) {
            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

            if (textureatlassprite != null) {
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }

        if (!flag1) {
            if (flag) {
                drawRect(i, j, i + 16, j + 16, -2130706433);
            }

            GlStateManager.enableDepth();
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
        }

        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    protected void updateDragSplitting() {
        ItemStack itemstack = this.mc.player.inventory.getItemStack();

        if (!itemstack.isEmpty() && this.dragSplitting) {
            if (this.dragSplittingLimit == 2) {
                this.dragSplittingRemnant = itemstack.getMaxStackSize();
            } else {
                this.dragSplittingRemnant = itemstack.getCount();

                for (Slot slot : this.dragSplittingSlots) {
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getStack();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));

                    if (itemstack1.getCount() > j) {
                        itemstack1.setCount(j);
                    }

                    this.dragSplittingRemnant -= itemstack1.getCount() - i;
                }
            }
        }
    }

    public void updateTickInterface() {

    }

    ;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    public void updateScreen() {
        super.updateScreen();
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        if (this.container.base instanceof IUpgradableBlock) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }

        for (final GuiElement<?> element : this.elements) {
            element.drawBackground(mouseX, mouseY);
        }

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.fontRenderer.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }


        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.xSize / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6)/scale);


        this.fontRenderer.drawString(name, textX, textY, 4210752);


        GlStateManager.popMatrix();

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
            element.drawForeground(mouseX, mouseY);
        }

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate(Constants.ABBREVIATION + ".generic.text.upgrade"));

            for (final ItemStack stack : getCompatibleUpgrades((IUpgradableBlock) this.container.base)) {
                text.add(stack.getDisplayName());
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
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
            if (element.contains(mouseX, mouseY)) {
                element.onMouseScroll(mouseX, mouseY, direction);
            }
        }
        final List<GuiButton> listButton = this.buttonList;
        for (GuiButton button : listButton) {
            if (button instanceof GuiVerticalSliderList) {
                GuiVerticalSliderList slider = (GuiVerticalSliderList) button;
                if (direction != ScrollDirection.stopped) {
                    slider.handleMouseWheel(direction, mouseX + guiLeft, mouseY + guiTop);
                }
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean handled = false;
        MouseButton button = MouseButton.get(mouseButton);
        if (button != null) {
            mouseX -= this.guiLeft;
            mouseY -= this.guiTop;

            for (final GuiElement<?> element : this.elements) {

                handled |= element.onMouseClick(mouseX, mouseY, button, element.contains(mouseX, mouseY));

            }

            if (!handled) {
                mouseX += this.guiLeft;
                mouseY += this.guiTop;
            } else {
                this.mouseHandled = true;
            }
        }

        if (!handled) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }

    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        boolean handled = false;
        MouseButton button = MouseButton.get(clickedMouseButton);
        if (button != null) {
            mouseX -= this.guiLeft;
            mouseY -= this.guiTop;

            for (final GuiElement<?> element : this.elements) {
                handled |= element.onMouseDrag(
                        mouseX,
                        mouseY,
                        button,
                        timeSinceLastClick,
                        element.contains(mouseX, mouseY)
                );

            }

            if (!handled) {
                mouseX += this.guiLeft;
                mouseY += this.guiTop;
            } else {
                this.mouseHandled = true;
            }
        }


        if (!handled) {
            super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }

    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        boolean handled = false;
        MouseButton button = MouseButton.get(state);
        if (button != null) {
            mouseX -= this.guiLeft;
            mouseY -= this.guiTop;

            for (final GuiElement<?> element : this.elements) {

                handled |= element.onMouseRelease(mouseX, mouseY, button, element.contains(mouseX, mouseY));

            }

            if (!handled) {
                mouseX += this.guiLeft;
                mouseY += this.guiTop;
            } else {
                this.mouseHandled = true;
            }
        }


        if (!handled) {
            super.mouseReleased(mouseX, mouseY, state);
        }

    }


    public void onGuiClosed() {
        super.onGuiClosed();

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


    public void drawTooltip(int x, int y, List<String> text) {
        this.queuedTooltips.add(new GuiCore.Tooltip(text, x, y));
    }

    public void drawTooltip(int x, int y, ItemStack stack) {
        assert !ModUtils.isEmpty(stack);

        this.renderToolTip(stack, x, y);
    }

    public void drawTooltipOnlyName(int x, int y, ItemStack stack, List<String> strings) {
        assert !ModUtils.isEmpty(stack);

        this.renderToolTipOnlyName(stack, x, y, strings);
    }

    protected void renderToolTipOnlyName(ItemStack stack, int x, int y, final List<String> strings) {
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
        this.drawHoveringText(strings, x, y, (font == null ? fontRenderer : font));
        net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
    }

    public void flushTooltips() {

        for (final Tooltip tooltip : this.queuedTooltips) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawHoveringText(tooltip.text, tooltip.x, tooltip.y);
            GlStateManager.disableLighting();
        }

        this.queuedTooltips.clear();
    }

    protected void addElement(GuiElement<?> element) {
        this.elements.add(element);

    }

    public final void bindTexture() {
        this.mc.getTextureManager().bindTexture(this.getTexture());
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
