package com.denfop.api.multiblock.preview;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.denfop.screen.ScreenResearchTableSpace.enableScissor;
import static com.mojang.blaze3d.systems.RenderSystem.disableScissor;

public class MultiblockPreviewScreen extends Screen {

    private static final int MARGIN = 12;
    private static final int HEADER_H = 18;
    private static final int FOOTER_H = 26;
    private static final int SIDE_GAP = 8;

    private static final int INFO_W = 150;
    private static final int USED_W = 150;

    private static final int USED_ENTRY_HEIGHT = 20;
    private static final int USED_HEADER_HEIGHT = 20;
    private static final int USED_VISIBLE_ROWS = 12;

    private static final int INFO_PADDING_X = 6;
    private static final int INFO_PADDING_Y = 8;
    private static final int INFO_LINE_GAP = 2;

    private static final int ANIMATION_TICKS = 4;
    private static final float DROP_BLOCKS = 1.25F;
    private static final int AUTO_PLAY_INTERVAL_TICKS = 16;

    private final MultiblockPreviewModel model;
    private final PreviewAnimation animation;

    private PreviewMode mode = PreviewMode.STEP_BY_STEP;
    private int stageIndex = 0;

    private float yaw = -35.0F;
    private float pitch = 25.0F;
    private float zoom = 4.0F;

    private boolean draggingPreview = false;
    private boolean autoPlay = false;
    private int autoPlayTicker = 0;

    private boolean blueprintView = false;

    private int usedScrollOffset = 0;
    private int infoScrollOffset = 0;
    private int infoMaxScroll = 0;

    private Button previousButton;
    private Button nextButton;
    private Button modeButton;
    private Button resetViewButton;
    private Button autoPlayButton;
    private Button blueprintButton;

    private HoveredPreviewBlock hoveredPreviewBlock;
    private ItemStack hoveredUsedStack = ItemStack.EMPTY;

    public MultiblockPreviewScreen(Component title, MultiblockPreviewModel model) {
        super(title);
        this.model = model;
        this.animation = new PreviewAnimation(ANIMATION_TICKS);
    }

    @Override
    protected void init() {
        super.init();

        int panelX = getPanelX();
        int panelY = getPanelY();
        int panelW = getPanelW();
        int panelH = getPanelH();

        int buttonY = panelY + panelH - FOOTER_H + 3;
        int x = panelX + 10;

        this.previousButton = Button.builder(Component.literal("<"), btn -> this.previousStage())
                .bounds(x, buttonY, 20, 20)
                .build();
        x += 24;

        this.nextButton = Button.builder(Component.literal(">"), btn -> this.nextStage())
                .bounds(x, buttonY, 20, 20)
                .build();
        x += 28;

        this.modeButton = Button.builder(this.mode.title(), btn -> this.toggleMode())
                .bounds(x, buttonY, 120, 20)
                .build();
        x += 124;

        this.resetViewButton = Button.builder(Component.translatable("iu.multiblock.reset_view"), btn -> this.resetView())
                .bounds(x, buttonY, 82, 20)
                .build();
        x += 86;

        this.autoPlayButton = Button.builder(getAutoPlayButtonText(), btn -> this.toggleAutoPlay())
                .bounds(x, buttonY, 84, 20)
                .build();
        x += 88;

        this.blueprintButton = Button.builder(getBlueprintButtonText(), btn -> this.toggleBlueprintView())
                .bounds(x, buttonY, 100, 20)
                .build();

        this.addRenderableWidget(this.previousButton);
        this.addRenderableWidget(this.nextButton);
        this.addRenderableWidget(this.modeButton);
        this.addRenderableWidget(this.resetViewButton);
        this.addRenderableWidget(this.autoPlayButton);
        this.addRenderableWidget(this.blueprintButton);

        this.clampStageAndRefreshButtons();
    }

