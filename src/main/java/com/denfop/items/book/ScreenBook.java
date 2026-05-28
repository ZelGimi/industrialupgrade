package com.denfop.items.book;

import com.denfop.Constants;
import com.denfop.api.guidebook.*;
import com.denfop.api.widget.ItemWidget;
import com.denfop.api.widget.ScrollDirection;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.network.packet.PacketItemStackEvent;
import com.denfop.network.packet.PacketUpdateBookMarks;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static com.denfop.screen.ScreenResearchTableSpace.enableScissor;
import static com.mojang.blaze3d.systems.RenderSystem.disableScissor;

@OnlyIn(Dist.CLIENT)
public class ScreenBook<T extends ContainerMenuBook> extends ScreenMain<ContainerMenuBook> {

    public static final ResourceLocation background =
            ResourceLocation.fromNamespaceAndPath(Constants.TEXTURES, "textures/gui/guidebook.png");
    public static final ResourceLocation sprites =
            ResourceLocation.fromNamespaceAndPath(Constants.TEXTURES, "textures/gui/sprites.png");
    public static final ResourceLocation sprites_lines =
            ResourceLocation.fromNamespaceAndPath(Constants.TEXTURES, "textures/gui/slider_guide.png");
    public static final ResourceLocation background1 =
            ResourceLocation.fromNamespaceAndPath(Constants.TEXTURES, "textures/gui/guidebook1.png");

    private static final int GUI_WIDTH = 255;
    private static final int GUI_HEIGHT = 195;

    private static final int VIEW_X = 8;
    private static final int VIEW_Y = 9;
    private static final int VIEW_W = 238;
    private static final int VIEW_H = 176;

    private static final int CONTENT_ANCHOR_X = 116;
    private static final int CONTENT_ANCHOR_Y = 73;

    private static final int BOOKMARK_SPACING = 25;
    private static final int DEFAULT_NODE_SIZE = 24;
    private static final int EPIC_NODE_SIZE = 26;

    private static final int CONTENT_PADDING = 18;
    private static final int WHEEL_SCROLL_STEP = 28;

    private static final double SMOOTH_FACTOR = 0.35D;
    private static final double SNAP_EPSILON = 0.35D;

    private static final double MIN_ZOOM = 0.20D;
    private static final double MAX_ZOOM = 3.00D;
    private static final double ZOOM_STEP_FACTOR = 1.14D;

    private static final int SEARCH_BOX_X = 150;
    private static final int SEARCH_BOX_Y = 195 + 10;
    private static final int SEARCH_BOX_W = 82;
    private static final int SEARCH_BOX_H = 14;

    private static final int SEARCH_GLOW_OUTER = 0x45FFF176;
    private static final int SEARCH_GLOW_MIDDLE = 0x70FFD54F;
    private static final int SEARCH_GLOW_INNER = 0x95FFF59D;
    private static final int SEARCH_DIM_COLOR = 0x66000000;

    private final Player player;
    private final List<Quest> questList = new ArrayList<>();
    private final LinkedList<GuideQuest> guideQuests = new LinkedList<>();
    private final List<Tuple<Integer, Integer>> listBookMark = new LinkedList<>();
    public int tab = 0;
    private EditBox searchBox;
    private Map<String, List<String>> map;

    private boolean hoverDiscord = false;
    private boolean hoverGithub = false;
    private boolean hoverPU = false;
    private boolean hoverQG = false;
    private boolean hoverSQ = false;
    private boolean hoverWiki = false;
    private boolean hoverYoutube = false;
    private boolean hoverDeveloper = false;

    private boolean bookMark = false;

    private int[] bookMarksSize = new int[]{0, 0};

    private double targetOffsetX = 0.0D;
    private double targetOffsetY = 0.0D;
    private double renderOffsetX = 0.0D;
    private double renderOffsetY = 0.0D;

    private double targetZoom = 1.0D;
    private double renderZoom = 1.0D;

    private boolean contentDragArmed = false;
    private boolean questDragArmed = false;
    private GuideQuest draggedGuideQuest = null;
    private double lastDragMouseX = 0.0D;
    private double lastDragMouseY = 0.0D;

    private boolean scrollStateDirty = false;
    private int lastSyncedTab = 0;
    private int lastSyncedOffsetX = 0;
    private int lastSyncedOffsetY = 0;

    public ScreenBook(Player player, final ItemStack itemStack1, final ContainerMenuBook containerBook) {
        super(containerBook);
        this.player = player;
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        this.elements.clear();
        this.componentList.clear();

        if (container.base.itemStack1.has(DataComponentsInit.MODE)) {
            int[] decode = decode(container.base.itemStack1.get(DataComponentsInit.MODE));
            int maxTab = Math.max(0, GuideBookCore.instance.getGuideTabs().size() - 1);
            this.tab = Mth.clamp(decode[0], 0, maxTab);
            this.targetOffsetX = decode[1];
            this.targetOffsetY = decode[2];
            this.renderOffsetX = this.targetOffsetX;
            this.renderOffsetY = this.targetOffsetY;
        }

        if (container.base.itemStack1.has(DataComponentsInit.BOOKMARK)) {
            List<Tuple<Integer, Integer>> stored = container.base.itemStack1.get(DataComponentsInit.BOOKMARK);
            if (stored != null) {
                this.listBookMark.addAll(stored);
            }
        }

        if (DataComponentsInit.BOOK_ZOOM != null && container.base.itemStack1.has(DataComponentsInit.BOOK_ZOOM)) {
            Float storedZoom = container.base.itemStack1.get(DataComponentsInit.BOOK_ZOOM);
            if (storedZoom != null) {
                this.targetZoom = Mth.clamp(storedZoom, (float) MIN_ZOOM, (float) MAX_ZOOM);
                this.renderZoom = this.targetZoom;
            }
        }

        this.bookMarksSize = calculateGrid(listBookMark.size());
        this.questList.addAll(GuideBookCore.instance.getQuests(tab));
        clampOffsetsImmediately(false);

        this.lastSyncedTab = this.tab;
        this.lastSyncedOffsetX = getRoundedTargetOffsetX();
        this.lastSyncedOffsetY = getRoundedTargetOffsetY();
    }

    public static int encode(int tab, int offsetX, int offsetY) {
        if (tab < 0 || tab > 7) {
            throw new IllegalArgumentException("tab out of range: " + tab);
        }
        if (offsetX < -8192 || offsetX > 8191) {
            throw new IllegalArgumentException("offsetX out of range: " + offsetX);
        }
        if (offsetY < -8192 || offsetY > 8191) {
            throw new IllegalArgumentException("offsetY out of range: " + offsetY);
        }

        int x = offsetX & 0x3FFF;
        int y = offsetY & 0x3FFF;

        return (y << 17) | (x << 3) | (tab & 0b111);
    }

    public static int[] decode(int value) {
        int tab = value & 0b111;
        int x = (value >>> 3) & 0x3FFF;
        int y = (value >>> 17) & 0x3FFF;

        if ((x & 0x2000) != 0) {
            x |= ~0x3FFF;
        }
        if ((y & 0x2000) != 0) {
            y |= ~0x3FFF;
        }

        return new int[]{tab, x, y};
    }

