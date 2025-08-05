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
import com.denfop.mixin.access.AbstractContainerScreenAccessor;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
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
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public abstract class GuiCore<T extends ContainerBase<? extends IAdvInventory>> extends AbstractContainerScreen<T> implements MenuAccess<T> {
    public static final int textHeight = 8;
    protected static Runnable closeHandler;
    public final T container;
    protected final List<GuiElement<?>> elements;
    private final Queue<Tooltip> queuedTooltips;
    public int guiLeft;
    public int guiTop;
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
    public void drawSplitString(PoseStack poseStack, Font font, String str, int x, int y, int wrapWidth, int textColor)
    {
        List<FormattedCharSequence> strs = font.split(FormattedText.of(str), wrapWidth);
        for (FormattedCharSequence s : strs)
        {
            font.draw(poseStack, s, x, y, textColor);
            y += 9;
        }
    }
    public ItemRenderer getItemRenderer(){
        return this.itemRenderer;
    }
    public void drawSplitString(PoseStack poseStack, String str, int x, int y, int wrapWidth, int textColor)
    {
        if (this.font == null)
            font = Minecraft.getInstance().font;
        List<FormattedCharSequence> strs = font.split(FormattedText.of(str), wrapWidth);
        for (FormattedCharSequence s : strs)
        {
            font.draw(poseStack, s, x, y, textColor);
            y += 9;
        }
    }
    public void changeParams() {

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
        RenderSystem.setShaderTexture(0, resourceLocation);
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
    public void drawTextInCanvasWithScissor(PoseStack poseStack, String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, int scale) {
        int maxWidth = (int) (canvasWidth / 1);
        int lineHeight = (int) (10 * 1);
        int x = canvasX;
        int y = canvasY;


        List<String> lines = splitTextToLines(text, maxWidth,1);


        for (int i = scale - 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (y + lineHeight > canvasY + canvasHeight) {
                break;
            }
            poseStack.pushPose();
            poseStack.scale(1, 1, 1);
            drawString(poseStack, line, (int) (x / 1), (int) (y / 1), 0xFFFFFF);
            poseStack.popPose();

            y += lineHeight;
        }
    }
    public void drawTextInCanvas(PoseStack poseStack, String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, float scale) {
        int maxWidth = (int) (canvasWidth / scale);
        int lineHeight = (int) (font.lineHeight * scale);
        int x = canvasX;
        int y = canvasY;

        List<String> lines = wrapTextWithNewlines(text, maxWidth);

        for (String line : lines) {
            if (y + lineHeight > canvasY + canvasHeight) break;

            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            font.draw(poseStack, Component.literal(line), x / scale, y / scale, 0xFFFFFF);
            poseStack.popPose();

            y += lineHeight;
        }
    }

    public void drawTextInCanvas(PoseStack poseStack, String text, int canvasX, int canvasY, int canvasWidth, int canvasHeight, float scale, int color) {
        int maxWidth = (int) (canvasWidth / scale);
        int lineHeight = (int) (font.lineHeight * scale);
        int x = canvasX;
        int y = canvasY;

        List<String> lines = wrapTextWithNewlines(text, maxWidth);

        for (String line : lines) {
            if (y + lineHeight > canvasY + canvasHeight) break;

            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            font.draw(poseStack, Component.literal(line), x / scale, y / scale, color);
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
    public void renderFloatingItem(ItemStack p_97783_, int p_97784_, int p_97785_, String p_97786_) {
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.translate(0.0D, 0.0D, 32.0D);
        RenderSystem.applyModelViewMatrix();
        this.setBlitOffset(200);
        this.itemRenderer.blitOffset = 200.0F;
        var font = net.minecraftforge.client.extensions.common.IClientItemExtensions.of(p_97783_).getFont(p_97783_, net.minecraftforge.client.extensions.common.IClientItemExtensions.FontContext.ITEM_COUNT);
        if (font == null) font = this.font;
        this.itemRenderer.renderAndDecorateItem(p_97783_, p_97784_, p_97785_);
        this.itemRenderer.renderGuiItemDecorations(font, p_97783_, p_97784_, p_97785_ - (this.draggingItem.isEmpty() ? 0 : 8), p_97786_);
        this.setBlitOffset(0);
        this.itemRenderer.blitOffset = 0.0F;
    }
    public void render(@NotNull PoseStack graphics, int mouseX, int mouseY, float partialTick) {
        this.guiLeft = this.getGuiLeft();
        this.guiTop = this.getGuiTop();
        this.renderBackground(graphics);
        int i = this.leftPos;
        int j = this.topPos;
        this.renderBg(graphics, partialTick, mouseX, mouseY);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ContainerScreenEvent.Render.Background(this, graphics, mouseX, mouseY));
        RenderSystem.disableDepthTest();
        for(Widget widget : this.renderables) {
            widget.render(graphics, mouseX, mouseY, partialTick);
        }
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double) i, (double) j, 0.0D);
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.hoveredSlot = null;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        for (int k = 0; k < this.menu.slots.size(); ++k) {
            Slot slot = this.menu.slots.get(k);
            if (slot.isActive()) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                this.renderSlot(graphics, slot);
            }

            if (this.isHovering(slot, (double) mouseX, (double) mouseY) && slot.isActive()) {
                this.hoveredSlot = slot;
                int l = slot.x;
                int i1 = slot.y;
                renderSlotHighlight(graphics, l, i1, this.getBlitOffset(), this.getSlotColor(k));
            }
        }

        this.renderLabels(graphics, mouseX, mouseY);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ContainerScreenEvent.Render.Foreground(this, graphics, mouseX, mouseY));
        ItemStack itemstack = this.draggingItem.isEmpty() ? this.menu.getCarried() : this.draggingItem;
        if (!itemstack.isEmpty()) {
            int l1 = 8;
            int i2 = this.draggingItem.isEmpty() ? 8 : 16;
            String s = null;
            if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
                itemstack = itemstack.copy();
                itemstack.setCount(Mth.ceil((float) itemstack.getCount() / 2.0F));
            } else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.setCount(this.quickCraftingRemainder);
                if (itemstack.isEmpty()) {
                    s = ChatFormatting.YELLOW + "0";
                }
            }

            this.renderFloatingItem(itemstack, mouseX - i - 8, mouseY - j - i2, s);
        }

        if (!this.snapbackItem.isEmpty()) {
            float f = (float) (Util.getMillis() - this.snapbackTime) / 100.0F;
            if (f >= 1.0F) {
                f = 1.0F;
                this.snapbackItem = ItemStack.EMPTY;
            }

            int j2 = this.snapbackEnd.x - this.snapbackStartX;
            int k2 = this.snapbackEnd.y - this.snapbackStartY;
            int j1 = this.snapbackStartX + (int) ((float) j2 * f);
            int k1 = this.snapbackStartY + (int) ((float) k2 * f);
            this.renderFloatingItem(this.snapbackItem, j1, k1, (String) null);
        }

        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.enableDepthTest();

        this.renderTooltip(graphics, mouseX, mouseY);
        changeParams();
    }

    public void renderSlot(PoseStack p_97800_, Slot p_97801_) {
        int i = p_97801_.x;
        int j = p_97801_.y;
        ItemStack itemstack = p_97801_.getItem();
        boolean flag = false;
        boolean flag1 = p_97801_ == ((AbstractContainerScreenAccessor) this).getClickedSlot() && !this.draggingItem.isEmpty() && !this.isSplittingStack;
        ItemStack itemstack1 = this.menu.getCarried();
        String s = null;
        if (p_97801_ == ((AbstractContainerScreenAccessor) this).getClickedSlot() && !this.draggingItem.isEmpty() && this.isSplittingStack && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        } else if (this.isQuickCrafting && this.quickCraftSlots.contains(p_97801_) && !itemstack1.isEmpty()) {
            if (this.quickCraftSlots.size() == 1) {
                return;
            }

            if (AbstractContainerMenu.canItemQuickReplace(p_97801_, itemstack1, true) && this.menu.canDragTo(p_97801_)) {
                itemstack = itemstack1.copy();
                flag = true;
                AbstractContainerMenu.getQuickCraftSlotCount(this.quickCraftSlots, ((AbstractContainerScreenAccessor) this).getQuickCraftingType(), itemstack, p_97801_.getItem().isEmpty() ? 0 : p_97801_.getItem().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), p_97801_.getMaxStackSize(itemstack));
                if (itemstack.getCount() > k) {
                    s = ChatFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            } else {
                this.quickCraftSlots.remove(p_97801_);
                this.recalculateQuickCraftRemaining();
            }
        }

        this.setBlitOffset(100);
        this.itemRenderer.blitOffset = 100.0F;
        if (itemstack.isEmpty() && p_97801_.isActive()) {
            Pair<ResourceLocation, ResourceLocation> pair = p_97801_.getNoItemIcon();
            if (pair != null) {
                TextureAtlasSprite textureatlassprite = this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
                RenderSystem.setShaderTexture(0, textureatlassprite.atlas().location());
                blit(p_97800_, i, j, this.getBlitOffset(), 16, 16, textureatlassprite);
                flag1 = true;
            }
        }

        if (!flag1) {
            if (flag) {
                fill(p_97800_, i, j, i + 16, j + 16, -2130706433);
            }

            RenderSystem.enableDepthTest();
            this.itemRenderer.renderAndDecorateItem(this.minecraft.player, itemstack, i, j, p_97801_.x + p_97801_.y * this.imageWidth);
            this.itemRenderer.renderGuiItemDecorations(this.font, itemstack, i, j, s);
        }

        this.itemRenderer.blitOffset = 0.0F;
        this.setBlitOffset(0);
    }

    public void recalculateQuickCraftRemaining() {
        ItemStack itemstack = this.menu.getCarried();
        if (!itemstack.isEmpty() && this.isQuickCrafting) {
            if (((AbstractContainerScreenAccessor) this).getQuickCraftingType() == 2) {
                this.quickCraftingRemainder = itemstack.getMaxStackSize();
            } else {
                this.quickCraftingRemainder = itemstack.getCount();

                for (Slot slot : this.quickCraftSlots) {
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getItem();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    AbstractContainerMenu.getQuickCraftSlotCount(this.quickCraftSlots, ((AbstractContainerScreenAccessor) this).getQuickCraftingType(), itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getMaxStackSize(itemstack1));
                    if (itemstack1.getCount() > j) {
                        itemstack1.setCount(j);
                    }

                    this.quickCraftingRemainder -= itemstack1.getCount() - i;
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

    protected void renderBg(@NotNull PoseStack graphics, float partialTick, int mouseX, int mouseY) {
        this.drawBackgroundAndTitle(graphics, partialTick, mouseX - this.leftPos, mouseY - this.topPos);
        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = element;
            if (guiElement.visible()) {
                guiElement.drawBackground(graphics, this.leftPos, this.topPos);
            }
        }

    }

    public void drawItemStack(int x, int y, ItemStack stack) {
        this.itemRenderer.renderGuiItem(stack, x + this.guiLeft(), y + this.guiTop());
        this.itemRenderer.renderGuiItemDecorations(this.font, stack, x + this.guiLeft(), y + this.guiTop());
    }
    public void drawItemStack(ItemStack stack, int x, int y) {
        this.itemRenderer.renderGuiItem(stack, x + this.guiLeft(), y + this.guiTop());
        this.itemRenderer.renderGuiItemDecorations(this.font, stack, x + this.guiLeft(), y + this.guiTop());
    }

    protected void drawBackgroundAndTitle(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        bindTexture(this.getTexture());
        blit(poseStack, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
        this.drawXCenteredString(poseStack, this.imageWidth / 2, 6, this.title, 4210752, false);
    }

    protected void renderLabels(@NotNull PoseStack graphics, int mouseX, int mouseY) {
        this.drawForegroundLayer(graphics, mouseX - this.leftPos, mouseY - this.topPos);
        this.flushTooltips(graphics);
    }

    protected void drawForegroundLayer(@NotNull PoseStack graphics, int mouseX, int mouseY) {
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

    public boolean mouseScrolled(double d, double d2, double d3) {
        ScrollDirection scrollDirection = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;

        for (GuiElement<?> element : this.elements) {
            GuiElement<?> guiElement = element;
            if (guiElement.visible() && guiElement.contains((int) d, (int) d2)) {
                guiElement.onMouseScroll((int) d, (int) d2, scrollDirection);
            }
        }
        final List<Widget> listButton = this.renderables;
        for (Widget button : listButton) {
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

        return super.mouseScrolled(d, d2, d3);
    }

    protected void mouseClicked(int i, int j, int k) {
        for (Widget widget : this.renderables){
            if (widget instanceof GuiEventListener){
                ((GuiEventListener) widget).mouseClicked(i,j,k);
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
        for (Widget widget : this.renderables){
            if (widget instanceof GuiEventListener){
                ((GuiEventListener) widget).mouseDragged(d,d2,n,d3,d4);
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

    public void drawTexturedRect(PoseStack graphics, double x, double y, double width, double height, double texX, double texY) {
        this.drawTexturedRect(graphics, x, y, width, height, texX, texY, false);
    }

    public void drawTexturedRect(PoseStack graphics, double x, double y, double width, double height, double texX, double texY, boolean mirrorX) {
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

    public void drawTexturedRect(PoseStack graphics, double x,
                                 double y,
                                 double width,
                                 double height,
                                 double uS,
                                 double vS,
                                 double uE,
                                 double vE,
                                 boolean mirrorX) {
        // Calculate final position for the rectangle
        x += this.guiLeft();
        y += this.guiTop();
        double xE = x + width;
        double yE = y + height;

        // Handle mirroring of texture coordinates
        if (mirrorX) {
            double tmp = uS;
            uS = uE;
            uE = tmp;
        }
        blit(graphics, (int) x, (int) y, (int) uS, (int) vS, (int) width, (int) height);
        // Ensure rendering state is prepared
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        Matrix4f matrix = graphics.last().pose();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix, (float) x, (float) y, 0).uv((float) uS, (float) vS).endVertex();
        bufferBuilder.vertex(matrix, (float) x, (float) yE, 0).uv((float) uS, (float) vE).endVertex();
        bufferBuilder.vertex(matrix, (float) xE, (float) yE, 0).uv((float) uE, (float) vE).endVertex();
        bufferBuilder.vertex(matrix, (float) xE, (float) y, 0).uv((float) uE, (float) vS).endVertex();
        tessellator.end();


    }

    public void drawSprite(PoseStack graphics, double x, double y, double width, double height, TextureAtlasSprite sprite, int color, double textureSize, boolean wrapX, boolean wrapY) {
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
        int red   = (color >> 16) & 0xFF;
        int green = (color >> 8)  & 0xFF;
        int blue  = color & 0xFF;

        Matrix4f matrix = graphics.last().pose();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

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

                buffer.vertex(matrix, (float) tileX,    (float) tileY,     0.0F)
                        .uv((float) uStart, (float) vStart)
                        .color(red, green, blue, alpha)
                        .endVertex();
                buffer.vertex(matrix, (float) tileX,    (float) tileYEnd,  0.0F)
                        .uv((float) uStart, (float) vEnd)
                        .color(red, green, blue, alpha)
                        .endVertex();
                buffer.vertex(matrix, (float) tileXEnd, (float) tileYEnd,  0.0F)
                        .uv((float) uEnd,   (float) vEnd)
                        .color(red, green, blue, alpha)
                        .endVertex();
                buffer.vertex(matrix, (float) tileXEnd, (float) tileY,     0.0F)
                        .uv((float) uEnd,   (float) vStart)
                        .color(red, green, blue, alpha)
                        .endVertex();


                tileY += tileHeight;
            }


            tileX += tileWidth;
        }


        BufferUploader.drawWithShader(buffer.end());
    }

    public void drawItem(int x, int y, ItemStack itemStack) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderAndDecorateItem(itemStack, x, y);
    }

    public void drawColoredRect(PoseStack poseStack, int x, int y, int width, int height, int color, BufferBuilder bufferBuilder) {
        int alpha = color >>> 24;
        boolean translucent = alpha != 255 && alpha != 0;


        Matrix4f matrix4f = poseStack.last().pose();

        int x2 = (x += this.leftPos) + width;
        int y2 = (y += this.topPos) + height;

        int z = 0;


        if (translucent) {
            RenderSystem.enableBlend();
        }


        bufferBuilder.vertex(matrix4f, (float) x, (float) y, (float) z).color(color).endVertex();
        bufferBuilder.vertex(matrix4f, (float) x, (float) y2, (float) z).color(color).endVertex();
        bufferBuilder.vertex(matrix4f, (float) x2, (float) y2, (float) z).color(color).endVertex();
        bufferBuilder.vertex(matrix4f, (float) x2, (float) y, (float) z).color(color).endVertex();


        if (translucent) {
            RenderSystem.disableBlend();
        }
    }
    public int draw(PoseStack poseStack, String text, int x, int y, int color) {
        if (font == null)
            font = Minecraft.getInstance().font;
        this.font.draw(poseStack, text, x + this.guiLeft - leftPos, y + this.guiTop - topPos, color);
        return x;
    }
    public int drawString(PoseStack poseStack, String text, int x, int y, int color) {
        if (font == null)
            font = Minecraft.getInstance().font;
        this.font.draw(poseStack, text, x + this.guiLeft - leftPos, y + this.guiTop - topPos, color);
        return x;
    }

    public int drawString(PoseStack poseStack, int x, int y, String text, int color) {
        if (font == null)
            font = Minecraft.getInstance().font;
        this.font.draw(poseStack, text, x + this.guiLeft - leftPos, y + this.guiTop - topPos, color);
        return x;
    }

    public int drawString(PoseStack poseStack, int x, int y, String text, int color, boolean shadow) {
        if (font == null)
            font = Minecraft.getInstance().font;
        if (shadow) {
            this.font.drawShadow(poseStack, text, x + this.guiLeft - leftPos, y + this.guiTop - topPos, color);
        } else {
            this.font.draw(poseStack, text, x + this.guiLeft - leftPos, y + this.guiTop - topPos, color);
        }
        return x;
    }

    public void drawXCenteredString(PoseStack graphics, int n, int n2, Component component, int n3, boolean bl) {
        this.drawCenteredString(graphics, n, n2, component, n3, bl, true, false);
    }
    public void drawXCenteredString(PoseStack graphics, int n, int n2, String component, int n3, boolean bl) {
        this.drawCenteredString(graphics, n, n2, component, n3, bl, true, false);
    }
    public void drawXYCenteredString(PoseStack graphics, int n, int n2, String string, int n3, boolean bl) {
        this.drawCenteredString(graphics, n, n2, string, n3, bl, true, true);
    }

    public void drawCenteredString(PoseStack graphics, int n, int n2, String string, int n3, boolean bl, boolean bl2, boolean bl3) {
        if (bl2) {
            n -= this.getStringWidth(string) / 2;
        }

        if (bl3) {
            n2 -= 4;
        }

        this.font.draw(graphics, string, this.getGuiLeft() + n, this.getGuiTop() + n2, n3);
    }

    public void drawCenteredString(PoseStack graphics, int n, int n2, Component component, int n3, boolean bl, boolean bl2, boolean bl3) {
        if (bl2) {
            n -= this.getStringWidth(component) / 2;
        }

        if (bl3) {
            n2 -= 4;
        }

        this.font.draw(graphics, component.getString(), this.getGuiLeft() + n, this.getGuiTop() + n2, n3);
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

    public void drawTooltip(PoseStack poseStack, int x, int y, ItemStack stack) {
        assert !ModUtils.isEmpty(stack);
        this.renderTooltip(poseStack, stack, x, y);
    }

    public void drawTooltipOnlyName(PoseStack poseStack, int x, int y, ItemStack stack, List<String> strings) {
        if (stack.isEmpty()) {
            return;
        }
        List<Component> tooltipComponents = new ArrayList<>();
        tooltipComponents.add(stack.getDisplayName());
        strings.forEach(s -> tooltipComponents.add(Component.literal(s)));
        this.renderComponentTooltip(poseStack, tooltipComponents, x, y);
    }

    public void drawTooltip(int n, int n2, List<String> list) {
        this.queuedTooltips.add(new Tooltip(list, n, n2));
    }

    protected void flushTooltips(PoseStack graphics) {
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
            this.renderTooltip(graphics, tooltip.text, Optional.empty(), tooltip.x, tooltip.y);
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

    public void drawTexturedModalRect(PoseStack poseStack, int i, int i1, int i2, int i3, int i4, int i5) {
        blit(poseStack, i, i1, i2, i3, i4, i5);
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