    @Override
    public void tick() {
        super.tick();

        this.animation.tick();

        if (this.autoPlay) {
            this.autoPlayTicker++;
            if (this.autoPlayTicker >= AUTO_PLAY_INTERVAL_TICKS) {
                this.autoPlayTicker = 0;
                int max = Math.max(0, this.model.stageCount(this.mode) - 1);
                if (this.stageIndex < max) {
                    this.stageIndex++;
                    this.animation.reset();
                    this.clampStageAndRefreshButtons();
                } else {
                    this.autoPlay = false;
                    this.refreshAutoPlayButton();
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private int getPanelX() {
        return MARGIN;
    }

    private int getPanelY() {
        return MARGIN;
    }

    private int getPanelW() {
        return this.width - MARGIN * 2;
    }

    private int getPanelH() {
        return this.height - MARGIN * 2;
    }

    private int getInfoX() {
        return getPanelX() + 8;
    }

    private int getInfoY() {
        return getPanelY() + HEADER_H + 6;
    }

    private int getInfoH() {
        return getPanelH() - HEADER_H - FOOTER_H - 14;
    }

    private int getUsedX() {
        return getPanelX() + getPanelW() - USED_W - 8;
    }

    private int getUsedY() {
        return getPanelY() + HEADER_H + 6;
    }

    private int getUsedH() {
        return getPanelH() - HEADER_H - FOOTER_H - 14;
    }

    private int getPreviewX() {
        return getInfoX() + INFO_W + SIDE_GAP;
    }

    private int getPreviewY() {
        return getPanelY() + HEADER_H + 6;
    }

    private int getPreviewW() {
        return getUsedX() - getPreviewX() - SIDE_GAP;
    }

    private int getPreviewH() {
        return getPanelH() - HEADER_H - FOOTER_H - 14;
    }

    private Component getAutoPlayButtonText() {
        return this.autoPlay
                ? Component.translatable("iu.multiblock.auto_on")
                : Component.translatable("iu.multiblock.auto_off");
    }

    private Component getBlueprintButtonText() {
        return this.blueprintView
                ? Component.translatable("iu.multiblock.view_blueprint")
                : Component.translatable("iu.multiblock.view_solid");
    }

    private void refreshAutoPlayButton() {
        if (this.autoPlayButton != null) {
            this.autoPlayButton.setMessage(getAutoPlayButtonText());
        }
    }

    private void refreshBlueprintButton() {
        if (this.blueprintButton != null) {
            this.blueprintButton.setMessage(getBlueprintButtonText());
        }
    }

    private void toggleAutoPlay() {
        this.autoPlay = !this.autoPlay;
        this.autoPlayTicker = 0;
        this.refreshAutoPlayButton();
    }

    private void toggleBlueprintView() {
        this.blueprintView = !this.blueprintView;
        this.refreshBlueprintButton();
    }

    private void resetView() {
        this.yaw = -35.0F;
        this.pitch = 25.0F;
        this.zoom = 4.0F;
    }

    private void toggleMode() {
        this.mode = this.mode.next();
        this.stageIndex = Mth.clamp(this.stageIndex, 0, Math.max(0, this.model.stageCount(this.mode) - 1));
        this.modeButton.setMessage(this.mode.title());
        this.animation.reset();
        this.autoPlayTicker = 0;
        this.clampStageAndRefreshButtons();
    }

    private void previousStage() {
        if (this.stageIndex > 0) {
            this.stageIndex--;
            this.animation.reset();
            this.autoPlayTicker = 0;
            this.clampStageAndRefreshButtons();
        }
    }

    private void nextStage() {
        int last = Math.max(0, this.model.stageCount(this.mode) - 1);
        if (this.stageIndex < last) {
            this.stageIndex++;
            this.animation.reset();
            this.autoPlayTicker = 0;
            this.clampStageAndRefreshButtons();
        }
    }

    private void clampStageAndRefreshButtons() {
        int count = this.model.stageCount(this.mode);
        if (count <= 0) {
            this.stageIndex = 0;
        } else {
            this.stageIndex = Mth.clamp(this.stageIndex, 0, count - 1);
        }

        if (this.previousButton != null) {
            this.previousButton.active = this.stageIndex > 0;
        }
        if (this.nextButton != null) {
            this.nextButton.active = this.stageIndex < count - 1;
        }
        if (this.modeButton != null) {
            this.modeButton.setMessage(this.mode.title());
        }

        this.refreshAutoPlayButton();
        this.refreshBlueprintButton();
        this.clampUsedScroll();
        this.clampInfoScroll();
    }

    private int getUsedMaxVisible() {
        return USED_VISIBLE_ROWS;
    }

    private int getUsedMaxScroll() {
        int total = this.model.usedStacks().size();
        return Math.max(0, total - getUsedMaxVisible());
    }

    private void clampUsedScroll() {
        this.usedScrollOffset = Mth.clamp(this.usedScrollOffset, 0, getUsedMaxScroll());
    }

    private void clampInfoScroll() {
        this.infoScrollOffset = Mth.clamp(this.infoScrollOffset, 0, this.infoMaxScroll);
    }

    private boolean isInsideUsedPanel(double mouseX, double mouseY) {
        int x = getUsedX();
        int y = getUsedY();
        return mouseX >= x && mouseX < x + USED_W && mouseY >= y && mouseY < y + getUsedH();
    }

    private boolean isInsideInfoPanel(double mouseX, double mouseY) {
        int x = getInfoX();
        int y = getInfoY();
        return mouseX >= x && mouseX < x + INFO_W && mouseY >= y && mouseY < y + getInfoH();
    }

    private boolean isInsidePreview(double mouseX, double mouseY) {
        int x = getPreviewX();
        int y = getPreviewY();
        return mouseX >= x && mouseX < x + getPreviewW() && mouseY >= y && mouseY < y + getPreviewH();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.hoveredPreviewBlock = null;
        this.hoveredUsedStack = ItemStack.EMPTY;

        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        int panelX = getPanelX();
        int panelY = getPanelY();
        int panelW = getPanelW();
        int panelH = getPanelH();

        int infoX = getInfoX();
        int infoY = getInfoY();
        int infoH = getInfoH();

        int previewX = getPreviewX();
        int previewY = getPreviewY();
        int previewW = getPreviewW();
        int previewH = getPreviewH();

        int usedX = getUsedX();
        int usedY = getUsedY();
        int usedH = getUsedH();

        guiGraphics.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xE0101018);
        guiGraphics.fill(panelX + 1, panelY + 1, panelX + panelW - 1, panelY + panelH - 1, 0xE022222E);

        guiGraphics.fill(infoX, infoY, infoX + INFO_W, infoY + infoH, 0x802B2B3C);
        guiGraphics.fill(previewX, previewY, previewX + previewW, previewY + previewH, 0xFF111119);
        guiGraphics.fill(previewX + 1, previewY + 1, previewX + previewW - 1, previewY + previewH - 1, 0xFF171725);
        guiGraphics.fill(usedX, usedY, usedX + USED_W, usedY + usedH, 0x802B2B3C);

        guiGraphics.drawString(this.font, this.title, panelX + 12, panelY + 6, 0xFFFFFF, false);

        enableScissor(previewX, previewY, previewX + previewW, previewY + previewH);
        this.renderPreview(guiGraphics, previewX, previewY, previewW, previewH, partialTick, mouseX, mouseY);
        disableScissor();
        this.renderInfo(guiGraphics, infoX, infoY, INFO_W, infoH);
        this.renderUsedBlocksPanel(guiGraphics, usedX, usedY, USED_W, usedH, mouseX, mouseY);

        for (Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        if (!this.hoveredUsedStack.isEmpty()) {
            guiGraphics.renderTooltip(this.font, this.hoveredUsedStack, mouseX, mouseY);
        }

        if (this.hoveredPreviewBlock != null) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(this.hoveredPreviewBlock.entry.stack().getHoverName().copy().withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable(
                    "iu.multiblock.tooltip.pos",
                    this.hoveredPreviewBlock.entry.relativePos().getX(),
                    this.hoveredPreviewBlock.entry.relativePos().getY(),
                    this.hoveredPreviewBlock.entry.relativePos().getZ()
            ).withStyle(ChatFormatting.GRAY));

            if (this.hoveredPreviewBlock.entry.direction() != null) {
                tooltip.add(Component.translatable(
                        "iu.multiblock.tooltip.facing",
                        getDirectionName(this.hoveredPreviewBlock.entry.direction())
                ).withStyle(ChatFormatting.AQUA));
            }

            guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    private void renderInfo(GuiGraphics guiGraphics, int panelX, int panelY, int panelW, int panelH) {
        int contentX = panelX + INFO_PADDING_X;
        int contentY = panelY + INFO_PADDING_Y;
        int contentWidth = panelW - INFO_PADDING_X * 2 - 4;
        int visibleHeight = panelH - INFO_PADDING_Y * 2;

        int color = 0xE0E0E0;
        int soft = 0xAFAFC0;

        List<InfoLine> lines = new ArrayList<>();

        appendWrappedLines(lines, Component.translatable("iu.multiblock.info.mode"), color, contentWidth);
        appendWrappedLines(lines, this.mode.title().copy().withStyle(ChatFormatting.AQUA), color, contentWidth);
        lines.add(new InfoLine(FormattedCharSequence.EMPTY, color));

        appendWrappedLines(lines, Component.translatable("iu.multiblock.info.view"), color, contentWidth);
        appendWrappedLines(
                lines,
                this.blueprintView
                        ? Component.translatable("iu.multiblock.info.blueprint").withStyle(ChatFormatting.LIGHT_PURPLE)
                        : Component.translatable("iu.multiblock.info.solid").withStyle(ChatFormatting.GOLD),
                color,
                contentWidth
        );
        lines.add(new InfoLine(FormattedCharSequence.EMPTY, color));

        appendWrappedLines(lines, Component.translatable("iu.multiblock.info.stage"), color, contentWidth);
        int totalStages = Math.max(1, this.model.stageCount(this.mode));
        appendWrappedLines(
                lines,
                Component.translatable("iu.multiblock.info.stage_value", this.stageIndex + 1, totalStages),
                0xFFD37F,
                contentWidth
        );
        lines.add(new InfoLine(FormattedCharSequence.EMPTY, color));

        appendWrappedLines(lines, Component.translatable("iu.multiblock.info.auto"), color, contentWidth);
        appendWrappedLines(
                lines,
                this.autoPlay
                        ? Component.translatable("iu.multiblock.info.playing").withStyle(ChatFormatting.GREEN)
                        : Component.translatable("iu.multiblock.info.stopped").withStyle(ChatFormatting.RED),
                color,
                contentWidth
        );
        lines.add(new InfoLine(FormattedCharSequence.EMPTY, color));

        if (this.mode == PreviewMode.STEP_BY_STEP) {
            MultiblockPreviewEntry current = this.model.getStepEntry(this.stageIndex);
            appendWrappedLines(lines, Component.translatable("iu.multiblock.info.current_block"), color, contentWidth);

            if (current != null) {
                appendWrappedLines(lines, current.stack().getHoverName(), 0x90FF90, contentWidth);
                appendWrappedLines(lines, formatPos(current.relativePos()), soft, contentWidth);
            }

            lines.add(new InfoLine(FormattedCharSequence.EMPTY, color));
        } else {
            MultiblockPreviewModel.LayerGroup layer = this.model.getLayer(this.stageIndex);
            appendWrappedLines(lines, Component.translatable("iu.multiblock.info.current_layer"), color, contentWidth);

            if (layer != null) {
                appendWrappedLines(
                        lines,
                        Component.translatable("iu.multiblock.info.layer_y", layer.yLevel()).withStyle(ChatFormatting.AQUA),
                        0x90FFFF,
                        contentWidth
                );
                appendWrappedLines(
                        lines,
                        Component.translatable("iu.multiblock.info.blocks_count", layer.size()),
                        soft,
                        contentWidth
                );
            }

            lines.add(new InfoLine(FormattedCharSequence.EMPTY, color));
        }

        appendWrappedLines(lines, Component.translatable("iu.multiblock.info.controls"), color, contentWidth);
        appendWrappedLines(lines, Component.translatable("iu.multiblock.control.drag_rotate"), soft, contentWidth);
        appendWrappedLines(lines, Component.translatable("iu.multiblock.control.wheel_zoom"), soft, contentWidth);
        appendWrappedLines(lines, Component.translatable("iu.multiblock.control.step"), soft, contentWidth);
        appendWrappedLines(lines, Component.translatable("iu.multiblock.control.mode"), soft, contentWidth);
        appendWrappedLines(lines, Component.translatable("iu.multiblock.control.blueprint"), soft, contentWidth);
        appendWrappedLines(lines, Component.translatable("iu.multiblock.control.auto"), soft, contentWidth);

        int lineHeight = this.font.lineHeight + INFO_LINE_GAP;
        int contentHeight = lines.size() * lineHeight;

        this.infoMaxScroll = Math.max(0, contentHeight - visibleHeight);
        this.clampInfoScroll();

        guiGraphics.enableScissor(
                panelX + 1,
                panelY + 1,
                panelX + panelW - 1,
                panelY + panelH - 1
        );

        int drawY = contentY - this.infoScrollOffset;
        for (InfoLine line : lines) {
            if (drawY + this.font.lineHeight >= panelY + 2 && drawY <= panelY + panelH - 2) {
                guiGraphics.drawString(this.font, line.text(), contentX, drawY, line.color(), false);
            }
            drawY += lineHeight;
        }

        guiGraphics.disableScissor();

        renderInfoScrollbar(guiGraphics, panelX, panelY, panelW, panelH);
    }

    private void renderInfoScrollbar(GuiGraphics guiGraphics, int panelX, int panelY, int panelW, int panelH) {
        int trackX1 = panelX + panelW - 6;
        int trackX2 = panelX + panelW - 3;
        int trackY1 = panelY + 4;
        int trackY2 = panelY + panelH - 4;

        guiGraphics.fill(trackX1, trackY1, trackX2, trackY2, 0x50505060);

        if (this.infoMaxScroll <= 0) {
            guiGraphics.fill(trackX1, trackY1, trackX2, trackY2, 0x90909070);
            return;
        }

        int trackHeight = trackY2 - trackY1;
        int visibleHeight = panelH - INFO_PADDING_Y * 2;
        int totalHeight = visibleHeight + this.infoMaxScroll;

        int thumbHeight = Math.max(14, (int) ((visibleHeight / (float) totalHeight) * trackHeight));
        int movable = trackHeight - thumbHeight;
        int thumbY = trackY1 + Math.round((this.infoScrollOffset / (float) this.infoMaxScroll) * movable);

        guiGraphics.fill(trackX1, thumbY, trackX2, thumbY + thumbHeight, 0xC0D0D0F0);
    }

    private void renderUsedBlocksPanel(GuiGraphics guiGraphics, int panelX, int panelY, int panelW, int panelH, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("iu.multiblock.used"), panelX + 6, panelY + 6, 0xFFFFFF, false);

        List<MultiblockPreviewModel.UsedStackEntry> used = this.model.usedStacks();
        this.clampUsedScroll();

        int startY = panelY + USED_HEADER_HEIGHT;
        int iconX = panelX + 8;
        int textX = panelX + 30;

        int visibleRows = getUsedMaxVisible();
        int fromIndex = this.usedScrollOffset;
        int toIndex = Math.min(used.size(), fromIndex + visibleRows);

        guiGraphics.enableScissor(
                panelX + 2,
                startY,
                panelX + panelW - 2,
                panelY + panelH - 2
        );

        for (int index = fromIndex; index < toIndex; index++) {
            MultiblockPreviewModel.UsedStackEntry usedEntry = used.get(index);
            int visibleIndex = index - fromIndex;
            int rowY = startY + visibleIndex * USED_ENTRY_HEIGHT;

            guiGraphics.fill(panelX + 4, rowY - 2, panelX + panelW - 10, rowY + 16, 0x403A3A4A);
            guiGraphics.renderItem(usedEntry.stack(), iconX, rowY);
            guiGraphics.drawString(
                    this.font,
                    Component.translatable("iu.multiblock.used.count", usedEntry.count()),
                    textX,
                    rowY + 4,
                    0xE0E0E0,
                    false
            );

            if (mouseX >= iconX && mouseX < iconX + 16 && mouseY >= rowY && mouseY < rowY + 16) {
                this.hoveredUsedStack = usedEntry.stack();
            }
        }

        guiGraphics.disableScissor();

        renderUsedScrollbar(guiGraphics, panelX, panelY, panelW, panelH, used.size());
    }

    private void renderUsedScrollbar(GuiGraphics guiGraphics, int panelX, int panelY, int panelW, int panelH, int totalEntries) {
        int trackX1 = panelX + panelW - 6;
        int trackX2 = panelX + panelW - 3;
        int trackY1 = panelY + USED_HEADER_HEIGHT;
        int trackY2 = panelY + panelH - 4;

        guiGraphics.fill(trackX1, trackY1, trackX2, trackY2, 0x50505060);

        int maxScroll = getUsedMaxScroll();
        if (totalEntries <= getUsedMaxVisible() || maxScroll <= 0) {
            guiGraphics.fill(trackX1, trackY1, trackX2, trackY2, 0x909090B0);
            return;
        }

        int trackHeight = trackY2 - trackY1;
        int thumbHeight = Math.max(14, (int) ((getUsedMaxVisible() / (float) totalEntries) * trackHeight));
        int movable = trackHeight - thumbHeight;
        int thumbY = trackY1 + Math.round((this.usedScrollOffset / (float) maxScroll) * movable);

        guiGraphics.fill(trackX1, thumbY, trackX2, thumbY + thumbHeight, 0xC0D0D0F0);
    }

    private void renderPreview(GuiGraphics guiGraphics, int x, int y, int w, int h, float partialTick, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();

        float fitScale = computeFitScale(w, h);
        float finalScale = fitScale * this.zoom;

        int centerX = x + w / 2;
        int centerY = y + h / 2 + 60;

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        Lighting.setupForFlatItems();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX, centerY, 300.0F);
        guiGraphics.pose().scale(finalScale, -finalScale, finalScale);
        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(this.pitch));
        guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(this.yaw));

        float centerOffsetX = -(this.model.minX() + this.model.maxX() + 1) / 2.0F;
        float centerOffsetY = -(this.model.minY() + this.model.maxY() + 1) / 2.0F;
        float centerOffsetZ = -(this.model.minZ() + this.model.maxZ() + 1) / 2.0F;

        guiGraphics.pose().translate(centerOffsetX, -centerOffsetY, centerOffsetZ);

        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

        if (this.mode == PreviewMode.STEP_BY_STEP) {
            renderStepMode(guiGraphics, bufferSource, partialTick, x, y, w, h, mouseX, mouseY);
        } else {
            renderLayerMode(guiGraphics, bufferSource, partialTick, x, y, w, h, mouseX, mouseY);
        }

        if (this.hoveredPreviewBlock != null) {
            RenderSystem.lineWidth(2.5F);
            renderHoveredBlockOutline(guiGraphics.pose(), bufferSource, this.hoveredPreviewBlock.entry.relativePos(), 1.0F, 0.35F, 1.0F, 1.0F);
        }

        bufferSource.endBatch();
        RenderSystem.lineWidth(1.0F);

        guiGraphics.pose().popPose();

        Lighting.setupForFlatItems();
        RenderSystem.enableCull();
    }

    private float computeFitScale(int previewWidth, int previewHeight) {
        int spanX = Math.max(1, this.model.sizeX());
        int spanY = Math.max(1, this.model.sizeY());
        int spanZ = Math.max(1, this.model.sizeZ());

        int horizontalSpan = Math.max(spanX, spanZ);
        int verticalSpan = spanY;

        float byWidth = (previewWidth - 30.0F) / (float) (horizontalSpan * 18.0F);
        float byHeight = (previewHeight - 30.0F) / (float) ((verticalSpan + horizontalSpan * 0.35F) * 18.0F);

        return Math.min(byWidth, byHeight) * 4.8F;
    }

    private void renderStepMode(GuiGraphics guiGraphics,
                                MultiBufferSource.BufferSource bufferSource,
                                float partialTick,
                                int previewX, int previewY, int previewW, int previewH,
                                int mouseX, int mouseY) {
        float dropOffset = (1.0F - getAnimatedProgress(partialTick)) * DROP_BLOCKS;

        for (int i = 0; i <= this.stageIndex && i < this.model.stepEntries().size(); i++) {
            MultiblockPreviewEntry entry = this.model.stepEntries().get(i);
            boolean animatedBlock = i == this.stageIndex;

            if (this.blueprintView) {
                boolean current = i == this.stageIndex;
                renderBlueprintEntry(guiGraphics.pose(), bufferSource, entry.relativePos(), current, false);
            } else {
                renderBlockEntry(guiGraphics, bufferSource, entry, animatedBlock ? dropOffset : 0.0F);
            }
        }

        this.updateHoveredBlock(previewX, previewY, previewW, previewH, mouseX, mouseY);
    }

    private void renderLayerMode(GuiGraphics guiGraphics,
                                 MultiBufferSource.BufferSource bufferSource,
                                 float partialTick,
                                 int previewX, int previewY, int previewW, int previewH,
                                 int mouseX, int mouseY) {
        float dropOffset = (1.0F - getAnimatedProgress(partialTick)) * DROP_BLOCKS;

        for (int layerIndex = 0; layerIndex <= this.stageIndex && layerIndex < this.model.layers().size(); layerIndex++) {
            MultiblockPreviewModel.LayerGroup layer = this.model.layers().get(layerIndex);
            boolean animatedLayer = layerIndex == this.stageIndex;

            for (MultiblockPreviewEntry entry : layer.entries()) {
                if (this.blueprintView) {
                    renderBlueprintEntry(guiGraphics.pose(), bufferSource, entry.relativePos(), animatedLayer, false);
                } else {
                    renderBlockEntry(guiGraphics, bufferSource, entry, animatedLayer ? dropOffset : 0.0F);
                }
            }
        }

        this.updateHoveredBlock(previewX, previewY, previewW, previewH, mouseX, mouseY);
    }

    private void renderBlueprintEntry(PoseStack poseStack,
                                      MultiBufferSource.BufferSource bufferSource,
                                      BlockPos pos,
                                      boolean current,
                                      boolean hovered) {
        float r;
        float g;
        float b;
        float a;

        if (hovered) {
            r = 1.0F;
            g = 0.35F;
            b = 1.0F;
            a = 1.0F;
        } else if (current) {
            r = 0.45F;
            g = 1.0F;
            b = 1.0F;
            a = 1.0F;
        } else {
            r = 0.25F;
            g = 0.65F;
            b = 1.0F;
            a = 0.9F;
        }

        renderHoveredBlockOutline(poseStack, bufferSource, pos, r, g, b, a);
    }

    private float getAnimatedProgress(float partialTick) {
        float progress = this.animation.normalized();
        float next = Mth.clamp(progress + (1.0F / ANIMATION_TICKS) * partialTick, 0.0F, 1.0F);
        float inv = 1.0F - next;
        return 1.0F - inv * inv * inv;
    }

    private void renderBlockEntry(GuiGraphics guiGraphics,
                                  MultiBufferSource.BufferSource bufferSource,
                                  MultiblockPreviewEntry entry,
                                  float dropOffsetBlocks) {
        Minecraft minecraft = Minecraft.getInstance();

        ItemStack stack = entry.stack();
        BlockPos pos = entry.relativePos();

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(pos.getX(), (-pos.getY() - dropOffsetBlocks), pos.getZ());

            minecraft.getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    guiGraphics.pose(),
                    bufferSource,
                    minecraft.level,
                    0
            );

            guiGraphics.pose().popPose();
            return;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(
                pos.getX() - 0.5F,
                -1 * (-pos.getY() - dropOffsetBlocks - 0.5F),
                pos.getZ() - 0.5F
        );

        BlockState renderState = applyDirectionToState(
                blockItem.getBlock().defaultBlockState(),
                entry.direction()
        );

        minecraft.getBlockRenderer().renderSingleBlock(
                renderState,
                guiGraphics.pose(),
                bufferSource,
                0x00F000F0,
                OverlayTexture.NO_OVERLAY
        );

        guiGraphics.pose().popPose();
    }