    @Override
    protected void init() {
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        super.init();

        String oldValue = this.searchBox != null ? this.searchBox.getValue() : "";

        this.searchBox = new EditBox(
                this.font,
                this.guiLeft + SEARCH_BOX_X,
                this.guiTop + SEARCH_BOX_Y,
                SEARCH_BOX_W,
                SEARCH_BOX_H,
                Component.empty()
        );
        this.searchBox.setMaxLength(64);
        this.searchBox.setBordered(true);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(0xFFFFFF);
        this.searchBox.setValue(oldValue);
        this.searchBox.setHint(Component.empty());

        this.addRenderableWidget(this.searchBox);
    }

    @Override
    public void containerTick() {
        super.containerTick();

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (hasControlDown() && keyCode == 70) {
            if (this.searchBox != null) {
                this.setFocused(this.searchBox);
                this.searchBox.setFocused(true);
                return true;
            }
        }

        if (this.searchBox != null && this.searchBox.isFocused()) {
            if (keyCode == 256) {
                this.searchBox.setFocused(false);
                return true;
            }
            if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.searchBox != null && this.searchBox.isFocused() && this.searchBox.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    public void addBookMark(int tab, int id) {
        listBookMark.add(new Tuple<>(tab, id));
        container.base.itemStack1.set(DataComponentsInit.BOOKMARK, new ArrayList<>(listBookMark));
        bookMarksSize = calculateGrid(listBookMark.size());
        clampOffsetsImmediately(true);
        new PacketUpdateBookMarks(container.base.itemStack1.getComponentsPatch(), this.player);
    }

    public void removeBookMark(int tab, int id) {
        listBookMark.removeIf(bookMark -> bookMark.getA() == tab && bookMark.getB() == id);
        container.base.itemStack1.set(DataComponentsInit.BOOKMARK, new ArrayList<>(listBookMark));
        new PacketUpdateBookMarks(container.base.itemStack1.getComponentsPatch(), this.player);
        bookMarksSize = calculateGrid(listBookMark.size());
        clampOffsetsImmediately(true);
    }

    public boolean hasBookMark(int tab, int id) {
        for (Tuple<Integer, Integer> tuple : listBookMark) {
            if (tuple.getA() == tab && tuple.getB() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void changeParams() {
        super.changeParams();
        imageHeight = 4000;
        imageWidth = 9000;
    }


    @Override
    public void renderTransparentBackground(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.map = GuideBookCore.uuidGuideMap.get(player.getUUID());
        updateSmoothState();

        if (this.searchBox != null) {
            this.searchBox.setX(this.guiLeft + SEARCH_BOX_X);
            this.searchBox.setY(this.guiTop + SEARCH_BOX_Y);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        poseStack.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;

        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int localX = mouseX - xMin;
        int localY = mouseY - yMin;

        if (this.searchBox != null && this.searchBox.mouseClicked(mouseX, mouseY, button)) {
            return;
        }

        this.contentDragArmed = false;
        this.questDragArmed = false;
        this.draggedGuideQuest = null;
        this.lastDragMouseX = mouseX;
        this.lastDragMouseY = mouseY;

        if (map == null) {
            return;
        }

        for (GuideQuest guideQuest : new ArrayList<>(guideQuests)) {
            if (guideQuest.is(localX, localY)) {
                if (guideQuest.isRemove(localX, localY)) {
                    guideQuests.remove(guideQuest);
                } else if (guideQuest.isComplete(player, tab)) {
                    guideQuest.complete(player, tab);
                } else if (guideQuest.isSkip(player, tab)) {
                    guideQuest.skip(player, tab);
                } else if (guideQuest.isBookMark(player, tab)) {
                    guideQuest.bookMark(this, tab);
                } else if (button == 0) {
                    this.draggedGuideQuest = guideQuest;
                    this.questDragArmed = true;
                }
                return;
            }
        }

        if (handleTabClick(localX, localY)) {
            return;
        }

        if (handleSideButtonsClick(localX, localY)) {
            return;
        }

        if (handleExternalLinksClick(localX, localY)) {
            return;
        }

        if (!bookMark) {
            if (handleQuestNodeClick(localX, localY)) {
                return;
            }
        } else {
            if (handleBookmarkNodeClick(localX, localY)) {
                return;
            }
        }

        if (button == 0 && isInsideViewportLocal(localX, localY)) {
            this.contentDragArmed = true;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalScroll, double verticalScroll) {
        int localMouseX = (int) (mouseX - this.guiLeft);
        int localMouseY = (int) (mouseY - this.guiTop);
        ScrollDirection direction = verticalScroll != 0.0D
                ? (verticalScroll < 0.0D ? ScrollDirection.down : ScrollDirection.up)
                : ScrollDirection.stopped;

        if (direction != ScrollDirection.stopped) {
            for (GuideQuest guideQuest : guideQuests) {
                if (guideQuest.is(localMouseX, localMouseY)) {
                    if (guideQuest.isTextFields(localMouseX, localMouseY) && guideQuest.canScroll(direction)) {
                        guideQuest.scroll(direction);
                        return true;
                    }
                    if (guideQuest.isItems(localMouseX, localMouseY) && guideQuest.canScrollItem(direction)) {
                        guideQuest.scrollItem(direction);
                        return true;
                    }
                }
            }
        }

        if (isInsideViewportLocal(localMouseX, localMouseY) && hasControlDown()) {
            handleZoomScroll(localMouseX, localMouseY, verticalScroll);
            return true;
        }

        if (isInsideViewportLocal(localMouseX, localMouseY)) {
            ScrollBounds bounds = computeScrollBounds(this.targetZoom);
            double step = Math.signum(verticalScroll) * WHEEL_SCROLL_STEP;

            if (hasShiftDown()) {
                this.targetOffsetX = clamp(bounds.minX, bounds.maxX, this.targetOffsetX + step);
            } else {
                this.targetOffsetY = clamp(bounds.minY, bounds.maxY, this.targetOffsetY + step);
            }

            this.scrollStateDirty = true;
            flushScrollState();
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalScroll, verticalScroll);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button != 0) {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        if (this.questDragArmed && this.draggedGuideQuest != null && this.guideQuests.contains(this.draggedGuideQuest)) {
            int dx = (int) Math.round(mouseX - this.lastDragMouseX);
            int dy = (int) Math.round(mouseY - this.lastDragMouseY);

            if (dx != 0 || dy != 0) {
                this.draggedGuideQuest.setOffsetX1(this.draggedGuideQuest.getOffsetX1() + dx);
                this.draggedGuideQuest.setOffsetY1(this.draggedGuideQuest.getOffsetY1() + dy);
            }

            this.lastDragMouseX = mouseX;
            this.lastDragMouseY = mouseY;
            return true;
        }

        if (this.contentDragArmed) {
            ScrollBounds bounds = computeScrollBounds(this.targetZoom);

            double dx = mouseX - this.lastDragMouseX;
            double dy = mouseY - this.lastDragMouseY;

            if (dx != 0.0D || dy != 0.0D) {
                this.targetOffsetX = clamp(bounds.minX, bounds.maxX, this.targetOffsetX + dx);
                this.targetOffsetY = clamp(bounds.minY, bounds.maxY, this.targetOffsetY + dy);

                this.renderOffsetX = this.targetOffsetX;
                this.renderOffsetY = this.targetOffsetY;

                this.scrollStateDirty = true;
            }

            this.lastDragMouseX = mouseX;
            this.lastDragMouseY = mouseY;
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.contentDragArmed = false;
            this.questDragArmed = false;
            this.draggedGuideQuest = null;
            flushScrollState();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void removed() {
        flushScrollState();
        super.removed();
    }

    @Override
    public void drawForegroundLayer(GuiGraphics poseStack, int mouseX, int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);

        for (GuideQuest guideQuest : guideQuests) {
            if (guideQuest.is(mouseX, mouseY)) {
                guideQuest.drawForegroundLayer(this, poseStack, mouseX, mouseY);
                return;
            }
        }

        new TooltipWidget(this, 255, 5, 30, 27)
                .withTooltip(Localization.translate("iu.quest.bookmark"))
                .drawForeground(poseStack, mouseX, mouseY);

        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();
        int y = 5;

        for (int index = 0; index < guideTabs.size(); index++) {
            GuideTab guideTab = guideTabs.get(index);
            boolean isSelectedTab = (index == tab);

            if (isSelectedTab) {
                new TooltipWidget(this, -33, y, 33, 27)
                        .withTooltip(Localization.translate(guideTab.name))
                        .drawForeground(poseStack, mouseX, mouseY);
            } else {
                boolean hasQuests = !getCompletedQuests(guideTab.unLocalized).isEmpty();
                int dx = hasQuests ? -28 : -33;
                int w = hasQuests ? 28 : 33;

                new TooltipWidget(this, dx, y, w, 27)
                        .withTooltip(Localization.translate(guideTab.name))
                        .drawForeground(poseStack, mouseX, mouseY);
            }

            y += 27;
        }

        hoverDiscord = isInRect(mouseX, mouseY, 10, -15, 27, 17);
        hoverGithub = isInRect(mouseX, mouseY, 50, -15, 27, 17);
        hoverYoutube = isInRect(mouseX, mouseY, 90, -15, 27, 17);
        hoverPU = isInRect(mouseX, mouseY, 150, -15, 27, 17);
        hoverQG = isInRect(mouseX, mouseY, 180, -15, 27, 17);
        hoverSQ = isInRect(mouseX, mouseY, 210, -15, 27, 17);

        hoverWiki = isInRect(mouseX, mouseY, 255, 35, 30, 27);
        hoverDeveloper = isInRect(mouseX, mouseY, 255, 65, 30, 27);

        new TooltipWidget(this, 255, 65, 30, 27)
                .withTooltip(Localization.translate("iu.quest.developer"))
                .drawForeground(poseStack, mouseX, mouseY);

        new TooltipWidget(this, 255, 35, 30, 27)
                .withTooltip(Localization.translate("iu.quest.wiki"))
                .drawForeground(poseStack, mouseX, mouseY);

        new TooltipWidget(this, 90, -15, 27, 17)
                .withTooltip(Localization.translate("iu.quest.youtube"))
                .drawForeground(poseStack, mouseX, mouseY);

        if (!isInsideViewportLocal(mouseX, mouseY)) {
            return;
        }

        if (!bookMark) {
            for (Quest quest : GuideBookCore.instance.getQuests(tab)) {
                int contentX = quest.x + getNodeRenderPadding(quest);
                int contentY = quest.y + getNodeRenderPadding(quest);
                int localX = contentToLocalX(contentX);
                int localY = contentToLocalY(contentY);
                int size = scaleSize(getNodeSize(quest));

                if (isRectVisibleInViewport(localX, localY, size, size)) {
                    new TooltipWidget(this, localX, localY, size, size)
                            .withTooltip(quest.getLocalizedName())
                            .drawForeground(poseStack, mouseX, mouseY);
                }
            }
        } else {
            for (int i = 0; i < listBookMark.size(); i++) {
                int[] contentPos = getBookmarkContentPosition(i);
                Tuple<Integer, Integer> tuple = listBookMark.get(i);
                Quest quest = GuideBookCore.instance.getQuests(tuple.getA()).get(tuple.getB());

                int localX = contentToLocalX(contentPos[0]);
                int localY = contentToLocalY(contentPos[1]);
                int size = scaleSize(getNodeSize(quest));

                if (isRectVisibleInViewport(localX, localY, size, size)) {
                    new TooltipWidget(this, localX, localY, size, size)
                            .withTooltip(quest.getLocalizedName())
                            .drawForeground(poseStack, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);

        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;

        if (map == null) {
            return;
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);

        bindTexture(sprites);

        if (!hoverDiscord) {
            drawTexturedModalRect(poseStack, this.guiLeft + 10, this.guiTop - 15, 202, 7, 27, 17);
        } else {
            drawTexturedModalRect(poseStack, this.guiLeft + 10, this.guiTop - 21, 229, 1, 27, 22);
        }

        if (!hoverGithub) {
            drawTexturedModalRect(poseStack, this.guiLeft + 50, this.guiTop - 15, 202, 29, 27, 17);
        } else {
            drawTexturedModalRect(poseStack, this.guiLeft + 50, this.guiTop - 21, 229, 23, 27, 22);
        }

        if (!hoverYoutube) {
            drawTexturedModalRect(poseStack, this.guiLeft + 90, this.guiTop - 15, 202, 117, 27, 17);
        } else {
            drawTexturedModalRect(poseStack, this.guiLeft + 90, this.guiTop - 21, 229, 111, 27, 22);
        }

        if (!hoverPU) {
            drawTexturedModalRect(poseStack, this.guiLeft + 150, this.guiTop - 15, 202, 51, 27, 17);
        } else {
            drawTexturedModalRect(poseStack, this.guiLeft + 150, this.guiTop - 21, 229, 45, 27, 22);
        }

        if (!hoverQG) {
            drawTexturedModalRect(poseStack, this.guiLeft + 180, this.guiTop - 15, 202, 73, 27, 17);
        } else {
            drawTexturedModalRect(poseStack, this.guiLeft + 180, this.guiTop - 21, 229, 67, 27, 22);
        }

        if (!hoverSQ) {
            drawTexturedModalRect(poseStack, this.guiLeft + 210, this.guiTop - 15, 202, 95, 27, 17);
        } else {
            drawTexturedModalRect(poseStack, this.guiLeft + 210, this.guiTop - 21, 229, 89, 27, 22);
        }

        drawTabs(poseStack);

        bindTexture(sprites);
        drawTexturedModalRect(poseStack, guiLeft + 255, guiTop + 5, 140, 1, 26, 27);
        if (bookMark) {
            drawTexturedModalRect(poseStack, guiLeft + 255, guiTop + 5, 171, 1, 30, 27);
        }

        drawTexturedModalRect(poseStack, guiLeft + 255, guiTop + 35, 140, 57, 26, 27);
        if (hoverWiki) {
            drawTexturedModalRect(poseStack, guiLeft + 255, guiTop + 35, 171, 57, 30, 27);
        }

        drawTexturedModalRect(poseStack, guiLeft + 255, guiTop + 65, 140, 85, 30, 27);
        if (hoverDeveloper) {
            drawTexturedModalRect(poseStack, guiLeft + 255, guiTop + 65, 171, 85, 30, 27);
        }

        drawSearchInfo(poseStack);
        renderViewportContent(poseStack);
        renderGuideQuestWindows(poseStack);
    }

    public void renderLines(GuiGraphics poseStack, Quest current, Lines lines) {
        int x = current.x + 3;
        int y = current.y + 3;
        int prevX = current.prevX + 3;
        int prevY = current.prevY + 3;
        Shape prevShape = current.prevShape;
        Shape shape = current.shape;

        int startPosRender = 24;
        int endPosRender = 24;

        switch (prevShape) {
            case DEFAULT:
            case UNIQUE:
                startPosRender = 24;
                break;
            case EPIC:
                startPosRender = 26;
                break;
        }

        switch (shape) {
            case DEFAULT:
            case UNIQUE:
                endPosRender = 24;
                break;
            case EPIC:
                endPosRender = 26;
                break;
        }

        int dx = Math.abs(prevX - x);
        int dy = Math.abs(prevY - y);
        if (prevY < 0 && y < 0) {
            dy = Math.abs(-prevY + y);
        }

        boolean firstHorizontal = dx < dy;

        if (firstHorizontal) {
            if (prevX < x) {
                drawHorizontalLine(
                        poseStack,
                        prevX + startPosRender,
                        prevY + startPosRender / 2 - 1,
                        x + startPosRender / 2 + 1,
                        prevY,
                        lines
                );

                if (y - 1 > 0) {
                    drawVerticalLine(poseStack, x + startPosRender / 2 - 1, prevY + startPosRender / 2, x, y, lines);
                } else {
                    drawVerticalLine(poseStack, x + startPosRender / 2 - 1, y + startPosRender, x, prevY + startPosRender / 2, lines);
                }
            } else {
                drawHorizontalLine(
                        poseStack,
                        x + startPosRender,
                        prevY + startPosRender / 2 - 1,
                        prevX + startPosRender / 2 + 1,
                        prevY,
                        lines
                );

                if (y > prevY) {
                    if (y - 1 > 0) {
                        drawVerticalLine(poseStack, x + startPosRender / 2 - 1, prevY + startPosRender, x, y, lines);
                    } else {
                        drawVerticalLine(poseStack, x + startPosRender / 2 - 1, y + startPosRender, x, prevY + startPosRender / 2, lines);
                    }
                } else {
                    if (y - 1 > 0) {
                        drawVerticalLine(poseStack, x + startPosRender / 2 - 1, y + startPosRender, x, prevY, lines);
                    } else {
                        drawVerticalLine(poseStack, x + startPosRender / 2 - 1, y + startPosRender, x, prevY, lines);
                    }
                }
            }
        } else {
            if (y - 1 > 0) {
                if (y > prevY) {
                    drawVerticalLine(
                            poseStack,
                            prevX + startPosRender / 2 - 1,
                            prevY + startPosRender,
                            prevX + startPosRender,
                            y + startPosRender / 2 + 1,
                            lines
                    );

                    if (prevX < x) {
                        drawHorizontalLine(poseStack, prevX + startPosRender / 2, y + startPosRender / 2 - 1, x, y, lines);
                    } else {
                        drawHorizontalLine(
                                poseStack,
                                x + startPosRender,
                                y + startPosRender / 2 - 1,
                                prevX + startPosRender / 2,
                                y,
                                lines
                        );
                    }
                } else {
                    drawVerticalLine(
                            poseStack,
                            prevX + startPosRender / 2 - 1,
                            y + startPosRender / 2 - 1,
                            prevX + startPosRender,
                            prevY,
                            lines
                    );

                    if (prevX < x) {
                        drawHorizontalLine(poseStack, prevX + startPosRender, y + startPosRender / 2 - 1, x, y, lines);
                    } else {
                        if (y == prevY) {
                            drawHorizontalLine(poseStack, x + startPosRender, y + startPosRender / 2 - 1, prevX, y, lines);
                        } else {
                            drawHorizontalLine(
                                    poseStack,
                                    x + startPosRender,
                                    y + startPosRender / 2 - 1,
                                    prevX + startPosRender / 2,
                                    y,
                                    lines
                            );
                        }
                    }
                }
            } else {
                if ((prevY > 0 && y < 0) || (prevY < 0 && y > 0)) {
                    drawVerticalLine(
                            poseStack,
                            prevX + startPosRender / 2 - 1,
                            y + startPosRender / 2 - 1,
                            prevX + startPosRender,
                            prevY,
                            lines
                    );

                    if (prevX < x) {
                        drawHorizontalLine(poseStack, prevX + startPosRender / 2, y + startPosRender / 2 - 1, x, y, lines);
                    } else {
                        drawHorizontalLine(poseStack, x + startPosRender / 2, y + startPosRender / 2 - 1, prevX, y, lines);
                    }
                } else {
                    drawVerticalLine(
                            poseStack,
                            prevX + startPosRender / 2 - 1,
                            prevY + startPosRender,
                            prevX + startPosRender,
                            y + startPosRender,
                            lines
                    );

                    if (prevX < x) {
                        drawHorizontalLine(poseStack, prevX + startPosRender, y + startPosRender / 2 - 1, x, y, lines);
                    } else {
                        drawHorizontalLine(poseStack, x + startPosRender, y + startPosRender / 2 - 1, prevX, y, lines);
                    }
                }
            }
        }
    }

    public void drawHorizontalLine(GuiGraphics poseStack, int startX, int startY, int endX, int endY, Lines line) {
        bindTexture(sprites_lines);
        this.drawTexturedModalRect(
                poseStack,
                startX,
                startY,
                line.getHX(),
                line.getHY(),
                endX - startX,
                2
        );
    }

    public void drawVerticalLine(GuiGraphics poseStack, int startX, int startY, int endX, int endY, Lines line) {
        bindTexture(sprites_lines);
        this.drawTexturedModalRect(
                poseStack,
                startX,
                startY,
                line.getVX(),
                line.getVY(),
                2,
                endY - startY
        );
    }

    private void drawTabs(GuiGraphics poseStack) {
        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();
        int y = 5;

        for (int i = 0; i < guideTabs.size(); i++) {
            GuideTab guideTab = guideTabs.get(i);
            List<String> completed = getCompletedQuests(guideTab.unLocalized);
            boolean isSelectedTab = i == tab;
            boolean hasQuests = !completed.isEmpty();

            bindTexture(sprites);

            if (isSelectedTab) {
                this.drawTexturedModalRect(poseStack, guiLeft - 33, guiTop + y, 0, hasQuests ? 0 : 56, 33, 27);
                new ItemWidget(this, -33 + 9, y + 6, () -> guideTab.icon).drawBackground(poseStack, guiLeft, guiTop);
            } else {
                int dx = hasQuests ? -28 : -33;
                int u = hasQuests ? 5 : 0;
                int v = hasQuests ? 28 : 56;
                int w = hasQuests ? 28 : 33;

                drawTexturedModalRect(poseStack, guiLeft + dx, guiTop + y, u, v, w, 27);
                new ItemWidget(this, dx + 8, y + 6, () -> guideTab.icon).drawBackground(poseStack, guiLeft, guiTop);
            }

            y += 27;
        }
    }

    private void renderViewportContent(GuiGraphics poseStack) {
        beginViewportScissor();
        try {
            poseStack.pose().pushPose();
            poseStack.pose().translate(
                    guiLeft + CONTENT_ANCHOR_X + this.renderOffsetX,
                    guiTop + CONTENT_ANCHOR_Y + this.renderOffsetY,
                    0.0F
            );
            poseStack.pose().scale((float) this.renderZoom, (float) this.renderZoom, 1.0F);

            if (!bookMark) {
                renderQuestTreeTransformed(poseStack);
            } else {
                renderBookmarksTransformed(poseStack);
            }

            poseStack.pose().popPose();
        } finally {
            endViewportScissor();
        }
    }

    private void renderQuestTreeTransformed(GuiGraphics poseStack) {
        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();
        if (guideTabs.isEmpty() || tab < 0 || tab >= guideTabs.size()) {
            return;
        }

        GuideTab guideTab = guideTabs.get(tab);
        List<String> quests = getCompletedQuests(guideTab.unLocalized);
        boolean searchActive = isSearchActive();

        for (Quest quest : GuideBookCore.instance.getQuests(tab)) {
            bindTexture(sprites);

            boolean hasPrev = quest.hasPrev;
            boolean isUnlocked = hasPrev ? quests.contains(quest.prevName) : quests.contains(quest.unLocalizedName);
            boolean matched = matchesSearch(quest);

            int xOffset = quest.x + getNodeRenderPadding(quest);
            int yOffset = quest.y + getNodeRenderPadding(quest);

            int texX = switch (quest.shape) {
                case DEFAULT -> 36;
                case UNIQUE -> 61;
                case EPIC -> 86;
            };

            int texY;
            if (hasPrev) {
                texY = isUnlocked ? 28 : 1;
                if (!quests.contains(quest.unLocalizedName)) {
                    texY = 57;
                }
            } else {
                texY = isUnlocked ? 1 : 57;
            }

            int nodeSize = getNodeSize(quest);

            if (searchActive && matched) {
                drawSearchGlow(poseStack, xOffset, yOffset, nodeSize);
            }

            if (hasPrev) {
                bindTexture(sprites_lines);

                boolean prevMatched = false;
                Quest prevQuest = GuideBookCore.instance.getPrev(quest.prevName, guideTab);
                if (prevQuest != null) {
                    prevMatched = matchesSearch(prevQuest);
                }

                if (searchActive && !(matched || prevMatched)) {
                    RenderSystem.setShaderColor(0.45F, 0.45F, 0.45F, 0.70F);
                } else {
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }

                renderLines(poseStack, quest, getLine(quest, quests, guideTab));
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                bindTexture(sprites);
            }

            drawTexturedModalRect(poseStack, xOffset, yOffset, texX, texY, nodeSize, nodeSize);
            renderScaledQuestItem(poseStack, quest.icon, quest.x + 7, quest.y + 7);

            if (searchActive && !matched) {
                poseStack.fill(xOffset, yOffset, xOffset + nodeSize, yOffset + nodeSize, SEARCH_DIM_COLOR);
            }
        }
    }

    private void renderBookmarksTransformed(GuiGraphics poseStack) {
        boolean searchActive = isSearchActive();

        for (int i = 0; i < listBookMark.size(); i++) {
            int[] contentPos = getBookmarkContentPosition(i);
            int drawX = contentPos[0];
            int drawY = contentPos[1];

            Tuple<Integer, Integer> tuple = listBookMark.get(i);
            Quest quest = GuideBookCore.instance.getQuests(tuple.getA()).get(tuple.getB());

            boolean hasPrev = quest.hasPrev;
            List<String> quests = getCompletedQuests(GuideBookCore.instance.getGuideTabs().get(tuple.getA()).unLocalized);
            boolean isUnlocked = hasPrev ? quests.contains(quest.prevName) : quests.contains(quest.unLocalizedName);
            boolean matched = matchesSearch(quest);

            int texX = switch (quest.shape) {
                case DEFAULT -> 36;
                case UNIQUE -> 61;
                case EPIC -> 86;
            };

            int texY;
            if (hasPrev) {
                texY = isUnlocked ? 28 : 1;
                if (!quests.contains(quest.unLocalizedName)) {
                    texY = 57;
                }
            } else {
                texY = isUnlocked ? 1 : 57;
            }

            int nodeSize = getNodeSize(quest);

            if (searchActive && matched) {
                drawSearchGlow(poseStack, drawX, drawY, nodeSize);
            }

            bindTexture(sprites);
            drawTexturedModalRect(poseStack, drawX, drawY, texX, texY, nodeSize, nodeSize);
            renderScaledQuestItem(poseStack, quest.icon, drawX + 4, drawY + 4);

            if (searchActive && !matched) {
                poseStack.fill(drawX, drawY, drawX + nodeSize, drawY + nodeSize, SEARCH_DIM_COLOR);
            }
        }
    }

    private void renderGuideQuestWindows(GuiGraphics poseStack) {
        if (guideQuests.isEmpty()) {
            return;
        }

        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();
        List<String> currentCompleted = Collections.emptyList();
        if (!guideTabs.isEmpty() && tab >= 0 && tab < guideTabs.size()) {
            currentCompleted = getCompletedQuests(guideTabs.get(tab).unLocalized);
        }

        for (GuideQuest guideQuest : guideQuests) {
            poseStack.pose().pushPose();
            poseStack.pose().translate(0.0F, 0.0F, 300.0F);
            guideQuest.drawBackgroundLayer(this, poseStack, guiLeft, guiTop,
                    !currentCompleted.contains(guideQuest.getQuest().unLocalizedName));
            poseStack.pose().popPose();
        }
    }

    private boolean handleTabClick(int localX, int localY) {
        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();
        int y = 5;

        for (int index = 0; index < guideTabs.size(); index++) {
            if (index != tab) {
                GuideTab guideTab = guideTabs.get(index);
                boolean hasQuests = !getCompletedQuests(guideTab.unLocalized).isEmpty();

                int dx = hasQuests ? -28 : -33;
                int width = hasQuests ? 28 : 33;

                if (isInRect(localX, localY, dx, y, width, 27)) {
                    selectTab(index);
                    return true;
                }
            }
            y += 27;
        }

        return false;
    }

    private boolean handleSideButtonsClick(int localX, int localY) {
        if (!bookMark && isInRect(localX, localY, 255, 5, 30, 27)) {
            openBookmarks();
            return true;
        }
        return false;
    }

    private boolean handleExternalLinksClick(int localX, int localY) {
        if (isInRect(localX, localY, 10, -15, 27, 17)) {
            openUri("https://discord.com/invite/fqQPH6HKJV");
            return true;
        }
        if (isInRect(localX, localY, 50, -15, 27, 17)) {
            openUri("https://github.com/ZelGimi/industrialupgrade");
            return true;
        }
        if (isInRect(localX, localY, 90, -15, 27, 17)) {
            openUri("https://www.youtube.com/watch?v=iyCaNkGM77k&list=PLHDBETKnEsdwcMxHDxI75eYkuthlqIjox&index=2");
            return true;
        }
        if (isInRect(localX, localY, 150, -15, 27, 17)) {
            openUri("https://www.curseforge.com/minecraft/mc-mods/power-utilities-iu");
            return true;
        }
        if (isInRect(localX, localY, 180, -15, 27, 17)) {
            openUri("https://www.curseforge.com/minecraft/mc-mods//quantum-generators");
            return true;
        }
        if (isInRect(localX, localY, 210, -15, 27, 17)) {
            openUri("https://www.curseforge.com/minecraft/mc-mods/simply-quarries");
            return true;
        }
        if (isInRect(localX, localY, 255, 35, 30, 27)) {
            openUri("https://zelgimi.github.io/industrialupgrade/docs/intro");
            return true;
        }
        if (isInRect(localX, localY, 255, 65, 30, 27)) {
            openUri("https://zelgimi.github.io/industrialupgrade/docs/kubejs/");
            return true;
        }
        return false;
    }

    private boolean handleQuestNodeClick(int localX, int localY) {
        if (!isInsideViewportLocal(localX, localY)) {
            return false;
        }

        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();
        if (guideTabs.isEmpty() || tab < 0 || tab >= guideTabs.size()) {
            return false;
        }

        GuideTab guideTab = guideTabs.get(tab);
        List<String> quests = getCompletedQuests(guideTab.unLocalized);

        double contentX = localToContentX(localX);
        double contentY = localToContentY(localY);

        int index = 0;
        for (Quest quest : GuideBookCore.instance.getQuests(tab)) {
            int nodeX = quest.x + getNodeRenderPadding(quest);
            int nodeY = quest.y + getNodeRenderPadding(quest);
            int nodeSize = getNodeSize(quest);

            boolean hasPrev = quest.hasPrev;
            boolean isUnlocked = hasPrev ? quests.contains(quest.prevName) : false;

            if (contentX >= nodeX && contentX <= nodeX + nodeSize
                    && contentY >= nodeY && contentY <= nodeY + nodeSize) {

                GuideQuest key = new GuideQuest(quest, tab, index);

                if (this.guideQuests.contains(key)) {
                    this.guideQuests.removeIf(guideQuest -> guideQuest.getQuest().equals(quest));
                } else if (guideQuests.size() <= 1) {
                    this.guideQuests.add(new GuideQuest(quest, container.base.player, isUnlocked, tab, index));
                }
                return true;
            }

            index++;
        }

        return false;
    }

    private boolean handleBookmarkNodeClick(int localX, int localY) {
        if (!isInsideViewportLocal(localX, localY)) {
            return false;
        }

        double contentX = localToContentX(localX);
        double contentY = localToContentY(localY);

        for (int i = 0; i < listBookMark.size(); i++) {
            int[] pos = getBookmarkContentPosition(i);
            Tuple<Integer, Integer> tuple = listBookMark.get(i);

            Quest quest = GuideBookCore.instance.getQuests(tuple.getA()).get(tuple.getB());
            GuideTab guideTab = GuideBookCore.instance.getGuideTabs().get(tuple.getA());
            List<String> quests = getCompletedQuests(guideTab.unLocalized);
            boolean hasPrev = quest.hasPrev;
            boolean isUnlocked = hasPrev ? quests.contains(quest.prevName) : false;
            int nodeSize = getNodeSize(quest);

            if (contentX >= pos[0] && contentX <= pos[0] + nodeSize
                    && contentY >= pos[1] && contentY <= pos[1] + nodeSize) {

                GuideQuest key = new GuideQuest(quest, tuple.getA(), tuple.getB());

                if (this.guideQuests.contains(key)) {
                    this.guideQuests.removeIf(guideQuest -> guideQuest.getQuest().equals(quest));
                } else if (guideQuests.size() <= 1) {
                    this.guideQuests.add(new GuideQuest(quest, container.base.player, isUnlocked, tuple.getA(), tuple.getB()));
                }
                return true;
            }
        }

        return false;
    }

    private void selectTab(int newTab) {
        this.tab = newTab;
        this.bookMark = false;
        this.guideQuests.clear();
        this.questList.clear();
        this.questList.addAll(GuideBookCore.instance.getQuests(newTab));
        setOffsetsImmediate(0.0D, 0.0D, true);
        flushScrollState();
    }

    private void openBookmarks() {
        this.bookMark = true;
        this.tab = 0;
        this.guideQuests.clear();
        this.bookMarksSize = calculateGrid(listBookMark.size());
        setOffsetsImmediate(0.0D, 0.0D, true);
        flushScrollState();
    }

    private void handleZoomScroll(double localMouseX, double localMouseY, double delta) {
        if (delta == 0.0D) {
            return;
        }

        double oldZoom = this.targetZoom;
        double factor = Math.pow(ZOOM_STEP_FACTOR, delta);
        double newZoom = Mth.clamp(oldZoom * factor, MIN_ZOOM, MAX_ZOOM);

        if (Math.abs(newZoom - oldZoom) < 1.0E-6D) {
            return;
        }

        double contentX = (localMouseX - CONTENT_ANCHOR_X - this.targetOffsetX) / oldZoom;
        double contentY = (localMouseY - CONTENT_ANCHOR_Y - this.targetOffsetY) / oldZoom;

        this.targetZoom = newZoom;

        ScrollBounds bounds = computeScrollBounds(newZoom);
        this.targetOffsetX = localMouseX - CONTENT_ANCHOR_X - contentX * newZoom;
        this.targetOffsetY = localMouseY - CONTENT_ANCHOR_Y - contentY * newZoom;

        this.targetOffsetX = clamp(bounds.minX, bounds.maxX, this.targetOffsetX);
        this.targetOffsetY = clamp(bounds.minY, bounds.maxY, this.targetOffsetY);

        if (DataComponentsInit.BOOK_ZOOM != null) {
            container.base.itemStack1.set(DataComponentsInit.BOOK_ZOOM, (float) this.targetZoom);
        }

        this.scrollStateDirty = true;
        flushScrollState();
    }

    private void updateSmoothState() {
        ScrollBounds bounds = computeScrollBounds(this.targetZoom);

        this.targetOffsetX = clamp(bounds.minX, bounds.maxX, this.targetOffsetX);
        this.targetOffsetY = clamp(bounds.minY, bounds.maxY, this.targetOffsetY);
        this.targetZoom = Mth.clamp(this.targetZoom, MIN_ZOOM, MAX_ZOOM);

        if (this.contentDragArmed) {
            this.renderOffsetX = this.targetOffsetX;
            this.renderOffsetY = this.targetOffsetY;
        } else {
            this.renderOffsetX += (this.targetOffsetX - this.renderOffsetX) * SMOOTH_FACTOR;
            this.renderOffsetY += (this.targetOffsetY - this.renderOffsetY) * SMOOTH_FACTOR;

            if (Math.abs(this.targetOffsetX - this.renderOffsetX) <= SNAP_EPSILON) {
                this.renderOffsetX = this.targetOffsetX;
            }
            if (Math.abs(this.targetOffsetY - this.renderOffsetY) <= SNAP_EPSILON) {
                this.renderOffsetY = this.targetOffsetY;
            }
        }

        this.renderZoom += (this.targetZoom - this.renderZoom) * SMOOTH_FACTOR;
        if (Math.abs(this.targetZoom - this.renderZoom) <= 0.0025D) {
            this.renderZoom = this.targetZoom;
        }
    }

    private void clampOffsetsImmediately(boolean markDirty) {
        ScrollBounds bounds = computeScrollBounds(this.targetZoom);
        this.targetOffsetX = clamp(bounds.minX, bounds.maxX, this.targetOffsetX);
        this.targetOffsetY = clamp(bounds.minY, bounds.maxY, this.targetOffsetY);
        this.renderOffsetX = this.targetOffsetX;
        this.renderOffsetY = this.targetOffsetY;
        this.renderZoom = this.targetZoom;

        if (markDirty) {
            this.scrollStateDirty = true;
        }
    }

    private void setOffsetsImmediate(double x, double y, boolean markDirty) {
        this.targetOffsetX = x;
        this.targetOffsetY = y;
        clampOffsetsImmediately(markDirty);
    }

    private void flushScrollState() {
        if (!this.scrollStateDirty || this.minecraft == null || this.minecraft.player == null) {
            return;
        }

        int roundedX = getRoundedTargetOffsetX();
        int roundedY = getRoundedTargetOffsetY();

        if (roundedX != lastSyncedOffsetX || roundedY != lastSyncedOffsetY || this.tab != lastSyncedTab) {
            int encoded = encode(this.tab, roundedX, roundedY);
            new PacketItemStackEvent(encoded, minecraft.player);
            container.base.itemStack1.set(DataComponentsInit.MODE, encoded);

            this.lastSyncedTab = this.tab;
            this.lastSyncedOffsetX = roundedX;
            this.lastSyncedOffsetY = roundedY;
        }

        this.scrollStateDirty = false;
    }

    private boolean isSearchActive() {
        return this.searchBox != null && !normalizeSearch(this.searchBox.getValue()).isEmpty();
    }

    private boolean matchesSearch(Quest quest) {
        String query = getSearchQuery();
        if (query.isEmpty()) {
            return false;
        }

        String localized = normalizeSearch(quest.getLocalizedName());
        String unlocalized = normalizeSearch(quest.unLocalizedName);

        return matchesSearchText(localized, query) || matchesSearchText(unlocalized, query);
    }

    private boolean matchesSearchText(String text, String query) {
        if (text.isEmpty() || query.isEmpty()) {
            return false;
        }

        if (text.equals(query) || text.contains(query)) {
            return true;
        }

        String[] parts = query.split("\\s+");
        if (parts.length == 0) {
            return false;
        }

        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            if (!text.contains(part)) {
                return false;
            }
        }

        return true;
    }

    private String getSearchQuery() {
        if (this.searchBox == null) {
            return "";
        }
        return normalizeSearch(this.searchBox.getValue());
    }

    private String normalizeSearch(String value) {
        if (value == null) {
            return "";
        }
        return value.toLowerCase(Locale.ROOT).trim();
    }

    private void drawSearchGlow(GuiGraphics guiGraphics, int x, int y, int size) {
        drawBorderRect(guiGraphics, x - 3, y - 3, size + 6, size + 6, SEARCH_GLOW_OUTER);
        drawBorderRect(guiGraphics, x - 2, y - 2, size + 4, size + 4, SEARCH_GLOW_MIDDLE);
        drawBorderRect(guiGraphics, x - 1, y - 1, size + 2, size + 2, SEARCH_GLOW_INNER);
    }

    private void drawBorderRect(GuiGraphics guiGraphics, int x, int y, int w, int h, int color) {
        guiGraphics.fill(x, y, x + w, y + 1, color);
        guiGraphics.fill(x, y + h - 1, x + w, y + h, color);
        guiGraphics.fill(x, y, x + 1, y + h, color);
        guiGraphics.fill(x + w - 1, y, x + w, y + h, color);
    }

    private int getSearchMatchCount() {
        if (!isSearchActive()) {
            return 0;
        }

        int count = 0;

        if (!bookMark) {
            for (Quest quest : GuideBookCore.instance.getQuests(tab)) {
                if (matchesSearch(quest)) {
                    count++;
                }
            }
        } else {
            for (Tuple<Integer, Integer> tuple : listBookMark) {
                Quest quest = GuideBookCore.instance.getQuests(tuple.getA()).get(tuple.getB());
                if (matchesSearch(quest)) {
                    count++;
                }
            }
        }

        return count;
    }

    private void drawSearchInfo(GuiGraphics guiGraphics) {
        if (this.searchBox == null) {
            return;
        }

        guiGraphics.drawString(
                this.font,
                "Пошук",
                this.guiLeft + SEARCH_BOX_X,
                this.guiTop + SEARCH_BOX_Y - 10,
                0xCFCFCF,
                false
        );


    }

    private void renderScaledQuestItem(GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        guiGraphics.renderItem(stack, x, y);
    }

    private ScrollBounds computeScrollBounds(double zoom) {
        double minContentX;
        double maxContentX;
        double minContentY;
        double maxContentY;

        if (bookMark) {
            if (listBookMark.isEmpty() || bookMarksSize[0] <= 0 || bookMarksSize[1] <= 0) {
                return createFixedBounds(0.0D, 0.0D);
            }

            int rows = bookMarksSize[0];
            int cols = bookMarksSize[1];

            minContentX = -CONTENT_PADDING;
            minContentY = -CONTENT_PADDING;
            maxContentX = (cols - 1) * BOOKMARK_SPACING + EPIC_NODE_SIZE + CONTENT_PADDING;
            maxContentY = (rows - 1) * BOOKMARK_SPACING + EPIC_NODE_SIZE + CONTENT_PADDING;
        } else {
            List<Quest> quests = GuideBookCore.instance.getQuests(tab);
            if (quests.isEmpty()) {
                return createFixedBounds(0.0D, 0.0D);
            }

            minContentX = Double.POSITIVE_INFINITY;
            minContentY = Double.POSITIVE_INFINITY;
            maxContentX = Double.NEGATIVE_INFINITY;
            maxContentY = Double.NEGATIVE_INFINITY;

            for (Quest quest : quests) {
                int nodePad = getNodeRenderPadding(quest);
                int nodeSize = getNodeSize(quest);

                minContentX = Math.min(minContentX, quest.x + nodePad - CONTENT_PADDING);
                minContentY = Math.min(minContentY, quest.y + nodePad - CONTENT_PADDING);
                maxContentX = Math.max(maxContentX, quest.x + nodePad + nodeSize + CONTENT_PADDING);
                maxContentY = Math.max(maxContentY, quest.y + nodePad + nodeSize + CONTENT_PADDING);

                if (quest.hasPrev) {
                    int prevPad = getShapePadding(quest.prevShape);
                    int prevSize = getShapeSize(quest.prevShape);

                    minContentX = Math.min(minContentX, quest.prevX + prevPad - CONTENT_PADDING);
                    minContentY = Math.min(minContentY, quest.prevY + prevPad - CONTENT_PADDING);
                    maxContentX = Math.max(maxContentX, quest.prevX + prevPad + prevSize + CONTENT_PADDING);
                    maxContentY = Math.max(maxContentY, quest.prevY + prevPad + prevSize + CONTENT_PADDING);
                }
            }
        }

        double minOffsetX = (VIEW_X + VIEW_W) - (CONTENT_ANCHOR_X + maxContentX * zoom);
        double maxOffsetX = VIEW_X - (CONTENT_ANCHOR_X + minContentX * zoom);
        double minOffsetY = (VIEW_Y + VIEW_H) - (CONTENT_ANCHOR_Y + maxContentY * zoom);
        double maxOffsetY = VIEW_Y - (CONTENT_ANCHOR_Y + minContentY * zoom);

        if (minOffsetX > maxOffsetX) {
            double centered = (minOffsetX + maxOffsetX) * 0.5D;
            minOffsetX = centered;
            maxOffsetX = centered;
        }

        if (minOffsetY > maxOffsetY) {
            double centered = (minOffsetY + maxOffsetY) * 0.5D;
            minOffsetY = centered;
            maxOffsetY = centered;
        }

        return new ScrollBounds(minOffsetX, maxOffsetX, minOffsetY, maxOffsetY);
    }

    private ScrollBounds createFixedBounds(double x, double y) {
        return new ScrollBounds(x, x, y, y);
    }

    private List<String> getCompletedQuests(String tabKey) {
        if (this.map == null) {
            return Collections.emptyList();
        }
        List<String> quests = this.map.get(tabKey);
        return quests == null ? Collections.emptyList() : quests;
    }

    public int[] calculateGrid(int count) {
        if (count <= 0) {
            return new int[]{0, 0};
        }

        int bestRows = 1;
        int bestCols = count;
        int bestDiff = Integer.MAX_VALUE;

        for (int rows = 1; rows <= count; rows++) {
            int cols = (int) Math.ceil((double) count / rows);
            int diff = Math.abs(rows - cols);

            if (rows * cols >= count && diff < bestDiff) {
                bestRows = rows;
                bestCols = cols;
                bestDiff = diff;
            }
        }

        return new int[]{bestRows, bestCols};
    }

    private int[] getBookmarkContentPosition(int index) {
        int cols = Math.max(1, bookMarksSize[1]);
        int row = index / cols;
        int col = index % cols;
        return new int[]{col * BOOKMARK_SPACING, row * BOOKMARK_SPACING};
    }

    private int getRoundedTargetOffsetX() {
        return (int) Math.round(this.targetOffsetX);
    }

    private int getRoundedTargetOffsetY() {
        return (int) Math.round(this.targetOffsetY);
    }

    private int getNodeRenderPadding(Quest quest) {
        return quest.shape == Shape.EPIC ? 2 : 3;
    }

    private int getNodeSize(Quest quest) {
        return quest.shape == Shape.EPIC ? EPIC_NODE_SIZE : DEFAULT_NODE_SIZE;
    }

    private int getShapePadding(Shape shape) {
        return shape == Shape.EPIC ? 2 : 3;
    }

    private int getShapeSize(Shape shape) {
        return shape == Shape.EPIC ? EPIC_NODE_SIZE : DEFAULT_NODE_SIZE;
    }

    private int contentToLocalX(double contentX) {
        return (int) Math.round(CONTENT_ANCHOR_X + this.renderOffsetX + contentX * this.renderZoom);
    }

    private int contentToLocalY(double contentY) {
        return (int) Math.round(CONTENT_ANCHOR_Y + this.renderOffsetY + contentY * this.renderZoom);
    }

    private double localToContentX(double localX) {
        return (localX - CONTENT_ANCHOR_X - this.renderOffsetX) / this.renderZoom;
    }

    private double localToContentY(double localY) {
        return (localY - CONTENT_ANCHOR_Y - this.renderOffsetY) / this.renderZoom;
    }

    private int scaleSize(int value) {
        return Math.max(1, (int) Math.round(value * this.renderZoom));
    }

    private boolean isRectVisibleInViewport(int x, int y, int w, int h) {
        int left = VIEW_X;
        int top = VIEW_Y;
        int right = VIEW_X + VIEW_W;
        int bottom = VIEW_Y + VIEW_H;

        return x < right && x + w > left && y < bottom && y + h > top;
    }

    private boolean isInsideViewportLocal(double localX, double localY) {
        return localX >= VIEW_X && localX <= VIEW_X + VIEW_W && localY >= VIEW_Y && localY <= VIEW_Y + VIEW_H;
    }

    private boolean isInRect(double x, double y, int rectX, int rectY, int rectW, int rectH) {
        return x >= rectX && x <= rectX + rectW && y >= rectY && y <= rectY + rectH;
    }

    private void beginViewportScissor() {
        enableScissor(guiLeft + VIEW_X, guiTop + VIEW_Y, guiLeft + VIEW_X + VIEW_W, guiTop + VIEW_Y + VIEW_H);
    }

    private void endViewportScissor() {
        disableScissor();
    }

    private void openUri(String uri) {
        try {
            Util.getPlatform().openUri(new URI(uri));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private double clamp(double min, double max, double value) {
        return Mth.clamp(value, min, max);
    }

    private Lines getLine(Quest quest, List<String> quests, GuideTab guideTab) {
        final String prevName = quest.prevName;
        if (!quests.contains(prevName)) {
            return Lines.GOLD;
        } else {
            Quest quest1 = GuideBookCore.instance.getPrev(quest.prevName, guideTab);
            if (quest1.hasPrev) {
                if (quests.contains(quest1.prevName)) {
                    return Lines.DARK;
                } else {
                    return Lines.GRAY;
                }
            } else {
                return Lines.GRAY;
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

    private static final class ScrollBounds {
        private final double minX;
        private final double maxX;
        private final double minY;
        private final double maxY;

        private ScrollBounds(double minX, double maxX, double minY, double maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }
    }
}