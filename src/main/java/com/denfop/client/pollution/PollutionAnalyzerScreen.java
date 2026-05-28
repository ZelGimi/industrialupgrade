package com.denfop.client.pollution;

import com.denfop.api.pollution.analyzer.*;
import com.denfop.items.ItemPollutionDevice;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.*;

public class PollutionAnalyzerScreen extends Screen {

    private final ItemStack analyzerStack;
    private final List<PollutionAnalyzerButton> tabButtons = new ArrayList<>();
    private final PollutionAnalyzerWorldPreviewCache worldPreviewCache = new PollutionAnalyzerWorldPreviewCache();
    private final PollutionAnalyzerClientHistory historyTracker = new PollutionAnalyzerClientHistory();
    private Tab activeTab = Tab.OVERVIEW;
    private PollutionAnalyzerButton resetViewButton;
    private float yaw = -44.0F;
    private float pitch = 30.0F;
    private float zoom = 1.0F;
    private boolean previewDragging;
    private boolean previewMoved;
    private double previewMouseDownX;
    private double previewMouseDownY;
    private int rightScroll;
    private int rightContentHeight;
    private int rightMaxScroll;
    private boolean rightScrollbarDragging;
    private int rightScrollbarGrabOffset;
    private long selectedChunkKey = Long.MIN_VALUE;
    private long hoveredChunkKey = Long.MIN_VALUE;
    private ItemStack hoveredItem = ItemStack.EMPTY;
    private List<Component> hoveredTextTooltip = List.of();
    public PollutionAnalyzerScreen(ItemStack analyzerStack) {
        super(analyzerStack.getHoverName().copy());
        this.analyzerStack = analyzerStack.copy();
    }

    @Override
    protected void init() {
        super.init();
        this.tabButtons.clear();

        int resetWidth = 124;
        int buttonHeight = 22;
        int gap = 8;
        int columns = 3;

        int resetX = panelX() + panelW() - resetWidth - 14;
        int tabsAreaX = panelX() + 14;
        int tabsAreaW = resetX - tabsAreaX - 12;

        int buttonWidth = (tabsAreaW - gap * (columns - 1)) / columns;
        int tabsStartY = panelY() + 42;

        Tab[] tabs = Tab.values();
        for (int i = 0; i < tabs.length; i++) {
            Tab tab = tabs[i];
            int row = i / columns;
            int col = i % columns;

            int x = tabsAreaX + col * (buttonWidth + gap);
            int y = tabsStartY + row * (buttonHeight + gap);

            PollutionAnalyzerButton button = new PollutionAnalyzerButton(
                    x,
                    y,
                    buttonWidth,
                    buttonHeight,
                    tab.title(),
                    tab.accent(),
                    btn -> switchTab(tab)
            );
            this.addRenderableWidget(button);
            this.tabButtons.add(button);
        }

        this.resetViewButton = this.addRenderableWidget(new PollutionAnalyzerButton(
                resetX,
                panelY() + 16,
                resetWidth,
                buttonHeight,
                Component.translatable("iu.pollution_analyzer.button.reset_view"),
                0x69B4FF,
                btn -> resetView()
        ));

        updateTabButtons();
    }

    private void switchTab(Tab tab) {
        this.activeTab = tab;
        this.rightScroll = 0;
        updateTabButtons();
    }

    private void updateTabButtons() {
        Tab[] tabs = Tab.values();
        for (int i = 0; i < this.tabButtons.size(); i++) {
            this.tabButtons.get(i).setSelected(tabs[i] == this.activeTab);
        }
    }

    private void resetView() {
        this.yaw = -44.0F;
        this.pitch = 30.0F;
        this.zoom = 1.0F;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.minecraft == null || this.minecraft.player == null) {
            this.onClose();
            return;
        }

        boolean holdingAnalyzer =
                this.minecraft.player.getMainHandItem().getItem() instanceof ItemPollutionDevice
                        || this.minecraft.player.getOffhandItem().getItem() instanceof ItemPollutionDevice;

        if (!holdingAnalyzer) {
            this.onClose();
            return;
        }

        if (this.minecraft.player.tickCount % 20 == 0) {
            PollutionAnalyzerClientCache.requestRefresh();
        }

        PollutionAnalyzerSnapshot snapshot = PollutionAnalyzerClientCache.getSnapshot();
        if (snapshot != null) {
            if (this.selectedChunkKey == Long.MIN_VALUE || snapshot.getChunk(this.selectedChunkKey) == null) {
                this.selectedChunkKey = snapshot.centerKey();
            }
            this.historyTracker.capture(snapshot);
        }