    private BlockState applyDirectionToState(BlockState state, Direction direction) {
        if (direction == null) {
            direction = Direction.NORTH;
        }

        if (state.hasProperty(BlockStateProperties.FACING)) {
            return state.setValue(BlockStateProperties.FACING, direction);
        }

        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction horizontal = direction.getAxis().isVertical() ? Direction.NORTH : direction;
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, horizontal);
        }

        if (state.hasProperty(BlockStateProperties.FACING_HOPPER)) {
            return state.setValue(BlockStateProperties.FACING_HOPPER, direction);
        }

        if (state.hasProperty(BlockStateProperties.AXIS)) {
            return state.setValue(BlockStateProperties.AXIS, direction.getAxis());
        }

        for (var property : state.getProperties()) {
            if (property instanceof DirectionProperty directionProperty) {
                if (directionProperty.getPossibleValues().contains(direction)) {
                    return state.setValue(directionProperty, direction);
                }

                Direction horizontal = direction.getAxis().isVertical() ? Direction.NORTH : direction;
                if (directionProperty.getPossibleValues().contains(horizontal)) {
                    return state.setValue(directionProperty, horizontal);
                }
            }
        }

        return state;
    }

    private void updateHoveredBlock(int previewX, int previewY, int previewW, int previewH, int mouseX, int mouseY) {
        if (!isInsidePreview(mouseX, mouseY)) {
            return;
        }

        int centerX = previewX + previewW / 2;
        int centerY = previewY + previewH / 2 + 60;

        float fitScale = computeFitScale(previewW, previewH);
        float finalScale = fitScale * this.zoom;

        MultiblockPreviewEntry bestEntry = null;
        double bestDistance = Double.MAX_VALUE;

        List<MultiblockPreviewEntry> visibleEntries = getVisibleEntries();
        for (MultiblockPreviewEntry entry : visibleEntries) {
            double[] projected = projectBlockToScreen(entry.relativePos(), centerX, centerY, finalScale);
            double dx = mouseX - projected[0];
            double dy = mouseY - projected[1];
            double distSq = dx * dx + dy * dy;
            double score = distSq - projected[2] * 0.001D;

            if (distSq < 256.0D && score < bestDistance) {
                bestDistance = score;
                bestEntry = entry;
            }
        }

        if (bestEntry != null) {
            this.hoveredPreviewBlock = new HoveredPreviewBlock(bestEntry);
        }
    }

    private List<MultiblockPreviewEntry> getVisibleEntries() {
        List<MultiblockPreviewEntry> result = new ArrayList<>();

        if (this.mode == PreviewMode.STEP_BY_STEP) {
            for (int i = 0; i <= this.stageIndex && i < this.model.stepEntries().size(); i++) {
                result.add(this.model.stepEntries().get(i));
            }
        } else {
            for (int layerIndex = 0; layerIndex <= this.stageIndex && layerIndex < this.model.layers().size(); layerIndex++) {
                result.addAll(this.model.layers().get(layerIndex).entries());
            }
        }

        return result;
    }

    private double[] projectBlockToScreen(BlockPos pos, int centerX, int centerY, float scale) {
        Matrix4f matrix = new Matrix4f();

        matrix.translate(centerX, centerY, 300.0F);
        matrix.scale(scale, -scale, scale);
        matrix.rotate(Axis.XP.rotationDegrees(this.pitch));
        matrix.rotate(Axis.YP.rotationDegrees(this.yaw));

        float centerOffsetX = -(this.model.minX() + this.model.maxX() + 1) / 2.0F;
        float centerOffsetY = -(this.model.minY() + this.model.maxY() + 1) / 2.0F;
        float centerOffsetZ = -(this.model.minZ() + this.model.maxZ() + 1) / 2.0F;

        matrix.translate(centerOffsetX, -centerOffsetY, centerOffsetZ);

        Vector4f vec = new Vector4f(
                pos.getX() + 0.5F,
                pos.getY() + 0.5F,
                pos.getZ() + 0.5F,
                1.0F
        );

        vec.mul(matrix);

        return new double[]{vec.x(), vec.y(), vec.z()};
    }

    private void renderHoveredBlockOutline(PoseStack poseStack,
                                           MultiBufferSource.BufferSource bufferSource,
                                           BlockPos pos,
                                           float r, float g, float b, float a) {
        poseStack.pushPose();

        float minX = pos.getX() - 0.505F;
        float minY = pos.getY() + 0.495F;
        float minZ = pos.getZ() - 0.505F;

        float maxX = pos.getX() + 0.505F;
        float maxY = pos.getY() + 1.505F;
        float maxZ = pos.getZ() + 0.505F;

        LevelRenderer.renderLineBox(
                poseStack,
                bufferSource.getBuffer(RenderType.lines()),
                minX, minY, minZ,
                maxX, maxY, maxZ,
                r, g, b, a
        );

        poseStack.popPose();
    }

    private void line(com.mojang.blaze3d.vertex.VertexConsumer consumer,
                      Matrix4f matrix,
                      float x1, float y1, float z1,
                      float x2, float y2, float z2,
                      float r, float g, float b, float a) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;

        float len = Mth.sqrt(dx * dx + dy * dy + dz * dz);
        if (len == 0.0F) {
            return;
        }

        float nx = dx / len;
        float ny = dy / len;
        float nz = dz / len;

        consumer.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setNormal(nx, ny, nz);
        consumer.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a).setNormal(nx, ny, nz);
    }

    private Component formatPos(BlockPos pos) {
        return Component.translatable("iu.multiblock.block_pos", pos.getX(), pos.getY(), pos.getZ());
    }

    private Component getDirectionName(Direction direction) {
        return switch (direction) {
            case DOWN -> Component.translatable("iu.multiblock.direction.down");
            case UP -> Component.translatable("iu.multiblock.direction.up");
            case NORTH -> Component.translatable("iu.multiblock.direction.north");
            case SOUTH -> Component.translatable("iu.multiblock.direction.south");
            case WEST -> Component.translatable("iu.multiblock.direction.west");
            case EAST -> Component.translatable("iu.multiblock.direction.east");
        };
    }

    private void appendWrappedLines(List<InfoLine> target, Component text, int color, int maxWidth) {
        List<FormattedCharSequence> wrapped = this.font.split(text, maxWidth);
        if (wrapped.isEmpty()) {
            target.add(new InfoLine(FormattedCharSequence.forward(text.getString(), text.getStyle()), color));
            return;
        }

        for (FormattedCharSequence seq : wrapped) {
            target.add(new InfoLine(seq, color));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isInsidePreview(mouseX, mouseY)) {
            this.draggingPreview = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.draggingPreview = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.draggingPreview && button == 0) {
            this.yaw += (float) dragX * 0.85F;
            this.pitch += (float) dragY * 0.65F;
            this.pitch = Mth.clamp(this.pitch, -89.0F, 89.0F);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (isInsideInfoPanel(mouseX, mouseY)) {
            if (deltaY < 0) {
                this.infoScrollOffset += 12;
            } else if (deltaY > 0) {
                this.infoScrollOffset -= 12;
            }
            this.clampInfoScroll();
            return true;
        }

        if (isInsideUsedPanel(mouseX, mouseY)) {
            if (deltaY < 0) {
                this.usedScrollOffset++;
            } else if (deltaY > 0) {
                this.usedScrollOffset--;
            }
            this.clampUsedScroll();
            return true;
        }

        if (isInsidePreview(mouseX, mouseY)) {
            this.zoom = Mth.clamp(this.zoom + (float) deltaY * 0.08F, 3.0F, 8.0F);
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.minecraft != null && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }

        if (keyCode == 263) {
            this.previousStage();
            return true;
        }
        if (keyCode == 262) {
            this.nextStage();
            return true;
        }
        if (keyCode == 77) {
            this.toggleMode();
            return true;
        }
        if (keyCode == 66) {
            this.toggleBlueprintView();
            return true;
        }
        if (keyCode == 82) {
            this.resetView();
            return true;
        }
        if (keyCode == 32) {
            this.toggleAutoPlay();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private static class HoveredPreviewBlock {
        private final MultiblockPreviewEntry entry;

        private HoveredPreviewBlock(MultiblockPreviewEntry entry) {
            this.entry = entry;
        }
    }

    private record InfoLine(FormattedCharSequence text, int color) {
    }
}