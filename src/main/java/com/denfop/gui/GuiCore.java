package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.upgrades.UpgradeRegistry;
import com.denfop.container.ContainerBase;
import com.denfop.container.SlotInvSlot;
import com.denfop.mixin.access.AbstractContainerScreenAccessor;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public abstract class GuiCore<T extends ContainerBase<? extends IAdvInventory>> extends AbstractContainerScreen<T> implements MenuAccess<T> {
    public static final int textHeight = 8;
    public static ResourceLocation currentTexture;
    protected static Runnable closeHandler;
    public final T container;
    protected final List<GuiElement<?>> elements;
    private final Queue<Tooltip> queuedTooltips;
    public int guiLeft;
    public int guiTop;
    public Component title;
    private boolean fixKeyEvents;

    public GuiCore(T container) {
        this(container, 176, 166);
    }


    public GuiCore(T container, int ySize) {
        this(container, 176, ySize);
    }

    public GuiCore(T t, int n, int n2) {
        super(t, t.getInventory(), Component.empty());
        this.fixKeyEvents = false;
        this.container = t;
        this.queuedTooltips = new ArrayDeque();
        this.elements = new ArrayList();
        this.imageHeight = n2;
        this.imageWidth = n;

    }

    private static List<ItemStack> getCompatibleUpgrades(IUpgradableBlock block) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        Set<UpgradableProperty> properties = block.getUpgradableProperties();

        for (final ItemStack stack : UpgradeRegistry.getUpgrades()) {
            IUpgradeItem item = (IUpgradeItem) stack.getItem();
            if (item.isSuitableFor(stack, properties)) {
                ret.add(stack);
            }
        }

        return ret;
    }

    public static void bindTexture(ResourceLocation resourceLocation) {
        currentTexture = resourceLocation;
        RenderSystem.setShaderTexture(0, resourceLocation);
    }

    public static void bindTexture(int i, ResourceLocation resourceLocation) {
        currentTexture = resourceLocation;
        RenderSystem.setShaderTexture(i, resourceLocation);
    }

    public void drawSplitString(GuiGraphics poseStack, Font font, String str, int x, int y, int wrapWidth, int textColor) {
        List<FormattedCharSequence> strs = font.split(FormattedText.of(str), wrapWidth);
        for (FormattedCharSequence s : strs) {
            poseStack.drawString(font, s, x, y, textColor, false);
            y += 9;
        }
    }

    public void changeParams() {
    }

    public void drawSplitString(GuiGraphics poseStack, String str, int x, int y, int wrapWidth, int textColor) {
        if (this.font == null)
            font = Minecraft.getInstance().font;
        List<FormattedCharSequence> strs = font.split(FormattedText.of(str), wrapWidth);
        for (FormattedCharSequence s : strs) {
            poseStack.drawString(font, s, x, y, textColor, false);
            y += 9;
        }
    }

    public float adjustTextScale(String text, int canvasWidth, int canvasHeight, float scale, float scaleStep) {
        float newScale = scale;
        float min = 70;
        float max = 0;
        boolean prevScaleDecrease = false;
        boolean prevScaleIncrease = false;

        while (true) {
            if (newScale < min) min = newScale;
            if (newScale > max) max = newScale;

            List<String> lines = splitTextToLines(text, canvasWidth, newScale);

            int totalTextHeight = (int) (lines.size() * font.lineHeight * newScale);

            if (isTextTooLarge(lines, canvasWidth, canvasHeight, newScale)) {
                newScale *= 1 - scaleStep;
                prevScaleDecrease = true;
                if (prevScaleIncrease) {
                    newScale = (min + max) * 0.95f / 2;
                    break;
                }
            } else if (totalTextHeight < canvasHeight * 0.8F) {
                prevScaleIncrease = true;
                newScale *= (1 + scaleStep);
                if (prevScaleDecrease) {
                    newScale = (min + max) * 1.2f / 2;
                    break;
                }
            } else {
                break;
            }
        }

        return newScale;
    }

    public boolean isTextTooLarge(List<String> lines, int canvasWidth, int canvasHeight, float scale) {
        int totalHeight = (int) (lines.size() * font.lineHeight * scale);

        for (String line : lines) {
            int lineWidth = (int) (font.width(line) * scale);
            if (lineWidth > canvasWidth) return true;
        }

        return totalHeight > canvasHeight;
    }

    public List<String> wrapTextWithNewlines(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] paragraphs = text.split("\n");

        for (String paragraph : paragraphs) {
            lines.addAll(wrapText(paragraph, maxWidth));
        }

        return lines;
    }

    public List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        String[] words = text.split(" ");

        for (String word : words) {
            if (font.width(currentLine + word) <= maxWidth) {
                currentLine.append(word).append(" ");
            } else {
                lines.add(currentLine.toString().trim());
                currentLine.setLength(0);
                currentLine.append(word).append(" ");
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString().trim());
        }

        return lines;
    }

    public void drawTextInCanvas(GuiGraphics graphics, String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, float scale) {
        int maxWidth = (int) (canvasWidth / scale);
        int lineHeight = (int) (font.lineHeight * scale);
        int x = canvasX;
        int y = canvasY;
        PoseStack poseStack = graphics.pose();
        List<String> lines = wrapTextWithNewlines(text, maxWidth);

        for (String line : lines) {
            if (y + lineHeight > canvasY + canvasHeight) break;

            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            graphics.drawString(font, line, (int) (x / scale), (int) (y / scale), 0xFFFFFF, false);
            poseStack.popPose();

            y += lineHeight;
        }
    }

    public void drawTextInCanvas(GuiGraphics graphics, String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, float scale, int color) {
        int maxWidth = (int) (canvasWidth / scale);
        int lineHeight = (int) (font.lineHeight * scale);
        int x = canvasX;
        int y = canvasY;

        List<String> lines = wrapTextWithNewlines(text, maxWidth);
        PoseStack poseStack = graphics.pose();
        for (String line : lines) {
            if (y + lineHeight > canvasY + canvasHeight) break;

            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            graphics.drawString(font, line, (int) (x / scale), (int) (y / scale), color, false);
            poseStack.popPose();

            y += lineHeight;
        }
    }

    public List<String> splitTextToLines(String text, int canvasWidth, float scale) {
        List<String> lines = new ArrayList<>();
        String[] manualLines = text.split("\n");

        for (String manualLine : manualLines) {
            StringBuilder currentLine = new StringBuilder();
            String[] words = manualLine.split(" ");

            for (String word : words) {
                String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                int lineWidth = (int) (font.width(testLine) * scale);

                if (lineWidth > canvasWidth) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine.append(currentLine.isEmpty() ? word : " " + word);
                }
            }

            if (!currentLine.isEmpty()) {
                lines.add(currentLine.toString());
            }
        }

        return lines;
    }

    public final T getContainer() {
        return this.menu;
    }

    public final int guiLeft() {
        return this.leftPos;
    }

    public final int guiTop() {
        return this.topPos;
    }

    public final Slot getFocusedSlot() {
        return this.hoveredSlot;
    }

    public boolean isHovering(Slot p_97775_, double p_97776_, double p_97777_) {
        return this.isHovering(p_97775_.x, p_97775_.y, 16, 16, p_97776_, p_97777_);
    }

    public void renderFloatingItem(GuiGraphics guiGraphics, ItemStack stack, int x, int y, String text) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 232.0F);
        guiGraphics.renderItem(stack, x, y);
        Font font = IClientItemExtensions.of(stack).getFont(stack, IClientItemExtensions.FontContext.ITEM_COUNT);
        guiGraphics.renderItemDecorations(font == null ? this.font : font, stack, x, y - (((AbstractContainerScreenAccessor) this).getDraggingItem().isEmpty() ? 0 : 8), text);
        guiGraphics.pose().popPose();
    }
    public void renderItemTooltipGrid(GuiGraphics graphics, List<IInputItemStack> items, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = width - mouseX;
        int screenHeight = height - mouseY;

        int itemSize = 18;
        int padding = 6;
        int maxColumns = Math.min(12, Math.max(1, (screenWidth - 20) / itemSize));
        int columns = Math.min(maxColumns, items.size());

        int itemsPerRow = columns;
        int maxRows = Math.max(1, (screenHeight - 40) / itemSize);
        int itemsPerPage = itemsPerRow * maxRows;

        int totalPages = (int) Math.ceil((double) items.size() / itemsPerPage);

        if (totalPages <= 0)
            totalPages = 1;
        int currentPage = (int) ((System.currentTimeMillis() / 2000L) % totalPages);

        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        List<IInputItemStack> visibleItems = items.subList(startIndex, endIndex);

        int rows = (int) Math.ceil((double) visibleItems.size() / columns);

        int tooltipWidth = columns * itemSize + padding * 2;
        int tooltipHeight = rows * itemSize + padding * 2;

        int x = mouseX + 12;
        int y = mouseY - 12;

        if (x + tooltipWidth > screenWidth) x = screenWidth - tooltipWidth - 4;
        if (y + tooltipHeight > screenHeight) y = screenHeight - tooltipHeight - 4;
        if (x < 4) x = 4;
        if (y < 4) y = 4;


        TooltipRenderUtil.renderTooltipBackground(graphics, x, y, tooltipWidth, tooltipHeight, 400);

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 400.0F);
        RenderSystem.enableDepthTest();

        long tick = System.currentTimeMillis() / 1000L;

        for (int i = 0; i < visibleItems.size(); i++) {
            int col = i % columns;
            int row = i / columns;
            int drawX = x + padding + col * itemSize;
            int drawY = y + padding + row * itemSize;

            List<ItemStack> stacks = visibleItems.get(i).getInputs();
            ItemStack stack = stacks.get((int) (tick % stacks.size()));

            graphics.renderItem(stack, drawX, drawY);
        }

        RenderSystem.disableDepthTest();
        graphics.pose().popPose();
    }


    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.guiLeft = this.getGuiLeft();
        this.guiTop = this.getGuiTop();
        this.renderTransparentBackground(guiGraphics);
        int i = this.leftPos;
        int j = this.topPos;
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        NeoForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Background(this, guiGraphics, mouseX, mouseY));
        RenderSystem.disableDepthTest();

        for (Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) i, (float) j, 0.0F);
        this.hoveredSlot = null;

        int j2;
        int k2;
        for (int k = 0; k < this.menu.slots.size(); ++k) {
            Slot slot = (Slot) this.menu.slots.get(k);
            if (slot.isActive()) {
                this.renderSlot(guiGraphics, slot);
            }

            if (this.isHovering(slot, (double) mouseX, (double) mouseY) && slot.isActive()) {
                this.hoveredSlot = slot;
                j2 = slot.x;
                k2 = slot.y;
                if (this.hoveredSlot.isHighlightable()) {
                    renderSlotHighlight(guiGraphics, j2, k2, 0, this.getSlotColor(k));
                }
                if (slot instanceof SlotInvSlot)
                    if ( ((SlotInvSlot)slot).invSlot.hasItemList() && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                        renderItemTooltipGrid(guiGraphics, ((SlotInvSlot)slot).invSlot.getStacks(((SlotInvSlot)slot).index), mouseX-guiLeft + 5, mouseY-guiTop + 5);
                    }
            }
        }

        this.renderLabels(guiGraphics, mouseX, mouseY);
        NeoForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Foreground(this, guiGraphics, mouseX, mouseY));
        ItemStack itemstack = ((AbstractContainerScreenAccessor) this).getDraggingItem().isEmpty() ? this.menu.getCarried() : ((AbstractContainerScreenAccessor) this).getDraggingItem();
        if (!itemstack.isEmpty()) {
            j2 = ((AbstractContainerScreenAccessor) this).getDraggingItem().isEmpty() ? 8 : 16;
            String s = null;
            if (!((AbstractContainerScreenAccessor) this).getDraggingItem().isEmpty() && ((AbstractContainerScreenAccessor) this).getIsSplittingStack()) {
                itemstack = itemstack.copyWithCount(Mth.ceil((float) itemstack.getCount() / 2.0F));
            } else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
                itemstack = itemstack.copyWithCount(((AbstractContainerScreenAccessor) this).getQuickCraftingType());
                if (itemstack.isEmpty()) {
                    s = String.valueOf(ChatFormatting.YELLOW) + "0";
                }
            }

            this.renderFloatingItem(guiGraphics, itemstack, mouseX - i - 8, mouseY - j - j2, s);
        }

        if (!((AbstractContainerScreenAccessor) this).getSnapbackItem().isEmpty()) {
            float f = (float) (Util.getMillis() - ((AbstractContainerScreenAccessor) this).getSnapbackTime()) / 100.0F;
            if (f >= 1.0F) {
                f = 1.0F;
                ((AbstractContainerScreenAccessor) this).setSnapbackItem(ItemStack.EMPTY);
            }

            j2 = ((AbstractContainerScreenAccessor) this).getSnapbackEnd().x - ((AbstractContainerScreenAccessor) this).getSnapbackStartX();
            k2 = ((AbstractContainerScreenAccessor) this).getSnapbackEnd().y - ((AbstractContainerScreenAccessor) this).getSnapbackStartY();
            int j1 = ((AbstractContainerScreenAccessor) this).getSnapbackStartX() + (int) ((float) j2 * f);
            int k1 = ((AbstractContainerScreenAccessor) this).getSnapbackStartY() + (int) ((float) k2 * f);
            this.renderFloatingItem(guiGraphics, ((AbstractContainerScreenAccessor) this).getSnapbackItem(), j1, k1, (String) null);
        }

        guiGraphics.pose().popPose();
        RenderSystem.enableDepthTest();
        changeParams();
    }

    public void renderSlot(GuiGraphics guiGraphics, Slot slot) {
        int i = slot.x;
        int j = slot.y;
        ItemStack itemstack = slot.getItem();
        boolean flag = false;
        boolean flag1 = slot == ((AbstractContainerScreenAccessor) this).getClickedSlot() && !((AbstractContainerScreenAccessor) this).getDraggingItem().isEmpty() && !((AbstractContainerScreenAccessor) this).getIsSplittingStack();
        ItemStack itemstack1 = this.menu.getCarried();
        String s = null;
        if (slot == ((AbstractContainerScreenAccessor) this).getClickedSlot() && !((AbstractContainerScreenAccessor) this).getDraggingItem().isEmpty() && ((AbstractContainerScreenAccessor) this).getIsSplittingStack() && !itemstack.isEmpty()) {
            itemstack = itemstack.copyWithCount(itemstack.getCount() / 2);
        } else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !itemstack1.isEmpty()) {
            if (this.quickCraftSlots.size() == 1) {
                return;
            }

            if (AbstractContainerMenu.canItemQuickReplace(slot, itemstack1, true) && this.menu.canDragTo(slot)) {
                flag = true;
                int k = Math.min(itemstack1.getMaxStackSize(), slot.getMaxStackSize(itemstack1));
                int l = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
                int i1 = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, ((AbstractContainerScreenAccessor) this).getQuickCraftingType(), itemstack1) + l;
                if (i1 > k) {
                    i1 = k;
                    String var10000 = ChatFormatting.YELLOW.toString();
                    s = var10000 + k;
                }

                itemstack = itemstack1.copyWithCount(i1);
            } else {
                this.quickCraftSlots.remove(slot);
                this.recalculateQuickCraftRemaining();
            }
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
        if (itemstack.isEmpty() && slot.isActive()) {
            Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
            if (pair != null) {
                TextureAtlasSprite textureatlassprite = (TextureAtlasSprite) this.minecraft.getTextureAtlas((ResourceLocation) pair.getFirst()).apply((ResourceLocation) pair.getSecond());
                guiGraphics.blit(i, j, 0, 16, 16, textureatlassprite);
                flag1 = true;
            }
        }

        if (!flag1) {
            if (flag) {
                guiGraphics.fill(i, j, i + 16, j + 16, -2130706433);
            }

            guiGraphics.renderItem(itemstack, i, j, slot.x + slot.y * this.imageWidth);
            guiGraphics.renderItemDecorations(this.font, itemstack, i, j, s);
        }

        guiGraphics.pose().popPose();
    }

    public void recalculateQuickCraftRemaining() {
        ItemStack itemstack = this.menu.getCarried();
        if (!itemstack.isEmpty() && this.isQuickCrafting) {
            if (((AbstractContainerScreenAccessor) this).getQuickCraftingType() == 2) {
                ((AbstractContainerScreenAccessor) this).setQuickCraftingRemainder(itemstack.getMaxStackSize());
            } else {
                ((AbstractContainerScreenAccessor) this).setQuickCraftingRemainder(itemstack.getCount());


                int i;
                int k;
                for (Iterator var2 = this.quickCraftSlots.iterator(); var2.hasNext(); ((AbstractContainerScreenAccessor) this).setQuickCraftingRemainder(((AbstractContainerScreenAccessor) this).getQuickCraftingRemainder() - (k - i))) {
                    Slot slot = (Slot) var2.next();
                    ItemStack itemstack1 = slot.getItem();
                    i = itemstack1.isEmpty() ? 0 : itemstack1.getCount();
                    int j = Math.min(itemstack.getMaxStackSize(), slot.getMaxStackSize(itemstack));
                    k = Math.min(AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, ((AbstractContainerScreenAccessor) this).getQuickCraftingType(), itemstack) + i, j);
                }
            }
        }

    }

    public void containerTick() {
        super.containerTick();
        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = element;
            if (guiElement.visible()) {
                guiElement.tick();
            }
        }

    }

    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        if (font == null)
            font = Minecraft.getInstance().font;
        if (this.title == null)
            this.title = Component.empty();
        this.drawBackgroundAndTitle(graphics, partialTick, mouseX - this.leftPos, mouseY - this.topPos);
        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = element;
            if (guiElement.visible()) {
                guiElement.drawBackground(graphics, this.leftPos, this.topPos);
            }
        }

    }

    public void drawItemStack(GuiGraphics graphics, int x, int y, ItemStack stack) {
        graphics.renderItem(stack, x + this.guiLeft(), y + this.guiTop());
        graphics.renderItemDecorations(this.font, stack, x + this.guiLeft(), y + this.guiTop());
    }

    public void drawItemStack(GuiGraphics graphics, ItemStack stack, int x, int y) {
        graphics.renderItem(stack, x + this.guiLeft(), y + this.guiTop());
        graphics.renderItemDecorations(this.font, stack, x + this.guiLeft(), y + this.guiTop());
    }

    protected void drawBackgroundAndTitle(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        bindTexture(this.getTexture());
        graphics.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
        graphics.drawCenteredString(font, this.title, this.imageWidth / 2, 6, 4210752);
    }

    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        this.drawForegroundLayer(graphics, mouseX - this.leftPos, mouseY - this.topPos);
        this.flushTooltips(graphics);
        renderTooltip(graphics, mouseX - this.leftPos, mouseY - this.topPos - 5);
    }

    protected void draw(GuiGraphics poseStack, String s, int i, int i1, int i2) {
        poseStack.drawString(Minecraft.getInstance().font, s, i, i1, i2, false);
    }

    protected void draw(GuiGraphics poseStack, FormattedCharSequence s, int i, int i1, int i2) {
        poseStack.drawString(Minecraft.getInstance().font, s, i, i1, i2, false);
    }

    protected void draw(GuiGraphics poseStack, Component s, int i, int i1, int i2) {
        poseStack.drawString(Minecraft.getInstance().font, s, i, i1, i2, false);
    }

    protected void drawForegroundLayer(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.menu.base instanceof IUpgradableBlock) {
            this.handleUpgradeTooltip(mouseX, mouseY);
        }

        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = element;
            if (guiElement.visible()) {
                guiElement.drawForeground(graphics, mouseX, mouseY);
            }
        }

    }

    private void handleUpgradeTooltip(int n, int n2) {
        if (n >= 0 && n <= 12 && n2 >= 0 && n2 <= 12) {
            ArrayList<String> arrayList = new ArrayList();
            arrayList.add(Localization.translate(Constants.ABBREVIATION + ".generic.text.upgrade"));

            for (ItemStack itemStack : getCompatibleUpgrades((IUpgradableBlock) this.menu.base)) {
                arrayList.add(itemStack.getHoverName().getString());
            }

            this.drawTooltip(n, n2, arrayList);
        }
    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d4, double d3) {
        ScrollDirection scrollDirection = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;

        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = element;
            if (guiElement.visible() && guiElement.contains((int) d, (int) d2)) {
                guiElement.onMouseScroll((int) d, (int) d2, scrollDirection);
            }
        }
        final List<Renderable> listButton = this.renderables;
        for (Renderable button : listButton) {
            if (button instanceof GuiVerticalSliderList) {
                GuiVerticalSliderList slider = (GuiVerticalSliderList) button;
                if (scrollDirection != ScrollDirection.stopped) {

                    slider.handleMouseWheel(scrollDirection, (int) (d), (int) (d2));
                }
            }
            if (button instanceof GuiSlider) {
                GuiSlider slider = (GuiSlider) button;
                if (scrollDirection != ScrollDirection.stopped) {

                    slider.handleMouseWheel(scrollDirection, (int) (d), (int) (d2));
                }
            }
            if (button instanceof GuiVerticalSlider) {
                GuiVerticalSlider slider = (GuiVerticalSlider) button;
                if (scrollDirection != ScrollDirection.stopped) {
                    slider.handleMouseWheel(scrollDirection, (int) (d), (int) (d2));
                }
            }
        }
        return super.mouseScrolled(d, d2, d4, d3);
    }


    protected void mouseClicked(int i, int j, int k) {
        for (Renderable widget : this.renderables) {
            if (widget instanceof GuiEventListener) {
                ((GuiEventListener) widget).mouseClicked(i, j, k);
            }
        }
    }

    public boolean mouseClicked(double d, double d2, int n) {
        MouseButton mouseButton = MouseButton.get(n);
        boolean bl = false;
        d -= (double) this.leftPos;
        d2 -= (double) this.topPos;

        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = (GuiElement) element;
            if (guiElement.visible()) {
                bl |= guiElement.onMouseClick((int) d, (int) d2, mouseButton, guiElement.contains((int) d, (int) d2));
            }
        }

        if (bl) {
            return true;
        }

        d += (double) this.leftPos;
        d2 += (double) this.topPos;

        mouseClicked((int) d, (int) d2, n);
        return super.mouseClicked(d, d2, n);
    }

    public boolean mouseDragged(double d, double d2, int n, double d3, double d4) {
        MouseButton mouseButton = MouseButton.get(n);
        boolean bl = false;
        d -= (double) this.leftPos;
        d2 -= (double) this.topPos;

        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = element;
            if (guiElement.visible()) {
                bl |= guiElement.onMouseDrag((int) d, (int) d2, mouseButton, (long) d3, guiElement.contains((int) d, (int) d2));
            }
        }

        if (bl) {
            return true;
        }

        d += (double) this.leftPos;
        d2 += (double) this.topPos;
        for (Renderable widget : this.renderables) {
            if (widget instanceof GuiEventListener) {
                ((GuiEventListener) widget).mouseDragged(d, d2, n, d3, d4);
            }
        }

        return super.mouseDragged(d, d2, n, d3, d4);
    }

    public boolean mouseReleased(double d, double d2, int n) {
        MouseButton mouseButton = MouseButton.get(n);
        boolean bl = false;
        d -= (double) this.leftPos;
        d2 -= (double) this.topPos;

        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = (GuiElement) element;
            if (guiElement.visible()) {
                bl |= guiElement.onMouseRelease((int) d, (int) d2, mouseButton, guiElement.contains((int) d, (int) d2));
            }
        }

        if (bl) {
            return true;
        }

        d += (double) this.leftPos;
        d2 += (double) this.topPos;


        return super.mouseReleased(d, d2, n);
    }

    public boolean charTyped(char c, int n) {
        boolean bl = false;

        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = (GuiElement) element;
            if (guiElement.visible()) {
                bl |= guiElement.onKeyTyped(c, n);
            }
        }

        if (bl) {
            return true;
        }

        return super.charTyped(c, n);
    }

    public void removed() {
        super.removed();
        if (closeHandler != null) {
            closeHandler.run();
        }

    }

    public void drawTexturedRect(GuiGraphics graphics, double x, double y, double width, double height, double texX, double texY) {
        this.drawTexturedRect(graphics, x, y, width, height, texX, texY, false);
    }

    public void drawTexturedRect(GuiGraphics graphics, double x, double y, double width, double height, double texX, double texY, boolean mirrorX) {
        this.drawTexturedRect(graphics,
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

    public void drawTexturedRect(GuiGraphics graphics, double x,
                                 double y,
                                 double width,
                                 double height,
                                 double uS,
                                 double vS,
                                 double uE,
                                 double vE,
                                 boolean mirrorX) {
        x += this.guiLeft();
        y += this.guiTop();
        double xE = x + width;
        double yE = y + height;

        if (mirrorX) {
            double tmp = uS;
            uS = uE;
            uE = tmp;
        }
        graphics.blit(currentTexture, (int) x, (int) y, (int) uS, (int) vS, (int) width, (int) height);
        // Ensure rendering state is prepared
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = graphics.pose().last().pose();
        bufferBuilder.addVertex(matrix, (float) x, (float) y, 0).setUv((float) uS, (float) vS);
        bufferBuilder.addVertex(matrix, (float) x, (float) yE, 0).setUv((float) uS, (float) vE);
        bufferBuilder.addVertex(matrix, (float) xE, (float) yE, 0).setUv((float) uE, (float) vE);
        bufferBuilder.addVertex(matrix, (float) xE, (float) y, 0).setUv((float) uE, (float) vS);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());


    }

    public void drawSprite(GuiGraphics graphics, double x, double y, double width, double height, TextureAtlasSprite sprite, int color, double textureSize, boolean wrapX, boolean wrapY) {
        if (sprite == null) {
            sprite = ((TextureAtlas) Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS))
                    .getSprite(MissingTextureAtlasSprite.getLocation());
        }

        double startX = x;
        double startY = y;
        double tileSize = 16.0;

        double u0 = sprite.getU0();
        double v0 = sprite.getV0();
        double u1 = sprite.getU1();
        double v1 = sprite.getV1();
        double uSize = u1 - u0;
        double vSize = v1 - v0;

        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        Matrix4f matrix = graphics.pose().last().pose();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);


        double remX = width % tileSize;
        double remY = height % tileSize;

        for (double tileX = startX; tileX < startX + width; ) {
            double tileWidth = tileSize;
            double uStart = u0;

            if (tileX == startX && remX > 0.0) {
                tileWidth = remX;
                uStart = u0 + uSize * (1.0 - remX / tileSize);
            }

            if (tileX + tileWidth > startX + width) {
                tileWidth = (startX + width) - tileX;

                uStart = u0;
            }

            double tileXEnd = tileX + tileWidth;
            double uEnd = uStart + (tileXEnd - tileX) / tileSize * uSize;

            for (double tileY = startY; tileY < startY + height; ) {
                double tileHeight = tileSize;
                double vStart = v0;

                if (tileY == startY && remY > 0.0) {
                    tileHeight = remY;
                    vStart = v0 + vSize * (1.0 - remY / tileSize);
                }
                if (tileY + tileHeight > startY + height) {
                    tileHeight = (startY + height) - tileY;
                    vStart = v0;
                }

                double tileYEnd = tileY + tileHeight;
                double vEnd = vStart + (tileYEnd - tileY) / tileSize * vSize;

                buffer.addVertex(matrix, (float) tileX, (float) tileY, 0.0F)
                        .setUv((float) uStart, (float) vStart)
                        .setColor(red, green, blue, alpha);
                buffer.addVertex(matrix, (float) tileX, (float) tileYEnd, 0.0F)
                        .setUv((float) uStart, (float) vEnd)
                        .setColor(red, green, blue, alpha);
                buffer.addVertex(matrix, (float) tileXEnd, (float) tileYEnd, 0.0F)
                        .setUv((float) uEnd, (float) vEnd)
                        .setColor(red, green, blue, alpha);
                buffer.addVertex(matrix, (float) tileXEnd, (float) tileY, 0.0F)
                        .setUv((float) uEnd, (float) vStart)
                        .setColor(red, green, blue, alpha);


                tileY += tileHeight;
            }


            tileX += tileWidth;
        }
        try {
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }catch (Exception e){};
    }

    public void drawItem(GuiGraphics graphics, int x, int y, ItemStack itemStack) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        graphics.renderItem(itemStack, x + this.guiLeft(), y + this.guiTop());

    }

    public void drawColoredRect(GuiGraphics poseStack, int x, int y, int width, int height, int color, BufferBuilder bufferBuilder) {
        int alpha = color >>> 24;
        boolean translucent = alpha != 255 && alpha != 0;


        Matrix4f matrix4f = poseStack.pose().last().pose();

        int x2 = (x += this.leftPos) + width;
        int y2 = (y += this.topPos) + height;

        int z = 0;


        if (translucent) {
            RenderSystem.enableBlend();
        }


        bufferBuilder.addVertex(matrix4f, (float) x, (float) y, (float) z).setColor(color);
        bufferBuilder.addVertex(matrix4f, (float) x, (float) y2, (float) z).setColor(color);
        bufferBuilder.addVertex(matrix4f, (float) x2, (float) y2, (float) z).setColor(color);
        bufferBuilder.addVertex(matrix4f, (float) x2, (float) y, (float) z).setColor(color);


        if (translucent) {
            RenderSystem.disableBlend();
        }
    }

    public int drawString(GuiGraphics poseStack, String text, int x, int y, int color) {
        if (font == null)
            font = Minecraft.getInstance().font;
        poseStack.drawString(font, text, x + this.guiLeft - leftPos, y + this.guiTop - topPos, color, false);
        return x;
    }

    public int drawString(GuiGraphics poseStack, int x, int y, String text, int color) {
        if (font == null)
            font = Minecraft.getInstance().font;
        poseStack.drawString(font, text, x + this.guiLeft - leftPos, y + this.guiTop - topPos, color, false);
        return x;
    }

    public int drawString(GuiGraphics poseStack, int x, int y, String text, int color, boolean shadow) {
        if (font == null)
            font = Minecraft.getInstance().font;
        poseStack.drawString(font, text, x + this.guiLeft - leftPos, y + this.guiTop - topPos, color, shadow);
        return x;
    }

    public void drawXCenteredString(GuiGraphics graphics, int n, int n2, Component component, int n3, boolean bl) {
        this.drawCenteredString(graphics, n, n2, component, n3, bl, true, false);
    }

    public void drawXCenteredString(GuiGraphics graphics, int n, int n2, String component, int n3, boolean bl) {
        this.drawCenteredString(graphics, n, n2, component, n3, bl, true, false);
    }

    public void drawXYCenteredString(GuiGraphics graphics, int n, int n2, String string, int n3, boolean bl) {
        this.drawCenteredString(graphics, n, n2, string, n3, bl, true, true);
    }

    public void drawCenteredString(GuiGraphics graphics, int n, int n2, String string, int n3, boolean bl, boolean bl2, boolean bl3) {
        if (bl2) {
            n -= this.getStringWidth(string) / 2;
        }

        if (bl3) {
            n2 -= 4;
        }

        graphics.drawString(font, string, this.getGuiLeft() + n, this.getGuiTop() + n2, n3, false);
    }

    public void drawCenteredString(GuiGraphics graphics, int n, int n2, Component component, int n3, boolean bl, boolean bl2, boolean bl3) {
        if (bl2) {
            n -= this.getStringWidth(component) / 2;
        }

        if (bl3) {
            n2 -= 4;
        }

        graphics.drawString(font, component.getString(), this.getGuiLeft() + n, this.getGuiTop() + n2, n3, false);
    }

    public int getStringWidth(String string) {
        return this.font.width(string);
    }

    public int getStringWidth(Component component) {
        return this.font.width(component);
    }

    public String trimStringToWidth(String string, int n) {
        return this.font.plainSubstrByWidth(string, n, false);
    }

    public String trimStringToWidthReverse(String string, int n) {
        return this.font.plainSubstrByWidth(string, n, true);
    }

    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack itemstack = this.hoveredSlot.getItem();
            pGuiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, pX, pY);
        }

    }

    public void drawTooltip(GuiGraphics poseStack, int x, int y, ItemStack stack) {
        assert !ModUtils.isEmpty(stack);
        poseStack.renderTooltip(font, stack, x, y);
    }

    public void drawTooltipOnlyName(GuiGraphics poseStack, int x, int y, ItemStack stack, List<String> strings) {
        if (stack.isEmpty()) {
            return;
        }
        List<Component> tooltipComponents = new ArrayList<>();
        tooltipComponents.add(stack.getDisplayName());
        strings.forEach(s -> tooltipComponents.add(Component.literal(s)));
        poseStack.renderComponentTooltip(font, tooltipComponents, x, y);
    }

    public void drawTooltip(int n, int n2, List<String> list) {
        this.queuedTooltips.add(new Tooltip(list, n, n2));
    }

    protected void flushTooltips(GuiGraphics graphics) {


        List<Tooltip> tooltips = new ArrayList<>(this.queuedTooltips);
        List<Tooltip> tooltipsToRender = new ArrayList<>();
        List<Rectangle> usedAreas = new ArrayList<>();


        for (int i = tooltips.size() - 1; i >= 0; i--) {
            Tooltip tooltip = tooltips.get(i);


            int maxWidth = 0;
            for (FormattedText line : tooltip.text) {
                int lineWidth = Minecraft.getInstance().font.width(line);
                if (lineWidth > maxWidth) maxWidth = lineWidth;
            }

            int tooltipWidth = maxWidth + 8;
            int tooltipHeight = tooltip.text.size() * 10 + 8;

            Rectangle tooltipArea = new Rectangle(tooltip.x, tooltip.y, tooltipWidth, tooltipHeight);


            boolean overlaps = false;
            for (Rectangle area : usedAreas) {
                if (tooltipArea.intersects(area)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                usedAreas.add(tooltipArea);
                tooltipsToRender.add(tooltip);
            }
        }


        Collections.reverse(tooltipsToRender);
        for (Tooltip tooltip : tooltipsToRender) {
            graphics.renderTooltip(font, tooltip.text, Optional.empty(), tooltip.x, tooltip.y);
        }

        this.queuedTooltips.clear();
    }

    protected void addElement(GuiElement<?> guiElement) {
        this.elements.add(guiElement);

    }

    public final void bindTexture() {
        bindTexture(this.getTexture());
    }

    protected abstract ResourceLocation getTexture();

    public void drawTexturedModalRect(GuiGraphics poseStack, int i, int i1, int i2, int i3, int i4, int i5) {
        poseStack.blit(currentTexture, i, i1, i2, i3, i4, i5);
    }

    public void drawTextInCanvasWithScissor(GuiGraphics poseStack, String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, int scale) {
        int maxWidth = (int) (canvasWidth / 1);
        int lineHeight = (int) (10 * 1);
        int x = canvasX;
        int y = canvasY;


        List<String> lines = splitTextToLines(text, maxWidth, 1);


        for (int i = scale - 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (y + lineHeight > canvasY + canvasHeight) {
                break;
            }
            poseStack.pose().pushPose();
            poseStack.pose().scale(1, 1, 1);
            drawString(poseStack, line, (int) (x / 1), (int) (y / 1), 0xFFFFFF);
            poseStack.pose().popPose();

            y += lineHeight;
        }
    }


    private static class Tooltip {
        final int x;
        final int y;
        final List<Component> text;

        Tooltip(List<String> list, int n, int n2) {
            this.text = new LinkedList<>();
            list.forEach(s -> this.text.add(Component.literal(s)));
            this.x = n;
            this.y = n2;
        }
    }
}