        if (this.minecraft.level != null) {
            this.worldPreviewCache.update(
                    this.minecraft.level,
                    new net.minecraft.world.level.ChunkPos(this.minecraft.player.blockPosition())
            );
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private int panelX() {
        return 10;
    }

    private int panelY() {
        return 10;
    }

    private int panelW() {
        return this.width - 20;
    }

    private int panelH() {
        return this.height - 20;
    }

    private int previewX() {
        return panelX() + 14;
    }

    private int previewY() {
        return panelY() + 102;
    }

    private int previewW() {
        return (int) (panelW() * 0.61F);
    }

    private int previewH() {
        return panelH() - 126;
    }

    private int sideX() {
        return previewX() + previewW() + 12;
    }

    private int sideY() {
        return previewY();
    }

    private int sideW() {
        return panelX() + panelW() - sideX() - 14;
    }

    private int sideH() {
        return previewH();
    }

    private int footerY() {
        return panelY() + panelH() - 18;
    }

    private int previewSceneY() {
        return previewY() + previewLegendHeight() + 10;
    }

    private int previewSceneH() {
        return Math.max(40, previewH() - previewLegendHeight() - 10);
    }

    private int rightScrollbarX() {
        return sideX() + sideW() - 10 - 4;
    }

    private int rightScrollbarY() {
        return sideY() + 30;
    }

    private int rightScrollbarVisibleH() {
        return sideH() - 40;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.hoveredItem = ItemStack.EMPTY;
        this.hoveredTextTooltip = List.of();
        this.hoveredChunkKey = Long.MIN_VALUE;

        graphics.fill(0, 0, this.width, this.height, 0xFF0A1018);

        renderWindowBackground(graphics);
        renderHeader(graphics);
        renderBodyFrames(graphics);

        PollutionAnalyzerSnapshot snapshot = PollutionAnalyzerClientCache.getSnapshot();

        if (snapshot == null) {
            drawCenteredBlockText(
                    graphics,
                    previewX(),
                    previewY(),
                    previewW(),
                    previewH(),
                    Component.translatable("iu.pollution_analyzer.loading"),
                    0xD7E3F5
            );
            drawCenteredBlockText(
                    graphics,
                    sideX(),
                    sideY(),
                    sideW(),
                    sideH(),
                    Component.translatable("iu.pollution_analyzer.waiting_for_server"),
                    0xB4C1D6
            );
            super.render(graphics, mouseX, mouseY, partialTick);
            return;
        }

        if (!snapshot.isSupportedDimension()) {
            drawCenteredBlockText(
                    graphics,
                    previewX(),
                    previewY(),
                    previewW(),
                    previewH(),
                    Component.translatable("iu.pollution_analyzer.unsupported_dimension", snapshot.getDimensionName()),
                    0xFFD27A7A
            );
            drawCenteredBlockText(
                    graphics,
                    sideX(),
                    sideY(),
                    sideW(),
                    sideH(),
                    Component.translatable("iu.pollution_analyzer.overworld_only"),
                    0xFFD27A7A
            );

            for (Renderable renderable : this.renderables) {
                renderable.render(graphics, mouseX, mouseY, partialTick);
            }
            return;
        }

        updateHoveredChunk(snapshot, mouseX, mouseY);

        renderPreviewBackground(graphics);

        graphics.enableScissor(
                previewX() + 1,
                previewSceneY(),
                previewX() + previewW() - 1,
                previewY() + previewH() - 1
        );
        renderPreview(graphics, snapshot, partialTick);
        graphics.disableScissor();

        graphics.enableScissor(
                previewX() + 1,
                previewY() + 1,
                previewX() + previewW() - 1,
                previewSceneY() - 1
        );
        renderPreviewLegendWrapped(graphics);
        graphics.disableScissor();

        renderSidePanel(graphics, snapshot, mouseX, mouseY);
        renderFooter(graphics, snapshot);

        for (Renderable renderable : this.renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTick);
        }

        if (!this.hoveredItem.isEmpty()) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 500);
            graphics.renderTooltip(this.font, this.hoveredItem, mouseX, mouseY);
            graphics.pose().popPose();
        } else if (!this.hoveredTextTooltip.isEmpty()) {
            graphics.renderComponentTooltip(this.font, this.hoveredTextTooltip, mouseX, mouseY);
        } else if (this.hoveredChunkKey != Long.MIN_VALUE) {
            PollutionAnalyzerChunkSnapshot chunk = snapshot.getChunk(this.hoveredChunkKey);
            if (chunk != null) {
                renderHoveredChunkCard(graphics, snapshot, chunk);
            }
        }
    }

    private void renderHoveredChunkCard(
            GuiGraphics graphics,
            PollutionAnalyzerSnapshot snapshot,
            PollutionAnalyzerChunkSnapshot chunk
    ) {
        int x0 = chunk.getChunkX() << 4;
        int z0 = chunk.getChunkZ() << 4;
        int x1 = x0 + 15;
        int z1 = z0 + 15;

        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("iu.pollution_analyzer.source.chunk", chunk.getChunkX(), chunk.getChunkZ()).withStyle(ChatFormatting.AQUA));
        lines.add(Component.translatable("iu.pollution_analyzer.tooltip.bounds.short", x0, x1, z0, z1).withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable(
                "iu.pollution_analyzer.source.air_full",
                formatTotalUnit("air", chunk.getAirSeverity()) + " (" + formatRateUnit("air", chunk.getAirRatePerSecond()) + ")"
        ).withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable(
                "iu.pollution_analyzer.source.soil_full",
                formatTotalUnit("soil", chunk.getSoilSeverity()) + " (" + formatRateUnit("soil", chunk.getSoilRatePerSecond()) + ")"
        ).withStyle(ChatFormatting.GRAY));

        if (chunk.getAirCleaningPerSecond() > 0.0D) {
            lines.add(Component.translatable(
                    "iu.pollution_analyzer.source.cleaner.air_cleaning",
                    formatRateUnit("air", chunk.getAirCleaningPerSecond())
            ).withStyle(ChatFormatting.BLUE));
        }
        if (chunk.getSoilCleaningPerSecond() > 0.0D) {
            lines.add(Component.translatable(
                    "iu.pollution_analyzer.source.cleaner.soil_cleaning",
                    formatRateUnit("soil", chunk.getSoilCleaningPerSecond())
            ).withStyle(ChatFormatting.BLUE));
        }

        int maxWidth = 0;
        for (Component line : lines) {
            maxWidth = Math.max(maxWidth, this.font.width(line));
        }

        int boxW = maxWidth + 14;
        int boxH = 8 + lines.size() * (this.font.lineHeight + 2);

        int[] anchor = projectChunkTooltipAnchor(snapshot, chunk);

        int x = anchor[0] + 12;
        int y = anchor[1] - boxH - 8;

        int minX = previewX() + 4;
        int maxX = previewX() + previewW() - boxW - 4;
        int minY = previewSceneY() + 4;
        int maxY = previewY() + previewH() - boxH - 4;

        x = Mth.clamp(x, minX, Math.max(minX, maxX));
        y = Mth.clamp(y, minY, Math.max(minY, maxY));

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(0.0F, 0.0F, 500.0F);

        RenderSystem.disableDepthTest();

        graphics.fill(x, y, x + boxW, y + boxH, 0xFF100618);
        graphics.fill(x + 1, y + 1, x + boxW - 1, y + boxH - 1, 0xFF180B24);
        graphics.fill(x, y, x + boxW, y + 2, 0xFF5A2CFF);
        graphics.fill(x, y, x + 2, y + boxH, 0xFF5A2CFF);
        graphics.fill(x + boxW - 2, y, x + boxW, y + boxH, 0xFF05070C);
        graphics.fill(x, y + boxH - 2, x + boxW, y + boxH, 0xFF05070C);

        int textY = y + 5;
        for (Component line : lines) {
            graphics.drawString(this.font, line, x + 6, textY, 0xF0F6FF, false);
            textY += this.font.lineHeight + 2;
        }

        pose.popPose();
    }

    private int[] projectChunkTooltipAnchor(
            PollutionAnalyzerSnapshot snapshot,
            PollutionAnalyzerChunkSnapshot chunk
    ) {
        PollutionAnalyzerWorldPreviewCache.PreviewChunk previewChunk = findPreviewChunk(chunk.key());
        if (previewChunk == null) {
            return new int[]{previewX() + 12, previewSceneY() + 12};
        }

        int globalBaseY = worldPreviewCache.getGlobalBaseY();
        int globalMaxY = worldPreviewCache.getGlobalMaxVisibleY();
        int displayHeight = Math.max(18, (globalMaxY - globalBaseY) + 2);

        float chunkGap = 0.0F;
        float totalWidth = 16 * 3 + chunkGap * 2;
        float fitByWidth = (previewW() - 44.0F) / totalWidth;
        float fitByHeight = (previewSceneH() - 40.0F) / (displayHeight + totalWidth * 0.42F);
        float scale = Math.min(fitByWidth, fitByHeight) * this.zoom;

        int centerX = previewX() + previewW() / 2;
        int centerY = previewSceneY() + (previewSceneH() / 2) + 20;

        int centerChunkX = snapshot.getCenterChunkX();
        int centerChunkZ = snapshot.getCenterChunkZ();

        float chunkShiftX = (chunk.getChunkX() - centerChunkX) * (16.0F + chunkGap);
        float chunkShiftZ = (chunk.getChunkZ() - centerChunkZ) * (16.0F + chunkGap);
        float chunkCenterY = previewChunk.getDisplayCenterHeight(globalBaseY);

        double[] projected = projectPoint(centerX, centerY, scale, chunkShiftX, chunkCenterY, chunkShiftZ);
        return new int[]{Mth.floor((float) projected[0]), Mth.floor((float) projected[1])};
    }

    private Component resolveWindDirection(String raw) {
        if (raw == null || raw.isBlank()) {
            return Component.literal("-");
        }

        String v = raw.trim();
        if (v.startsWith("iu.")) {
            return Component.translatable(v);
        }

        return switch (v.toLowerCase(Locale.ROOT)) {
            case "n" -> Component.translatable("iu.wind.n");
            case "nw" -> Component.translatable("iu.wind.nw");
            case "ne" -> Component.translatable("iu.wind.ne");
            case "s" -> Component.translatable("iu.wind.s");
            case "se" -> Component.translatable("iu.wind.se");
            case "sw" -> Component.translatable("iu.wind.sw");
            case "w" -> Component.translatable("iu.wind.w");
            case "e" -> Component.translatable("iu.wind.e");
            default -> Component.literal(v);
        };
    }

    private Component resolveWindLevel(String raw) {
        if (raw == null || raw.isBlank()) {
            return Component.literal("-");
        }

        String v = raw.trim().toLowerCase(Locale.ROOT);
        return switch (v) {
            case "1", "one" -> Component.translatable("iu.wind.level.one");
            case "2", "two" -> Component.translatable("iu.wind.level.two");
            case "3", "three" -> Component.translatable("iu.wind.level.three");
            case "4", "four" -> Component.translatable("iu.wind.level.four");
            case "5", "five" -> Component.translatable("iu.wind.level.five");
            case "6", "six" -> Component.translatable("iu.wind.level.six");
            case "7", "seven" -> Component.translatable("iu.wind.level.seven");
            case "8", "eight" -> Component.translatable("iu.wind.level.eight");
            case "9", "nine" -> Component.translatable("iu.wind.level.nine");
            case "10", "ten" -> Component.translatable("iu.wind.level.ten");
            default -> v.startsWith("iu.") ? Component.translatable(v) : Component.literal(raw);
        };
    }

    private int previewLegendHeight() {
        record LegendEntry(Component text) {
        }

        List<LegendEntry> entries = List.of(
                new LegendEntry(Component.translatable("iu.pollution_analyzer.legend.default")),
                new LegendEntry(Component.translatable("iu.pollution_analyzer.legend.good")),
                new LegendEntry(Component.translatable("iu.pollution_analyzer.legend.watch")),
                new LegendEntry(Component.translatable("iu.pollution_analyzer.legend.bad")),
                new LegendEntry(Component.translatable("iu.pollution_analyzer.legend.danger"))
        );

        int left = previewX() + 12;
        int right = previewX() + previewW() - 12;
        int x = left;
        int rows = 1;
        int gap = 14;

        for (LegendEntry entry : entries) {
            int entryW = 10 + 6 + this.font.width(entry.text()) + gap;
            if (x + entryW > right) {
                rows++;
                x = left;
            }
            x += entryW;
        }

        return rows * 16 + 8;
    }

    private void renderPreviewLegendWrapped(GuiGraphics graphics) {
        record LegendEntry(int color, Component text) {
        }

        List<LegendEntry> entries = List.of(
                new LegendEntry(0xFF69B4FF, Component.translatable("iu.pollution_analyzer.legend.default")),
                new LegendEntry(0xFF39B46C, Component.translatable("iu.pollution_analyzer.legend.good")),
                new LegendEntry(0xFFE1C34F, Component.translatable("iu.pollution_analyzer.legend.watch")),
                new LegendEntry(0xFFF09F3B, Component.translatable("iu.pollution_analyzer.legend.bad")),
                new LegendEntry(0xFFE05353, Component.translatable("iu.pollution_analyzer.legend.danger"))
        );

        int left = previewX() + 12;
        int right = previewX() + previewW() - 12;
        int x = left;
        int y = previewY() + 10;
        int rowH = 16;
        int gap = 14;

        for (LegendEntry entry : entries) {
            int entryW = 10 + 6 + this.font.width(entry.text()) + gap;
            if (x + entryW > right) {
                x = left;
                y += rowH;
            }

            graphics.fill(x, y + 2, x + 10, y + 12, entry.color());
            graphics.drawString(this.font, entry.text(), x + 16, y + 3, 0xD8E2F2, false);
            x += entryW;
        }
    }

    private void renderWindowBackground(GuiGraphics graphics) {
        graphics.fill(panelX(), panelY(), panelX() + panelW(), panelY() + panelH(), 0xFF0E1420);
        graphics.fill(panelX() + 1, panelY() + 1, panelX() + panelW() - 1, panelY() + panelH() - 1, 0xFF161D2A);

        graphics.fill(panelX(), panelY(), panelX() + panelW(), panelY() + 2, 0x66FFFFFF);
        graphics.fill(panelX(), panelY(), panelX() + 2, panelY() + panelH(), 0x403F5E86);
        graphics.fill(panelX() + panelW() - 2, panelY(), panelX() + panelW(), panelY() + panelH(), 0x403F5E86);
        graphics.fill(panelX(), panelY() + panelH() - 2, panelX() + panelW(), panelY() + panelH(), 0x403F5E86);

        graphics.fill(panelX() + 4, panelY() + 4, panelX() + panelW() - 4, panelY() + 92, 0xFF141C2B);
    }

    private void renderHeader(GuiGraphics graphics) {
        graphics.drawString(this.font, this.title, panelX() + 16, panelY() + 16, 0xF2F7FF, false);
        graphics.drawString(
                this.font,
                Component.translatable("iu.pollution_analyzer.header.subtitle"),
                panelX() + 16,
                panelY() + 28,
                0x98A9C2,
                false
        );
    }

    private void renderBodyFrames(GuiGraphics graphics) {
        graphics.fill(previewX(), previewY(), previewX() + previewW(), previewY() + previewH(), 0xFF0A101A);
        graphics.fill(previewX() + 1, previewY() + 1, previewX() + previewW() - 1, previewY() + previewH() - 1, 0xFF0C1220);

        graphics.fill(sideX(), sideY(), sideX() + sideW(), sideY() + sideH(), 0xFF111722);
        graphics.fill(sideX() + 1, sideY() + 1, sideX() + sideW() - 1, sideY() + sideH() - 1, 0xFF141B28);
    }

    private void renderPreviewBackground(GuiGraphics graphics) {
        int x1 = previewX() + 1;
        int y1 = previewY() + 1;
        int x2 = previewX() + previewW() - 1;
        int y2 = previewY() + previewH() - 1;

        graphics.fill(x1, y1, x2, y2, 0xFF07101D);
        graphics.fill(x1, y1, x2, y1 + 80, 0x220B2B58);
        graphics.fill(x1, y2 - 60, x2, y2, 0x22000000);
    }

    private void renderFooter(GuiGraphics graphics, PollutionAnalyzerSnapshot snapshot) {
        graphics.drawString(
                this.font,
                Component.translatable(
                        "iu.pollution_analyzer.footer.wind",
                        resolveWindDirection(snapshot.getWindSide()),
                        resolveWindLevel(snapshot.getWindType())
                ),
                previewX() + 2,
                footerY(),
                0xA8BCD9,
                false
        );

        long age = this.minecraft != null && this.minecraft.level != null
                ? Math.max(0L, this.minecraft.level.getGameTime() - snapshot.getWorldGameTime())
                : 0L;

        String updated = Component.translatable("iu.pollution_analyzer.footer.updated", age).getString();
        graphics.drawString(
                this.font,
                updated,
                panelX() + panelW() - this.font.width(updated) - 14,
                footerY(),
                0xA8BCD9,
                false
        );
    }

    private void renderSidePanel(GuiGraphics graphics, PollutionAnalyzerSnapshot snapshot, int mouseX, int mouseY) {
        graphics.drawString(this.font, activeTab.title(), sideX() + 12, sideY() + 10, 0xF1F6FF, false);

        int scrollbarW = 10;
        int contentX = sideX() + 10;
        int contentY = sideY() + 30;
        int contentW = sideW() - 10 - scrollbarW - 14;
        int contentH = sideH() - 40;

        graphics.enableScissor(sideX() + 1, sideY() + 28, sideX() + sideW() - 1, sideY() + sideH() - 1);

        switch (activeTab) {
            case OVERVIEW ->
                    renderOverviewTab(graphics, snapshot, mouseX, mouseY, contentX, contentY, contentW, contentH);
            case SOURCES ->
                    renderSourcesTab(graphics, snapshot, mouseX, mouseY, contentX, contentY, contentW, contentH);
            case RECOMMENDATIONS ->
                    renderRecommendationsTab(graphics, snapshot, contentX, contentY, contentW, contentH);
            case CLEANERS ->
                    renderCleanersTab(graphics, snapshot, mouseX, mouseY, contentX, contentY, contentW, contentH);
            case INFO -> renderInfoTab(graphics, contentX, contentY, contentW, contentH);
            case GRAPHS -> renderGraphsTab(graphics, snapshot, mouseX, mouseY, contentX, contentY, contentW, contentH);
        }

        graphics.disableScissor();
        renderRightScrollbar(graphics, rightScrollbarX(), rightScrollbarY(), rightScrollbarVisibleH());
    }

    private void renderOverviewTab(
            GuiGraphics graphics,
            PollutionAnalyzerSnapshot snapshot,
            int mouseX,
            int mouseY,
            int x,
            int y,
            int width,
            int visibleHeight
    ) {
        PollutionAnalyzerChunkSnapshot chunk = activeChunk(snapshot);
        if (chunk == null) {
            this.rightContentHeight = 0;
            this.rightMaxScroll = 0;
            drawCenteredBlockText(
                    graphics,
                    sideX(),
                    sideY(),
                    sideW(),
                    sideH(),
                    Component.translatable("iu.pollution_analyzer.no_data"),
                    0xC6D2E2
            );
            return;
        }

        PollutionAnalyzerWorldPreviewCache.PreviewChunk previewChunk = findPreviewChunk(chunk.key());

        List<BlockLine> headerLines = new ArrayList<>();
        appendWrapped(
                headerLines,
                Component.translatable("iu.pollution_analyzer.chunk.coords", chunk.getChunkX(), chunk.getChunkZ()),
                0xEAF2FF,
                width
        );

        int x0 = chunk.getChunkX() << 4;
        int z0 = chunk.getChunkZ() << 4;
        int x1 = x0 + 15;
        int z1 = z0 + 15;
        int y0 = worldPreviewCache.getGlobalBaseY();
        int y1 = previewChunk != null ? previewChunk.getMaxTopY() : worldPreviewCache.getGlobalMaxVisibleY();

        appendWrapped(
                headerLines,
                Component.translatable("iu.pollution_analyzer.chunk.bounds", x0, y0, z0, x1, y1, z1),
                0xA9B7CB,
                width
        );

        MediumCardContent airCard = buildMediumCardContent(
                "air",
                chunk.getAirSeverity(),
                chunk.getAirBandPollution(),
                chunk.getAirLevelOrdinal(),
                chunk.getAirRatePerSecond(),
                chunk.getAirTimeToRiseSeconds(),
                chunk.getAirTimeToFallSeconds(),
                chunk.getAirForecastFiveMinutes(),
                snapshot.getAirThresholds(),
                width
        );

        MediumCardContent soilCard = buildMediumCardContent(
                "soil",
                chunk.getSoilSeverity(),
                chunk.getSoilBandPollution(),
                chunk.getSoilLevelOrdinal(),
                chunk.getSoilRatePerSecond(),
                chunk.getSoilTimeToRiseSeconds(),
                chunk.getSoilTimeToFallSeconds(),
                chunk.getSoilForecastFiveMinutes(),
                snapshot.getSoilThresholds(),
                width
        );

        List<PollutionAnalyzerSourceSnapshot> localSources = snapshot.getSources().stream()
                .filter(source -> source.getChunkX() == chunk.getChunkX() && source.getChunkZ() == chunk.getChunkZ())
                .toList();

        List<OverviewLocalSourceLayout> layouts = new ArrayList<>();
        int localHeight = 26;

        for (PollutionAnalyzerSourceSnapshot source : localSources) {
            int textX = x + 34;
            int textW = calcInnerTextWidth(x, width, textX, 12);

            List<FormattedCharSequence> nameLines = new ArrayList<>(this.font.split(Component.literal(source.getName()), textW));
            List<BlockLine> statLines = new ArrayList<>();

            appendWrappedColored(
                    statLines,
                    Component.translatable("iu.pollution_analyzer.source.air_short", formatRateUnit("air", source.getAirCurrentContribution())),
                    0x9DB0C8,
                    textW
            );
            appendWrappedColored(
                    statLines,
                    Component.translatable("iu.pollution_analyzer.source.soil_short", formatRateUnit("soil", source.getSoilCurrentContribution())),
                    0x9DB0C8,
                    textW
            );

            appendCleanerStatLines(statLines, source, textW);

            int rowHeight = 10 + (nameLines.size() + statLines.size()) * (this.font.lineHeight + 1) + 10;
            layouts.add(new OverviewLocalSourceLayout(source, nameLines, statLines, rowHeight));
            localHeight += rowHeight + 4;
        }

        this.rightContentHeight =
                12
                        + headerLines.size() * (this.font.lineHeight + 2)
                        + 6
                        + airCard.height
                        + 10
                        + soilCard.height
                        + 10
                        + localHeight;

        this.rightMaxScroll = Math.max(0, this.rightContentHeight - visibleHeight);
        this.rightScroll = Mth.clamp(this.rightScroll, 0, this.rightMaxScroll);

        int drawY = y - this.rightScroll;
        for (BlockLine line : headerLines) {
            graphics.drawString(this.font, line.sequence(), x, drawY, line.color(), false);
            drawY += this.font.lineHeight + 2;
        }
        drawY += 6;

        drawY = renderMediumCard(graphics, x, drawY, width, airCard);
        drawY += 10;
        drawY = renderMediumCard(graphics, x, drawY, width, soilCard);
        drawY += 10;

        graphics.fill(x, drawY, x + width, drawY + localHeight, 0xFF202835);
        graphics.fill(x, drawY, x + 4, drawY + localHeight, 0xFF69B4FF);
        graphics.drawString(
                this.font,
                Component.translatable("iu.pollution_analyzer.chunk.active_sources", localSources.size()),
                x + 10,
                drawY + 8,
                0xEDF3FF,
                false
        );

        int rowY = drawY + 24;
        for (OverviewLocalSourceLayout layout : layouts) {
            PollutionAnalyzerSourceSnapshot source = layout.source();

            graphics.fill(x + 8, rowY - 2, x + width - 8, rowY + layout.height() - 4, 0xFF121924);

            if (!source.getRenderStack().isEmpty()) {
                graphics.renderItem(source.getRenderStack(), x + 12, rowY + 2);
                if (mouseX >= x + 12 && mouseX < x + 28 && mouseY >= rowY + 2 && mouseY < rowY + 18) {
                    hoveredItem = source.getRenderStack();
                }
            }

            int textX = x + 34;
            int textY = rowY;
            for (FormattedCharSequence line : layout.nameLines()) {
                graphics.drawString(this.font, line, textX, textY, 0xE3ECFA, false);
                textY += this.font.lineHeight + 1;
            }
            for (BlockLine line : layout.statLines()) {
                graphics.drawString(this.font, line.sequence(), textX, textY, line.color(), false);
                textY += this.font.lineHeight + 1;
            }

            rowY += layout.height() + 4;
        }
    }

    private PollutionAnalyzerWorldPreviewCache.PreviewChunk findPreviewChunk(long key) {
        for (PollutionAnalyzerWorldPreviewCache.PreviewChunk chunk : this.worldPreviewCache.getChunks()) {
            if (net.minecraft.world.level.ChunkPos.asLong(chunk.getChunkX(), chunk.getChunkZ()) == key) {
                return chunk;
            }
        }
        return null;
    }

    private MediumCardContent buildMediumCardContent(
            String medium,
            double severity,
            double bandPollution,
            int levelOrdinal,
            double ratePerSecond,
            int timeToRiseSeconds,
            int timeToFallSeconds,
            double forecastFiveMinutes,
            PollutionAnalyzerThresholds thresholds,
            int width
    ) {
        List<BlockLine> lines = new ArrayList<>();
        int lineWidth = width - 22;

        Component currentState = stateText(severity, thresholds);
        Component currentLevel = Component.translatable(
                "iu.pollution_analyzer.level." + levelName(levelOrdinal).toLowerCase(Locale.ROOT)
        );

        appendWrapped(lines,
                Component.translatable("iu.pollution_analyzer.metric.current_state", currentState),
                0xEAF2FF,
                lineWidth);

        appendWrapped(lines,
                Component.translatable("iu.pollution_analyzer.metric.current_level", currentLevel),
                0xC6D4E8,
                lineWidth);

        appendWrapped(lines,
                Component.translatable("iu.pollution_analyzer.metric.load", formatTotalUnit(medium, severity)),
                0xDDE7F7,
                lineWidth);

        appendWrapped(lines,
                Component.translatable(
                        "iu.pollution_analyzer.metric.band",
                        formatRawUnit(medium, bandPollution),
                        currentLevel
                ),
                0xB4C1D6,
                lineWidth);

        appendWrapped(lines,
                Component.translatable(
                        "iu.pollution_analyzer.metric.trend",
                        trendText(ratePerSecond),
                        formatRateUnit(medium, ratePerSecond)
                ),
                0xDDE7F7,
                lineWidth);

        appendWrapped(lines,
                Component.translatable("iu.pollution_analyzer.metric.forecast_5m", formatTotalUnit(medium, forecastFiveMinutes)),
                0xDDE7F7,
                lineWidth);

        appendWrapped(lines,
                Component.translatable("iu.pollution_analyzer.metric.worsen_in", formatTimeOrDash(timeToRiseSeconds)),
                0xDDE7F7,
                lineWidth);

        appendWrapped(lines,
                Component.translatable("iu.pollution_analyzer.metric.improve_in", formatTimeOrDash(timeToFallSeconds)),
                0xDDE7F7,
                lineWidth);

        appendWrapped(lines,
                Component.translatable("iu.pollution_analyzer.metric.safe", formatTotalUnit(medium, thresholds.getRecommendedMax())),
                0x9FDFB1,
                lineWidth);

        appendWrapped(lines,
                Component.translatable("iu.pollution_analyzer.metric.danger", formatTotalUnit(medium, thresholds.getDangerFrom())),
                0xF3C56A,
                lineWidth);

        appendWrapped(lines,
                Component.translatable("iu.pollution_analyzer.metric.critical", formatTotalUnit(medium, thresholds.getCriticalFrom())),
                0xF08A8A,
                lineWidth);

        int height = 30 + lines.size() * (this.font.lineHeight + 2) + 12;
        return new MediumCardContent(
                Component.translatable("iu.pollution_analyzer.medium." + medium),
                stateText(severity, thresholds),
                riskColor(severity, thresholds),
                lines,
                height
        );
    }

    private int renderMediumCard(GuiGraphics graphics, int x, int y, int width, MediumCardContent content) {
        graphics.fill(x, y, x + width, y + content.height(), 0xFF202835);
        graphics.fill(x, y, x + 4, y + content.height(), content.accentColor());

        graphics.drawString(this.font, content.title(), x + 10, y + 8, 0xFFFFFF, false);
        graphics.drawString(
                this.font,
                content.state(),
                x + width - this.font.width(content.state()) - 10,
                y + 8,
                content.accentColor(),
                false
        );

        int textY = y + 26;
        for (BlockLine line : content.lines()) {
            graphics.drawString(this.font, line.sequence(), x + 10, textY, line.color(), false);
            textY += this.font.lineHeight + 2;
        }

        return y + content.height();
    }

    private int calcInnerTextWidth(int cardX, int cardWidth, int textX, int rightPadding) {
        return Math.max(72, (cardX + cardWidth - rightPadding) - textX);
    }

    private void renderSourcesTab(
            GuiGraphics graphics,
            PollutionAnalyzerSnapshot snapshot,
            int mouseX,
            int mouseY,
            int x,
            int y,
            int width,
            int visibleHeight
    ) {
        List<PollutionAnalyzerSourceSnapshot> sources = snapshot.getSources();

        List<SourceCardLayout> layouts = new ArrayList<>();
        int totalHeight = 0;

        for (PollutionAnalyzerSourceSnapshot source : sources) {
            int textX = x + 34;
            int textW = calcInnerTextWidth(x, width, textX, 12);

            List<FormattedCharSequence> nameLines = new ArrayList<>(this.font.split(Component.literal(source.getName()), textW));
            List<BlockLine> statLines = new ArrayList<>();

            appendWrappedColored(
                    statLines,
                    Component.translatable("iu.pollution_analyzer.source.air_short", formatRateUnit("air", source.getAirCurrentContribution())),
                    0xE7C55F,
                    textW
            );
            appendWrappedColored(
                    statLines,
                    Component.translatable("iu.pollution_analyzer.source.soil_short", formatRateUnit("soil", source.getSoilCurrentContribution())),
                    0xE7C55F,
                    textW
            );

            appendCleanerStatLines(statLines, source, textW);

            List<FormattedCharSequence> posLines = new ArrayList<>();
            posLines.addAll(this.font.split(
                    Component.translatable("iu.pollution_analyzer.source.position",
                            source.getBlockX(), source.getBlockY(), source.getBlockZ()),
                    textW
            ));
            posLines.addAll(this.font.split(
                    Component.translatable("iu.pollution_analyzer.source.chunk",
                            source.getChunkX(), source.getChunkZ()),
                    textW
            ));

            int lineCount = nameLines.size() + statLines.size() + posLines.size();
            int height = Math.max(48, 14 + lineCount * (this.font.lineHeight + 1) + 10);

            layouts.add(new SourceCardLayout(source, nameLines, statLines, posLines, height));
            totalHeight += height + 8;
        }

        this.rightContentHeight = totalHeight;
        this.rightMaxScroll = Math.max(0, this.rightContentHeight - visibleHeight);
        this.rightScroll = Mth.clamp(this.rightScroll, 0, this.rightMaxScroll);

        int drawY = y - this.rightScroll;

        for (SourceCardLayout layout : layouts) {
            PollutionAnalyzerSourceSnapshot source = layout.source();

            graphics.fill(x, drawY, x + width, drawY + layout.height(), 0xFF202835);
            graphics.fill(x, drawY, x + 4, drawY + layout.height(), 0xFF69B4FF);

            if (!source.getRenderStack().isEmpty()) {
                graphics.renderItem(source.getRenderStack(), x + 8, drawY + 12);
                if (mouseX >= x + 8 && mouseX < x + 24 && mouseY >= drawY + 12 && mouseY < drawY + 28) {
                    this.hoveredItem = source.getRenderStack();
                }
            }

            int textX = x + 34;
            int textY = drawY + 8;

            for (FormattedCharSequence line : layout.nameLines()) {
                graphics.drawString(this.font, line, textX, textY, 0xE3ECFA, false);
                textY += this.font.lineHeight + 1;
            }
            for (BlockLine line : layout.statLines()) {
                graphics.drawString(this.font, line.sequence(), textX, textY, line.color(), false);
                textY += this.font.lineHeight + 1;
            }
            for (FormattedCharSequence line : layout.posLines()) {
                graphics.drawString(this.font, line, textX, textY, 0x9DB0C8, false);
                textY += this.font.lineHeight + 1;
            }

            if (mouseX >= x && mouseX < x + width && mouseY >= drawY && mouseY < drawY + layout.height()) {
                this.hoveredTextTooltip = buildSourceTooltip(source);
            }

            drawY += layout.height() + 8;
        }
    }

    private void renderRecommendationsTab(
            GuiGraphics graphics,
            PollutionAnalyzerSnapshot snapshot,
            int x,
            int y,
            int width,
            int visibleHeight
    ) {
        List<PollutionAnalyzerRecommendation> recommendations = snapshot.getRecommendations();

        List<RecommendationCardLayout> layouts = new ArrayList<>();
        int totalHeight = 0;

        for (PollutionAnalyzerRecommendation recommendation : recommendations) {
            List<FormattedCharSequence> titleLines = this.font.split(recommendation.titleComponent(), width - 20);
            List<FormattedCharSequence> bodyLines = this.font.split(recommendation.bodyComponent(), width - 20);

            int height = 16
                    + titleLines.size() * (this.font.lineHeight + 1)
                    + 6
                    + bodyLines.size() * (this.font.lineHeight + 1)
                    + 10;

            layouts.add(new RecommendationCardLayout(recommendation, titleLines, bodyLines, height));
            totalHeight += height + 8;
        }

        this.rightContentHeight = totalHeight;
        this.rightMaxScroll = Math.max(0, this.rightContentHeight - visibleHeight);
        this.rightScroll = Mth.clamp(this.rightScroll, 0, this.rightMaxScroll);

        int drawY = y - this.rightScroll;
        for (RecommendationCardLayout layout : layouts) {
            int accent = severityColor(layout.recommendation().getSeverity());

            graphics.fill(x, drawY, x + width, drawY + layout.height(), 0xFF202835);
            graphics.fill(x, drawY, x + 4, drawY + layout.height(), accent);

            int textY = drawY + 8;
            for (FormattedCharSequence line : layout.titleLines()) {
                graphics.drawString(this.font, line, x + 10, textY, 0xF0F6FF, false);
                textY += this.font.lineHeight + 1;
            }

            textY += 4;
            for (FormattedCharSequence line : layout.bodyLines()) {
                graphics.drawString(this.font, line, x + 10, textY, 0xB9C7DA, false);
                textY += this.font.lineHeight + 1;
            }

            drawY += layout.height() + 8;
        }
    }

    private void renderCleanersTab(
            GuiGraphics graphics,
            PollutionAnalyzerSnapshot snapshot,
            int mouseX,
            int mouseY,
            int x,
            int y,
            int width,
            int visibleHeight
    ) {
        PollutionAnalyzerCleanerOption air = snapshot.getAirCleaner();
        PollutionAnalyzerCleanerOption soil = snapshot.getSoilCleaner();

        int airHeight = measureCleanerCardHeight(air, width);
        int soilHeight = measureCleanerCardHeight(soil, width);

        this.rightContentHeight = airHeight + 12 + soilHeight;
        this.rightMaxScroll = Math.max(0, this.rightContentHeight - visibleHeight);
        this.rightScroll = Mth.clamp(this.rightScroll, 0, this.rightMaxScroll);

        int drawY = y - this.rightScroll;
        drawY = renderCleanerCard(graphics, x, drawY, width, air, mouseX, mouseY);
        drawY += 12;
        renderCleanerCard(graphics, x, drawY, width, soil, mouseX, mouseY);
    }

    private int measureCleanerCardHeight(PollutionAnalyzerCleanerOption cleaner, int width) {
        if (cleaner == null) {
            return 50;
        }

        int textW = width - 44;

        List<FormattedCharSequence> titleLines = splitCleanerTitleLines(cleaner, textW);
        List<FormattedCharSequence> bodyLines = splitCleanerBodyLines(cleaner, textW);
        List<FormattedCharSequence> footerLines = splitCleanerFooterLines(cleaner, textW);

        return 14
                + titleLines.size() * (this.font.lineHeight + 1)
                + 6
                + bodyLines.size() * (this.font.lineHeight + 1)
                + 8
                + footerLines.size() * (this.font.lineHeight + 1)
                + 10;
    }

    private int renderCleanerCard(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            PollutionAnalyzerCleanerOption cleaner,
            int mouseX,
            int mouseY
    ) {
        if (cleaner == null) {
            graphics.fill(x, y, x + width, y + 50, 0xFF202835);
            graphics.fill(x, y, x + 4, y + 50, 0xFF566A87);
            graphics.drawString(
                    this.font,
                    Component.translatable("iu.pollution_analyzer.no_data"),
                    x + 10,
                    y + 18,
                    0xC7D3E5,
                    false
            );
            return y + 50;
        }

        int accent = "air".equals(cleaner.getMedium()) ? 0xFFE3C25A : 0xFF7CCC62;
        int textX = x + 32;
        int textW = width - 44;

        List<FormattedCharSequence> titleLines = splitCleanerTitleLines(cleaner, textW);
        List<FormattedCharSequence> bodyLines = splitCleanerBodyLines(cleaner, textW);
        List<FormattedCharSequence> footerLines = splitCleanerFooterLines(cleaner, textW);

        int height = 14
                + titleLines.size() * (this.font.lineHeight + 1)
                + 6
                + bodyLines.size() * (this.font.lineHeight + 1)
                + 8
                + footerLines.size() * (this.font.lineHeight + 1)
                + 10;

        graphics.fill(x, y, x + width, y + height, 0xFF202835);
        graphics.fill(x, y, x + 4, y + height, accent);

        ItemStack stack = cleaner.getStack();
        if (!stack.isEmpty()) {
            graphics.renderItem(stack, x + 10, y + 12);
            if (mouseX >= x + 10 && mouseX < x + 26 && mouseY >= y + 12 && mouseY < y + 28) {
                this.hoveredItem = stack;
            }
        }

        int textY = y + 10;
        for (FormattedCharSequence line : titleLines) {
            graphics.drawString(this.font, line, textX, textY, 0xF0F6FF, false);
            textY += this.font.lineHeight + 1;
        }

        textY += 4;
        for (FormattedCharSequence line : bodyLines) {
            graphics.drawString(this.font, line, textX, textY, 0xBAC7D9, false);
            textY += this.font.lineHeight + 1;
        }

        textY += 6;
        for (int i = 0; i < footerLines.size(); i++) {
            int color = i == footerLines.size() - 1 && cleaner.getEstimatedReductionPerSecond() >= 0.0D
                    ? 0xB9E3A0
                    : 0xA4B7D1;
            graphics.drawString(this.font, footerLines.get(i), textX, textY, color, false);
            textY += this.font.lineHeight + 1;
        }

        return y + height;
    }

    private void renderInfoTab(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int visibleHeight
    ) {
        List<InfoSection> sections = List.of(
                buildInfoSection("air", width),
                buildInfoSection("soil", width)
        );

        int total = 0;
        for (InfoSection section : sections) {
            total += section.height() + 10;
        }

        this.rightContentHeight = total;
        this.rightMaxScroll = Math.max(0, this.rightContentHeight - visibleHeight);
        this.rightScroll = Mth.clamp(this.rightScroll, 0, this.rightMaxScroll);

        int drawY = y - this.rightScroll;
        for (InfoSection section : sections) {
            graphics.fill(x, drawY, x + width, drawY + section.height(), 0xFF202835);
            graphics.fill(x, drawY, x + 4, drawY + section.height(), section.accent());

            graphics.drawString(this.font, section.title(), x + 10, drawY + 8, 0xF0F6FF, false);

            int textY = drawY + 24;
            for (BlockLine line : section.lines()) {
                graphics.drawString(this.font, line.sequence(), x + 10, textY, line.color(), false);
                textY += this.font.lineHeight + 2;
            }

            drawY += section.height() + 10;
        }
    }

    private InfoSection buildInfoSection(String medium, int width) {
        int accent = "air".equals(medium) ? 0xFFE3C25A : 0xFF7CCC62;
        List<BlockLine> lines = new ArrayList<>();
        int wrapWidth = width - 22;

        appendWrapped(lines, Component.translatable("iu.pollution_analyzer.info.stage.very_low." + medium), 0xA7E2B4, wrapWidth);
        appendWrapped(lines, Component.translatable("iu.pollution_analyzer.info.stage.low." + medium), 0xD9E3F0, wrapWidth);
        appendWrapped(lines, Component.translatable("iu.pollution_analyzer.info.stage.medium." + medium), 0xF1D577, wrapWidth);
        appendWrapped(lines, Component.translatable("iu.pollution_analyzer.info.stage.high." + medium), 0xF2A65A, wrapWidth);
        appendWrapped(lines, Component.translatable("iu.pollution_analyzer.info.stage.very_high." + medium), 0xF08A8A, wrapWidth);

        int height = 28 + lines.size() * (this.font.lineHeight + 2) + 10;
        return new InfoSection(
                Component.translatable("iu.pollution_analyzer.info.title." + medium),
                accent,
                lines,
                height
        );
    }

    private void renderGraphsTab(
            GuiGraphics graphics,
            PollutionAnalyzerSnapshot snapshot,
            int mouseX,
            int mouseY,
            int x,
            int y,
            int width,
            int visibleHeight
    ) {
        PollutionAnalyzerChunkSnapshot chunk = activeChunk(snapshot);
        if (chunk == null) {
            this.rightContentHeight = 0;
            this.rightMaxScroll = 0;
            return;
        }

        long key = chunk.key();

        List<GraphLayout> graphs = List.of(
                new GraphLayout(
                        Component.translatable("iu.pollution_analyzer.graph.air_rate"),
                        historyTracker.getAirRate(key),
                        0xFFE35A5A,
                        "air",
                        true
                ),
                new GraphLayout(
                        Component.translatable("iu.pollution_analyzer.graph.soil_rate"),
                        historyTracker.getSoilRate(key),
                        0xFFE35A5A,
                        "soil",
                        true
                ),
                new GraphLayout(
                        Component.translatable("iu.pollution_analyzer.graph.air_total"),
                        historyTracker.getAirTotal(key),
                        0xFFE35A5A,
                        "air",
                        false
                ),
                new GraphLayout(
                        Component.translatable("iu.pollution_analyzer.graph.soil_total"),
                        historyTracker.getSoilTotal(key),
                        0xFFE35A5A,
                        "soil",
                        false
                ),
                new GraphLayout(
                        Component.translatable("iu.pollution_analyzer.graph.air_cleaning"),
                        historyTracker.getAirCleaning(key),
                        0xFF69B4FF,
                        "air",
                        true
                ),
                new GraphLayout(
                        Component.translatable("iu.pollution_analyzer.graph.soil_cleaning"),
                        historyTracker.getSoilCleaning(key),
                        0xFF69B4FF,
                        "soil",
                        true
                )
        );

        int graphHeight = 138;

        this.rightContentHeight = graphs.size() * (graphHeight + 10);
        this.rightMaxScroll = Math.max(0, this.rightContentHeight - visibleHeight);
        this.rightScroll = Mth.clamp(this.rightScroll, 0, this.rightMaxScroll);

        int drawY = y - this.rightScroll;
        for (GraphLayout graph : graphs) {
            renderGraphCard(graphics, mouseX, mouseY, x, drawY, width, graphHeight, graph);
            drawY += graphHeight + 10;
        }
    }

    private void renderGraphCard(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            int x,
            int y,
            int width,
            int height,
            GraphLayout graph
    ) {
        graphics.fill(x, y, x + width, y + height, 0xFF202835);
        graphics.fill(x, y, x + 4, y + height, graph.color());

        List<FormattedCharSequence> titleLines = this.font.split(graph.title(), width - 24);

        int textY = y + 8;
        for (FormattedCharSequence line : titleLines) {
            graphics.drawString(this.font, line, x + 10, textY, 0xF0F6FF, false);
            textY += this.font.lineHeight + 1;
        }

        int labelY = textY + 1;
        int labelW = (width - 24) / 2;

        String minLabel = trimToWidth(
                graph.rate()
                        ? formatRateUnit(graph.medium(), min(graph.values()))
                        : formatTotalUnit(graph.medium(), min(graph.values())),
                labelW
        );
        String maxLabel = trimToWidth(
                graph.rate()
                        ? formatRateUnit(graph.medium(), max(graph.values()))
                        : formatTotalUnit(graph.medium(), max(graph.values())),
                labelW
        );

        graphics.drawString(this.font, minLabel, x + 10, labelY, 0x90A7C5, false);
        graphics.drawString(this.font, maxLabel, x + width - 10 - this.font.width(maxLabel), labelY, 0x90A7C5, false);

        int chartX = x + 10;
        int chartY = labelY + this.font.lineHeight + 4;
        int chartW = width - 20;
        int chartH = 56;

        graphics.fill(chartX, chartY, chartX + chartW, chartY + chartH, 0xFF0A1018);
        drawChartBorder(graphics, chartX, chartY, chartW, chartH);

        ChartPlot plot = buildChartPlot(chartX, chartY, chartW, chartH, graph.values());
        drawChartLines(graphics, plot, graph.color());

        int hoveredIndex = findHoveredChartPoint(plot, mouseX, mouseY, chartX, chartY, chartW, chartH);
        if (hoveredIndex >= 0) {
            drawGraphDot(graphics, plot.px()[hoveredIndex], plot.py()[hoveredIndex], 2, 0xFFFFFFFF);
            drawGraphDot(graphics, plot.px()[hoveredIndex], plot.py()[hoveredIndex], 1, graph.color());
            this.hoveredTextTooltip = buildGraphTooltip(graph, plot.values()[hoveredIndex], hoveredIndex, plot.values().length);
        }

        Component historyLabel = Component.translatable("iu.pollution_analyzer.graph.legend.history");
        Component zeroLabel = Component.translatable("iu.pollution_analyzer.graph.legend.zero");
        Component last60Label = Component.translatable("iu.pollution_analyzer.graph.legend.last60");

        int legendRow1Y = chartY + chartH + 8;
        int legendRow2Y = legendRow1Y + this.font.lineHeight + 4;

        graphics.fill(chartX, legendRow1Y + 2, chartX + 8, legendRow1Y + 10, graph.color());
        graphics.drawString(this.font, historyLabel, chartX + 12, legendRow1Y + 1, 0xB8C7DA, false);

        int zeroX = chartX + 12 + this.font.width(historyLabel) + 18;
        boolean zeroFitsSameRow = zeroX + 16 + this.font.width(zeroLabel) <= chartX + chartW;

        if (zeroFitsSameRow) {
            graphics.fill(zeroX, legendRow1Y + 5, zeroX + 12, legendRow1Y + 6, 0xFF7A93B5);
            graphics.drawString(this.font, zeroLabel, zeroX + 16, legendRow1Y + 1, 0xB8C7DA, false);
        } else {
            graphics.fill(chartX, legendRow2Y + 5, chartX + 12, legendRow2Y + 6, 0xFF7A93B5);
            graphics.drawString(this.font, zeroLabel, chartX + 16, legendRow2Y + 1, 0xB8C7DA, false);
        }

        String last60 = trimToWidth(last60Label.getString(), chartW);
        graphics.drawString(
                this.font,
                last60,
                chartX + chartW - this.font.width(last60),
                legendRow2Y + 1,
                0x8FA6C5,
                false
        );
    }

    private ChartPlot buildChartPlot(int x, int y, int width, int height, double[] sourceValues) {
        double[] values = compressSeries(sourceValues, Math.max(12, width - 8));
        if (values == null || values.length == 0) {
            return new ChartPlot(new double[0], new float[0], new float[0], x, x + width, Integer.MIN_VALUE);
        }

        int innerPad = 3;
        int left = x + innerPad;
        int top = y + innerPad;
        int w = Math.max(4, width - innerPad * 2);
        int h = Math.max(4, height - innerPad * 2);

        double min = min(values);
        double max = max(values);

        if (Math.abs(max - min) < 0.000001D) {
            double base = Math.abs(max);
            double pad = Math.max(1.0D, base * 0.15D);
            min -= pad;
            max += pad;
        } else {
            double pad = (max - min) * 0.12D;
            min -= pad;
            max += pad;
        }

        int zeroY = Integer.MIN_VALUE;
        if (min <= 0.0D && max >= 0.0D) {
            float t = (float) ((0.0D - min) / (max - min));
            zeroY = top + h - Math.round(t * h);
            zeroY = Mth.clamp(zeroY, top, top + h);
        }

        float[] px = new float[values.length];
        float[] py = new float[values.length];

        if (values.length == 1) {
            px[0] = left + (w / 2.0F);
            float t = (float) ((values[0] - min) / (max - min));
            py[0] = Mth.clamp(top + h - (t * h), top + 1, top + h - 1);
        } else {
            for (int i = 0; i < values.length; i++) {
                float tX = (float) i / (float) (values.length - 1);
                float tY = (float) ((values[i] - min) / (max - min));
                px[i] = left + tX * w;
                py[i] = Mth.clamp(top + h - (tY * h), top + 1, top + h - 1);
            }
        }

        return new ChartPlot(values, px, py, left, left + w, zeroY);
    }

    private void drawChartLines(GuiGraphics graphics, ChartPlot plot, int lineColor) {
        if (plot.values() == null || plot.values().length == 0) {
            return;
        }

        if (plot.zeroY() != Integer.MIN_VALUE) {
            graphics.fill(plot.left(), plot.zeroY(), plot.right(), plot.zeroY() + 1, 0xFF7A93B5);
        }

        for (int i = 0; i < plot.values().length - 1; i++) {
            drawThickSegment(graphics, plot.px()[i], plot.py()[i], plot.px()[i + 1], plot.py()[i + 1], 2.8F, 0xFF101218);
        }
        if (plot.values().length == 1) {
            drawGraphDot(graphics, plot.px()[0], plot.py()[0], 2, 0xFF101218);
        }

        for (int i = 0; i < plot.values().length - 1; i++) {
            drawThickSegment(graphics, plot.px()[i], plot.py()[i], plot.px()[i + 1], plot.py()[i + 1], 1.4F, lineColor);
        }
        if (plot.values().length == 1) {
            drawGraphDot(graphics, plot.px()[0], plot.py()[0], 1, lineColor);
        }
    }

    private int findHoveredChartPoint(
            ChartPlot plot,
            int mouseX,
            int mouseY,
            int chartX,
            int chartY,
            int chartW,
            int chartH
    ) {
        if (plot.values() == null || plot.values().length == 0) {
            return -1;
        }
        if (mouseX < chartX || mouseX >= chartX + chartW || mouseY < chartY || mouseY >= chartY + chartH) {
            return -1;
        }

        int bestIndex = -1;
        double bestDistSq = 64.0D;

        for (int i = 0; i < plot.values().length; i++) {
            double dx = mouseX - plot.px()[i];
            double dy = mouseY - plot.py()[i];
            double distSq = dx * dx + dy * dy;

            if (distSq < bestDistSq) {
                bestDistSq = distSq;
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    private List<Component> buildGraphTooltip(GraphLayout graph, double value, int pointIndex, int totalPoints) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(graph.title().copy().withStyle(ChatFormatting.AQUA));

        String valueText = graph.rate()
                ? formatRateUnit(graph.medium(), value)
                : formatTotalUnit(graph.medium(), value);

        tooltip.add(Component.translatable("iu.pollution_analyzer.graph.hover.value", valueText).withStyle(ChatFormatting.GRAY));

        if (totalPoints > 1) {
            int secondsAgo = Math.round((totalPoints - 1 - pointIndex) * 60.0F / (totalPoints - 1));
            Component timeComponent = secondsAgo <= 0
                    ? Component.translatable("iu.pollution_analyzer.graph.hover.now")
                    : Component.translatable("iu.pollution_analyzer.graph.hover.seconds_ago", secondsAgo);

            tooltip.add(Component.translatable("iu.pollution_analyzer.graph.hover.time", timeComponent).withStyle(ChatFormatting.DARK_GRAY));
        }

        return tooltip;
    }

    private String cleanerDisplayName(PollutionAnalyzerCleanerOption cleaner) {
        String name = cleaner.getDisplayName() == null ? "" : cleaner.getDisplayName().trim();
        if (name.isEmpty()) {
            name = Component.translatable("iu.pollution_analyzer.medium." + cleaner.getMedium()).getString();
        }
        return name;
    }

    private List<FormattedCharSequence> splitCleanerTitleLines(PollutionAnalyzerCleanerOption cleaner, int textW) {
        return new ArrayList<>(this.font.split(Component.literal(cleanerDisplayName(cleaner)), textW));
    }

    private List<FormattedCharSequence> splitCleanerBodyLines(PollutionAnalyzerCleanerOption cleaner, int textW) {
        return new ArrayList<>(this.font.split(cleaner.descriptionComponent(), textW));
    }

    private List<FormattedCharSequence> splitCleanerFooterLines(PollutionAnalyzerCleanerOption cleaner, int textW) {
        List<FormattedCharSequence> footerLines = new ArrayList<>();

        footerLines.addAll(this.font.split(
                Component.translatable(cleaner.isBlockLike()
                        ? "iu.pollution_analyzer.cleaner.type.block"
                        : "iu.pollution_analyzer.cleaner.type.module"),
                textW
        ));

        if (cleaner.getDetectedLevel() >= 0) {
            footerLines.addAll(this.font.split(
                    Component.translatable(
                            "iu.pollution_analyzer.cleaner.current_level",
                            cleaner.getDetectedLevel(),
                            cleaner.getMaxLevel()
                    ),
                    textW
            ));
        }

        footerLines.addAll(this.font.split(
                Component.translatable(
                        "iu.pollution_analyzer.cleaner.base_power",
                        "air".equals(cleaner.getMedium())
                                ? formatRateUnit("air", cleaner.getBaseReductionPerSecond())
                                : formatRateUnit("soil", cleaner.getBaseReductionPerSecond())
                ),
                textW
        ));

        if (cleaner.getLevelBonusReductionPerSecond() > 0.0D) {
            footerLines.addAll(this.font.split(
                    Component.translatable(
                            "iu.pollution_analyzer.cleaner.per_level_power",
                            "air".equals(cleaner.getMedium())
                                    ? formatRateUnit("air", cleaner.getLevelBonusReductionPerSecond())
                                    : formatRateUnit("soil", cleaner.getLevelBonusReductionPerSecond())
                    ),
                    textW
            ));
        }

        if (cleaner.getEstimatedReductionPerSecond() >= 0.0D) {
            footerLines.addAll(this.font.split(
                    Component.translatable(
                            "iu.pollution_analyzer.cleaner.current_power",
                            "air".equals(cleaner.getMedium())
                                    ? formatRateUnit("air", cleaner.getEstimatedReductionPerSecond())
                                    : formatRateUnit("soil", cleaner.getEstimatedReductionPerSecond())
                    ),
                    textW
            ));
        }

        if (cleaner.getRecommendedSingleMachineLevel() >= 0) {
            footerLines.addAll(this.font.split(
                    Component.translatable(
                            "iu.pollution_analyzer.cleaner.plan.dual_single",
                            cleaner.getRecommendedBaseMachineCount(),
                            cleaner.getMaxLevel(),
                            cleaner.getRecommendedSingleMachineLevel(),
                            cleaner.getMaxLevel()
                    ),
                    textW
            ));
        } else {
            footerLines.addAll(this.font.split(
                    Component.translatable(
                            "iu.pollution_analyzer.cleaner.plan.dual_only",
                            cleaner.getRecommendedBaseMachineCount(),
                            cleaner.getMaxLevel()
                    ),
                    textW
            ));
        }

        footerLines.addAll(this.font.split(
                Component.translatable(
                        "air".equals(cleaner.getMedium())
                                ? "iu.pollution_analyzer.cleaner.module.air"
                                : "iu.pollution_analyzer.cleaner.module.soil"
                ),
                textW
        ));

        footerLines.addAll(this.font.split(
                Component.translatable("iu.pollution_analyzer.cleaner.combo"),
                textW
        ));

        return footerLines;
    }

    private void drawChartBorder(GuiGraphics graphics, int x, int y, int w, int h) {
        int c = 0xFF566E8F;
        graphics.fill(x, y, x + w, y + 1, c);
        graphics.fill(x, y + h - 1, x + w, y + h, c);
        graphics.fill(x, y, x + 1, y + h, c);
        graphics.fill(x + w - 1, y, x + w, y + h, c);
    }

    private double[] compressSeries(double[] source, int maxPoints) {
        if (source == null || source.length == 0 || maxPoints <= 0) {
            return new double[0];
        }
        if (source.length <= maxPoints) {
            return source;
        }

        double[] out = new double[maxPoints];
        double bucket = source.length / (double) maxPoints;

        for (int i = 0; i < maxPoints; i++) {
            int start = (int) Math.floor(i * bucket);
            int end = (int) Math.floor((i + 1) * bucket);
            if (end <= start) {
                end = Math.min(source.length, start + 1);
            }

            double sum = 0.0D;
            int count = 0;
            for (int j = start; j < end && j < source.length; j++) {
                sum += source[j];
                count++;
            }
            out[i] = count == 0 ? 0.0D : sum / count;
        }

        return out;
    }

    private void drawThickSegment(GuiGraphics graphics, float x1, float y1, float x2, float y2, float thickness, int color) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = Mth.sqrt(dx * dx + dy * dy);
        if (len < 0.001F) {
            drawGraphDot(graphics, x1, y1, Math.max(1, Math.round(thickness)), color);
            return;
        }

        float nx = -dy / len * (thickness / 2.0F);
        float ny = dx / len * (thickness / 2.0F);

        Matrix4f matrix = graphics.pose().last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        buffer.addVertex(matrix, x1 - nx, y1 - ny, 0.0F).setColor(r, g, b, a);
        buffer.addVertex(matrix, x1 + nx, y1 + ny, 0.0F).setColor(r, g, b, a);
        buffer.addVertex(matrix, x2 + nx, y2 + ny, 0.0F).setColor(r, g, b, a);
        buffer.addVertex(matrix, x2 - nx, y2 - ny, 0.0F).setColor(r, g, b, a);

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private void drawGraphDot(GuiGraphics graphics, float x, float y, int radius, int color) {
        graphics.fill((int) x - radius, (int) y - radius, (int) x + radius + 1, (int) y + radius + 1, color);
    }

    private void renderRightScrollbar(GuiGraphics graphics, int x, int y, int visibleHeight) {
        graphics.fill(x, y, x + 4, y + visibleHeight, 0xFF243040);

        if (rightMaxScroll <= 0) {
            graphics.fill(x, y, x + 4, y + visibleHeight, 0xFF6E86AB);
            return;
        }

        int thumbHeight = Math.max(18, (int) ((visibleHeight / (float) rightContentHeight) * visibleHeight));
        int movable = visibleHeight - thumbHeight;
        int thumbY = y + Math.round((rightScroll / (float) rightMaxScroll) * movable);

        graphics.fill(x, thumbY, x + 4, thumbY + thumbHeight, 0xFF6A87B8);
        graphics.fill(x, thumbY, x + 4, thumbY + 2, 0xFFFFFFFF);
    }

    private List<Component> buildSourceTooltip(PollutionAnalyzerSourceSnapshot source) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(source.getName()).withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable(
                "iu.pollution_analyzer.source.position",
                source.getBlockX(),
                source.getBlockY(),
                source.getBlockZ()
        ).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(
                "iu.pollution_analyzer.source.chunk",
                source.getChunkX(),
                source.getChunkZ()
        ).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(
                "iu.pollution_analyzer.source.air_full",
                formatRateUnit("air", source.getAirCurrentContribution())
        ).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(
                "iu.pollution_analyzer.source.soil_full",
                formatRateUnit("soil", source.getSoilCurrentContribution())
        ).withStyle(ChatFormatting.GRAY));

        if (source.isCleaner()) {
            tooltip.add(Component.translatable(
                    "iu.pollution_analyzer.source.cleaner.level",
                    source.getMechanismLevel(),
                    source.getMaxMechanismLevel()
            ).withStyle(ChatFormatting.BLUE));

            if (source.getAirCleaningPerSecond() > 0.0D) {
                tooltip.add(Component.translatable(
                        "iu.pollution_analyzer.source.cleaner.air_cleaning",
                        formatRateUnit("air", source.getAirCleaningPerSecond())
                ).withStyle(ChatFormatting.BLUE));
            }

            if (source.getSoilCleaningPerSecond() > 0.0D) {
                tooltip.add(Component.translatable(
                        "iu.pollution_analyzer.source.cleaner.soil_cleaning",
                        formatRateUnit("soil", source.getSoilCleaningPerSecond())
                ).withStyle(ChatFormatting.BLUE));
            }
        }

        return tooltip;
    }

    private void renderPreview(GuiGraphics graphics, PollutionAnalyzerSnapshot snapshot, float partialTick) {
        List<PollutionAnalyzerWorldPreviewCache.PreviewChunk> chunks = worldPreviewCache.getChunks();
        if (chunks.isEmpty()) {
            drawCenteredBlockText(
                    graphics,
                    previewX(),
                    previewSceneY(),
                    previewW(),
                    previewSceneH(),
                    Component.translatable("iu.pollution_analyzer.no_data"),
                    0xD2DDEF
            );
            return;
        }

        int globalBaseY = worldPreviewCache.getGlobalBaseY();
        int globalMaxY = worldPreviewCache.getGlobalMaxVisibleY();
        int displayHeight = Math.max(18, (globalMaxY - globalBaseY) + 2);

        float chunkGap = 0.0F;
        float totalWidth = 16 * 3 + chunkGap * 2;
        float fitByWidth = (previewW() - 44.0F) / totalWidth;
        float fitByHeight = (previewSceneH() - 40.0F) / (displayHeight + totalWidth * 0.42F);
        float scale = Math.min(fitByWidth, fitByHeight) * this.zoom;

        int centerX = previewX() + previewW() / 2;
        int centerY = previewSceneY() + (previewSceneH() / 2) + 20;

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        Lighting.setupFor3DItems();

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(centerX, centerY, 320.0F);
        poseStack.scale(scale, -scale, scale);
        poseStack.mulPose(Axis.XP.rotationDegrees(this.pitch));
        poseStack.mulPose(Axis.YP.rotationDegrees(this.yaw));

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        Map<Long, PollutionAnalyzerWorldPreviewCache.PreviewFluidCell> globalFluidMap = new HashMap<>();
        for (PollutionAnalyzerWorldPreviewCache.PreviewChunk chunk : chunks) {
            for (PollutionAnalyzerWorldPreviewCache.PreviewFluidCell fluid : chunk.getFluids()) {
                int worldX = (chunk.getChunkX() << 4) + fluid.getLocalX();
                int worldZ = (chunk.getChunkZ() << 4) + fluid.getLocalZ();
                globalFluidMap.put(fluidWorldKey(worldX, worldZ, fluid.getWorldY()), fluid);
            }
        }

        int centerChunkX = snapshot.getCenterChunkX();
        int centerChunkZ = snapshot.getCenterChunkZ();

        for (PollutionAnalyzerWorldPreviewCache.PreviewChunk chunk : chunks) {
            long chunkKey = net.minecraft.world.level.ChunkPos.asLong(chunk.getChunkX(), chunk.getChunkZ());

            float shiftX = (chunk.getChunkX() - centerChunkX) * (16.0F + chunkGap);
            float shiftZ = (chunk.getChunkZ() - centerChunkZ) * (16.0F + chunkGap);

            float lift = 0.0F;
            if (chunkKey == selectedChunkKey) {
                lift = 0.90F;
            } else if (chunkKey == hoveredChunkKey) {
                lift = 0.28F;
            }

            int tintColor = 0x00000000;
            if (chunkKey == selectedChunkKey) {
                PollutionAnalyzerChunkSnapshot data = snapshot.getChunk(chunkKey);
                if (data != null) {
                    tintColor = (riskColor(Math.max(data.getAirSeverity(), data.getSoilSeverity()), snapshot.getAirThresholds()) & 0x00FFFFFF) | 0x44000000;
                }
            } else if (chunkKey == hoveredChunkKey) {
                tintColor = 0x224FC7FF;
            }

            poseStack.pushPose();
            poseStack.translate(shiftX, -lift, shiftZ);

            if ((tintColor >>> 24) != 0) {
                renderChunkSideTint(poseStack, chunk, globalBaseY, tintColor);
            }

            for (PollutionAnalyzerWorldPreviewCache.PreviewSolid solid : chunk.getSolids()) {
                renderRealPreviewBlock(
                        poseStack,
                        bufferSource,
                        solid.getState(),
                        solid.displayX(),
                        solid.displayY(globalBaseY),
                        solid.displayZ()
                );
            }

            poseStack.popPose();
        }

        bufferSource.endBatch();

        for (PollutionAnalyzerWorldPreviewCache.PreviewChunk chunk : chunks) {
            float shiftX = (chunk.getChunkX() - centerChunkX) * (16.0F + chunkGap);
            float shiftZ = (chunk.getChunkZ() - centerChunkZ) * (16.0F + chunkGap);

            long chunkKey = net.minecraft.world.level.ChunkPos.asLong(chunk.getChunkX(), chunk.getChunkZ());

            float lift = 0.0F;
            if (chunkKey == selectedChunkKey) {
                lift = 0.90F;
            } else if (chunkKey == hoveredChunkKey) {
                lift = 0.28F;
            }

            poseStack.pushPose();
            poseStack.translate(shiftX, -lift, shiftZ);

            renderChunkFluidCellsTextured(
                    poseStack,
                    bufferSource,
                    chunk,
                    globalBaseY,
                    globalFluidMap
            );

            poseStack.popPose();
        }

        bufferSource.endBatch();

        for (PollutionAnalyzerWorldPreviewCache.PreviewChunk chunk : chunks) {
            long chunkKey = net.minecraft.world.level.ChunkPos.asLong(chunk.getChunkX(), chunk.getChunkZ());

            boolean selected = chunkKey == selectedChunkKey;
            boolean hovered = chunkKey == hoveredChunkKey;

            if (!selected && !hovered) {
                continue;
            }

            float shiftX = (chunk.getChunkX() - centerChunkX) * (16.0F + chunkGap);
            float shiftZ = (chunk.getChunkZ() - centerChunkZ) * (16.0F + chunkGap);

            float lift = selected ? 0.90F : 0.28F;

            int outlineColor;
            if (selected) {
                PollutionAnalyzerChunkSnapshot data = snapshot.getChunk(chunkKey);
                outlineColor = data != null
                        ? riskColor(Math.max(data.getAirSeverity(), data.getSoilSeverity()), snapshot.getAirThresholds())
                        : 0xFF69B4FF;
            } else {
                outlineColor = 0xFF90D8FF;
            }

            float minX = -8.0F;
            float minZ = -8.0F;
            float maxX = 8.0F;
            float maxZ = 8.0F;
            float minY = 1.0F;
            float maxY = (chunk.getMaxTopY() - globalBaseY) + 2.0F;

            poseStack.pushPose();
            poseStack.translate(shiftX, -lift, shiftZ);
            drawChunkOutlineImmediate(poseStack, minX, minY, minZ, maxX, maxY, maxZ, outlineColor);
            poseStack.popPose();
        }

        poseStack.popPose();

        bufferSource.endBatch();

        Lighting.setupForFlatItems();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    private void renderChunkSideTint(
            PoseStack poseStack,
            PollutionAnalyzerWorldPreviewCache.PreviewChunk chunk,
            int globalBaseY,
            int color
    ) {
        Matrix4f matrix = poseStack.last().pose();

        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        float minY = 1.0F;
        float maxY = (chunk.getMaxTopY() - globalBaseY) + 2.0F;

        float eps = 0.035F;
        float minX = -8.0F - eps;
        float maxX = 8.0F + eps;
        float minZ = -8.0F - eps;
        float maxZ = 8.0F + eps;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        buffer.addVertex(matrix, minX, minY, minZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, minX, maxY, minZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, maxX, maxY, minZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, maxX, minY, minZ).setColor(r, g, b, a);

        buffer.addVertex(matrix, maxX, minY, maxZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, maxX, maxY, maxZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, minX, maxY, maxZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, minX, minY, maxZ).setColor(r, g, b, a);

        buffer.addVertex(matrix, minX, minY, maxZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, minX, maxY, maxZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, minX, maxY, minZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, minX, minY, minZ).setColor(r, g, b, a);

        buffer.addVertex(matrix, maxX, minY, minZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, maxX, maxY, minZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, maxX, maxY, maxZ).setColor(r, g, b, a);
        buffer.addVertex(matrix, maxX, minY, maxZ).setColor(r, g, b, a);

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private long fluidWorldKey(int worldX, int worldZ, int worldY) {
        long x = ((long) worldX & 0x3FFFFFFL) << 38;
        long z = ((long) worldZ & 0x3FFFFFFL) << 12;
        long y = ((long) worldY & 0xFFFL);
        return x | z | y;
    }

    private void renderChunkFluidCellsTextured(
            PoseStack poseStack,
            MultiBufferSource.BufferSource bufferSource,
            PollutionAnalyzerWorldPreviewCache.PreviewChunk chunk,
            int globalBaseY,
            Map<Long, PollutionAnalyzerWorldPreviewCache.PreviewFluidCell> globalFluidMap
    ) {
        if (chunk.getFluids().isEmpty() || this.minecraft == null || this.minecraft.level == null) {
            return;
        }

        for (PollutionAnalyzerWorldPreviewCache.PreviewFluidCell fluid : chunk.getFluids()) {
            int worldX = (chunk.getChunkX() << 4) + fluid.getLocalX();
            int worldZ = (chunk.getChunkZ() << 4) + fluid.getLocalZ();
            int y = fluid.getWorldY();

            PollutionAnalyzerWorldPreviewCache.PreviewFluidCell north = globalFluidMap.get(fluidWorldKey(worldX, worldZ - 1, y));
            PollutionAnalyzerWorldPreviewCache.PreviewFluidCell south = globalFluidMap.get(fluidWorldKey(worldX, worldZ + 1, y));
            PollutionAnalyzerWorldPreviewCache.PreviewFluidCell west = globalFluidMap.get(fluidWorldKey(worldX - 1, worldZ, y));
            PollutionAnalyzerWorldPreviewCache.PreviewFluidCell east = globalFluidMap.get(fluidWorldKey(worldX + 1, worldZ, y));

            renderFluidCell(
                    poseStack,
                    bufferSource,
                    fluid,
                    globalBaseY,
                    north == null,
                    south == null,
                    west == null,
                    east == null
            );
        }
    }

    private void renderFluidCell(
            PoseStack poseStack,
            MultiBufferSource.BufferSource bufferSource,
            PollutionAnalyzerWorldPreviewCache.PreviewFluidCell fluid,
            int globalBaseY,
            boolean northOpen,
            boolean southOpen,
            boolean westOpen,
            boolean eastOpen
    ) {
        FluidState state = fluid.getFluidState();
        FluidStack fluidStack = new FluidStack(state.getType(), 1000);
        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(state.getType());

        if (state.isEmpty()) {
            return;
        }
        if (ext == null) {
            return;
        }

        ResourceLocation stillTexture = ext.getStillTexture(fluidStack);
        ResourceLocation flowTexture = ext.getFlowingTexture(fluidStack);
        if (stillTexture == null || flowTexture == null) {
            return;
        }

        TextureAtlasSprite still = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(stillTexture);

        TextureAtlasSprite flowing = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(flowTexture);

        VertexConsumer builder = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));

        float minX = fluid.minX();
        float minZ = fluid.minZ();
        float maxX = fluid.maxX();
        float maxZ = fluid.maxZ();

        float baseY = fluid.baseY(globalBaseY);
        float topY = baseY + Math.max(0.10F, fluid.fluidHeight());

        int packedLight = LightTexture.FULL_BRIGHT;

        int tint = fluid.getTint();
        int alpha = (tint >>> 24) & 0xFF;
        if (alpha == 0) {
            alpha = 134;
        } else {
            alpha = Math.min(alpha, 134);
        }
        tint = (alpha << 24) | (tint & 0x00FFFFFF);

        drawFluidQuad(
                builder, poseStack,
                minX, topY, minZ,
                maxX, topY, maxZ,
                still.getU0(), still.getV0(), still.getU1(), still.getV1(),
                packedLight, tint
        );

        if (northOpen) {
            drawFluidQuad(
                    builder, poseStack,
                    minX, baseY, minZ,
                    maxX, topY, minZ,
                    flowing.getU0(), flowing.getV0(), flowing.getU1(), flowing.getV1(),
                    packedLight, tint
            );
        }
        if (southOpen) {
            drawFluidQuad(
                    builder, poseStack,
                    maxX, baseY, maxZ,
                    minX, topY, maxZ,
                    flowing.getU0(), flowing.getV0(), flowing.getU1(), flowing.getV1(),
                    packedLight, tint
            );
        }
        if (westOpen) {
            drawFluidQuadX(
                    builder, poseStack,
                    minX,
                    baseY, minZ,
                    topY, maxZ,
                    flowing.getU0(), flowing.getV0(), flowing.getU1(), flowing.getV1(),
                    packedLight, tint,
                    true
            );
        }
        if (eastOpen) {
            drawFluidQuadX(
                    builder, poseStack,
                    maxX,
                    baseY, minZ,
                    topY, maxZ,
                    flowing.getU0(), flowing.getV0(), flowing.getU1(), flowing.getV1(),
                    packedLight, tint,
                    false
            );
        }
    }

    private void drawFluidQuadX(
            VertexConsumer builder,
            PoseStack poseStack,
            float x,
            float y0,
            float z0,
            float y1,
            float z1,
            float u0,
            float v0,
            float u1,
            float v1,
            int packedLight,
            int color,
            boolean west
    ) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        if (a == 0) {
            a = 255;
        }

        Matrix4f matrix = poseStack.last().pose();
        float nx = west ? -1.0F : 1.0F;

        if (west) {
            builder.addVertex(matrix, x, y0, z1).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(nx, 0.0F, 0.0F);
            builder.addVertex(matrix, x, y1, z1).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(nx, 0.0F, 0.0F);
            builder.addVertex(matrix, x, y1, z0).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(nx, 0.0F, 0.0F);
            builder.addVertex(matrix, x, y0, z0).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(nx, 0.0F, 0.0F);
        } else {
            builder.addVertex(matrix, x, y0, z0).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(nx, 0.0F, 0.0F);
            builder.addVertex(matrix, x, y1, z0).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(nx, 0.0F, 0.0F);
            builder.addVertex(matrix, x, y1, z1).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(nx, 0.0F, 0.0F);
            builder.addVertex(matrix, x, y0, z1).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(nx, 0.0F, 0.0F);
        }
    }

    private void drawFluidQuad(
            VertexConsumer builder,
            PoseStack poseStack,
            float x0,
            float y0,
            float z0,
            float x1,
            float y1,
            float z1,
            float u0,
            float v0,
            float u1,
            float v1,
            int packedLight,
            int color
    ) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        if (a == 0) {
            a = 135;
        }

        Matrix4f matrix = poseStack.last().pose();
        builder.addVertex(matrix, x0, y0, z0).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix, x0, y1, z1).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix, x1, y0, z0).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
    }

    private void drawChunkOutlineImmediate(
            PoseStack poseStack,
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ,
            int color
    ) {
        drawLineStripImmediate(poseStack, color, new float[][]{
                {minX, minY, minZ},
                {maxX, minY, minZ},
                {maxX, minY, maxZ},
                {minX, minY, maxZ},
                {minX, minY, minZ}
        });

        drawLineStripImmediate(poseStack, color, new float[][]{
                {minX, maxY, minZ},
                {maxX, maxY, minZ},
                {maxX, maxY, maxZ},
                {minX, maxY, maxZ},
                {minX, maxY, minZ}
        });

        drawLineStripImmediate(poseStack, color, new float[][]{
                {minX, minY, minZ},
                {minX, maxY, minZ}
        });
        drawLineStripImmediate(poseStack, color, new float[][]{
                {maxX, minY, minZ},
                {maxX, maxY, minZ}
        });
        drawLineStripImmediate(poseStack, color, new float[][]{
                {maxX, minY, maxZ},
                {maxX, maxY, maxZ}
        });
        drawLineStripImmediate(poseStack, color, new float[][]{
                {minX, minY, maxZ},
                {minX, maxY, maxZ}
        });
    }

    private void drawLineStripImmediate(PoseStack poseStack, int color, float[][] pts) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = Math.max(170, (color >> 24) & 0xFF);

        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        for (float[] p : pts) {
            buffer.addVertex(matrix, p[0], p[1], p[2]).setColor(r, g, b, a);
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private void renderRealPreviewBlock(
            PoseStack poseStack,
            MultiBufferSource.BufferSource bufferSource,
            BlockState state,
            float x,
            float y,
            float z
    ) {
        if (state.isAir()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        poseStack.pushPose();
        poseStack.translate(x, y, z);
        minecraft.getBlockRenderer().renderSingleBlock(
                state,
                poseStack,
                bufferSource,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();
    }

    private void updateHoveredChunk(PollutionAnalyzerSnapshot snapshot, int mouseX, int mouseY) {
        if (!insidePreviewScene(mouseX, mouseY)) {
            hoveredChunkKey = Long.MIN_VALUE;
            return;
        }

        List<PollutionAnalyzerWorldPreviewCache.PreviewChunk> chunks = worldPreviewCache.getChunks();
        if (chunks.isEmpty()) {
            hoveredChunkKey = Long.MIN_VALUE;
            return;
        }

        int globalBaseY = worldPreviewCache.getGlobalBaseY();
        int globalMaxY = worldPreviewCache.getGlobalMaxVisibleY();
        int displayHeight = Math.max(18, (globalMaxY - globalBaseY) + 2);

        float chunkGap = 0.0F;
        float totalWidth = 16 * 3 + chunkGap * 2;
        float fitByWidth = (previewW() - 44.0F) / totalWidth;
        float fitByHeight = (previewSceneH() - 40.0F) / (displayHeight + totalWidth * 0.42F);
        float scale = Math.min(fitByWidth, fitByHeight) * this.zoom;

        int centerX = previewX() + previewW() / 2;
        int centerY = previewSceneY() + (previewSceneH() / 2) + 20;

        double bestScore = Double.MAX_VALUE;
        long bestKey = Long.MIN_VALUE;

        int centerChunkX = snapshot.getCenterChunkX();
        int centerChunkZ = snapshot.getCenterChunkZ();

        for (PollutionAnalyzerWorldPreviewCache.PreviewChunk chunk : chunks) {
            long key = net.minecraft.world.level.ChunkPos.asLong(chunk.getChunkX(), chunk.getChunkZ());

            float chunkShiftX = (chunk.getChunkX() - centerChunkX) * (16.0F + chunkGap);
            float chunkShiftZ = (chunk.getChunkZ() - centerChunkZ) * (16.0F + chunkGap);

            float chunkCenterY = (chunk.getCenterGroundY() - globalBaseY) + 1.0F;

            double[] projected = projectPoint(centerX, centerY, scale, chunkShiftX, chunkCenterY, chunkShiftZ);
            double dx = mouseX - projected[0];
            double dy = mouseY - projected[1];
            double distSq = dx * dx + dy * dy;
            double score = distSq - projected[2] * 0.001D;

            if (distSq < 1600.0D && score < bestScore) {
                bestScore = score;
                bestKey = key;
            }
        }

        hoveredChunkKey = bestKey;
    }

    private double[] projectPoint(int centerX, int centerY, float scale, float x, float y, float z) {
        Matrix4f matrix = new Matrix4f()
                .translation(centerX, centerY, 300.0F)
                .scale(scale, -scale, scale)
                .rotate(Axis.XP.rotationDegrees(this.pitch))
                .rotate(Axis.YP.rotationDegrees(this.yaw));

        Vector4f vec = new Vector4f(x, y, z, 1.0F);
        vec.mul(matrix);
        return new double[]{vec.x(), vec.y(), vec.z()};
    }

    private PollutionAnalyzerChunkSnapshot activeChunk(PollutionAnalyzerSnapshot snapshot) {
        if (this.selectedChunkKey != Long.MIN_VALUE) {
            PollutionAnalyzerChunkSnapshot selected = snapshot.getChunk(this.selectedChunkKey);
            if (selected != null) {
                return selected;
            }
        }

        if (this.hoveredChunkKey != Long.MIN_VALUE) {
            PollutionAnalyzerChunkSnapshot hovered = snapshot.getChunk(this.hoveredChunkKey);
            if (hovered != null) {
                return hovered;
            }
        }

        return snapshot.getChunk(snapshot.centerKey());
    }

    private boolean insidePreviewScene(double mouseX, double mouseY) {
        return mouseX >= previewX() && mouseX < previewX() + previewW()
                && mouseY >= previewSceneY() && mouseY < previewSceneY() + previewSceneH();
    }

    private boolean insideSidePanel(double mouseX, double mouseY) {
        return mouseX >= sideX() && mouseX < sideX() + sideW()
                && mouseY >= sideY() && mouseY < sideY() + sideH();
    }

    private boolean insideRightScrollbar(double mouseX, double mouseY) {
        int x = rightScrollbarX();
        int y = rightScrollbarY();
        int h = rightScrollbarVisibleH();
        return mouseX >= x && mouseX < x + 4 && mouseY >= y && mouseY < y + h;
    }

    private int rightScrollbarThumbHeight() {
        int visibleHeight = rightScrollbarVisibleH();
        if (rightMaxScroll <= 0 || rightContentHeight <= 0) {
            return visibleHeight;
        }
        return Math.max(18, (int) ((visibleHeight / (float) rightContentHeight) * visibleHeight));
    }

    private int rightScrollbarThumbY() {
        int y = rightScrollbarY();
        int visibleHeight = rightScrollbarVisibleH();

        if (rightMaxScroll <= 0) {
            return y;
        }

        int thumbHeight = rightScrollbarThumbHeight();
        int movable = visibleHeight - thumbHeight;
        return y + Math.round((rightScroll / (float) rightMaxScroll) * movable);
    }

    private void updateRightScrollByMouse(double mouseY) {
        if (rightMaxScroll <= 0) {
            this.rightScroll = 0;
            return;
        }

        int y = rightScrollbarY();
        int visibleHeight = rightScrollbarVisibleH();
        int thumbHeight = rightScrollbarThumbHeight();
        int movable = Math.max(1, visibleHeight - thumbHeight);

        int target = (int) mouseY - y - this.rightScrollbarGrabOffset;
        float t = Mth.clamp(target / (float) movable, 0.0F, 1.0F);
        this.rightScroll = Math.round(t * rightMaxScroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && insideRightScrollbar(mouseX, mouseY)) {
            this.rightScrollbarDragging = true;
            this.rightScrollbarGrabOffset = (int) mouseY - rightScrollbarThumbY();
            this.rightScrollbarGrabOffset = Mth.clamp(this.rightScrollbarGrabOffset, 0, rightScrollbarThumbHeight());
            updateRightScrollByMouse(mouseY);
            return true;
        }

        if (insidePreviewScene(mouseX, mouseY) && button == 0) {
            this.previewDragging = true;
            this.previewMoved = false;
            this.previewMouseDownX = mouseX;
            this.previewMouseDownY = mouseY;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && this.rightScrollbarDragging) {
            this.rightScrollbarDragging = false;
            return true;
        }

        if (this.previewDragging && button == 0) {
            if (!this.previewMoved && this.hoveredChunkKey != Long.MIN_VALUE) {
                this.selectedChunkKey = this.hoveredChunkKey;
            }

            this.previewDragging = false;
            this.previewMoved = false;
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 && this.rightScrollbarDragging) {
            updateRightScrollByMouse(mouseY);
            return true;
        }

        if (this.previewDragging && button == 0) {
            double moved = Math.abs(mouseX - this.previewMouseDownX) + Math.abs(mouseY - this.previewMouseDownY);
            if (moved > 3.0D) {
                this.previewMoved = true;
                this.yaw += (float) dragX * 0.85F;
                this.pitch += (float) dragY * 0.60F;
                this.pitch = Mth.clamp(this.pitch, -78.0F, 78.0F);
            }
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double delta) {
        if (insidePreviewScene(mouseX, mouseY)) {
            this.zoom = Mth.clamp(this.zoom + (float) delta * 0.08F, 0.70F, 1.85F);
            return true;
        }

        if (insideSidePanel(mouseX, mouseY)) {
            this.rightScroll = Mth.clamp(this.rightScroll + (delta < 0 ? 20 : -20), 0, this.rightMaxScroll);
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, deltaX, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 49) {
            switchTab(Tab.OVERVIEW);
            return true;
        }
        if (keyCode == 50) {
            switchTab(Tab.SOURCES);
            return true;
        }
        if (keyCode == 51) {
            switchTab(Tab.RECOMMENDATIONS);
            return true;
        }
        if (keyCode == 52) {
            switchTab(Tab.CLEANERS);
            return true;
        }
        if (keyCode == 53) {
            switchTab(Tab.INFO);
            return true;
        }
        if (keyCode == 54) {
            switchTab(Tab.GRAPHS);
            return true;
        }
        if (keyCode == 82) {
            resetView();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void drawCenteredBlockText(GuiGraphics graphics, int x, int y, int w, int h, Component text, int color) {
        int tx = x + (w - this.font.width(text)) / 2;
        int ty = y + h / 2 - this.font.lineHeight / 2;
        graphics.drawString(this.font, text, tx, ty, color, false);
    }

    private void appendWrapped(List<BlockLine> target, Component text, int color, int width) {
        List<FormattedCharSequence> split = this.font.split(text, width);
        for (FormattedCharSequence sequence : split) {
            target.add(new BlockLine(sequence, color));
        }
    }

    private void appendWrappedColored(List<BlockLine> target, Component text, int color, int width) {
        List<FormattedCharSequence> split = this.font.split(text, width);
        for (FormattedCharSequence sequence : split) {
            target.add(new BlockLine(sequence, color));
        }
    }

    private void appendCleanerStatLines(
            List<BlockLine> lines,
            PollutionAnalyzerSourceSnapshot source,
            int textW
    ) {
        if (!source.isCleaner()) {
            return;
        }

        appendWrappedColored(
                lines,
                Component.translatable(
                        "iu.pollution_analyzer.source.cleaner.level",
                        source.getMechanismLevel(),
                        source.getMaxMechanismLevel()
                ),
                0x7EC8FF,
                textW
        );

        if (source.getAirCleaningPerSecond() > 0.0D) {
            appendWrappedColored(
                    lines,
                    Component.translatable(
                            "iu.pollution_analyzer.source.cleaner.air_cleaning",
                            formatRateUnit("air", source.getAirCleaningPerSecond())
                    ),
                    0x7EC8FF,
                    textW
            );
        }

        if (source.getSoilCleaningPerSecond() > 0.0D) {
            appendWrappedColored(
                    lines,
                    Component.translatable(
                            "iu.pollution_analyzer.source.cleaner.soil_cleaning",
                            formatRateUnit("soil", source.getSoilCleaningPerSecond())
                    ),
                    0x7EC8FF,
                    textW
            );
        }
    }

    private int riskColor(double severity, PollutionAnalyzerThresholds thresholds) {
        if (severity <= thresholds.getRecommendedMax()) {
            float t = Mth.clamp((float) (severity / Math.max(1.0D, thresholds.getRecommendedMax())), 0.0F, 1.0F);
            return lerpColor(0xFF39B46C, 0xFFE1C34F, t);
        }
        if (severity < thresholds.getDangerFrom()) {
            float t = Mth.clamp(
                    (float) ((severity - thresholds.getWarningFrom()) / Math.max(1.0D, thresholds.getDangerFrom() - thresholds.getWarningFrom())),
                    0.0F,
                    1.0F
            );
            return lerpColor(0xFFE1C34F, 0xFFF09F3B, t);
        }
        if (severity < thresholds.getCriticalFrom()) {
            float t = Mth.clamp(
                    (float) ((severity - thresholds.getDangerFrom()) / Math.max(1.0D, thresholds.getCriticalFrom() - thresholds.getDangerFrom())),
                    0.0F,
                    1.0F
            );
            return lerpColor(0xFFF09F3B, 0xFFE05353, t);
        }
        return 0xFFE05353;
    }

    private Component stateText(double severity, PollutionAnalyzerThresholds thresholds) {
        int band = PollutionAnalyzerMath.riskBand(severity, thresholds);
        return switch (band) {
            case 0 -> Component.translatable("iu.pollution_analyzer.state.good");
            case 1 -> Component.translatable("iu.pollution_analyzer.state.watch");
            case 2 -> Component.translatable("iu.pollution_analyzer.state.bad");
            default -> Component.translatable("iu.pollution_analyzer.state.danger");
        };
    }

    private Component trendText(double ratePerSecond) {
        if (Math.abs(ratePerSecond) < 0.05D) {
            return Component.translatable("iu.pollution_analyzer.trend.stable");
        }
        return ratePerSecond > 0.0D
                ? Component.translatable("iu.pollution_analyzer.trend.rising")
                : Component.translatable("iu.pollution_analyzer.trend.falling");
    }

    private int severityColor(int severity) {
        return switch (severity) {
            case 3 -> 0xFFE05353;
            case 2 -> 0xFFF0A03A;
            case 1 -> 0xFFE1C34F;
            default -> 0xFF75B46D;
        };
    }

    private String levelName(int ordinal) {
        return switch (ordinal) {
            case 0 -> "VERY_LOW";
            case 1 -> "LOW";
            case 2 -> "MEDIUM";
            case 3 -> "HIGH";
            default -> "VERY_HIGH";
        };
    }

    private int lerpColor(int a, int b, float t) {
        int ar = (a >> 16) & 0xFF;
        int ag = (a >> 8) & 0xFF;
        int ab = a & 0xFF;

        int br = (b >> 16) & 0xFF;
        int bg = (b >> 8) & 0xFF;
        int bb = b & 0xFF;

        int r = Mth.floor(Mth.lerp(t, ar, br));
        int g = Mth.floor(Mth.lerp(t, ag, bg));
        int bl = Mth.floor(Mth.lerp(t, ab, bb));
        return 0xFF000000 | (r << 16) | (g << 8) | bl;
    }

    private String trimToWidth(String text, int width) {
        return this.font.plainSubstrByWidth(text, width);
    }

    private String formatTotalUnit(String medium, double value) {
        return "air".equals(medium)
                ? String.format(Locale.ROOT, "%.1f " + Localization.translate("iu.pollution_analyzer.kg_unit"), value)
                : String.format(Locale.ROOT, "%.1f " + Localization.translate("iu.pollution_analyzer.g_unit"), value);
    }

    private String formatRawUnit(String medium, double value) {
        return "air".equals(medium)
                ? String.format(Locale.ROOT, "%.1f " + Localization.translate("iu.pollution_analyzer.kg_unit"), value)
                : String.format(Locale.ROOT, "%.1f " + Localization.translate("iu.pollution_analyzer.g_unit"), value);
    }

    private String formatRateUnit(String medium, double value) {
        return "air".equals(medium)
                ? String.format(Locale.ROOT, "%+.2f " + Localization.translate("iu.pollution_analyzer.kg_per_sec_unit"), value)
                : String.format(Locale.ROOT, "%+.2f " + Localization.translate("iu.pollution_analyzer.g_per_sec_unit"), value);
    }

    private String formatTimeOrDash(int seconds) {
        if (seconds < 0) {
            return "-";
        }
        int minutes = seconds / 60;
        int sec = seconds % 60;
        if (minutes > 0) {
            return minutes + "m " + sec + "s";
        }
        return sec + "s";
    }

    private double min(double[] values) {
        if (values == null || values.length == 0) {
            return 0.0D;
        }
        double min = values[0];
        for (double value : values) {
            min = Math.min(min, value);
        }
        return min;
    }

    private double max(double[] values) {
        if (values == null || values.length == 0) {
            return 0.0D;
        }
        double max = values[0];
        for (double value : values) {
            max = Math.max(max, value);
        }
        return max;
    }

    private enum Tab {
        OVERVIEW("iu.pollution_analyzer.tab.overview", 0x5CD3A0),
        SOURCES("iu.pollution_analyzer.tab.sources", 0x69B4FF),
        RECOMMENDATIONS("iu.pollution_analyzer.tab.recommendations", 0xF0B44F),
        CLEANERS("iu.pollution_analyzer.tab.cleaners", 0x86D96E),
        INFO("iu.pollution_analyzer.tab.info", 0xC68BFF),
        GRAPHS("iu.pollution_analyzer.tab.graphs", 0xF26D8E);

        private final String key;
        private final int accent;

        Tab(String key, int accent) {
            this.key = key;
            this.accent = accent;
        }

        public Component title() {
            return Component.translatable(this.key);
        }

        public int accent() {
            return this.accent;
        }
    }

    private record BlockLine(FormattedCharSequence sequence, int color) {
    }

    private record SourceCardLayout(
            PollutionAnalyzerSourceSnapshot source,
            List<FormattedCharSequence> nameLines,
            List<BlockLine> statLines,
            List<FormattedCharSequence> posLines,
            int height
    ) {
    }

    private record RecommendationCardLayout(
            PollutionAnalyzerRecommendation recommendation,
            List<FormattedCharSequence> titleLines,
            List<FormattedCharSequence> bodyLines,
            int height
    ) {
    }

    private record OverviewLocalSourceLayout(
            PollutionAnalyzerSourceSnapshot source,
            List<FormattedCharSequence> nameLines,
            List<BlockLine> statLines,
            int height
    ) {
    }

    private record MediumCardContent(
            Component title,
            Component state,
            int accentColor,
            List<BlockLine> lines,
            int height
    ) {
    }

    private record InfoSection(
            Component title,
            int accent,
            List<BlockLine> lines,
            int height
    ) {
    }

    private record ChartPlot(
            double[] values,
            float[] px,
            float[] py,
            int left,
            int right,
            int zeroY
    ) {
    }

    private record GraphLayout(
            Component title,
            double[] values,
            int color,
            String medium,
            boolean rate
    ) {
    }
}