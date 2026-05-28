package com.denfop.screen;

import com.denfop.api.gassensor.GasSensorClientCache;
import com.denfop.api.gassensor.GasSensorScanState;
import com.denfop.api.gassensor.GasSensorVeinEntry;
import com.denfop.componets.Fluids;
import com.denfop.network.packet.PacketGasSensorScanRequest;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

import java.util.*;

public class GasSensorScreen extends Screen {

    private static final int WINDOW_BG = 0xF0110C08;
    private static final int WINDOW_INNER = 0xF016100A;
    private static final int PANEL_BG = 0xD018120C;
    private static final int PANEL_INNER = 0xD020170F;
    private static final int PANEL_DARK = 0xD0100C07;
    private static final float MAP_BASE_CELL = 16.0F;
    private static final int BORDER = 0xFF6A4B28;
    private static final int BORDER_SOFT = 0xFF3A2918;
    private static final int ACCENT = 0xFFE39B3A;
    private static final int ACCENT_SOFT = 0xFFB87725;
    private static final int ACCENT_DARK = 0xFF7A4C17;
    private static final int ACCENT_GREEN = 0xFF91D95B;

    private static final int TEXT_MAIN = 0xFFF3E7D1;
    private static final int TEXT_DIM = 0xFFB8A68A;
    private static final int TEXT_MUTED = 0xFF8F7E67;
    private static final int TEXT_HIGHLIGHT = 0xFFF2DE84;

    private static final int MAP_GRID = 0xFF2D2217;
    private static final int MAP_GRID_BOLD = 0xFF523A23;
    private static final int MAP_CELL = 0xFF130E09;
    private static final int MAP_CENTER = 0xFF1D140D;

    private static final int ROW_BG = 0xFF1A130C;
    private static final int ROW_BG_HOVER = 0xFF24180F;
    private static final int ROW_BG_SELECTED = 0xFF2A1C11;
    private static final int ROW_LINE = 0xFF4B331F;

    private static final int HEADER_H = 34;
    private static final int SEARCH_H = 30;
    private static final int BUTTON_H = 28;
    private static final int MODE_BUTTON_H = 26;
    private static final int LIST_ROW_H = 78;

    private static final int OUTER_PAD = 20;
    private static final int CONTENT_GAP = 12;
    private static final int MAIN_INNER_PAD = 10;
    private static final int FOOTER_PAD = 10;
    private static final int MAP_LEGEND_H = 72;
    private static final int[] DETAIL_PREVIEW_SPLIT = new int[]{72, 28};

    private final ItemStack sensorStack;

    private OrangeEditBox searchBox;
    private OrangeButton rescanButton;
    private OrangeButton followButton;
    private OrangeButton listTabButton;
    private OrangeButton detailTabButton;
    private OrangeButton mapTabButton;

    private Mode activeMode = Mode.LIST;

    private boolean sliderDragging;
    private boolean listScrollbarDragging;
    private int listScrollbarGrabOffset;

    private boolean detailDragging;
    private boolean mapDragging;

    private int radiusChunks = 4;
    private int listScroll;

    private float detailYaw = -32.0F;
    private float detailPitch = 0.62F;
    private float detailZoom = 1.0F;

    private float mapZoom = 1.0F;
    private float mapPanX = 0.0F;
    private float mapPanY = 0.0F;

    private String selectedVeinKey = "";
    private List<GasSensorVeinEntry> visibleVeins = new ArrayList<>();
    private List<Component> hoveredTooltip = List.of();

    public GasSensorScreen(ItemStack sensorStack) {
        super(Component.translatable("iu.gas_sensor.title"));
        this.sensorStack = sensorStack.copy();
    }

    @Override
    protected void init() {
        super.init();

        GasSensorScanState state = snapshot();
        if (state != null && state.getRadiusChunks() > 0) {
            this.radiusChunks = Mth.clamp(state.getRadiusChunks(), 1, 32);
        }

        int searchX = topControlsLeft();
        int searchY = topControlsY();
        int searchW = topSearchW();
        int buttonX = topButtonsX();
        int actionW = topActionButtonW();

        this.searchBox = this.addRenderableWidget(new OrangeEditBox(
                this.font,
                searchX,
                searchY,
                searchW,
                SEARCH_H,
                Component.translatable("iu.gas_sensor.search_hint")
        ));
        this.searchBox.setMaxLength(80);
        this.searchBox.setBordered(false);
        this.searchBox.setTextColor(TEXT_MAIN);
        this.searchBox.setTextColorUneditable(TEXT_MAIN);
        this.searchBox.setValue("");
        this.searchBox.setResponder(value -> {
            rebuildVisibleVeins(snapshot());
            this.listScroll = 0;
        });

        this.rescanButton = this.addRenderableWidget(new OrangeButton(
                buttonX,
                searchY + 1,
                actionW,
                BUTTON_H,
                Component.translatable("iu.gas_sensor.button.rescan"),
                ACCENT,
                btn -> requestServerScan()
        ));

        this.followButton = this.addRenderableWidget(new OrangeButton(
                buttonX + actionW + 12,
                searchY + 1,
                actionW,
                BUTTON_H,
                followButtonText(),
                ACCENT_GREEN,
                btn -> toggleFollow()
        ));

        int tabsY = tabsY();
        int tabW = tabButtonW();
        int tabsX = topControlsLeft();

        this.listTabButton = this.addRenderableWidget(new OrangeButton(
                tabsX,
                tabsY,
                tabW,
                MODE_BUTTON_H,
                Component.translatable("iu.gas_sensor.tab.list"),
                ACCENT,
                btn -> setMode(Mode.LIST)
        ));

        this.detailTabButton = this.addRenderableWidget(new OrangeButton(
                tabsX + tabW + 12,
                tabsY,
                tabW,
                MODE_BUTTON_H,
                Component.translatable("iu.gas_sensor.tab.vein"),
                ACCENT,
                btn -> setMode(Mode.DETAIL)
        ));

        this.mapTabButton = this.addRenderableWidget(new OrangeButton(
                tabsX + (tabW + 12) * 2,
                tabsY,
                tabW,
                MODE_BUTTON_H,
                Component.translatable("iu.gas_sensor.tab.map"),
                ACCENT,
                btn -> setMode(Mode.MAP)
        ));

        this.listTabButton.setSelected(true);
        this.detailTabButton.setSelected(false);
        this.mapTabButton.setSelected(false);

        this.selectedVeinKey = GasSensorClientCache.getSelectedVeinId();
        rebuildVisibleVeins(state);
        resetMapView();
    }

    @Override
    public void tick() {
        super.tick();

        GasSensorScanState state = snapshot();
        rebuildVisibleVeins(state);

        String cacheSelected = GasSensorClientCache.getSelectedVeinId();
        if (cacheSelected != null && !cacheSelected.isBlank()) {
            this.selectedVeinKey = cacheSelected;
        }

        if (selectedVein().isEmpty() && !this.visibleVeins.isEmpty()) {
            this.selectedVeinKey = this.visibleVeins.get(0).getId();
            GasSensorClientCache.setSelectedVeinId(this.selectedVeinKey);
        }

        this.followButton.setMessage(followButtonText());
        this.listTabButton.setSelected(this.activeMode == Mode.LIST);
        this.detailTabButton.setSelected(this.activeMode == Mode.DETAIL);
        this.mapTabButton.setSelected(this.activeMode == Mode.MAP);

        this.listScroll = Mth.clamp(this.listScroll, 0, listMaxScroll());
        if (this.activeMode == Mode.MAP) {
            clampMapPan();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void setMode(Mode mode) {
        this.activeMode = mode;
        if (mode == Mode.MAP) {
            resetMapView();
        } else if (mode == Mode.DETAIL) {
            this.detailYaw = -32.0F;
            this.detailPitch = 0.62F;
            this.detailZoom = 1.0F;
        }
    }

    private void resetMapView() {
        this.mapZoom = Mth.clamp(mapFitZoom(), 0.25F, 6.0F);
        this.mapPanX = 0.0F;
        this.mapPanY = 0.0F;
        clampMapPan();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.hoveredTooltip = List.of();

        renderBackground(graphics, mouseX, mouseY, partialTick);
        renderWindow(graphics);
        renderHeader(graphics);
        renderTopControls(graphics);
        renderFrames(graphics);

        GasSensorScanState state = snapshot();

        renderMainPanel(graphics, state, mouseX, mouseY, partialTick);
        renderStatusPanel(graphics, state);
        renderSelectedPanel(graphics);

        for (Renderable renderable : this.renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTick);
        }

        if (!this.hoveredTooltip.isEmpty()) {
            graphics.renderComponentTooltip(this.font, this.hoveredTooltip, mouseX, mouseY);
        }
    }

    private void renderWindow(GuiGraphics graphics) {
        int px = panelX();
        int py = panelY();
        int pw = panelW();
        int ph = panelH();

        graphics.fill(px, py, px + pw, py + ph, WINDOW_BG);
        graphics.fill(px + 2, py + 2, px + pw - 2, py + ph - 2, WINDOW_INNER);

        graphics.fill(px, py, px + pw, py + 3, 0x88FFFFFF);
        graphics.fill(px, py, px + 3, py + ph, BORDER);
        graphics.fill(px + pw - 3, py, px + pw, py + ph, BORDER_SOFT);
        graphics.fill(px, py + ph - 3, px + pw, py + ph, BORDER_SOFT);
    }

    private void renderHeader(GuiGraphics graphics) {
        int px = panelX();
        int py = panelY();

        graphics.renderItem(this.sensorStack, px + 18, py + 14);

        graphics.drawString(
                this.font,
                Component.translatable("iu.gas_sensor.title"),
                px + 46,
                py + 15,
                TEXT_MAIN,
                false
        );

        graphics.drawString(
                this.font,
                Component.translatable("iu.gas_sensor.subtitle"),
                px + 46,
                py + 30,
                TEXT_DIM,
                false
        );
    }

    private void renderTopControls(GuiGraphics graphics) {
        drawSlider(graphics, topSliderX(), topSliderY(), topSliderW(), MODE_BUTTON_H, this.radiusChunks);
    }

    private void drawSlider(GuiGraphics graphics, int x, int y, int w, int h, int value) {
        graphics.fill(x, y, x + w, y + h, PANEL_DARK);
        graphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0xFF23180F);
        graphics.fill(x, y, x + w, y + 1, BORDER);
        graphics.fill(x, y + h - 1, x + w, y + h, BORDER_SOFT);
        graphics.fill(x, y, x + 1, y + h, BORDER);
        graphics.fill(x + w - 1, y, x + w, y + h, BORDER_SOFT);

        String text = Component.translatable("iu.gas_sensor.radius", value).getString();
        graphics.drawString(this.font, text, x + 10, y + (h - this.font.lineHeight) / 2 + 1, TEXT_MAIN, false);

        int trackX = radiusTrackX();
        int trackW = radiusTrackW();
        int trackY = y + h / 2 - 2;
        int trackH = 4;

        graphics.fill(trackX, trackY, trackX + trackW, trackY + trackH, 0xFF2E2116);

        float t = (value - 1) / 31.0F;
        int knobCenterX = trackX + Math.round(trackW * t);
        int knobW = 10;
        int knobH = 14;

        graphics.fill(trackX, trackY, knobCenterX, trackY + trackH, 0xFF8E6427);

        int knobX = Mth.clamp(knobCenterX - knobW / 2, trackX, trackX + trackW - knobW);
        int knobY = y + (h - knobH) / 2;

        graphics.fill(knobX, knobY, knobX + knobW, knobY + knobH, 0xFFE9E0D2);
        graphics.fill(knobX, knobY, knobX + knobW, knobY + 1, 0xFFFFFFFF);
        graphics.fill(knobX, knobY + knobH - 1, knobX + knobW, knobY + knobH, 0xFF8B7D68);
        graphics.fill(knobX + 2, knobY + 4, knobX + knobW - 2, knobY + knobH - 4, 0xFFBEB19B);
    }

    private void renderFrames(GuiGraphics graphics) {
        graphics.fill(mainAreaX(), mainAreaY(), mainAreaX() + mainAreaW(), mainAreaY() + mainAreaH(), PANEL_BG);
        graphics.fill(mainAreaX() + 1, mainAreaY() + 1, mainAreaX() + mainAreaW() - 1, mainAreaY() + mainAreaH() - 1, PANEL_INNER);

        graphics.fill(sideAreaX(), sideAreaY(), sideAreaX() + sideAreaW(), sideAreaY() + statusPanelH(), PANEL_BG);
        graphics.fill(sideAreaX() + 1, sideAreaY() + 1, sideAreaX() + sideAreaW() - 1, sideAreaY() + statusPanelH() - 1, PANEL_INNER);

        int selectedY = selectedPanelY();
        graphics.fill(sideAreaX(), selectedY, sideAreaX() + sideAreaW(), selectedY + selectedPanelH(), PANEL_BG);
        graphics.fill(sideAreaX() + 1, selectedY + 1, sideAreaX() + sideAreaW() - 1, selectedY + selectedPanelH() - 1, PANEL_INNER);
    }

    private void renderMainPanel(GuiGraphics graphics, GasSensorScanState state, int mouseX, int mouseY, float partialTick) {
        switch (this.activeMode) {
            case LIST -> renderListMode(graphics, mouseX, mouseY);
            case DETAIL -> renderDetailMode(graphics, mouseX, mouseY, partialTick);
            case MAP -> renderMapMode(graphics, state, mouseX, mouseY);
        }
    }

    private void renderListMode(GuiGraphics graphics, int mouseX, int mouseY) {
        int x = listViewportX();
        int y = listViewportY();
        int rowW = listRowsW();
        int h = listViewportH();

        graphics.enableScissor(x, y, x + rowW, y + h);

        if (this.visibleVeins.isEmpty()) {
            drawCenteredBoxText(graphics, x, y, rowW, h, Component.translatable("iu.gas_sensor.list.empty"), TEXT_DIM);
            graphics.disableScissor();
            renderListScrollbar(graphics, listScrollbarX(), y, listScrollbarW(), h);
            return;
        }

        int drawY = y - this.listScroll;
        for (GasSensorVeinEntry vein : this.visibleVeins) {
            if (drawY + LIST_ROW_H < y - 4) {
                drawY += LIST_ROW_H + 8;
                continue;
            }
            if (drawY > y + h + 4) {
                break;
            }

            boolean selected = Objects.equals(this.selectedVeinKey, vein.getId());
            boolean hovered = mouseX >= x && mouseX < x + rowW && mouseY >= drawY && mouseY < drawY + LIST_ROW_H;
            drawVeinRow(graphics, x, drawY, rowW, LIST_ROW_H, vein, selected, hovered);

            if (hovered) {
                this.hoveredTooltip = buildVeinTooltip(vein);
            }

            drawY += LIST_ROW_H + 8;
        }

        graphics.disableScissor();
        renderListScrollbar(graphics, listScrollbarX(), y, listScrollbarW(), h);
    }

    private void drawFluidIcon(GuiGraphics graphics, GasSensorVeinEntry vein, int x, int y, int w, int h) {
        ResourceLocation fluidId = ResourceLocation.tryParse(vein.getFluidRegistryName());
        if (fluidId == null) {
            drawFallbackFluidIcon(graphics, vein, x, y, w, h);
            return;
        }

        Fluid fluid = BuiltInRegistries.FLUID.get(fluidId);
        if (fluid == null || fluid == Fluids.EMPTY) {
            drawFallbackFluidIcon(graphics, vein, x, y, w, h);
            return;
        }

        FluidStack stack = new FluidStack(fluid, 1000);
        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid);
        if (ext == null) {
            drawFallbackFluidIcon(graphics, vein, x, y, w, h);
            return;
        }

        ResourceLocation still = ext.getStillTexture(stack);
        if (still == null) {
            drawFallbackFluidIcon(graphics, vein, x, y, w, h);
            return;
        }

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(still);

        int tint = ext.getTintColor(stack);
        if ((tint >>> 24) == 0) {
            tint = 0xFF000000 | tint;
        }

        graphics.fill(x - 1, y - 1, x + w + 1, y + h + 1, 0xFF4B331F);
        graphics.fill(x, y, x + w, y + h, 0xFF1A130C);

        drawTexturedSprite(graphics, sprite, x + 1, y + 1, w - 2, h - 2, tint);

        graphics.fill(x + 1, y + 1, x + w - 1, y + 4, 0x30FFFFFF);
        graphics.fill(x + 1, y + h - 2, x + w - 1, y + h - 1, 0x44000000);
    }

    private void drawFallbackFluidIcon(GuiGraphics graphics, GasSensorVeinEntry vein, int x, int y, int w, int h) {
        int color = veinColor(vein);

        graphics.fill(x - 1, y - 1, x + w + 1, y + h + 1, 0xFF4B331F);
        graphics.fill(x, y, x + w, y + h, multiplyColor(color, 0.70F));
        graphics.fill(x + 2, y + 2, x + w - 2, y + 10, lighten(color, 0.28F));
        graphics.fill(x + 2, y + 11, x + w - 2, y + h - 2, color);
    }

    private void drawTexturedSprite(GuiGraphics graphics, TextureAtlasSprite sprite, int x, int y, int w, int h, int color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

        int a = (color >>> 24) & 0xFF;
        int r = (color >>> 16) & 0xFF;
        int g = (color >>> 8) & 0xFF;
        int b = color & 0xFF;

        Matrix4f matrix = graphics.pose().last().pose();

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);


        buffer.addVertex(matrix, x, y + h, 0.0F).setUv(sprite.getU0(), sprite.getV1()).setColor(r, g, b, a);
        buffer.addVertex(matrix, x + w, y + h, 0.0F).setUv(sprite.getU1(), sprite.getV1()).setColor(r, g, b, a);
        buffer.addVertex(matrix, x + w, y, 0.0F).setUv(sprite.getU1(), sprite.getV0()).setColor(r, g, b, a);
        buffer.addVertex(matrix, x, y, 0.0F).setUv(sprite.getU0(), sprite.getV0()).setColor(r, g, b, a);

        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private void drawVeinRow(GuiGraphics graphics, int x, int y, int w, int h, GasSensorVeinEntry vein, boolean selected, boolean hovered) {
        int fill = selected ? ROW_BG_SELECTED : hovered ? ROW_BG_HOVER : ROW_BG;
        graphics.fill(x, y, x + w, y + h, fill);
        graphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0xFF21170F);
        graphics.fill(x, y, x + w, y + 1, selected ? ACCENT : ROW_LINE);
        graphics.fill(x, y + h - 1, x + w, y + h, selected ? ACCENT_SOFT : ROW_LINE);
        graphics.fill(x, y, x + 1, y + h, selected ? ACCENT : ROW_LINE);
        graphics.fill(x + w - 1, y, x + w, y + h, selected ? ACCENT_SOFT : ROW_LINE);

        int iconX = x + 12;
        int iconY = y + 16;
        drawFluidIcon(graphics, vein, iconX, iconY, 30, 30);

        String volume = formatBuckets(veinBuckets(vein));
        String distance = formatDistance(veinDistanceMeters(vein));

        int rightPad = 16;
        int rightValueW = Math.max(this.font.width(volume), this.font.width(distance));
        int textX = x + 52;
        int textRight = x + w - rightPad - rightValueW - 12;
        int textW = Math.max(90, textRight - textX);

        String name = this.font.plainSubstrByWidth(veinName(vein), textW);
        String coord = this.font.plainSubstrByWidth(
                "XYZ: " + vein.getBlockX() + ", " + vein.getBlockY() + ", " + vein.getBlockZ(),
                textW
        );

        int rightX = x + w - rightPad - rightValueW;

        graphics.drawString(this.font, name, textX, y + 16, TEXT_MAIN, false);
        graphics.drawString(this.font, coord, textX, y + 40, TEXT_DIM, false);

        graphics.drawString(this.font, volume, rightX, y + 14, TEXT_HIGHLIGHT, false);
        graphics.drawString(this.font, distance, rightX, y + 40, TEXT_DIM, false);

        if (selected || hovered) {
            graphics.fill(x + 4, y + h - 6, x + w - 4, y + h - 2, selected ? ACCENT : ACCENT_DARK);
        }
    }

    private void renderListScrollbar(GuiGraphics graphics, int x, int y, int w, int h) {
        graphics.fill(x, y, x + w, y + h, 0xFF1B130D);
        graphics.fill(x, y, x + w, y + 1, BORDER);
        graphics.fill(x, y + h - 1, x + w, y + h, BORDER_SOFT);

        int maxScroll = listMaxScroll();
        if (maxScroll <= 0) {
            graphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0xFF5E4325);
            return;
        }

        int contentH = listContentHeight();
        int thumbH = Math.max(22, (int) ((h / (float) contentH) * h));
        int movable = h - thumbH;
        int thumbY = y + Math.round((this.listScroll / (float) maxScroll) * movable);

        graphics.fill(x + 1, thumbY, x + w - 1, thumbY + thumbH, ACCENT_SOFT);
        graphics.fill(x + 1, thumbY, x + w - 1, thumbY + 2, 0xAAFFFFFF);
    }

    private void renderDetailMode(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int innerX = mainAreaX() + MAIN_INNER_PAD;
        int innerY = mainAreaY() + MAIN_INNER_PAD;
        int innerW = mainAreaW() - MAIN_INNER_PAD * 2;
        int innerH = mainAreaH() - MAIN_INNER_PAD * 2;

        int previewH = innerH * DETAIL_PREVIEW_SPLIT[0] / 100 - 8;
        int infoY = innerY + previewH + 12;
        int infoH = innerH - previewH - 12;

        graphics.fill(innerX, innerY, innerX + innerW, innerY + previewH, 0xFF0D0906);
        graphics.fill(innerX + 1, innerY + 1, innerX + innerW - 1, innerY + previewH - 1, 0xFF140E09);

        graphics.fill(innerX, infoY, innerX + innerW, infoY + infoH, 0xFF120D08);
        graphics.fill(innerX + 1, infoY + 1, innerX + innerW - 1, infoY + infoH - 1, 0xFF1A130C);

        graphics.enableScissor(innerX + 2, innerY + 2, innerX + innerW - 2, innerY + previewH - 2);
        renderDetailPreview(graphics, innerX, innerY, innerW, previewH);
        graphics.disableScissor();

        renderDetailInfoBox(graphics, infoY, innerX, innerW, infoH);
    }

    private void renderDetailPreview(GuiGraphics graphics, int x, int y, int w, int h) {
        GasSensorVeinEntry vein = selectedVein().orElse(null);
        if (vein == null) {
            drawCenteredBoxText(graphics, x, y, w, h, Component.translatable("iu.gas_sensor.selected.none.body"), TEXT_DIM);
            return;
        }

        List<GasSensorVeinEntry.PreviewCell> cells = vein.getPreviewCells();
        if (cells.isEmpty()) {
            drawCenteredBoxText(graphics, x, y, w, h, Component.translatable("iu.gas_sensor.detail.no_preview"), TEXT_DIM);
            return;
        }

        FluidRenderData fluid = resolveFluidRenderData(vein);
        if (fluid == null) {
            renderFallbackDetailPreview(graphics, x, y, w, h, vein, cells);
            return;
        }

        renderFluidDetailPreview(graphics, x, y, w, h, vein, cells, fluid);
    }

    private void renderFluidDetailPreview(
            GuiGraphics graphics,
            int x,
            int y,
            int w,
            int h,
            GasSensorVeinEntry vein,
            List<GasSensorVeinEntry.PreviewCell> cells,
            FluidRenderData fluid
    ) {
        int minX = vein.getMinRelativeX();
        int minY = vein.getMinRelativeY();
        int minZ = vein.getMinRelativeZ();
        int maxX = vein.getMaxRelativeX();
        int maxY = vein.getMaxRelativeY();
        int maxZ = vein.getMaxRelativeZ();

        float spanX = vein.getSpanX();
        float spanY = vein.getSpanY();
        float spanZ = vein.getSpanZ();

        float horizontalSpan = spanX + spanZ;
        float baseScale = Math.min(
                (w - 54.0F) / Math.max(5.0F, horizontalSpan),
                (h - 48.0F) / Math.max(5.0F, spanY + horizontalSpan * 0.38F)
        );
        float scale = Mth.clamp(baseScale * this.detailZoom, 4.0F, 18.0F);

        float centerX = x + w * 0.50F;
        float centerY = y + h * 0.84F;
        float pitchDegrees = 18.0F + this.detailPitch * 28.0F;

        Set<Long> occupied = new HashSet<>();
        for (GasSensorVeinEntry.PreviewCell cell : cells) {
            occupied.add(previewCellKey(cell.relativeX(), cell.relativeY(), cell.relativeZ()));
        }

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(centerX, centerY, 240.0F);
        poseStack.scale(scale, -scale, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(this.detailYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitchDegrees));
        poseStack.translate(
                -((minX + maxX + 1) / 2.0F),
                -(minY + spanY * 0.45F),
                -((minZ + maxZ + 1) / 2.0F)
        );

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();


        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        for (GasSensorVeinEntry.PreviewCell cell : cells) {
            boolean northOpen = !occupied.contains(previewCellKey(cell.relativeX(), cell.relativeY(), cell.relativeZ() - 1));
            boolean southOpen = !occupied.contains(previewCellKey(cell.relativeX(), cell.relativeY(), cell.relativeZ() + 1));
            boolean westOpen = !occupied.contains(previewCellKey(cell.relativeX() - 1, cell.relativeY(), cell.relativeZ()));
            boolean eastOpen = !occupied.contains(previewCellKey(cell.relativeX() + 1, cell.relativeY(), cell.relativeZ()));
            boolean upOpen = !occupied.contains(previewCellKey(cell.relativeX(), cell.relativeY() + 1, cell.relativeZ()));

            float fluidHeight = Mth.clamp(cell.amountMb() / 1000.0F, 0.18F, 1.0F);

            renderFluidPreviewCell(
                    poseStack,
                    bufferSource,
                    fluid,
                    cell.relativeX(),
                    cell.relativeY(),
                    cell.relativeZ(),
                    fluidHeight,
                    northOpen,
                    southOpen,
                    westOpen,
                    eastOpen,
                    upOpen
            );
        }

        bufferSource.endBatch();

        poseStack.popPose();

        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }

    private void renderFallbackDetailPreview(
            GuiGraphics graphics,
            int x,
            int y,
            int w,
            int h,
            GasSensorVeinEntry vein,
            List<GasSensorVeinEntry.PreviewCell> cells
    ) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (GasSensorVeinEntry.PreviewCell cell : cells) {
            minX = Math.min(minX, cell.relativeX());
            minY = Math.min(minY, cell.relativeY());
            minZ = Math.min(minZ, cell.relativeZ());
            maxX = Math.max(maxX, cell.relativeX());
            maxY = Math.max(maxY, cell.relativeY());
            maxZ = Math.max(maxZ, cell.relativeZ());
        }

        float spanX = maxX - minX + 1;
        float spanY = maxY - minY + 1;
        float spanZ = maxZ - minZ + 1;
        float baseScale = Math.min(
                (w - 80.0F) / Math.max(6.0F, spanX + spanZ),
                (h - 70.0F) / Math.max(6.0F, spanY + (spanX + spanZ) * 0.30F)
        );
        float cellSize = Mth.clamp(baseScale * this.detailZoom, 3.0F, 16.0F);

        float centerX = x + w * 0.52F;
        float centerY = y + h * 0.68F;
        float yawRad = (float) Math.toRadians(this.detailYaw);

        List<ProjectedCell> projected = new ArrayList<>(cells.size());
        int baseVeinColor = veinColor(vein);

        for (GasSensorVeinEntry.PreviewCell cell : cells) {
            float lx = (cell.relativeX() - minX) - spanX / 2.0F;
            float ly = (cell.relativeY() - minY);
            float lz = (cell.relativeZ() - minZ) - spanZ / 2.0F;

            float rx = (float) (lx * Math.cos(yawRad) - lz * Math.sin(yawRad));
            float rz = (float) (lx * Math.sin(yawRad) + lz * Math.cos(yawRad));

            float sx = centerX + rx * cellSize * 0.95F;
            float sy = centerY + rz * cellSize * 0.30F - ly * cellSize * this.detailPitch;

            float fullness = Mth.clamp(cell.amountMb() / 1000.0F, 0.35F, 1.0F);
            int cellColor = multiplyColor(baseVeinColor, 0.70F + fullness * 0.30F);
            float depthShade = Mth.clamp(0.78F + (rz / Math.max(1.0F, spanX + spanZ)) * 0.32F, 0.52F, 1.15F);

            projected.add(new ProjectedCell(sx, sy, rz + ly * 0.5F, multiplyColor(cellColor, depthShade), cellSize));
        }

        projected.sort(Comparator.comparingDouble(ProjectedCell::depth));

        for (ProjectedCell cell : projected) {
            int size = Math.max(3, Math.round(cell.size()));
            int sx = Math.round(cell.screenX());
            int sy = Math.round(cell.screenY());
            int color = multiplyColor(cell.color(), 0.85F);
            int top = lighten(color, 0.22F);

            graphics.fill(sx, sy, sx + size, sy + size, color);
            graphics.fill(sx + 1, sy + 1, sx + size - 1, sy + Math.max(2, size / 3), top);
            graphics.fill(sx, sy, sx + size, sy + 1, 0x60FFFFFF);
            graphics.fill(sx, sy + size - 1, sx + size, sy + size, 0x44000000);
        }
    }

    private void renderFluidPreviewCell(
            PoseStack poseStack,
            MultiBufferSource.BufferSource bufferSource,
            FluidRenderData fluid,
            float x,
            float y,
            float z,
            float fluidHeight,
            boolean northOpen,
            boolean southOpen,
            boolean westOpen,
            boolean eastOpen,
            boolean upOpen
    ) {
        VertexConsumer builder = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluid.state()));

        float minX = x;
        float maxX = x + 1.0F;
        float minZ = z;
        float maxZ = z + 1.0F;
        float baseY = y;
        float topY = y + fluidHeight;

        int packedLight = LightTexture.FULL_BRIGHT;
        int tint = fluid.tint();

        if (upOpen) {
            drawFluidHorizontalQuad(
                    builder,
                    poseStack,
                    minX, topY, minZ,
                    maxX, topY, maxZ,
                    fluid.still().getU0(), fluid.still().getV0(),
                    fluid.still().getU1(), fluid.still().getV1(),
                    packedLight,
                    tint
            );
        }

        if (northOpen) {
            drawFluidVerticalQuadZ(
                    builder,
                    poseStack,
                    minX, baseY, minZ,
                    maxX, topY, minZ,
                    fluid.flow().getU0(), fluid.flow().getV0(),
                    fluid.flow().getU1(), fluid.flow().getV1(),
                    packedLight,
                    tint,
                    true
            );
        }

        if (southOpen) {
            drawFluidVerticalQuadZ(
                    builder,
                    poseStack,
                    minX, baseY, maxZ,
                    maxX, topY, maxZ,
                    fluid.flow().getU0(), fluid.flow().getV0(),
                    fluid.flow().getU1(), fluid.flow().getV1(),
                    packedLight,
                    tint,
                    false
            );
        }

        if (westOpen) {
            drawFluidVerticalQuadX(
                    builder,
                    poseStack,
                    minX,
                    baseY, minZ,
                    topY, maxZ,
                    fluid.flow().getU0(), fluid.flow().getV0(),
                    fluid.flow().getU1(), fluid.flow().getV1(),
                    packedLight,
                    tint,
                    true
            );
        }

        if (eastOpen) {
            drawFluidVerticalQuadX(
                    builder,
                    poseStack,
                    maxX,
                    baseY, minZ,
                    topY, maxZ,
                    fluid.flow().getU0(), fluid.flow().getV0(),
                    fluid.flow().getU1(), fluid.flow().getV1(),
                    packedLight,
                    tint,
                    false
            );
        }
    }

    private void drawFluidHorizontalQuad(
            VertexConsumer builder,
            PoseStack poseStack,
            float x0, float y0, float z0,
            float x1, float y1, float z1,
            float u0, float v0, float u1, float v1,
            int packedLight,
            int color
    ) {
        int a = ((color >> 24) & 0xFF);
        int r = ((color >> 16) & 0xFF);
        int g = ((color >> 8) & 0xFF);
        int b = (color & 0xFF);
        if (a == 0) {
            a = 180;
        }

        Matrix4f matrix = poseStack.last().pose();
        builder.addVertex(matrix, x0, y0, z0).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix, x0, y1, z1).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(matrix, x1, y0, z0).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
    }

    private void drawFluidVerticalQuadZ(
            VertexConsumer builder,
            PoseStack poseStack,
            float x0, float y0, float z0,
            float x1, float y1, float z1,
            float u0, float v0, float u1, float v1,
            int packedLight,
            int color,
            boolean north
    ) {
        int a = ((color >> 24) & 0xFF);
        int r = ((color >> 16) & 0xFF);
        int g = ((color >> 8) & 0xFF);
        int b = (color & 0xFF);
        if (a == 0) {
            a = 180;
        }

        Matrix4f matrix = poseStack.last().pose();

        if (north) {
            builder.addVertex(matrix, x0, y0, z0).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x0, y1, z0).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x1, y0, z1).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        } else {
            builder.addVertex(matrix, x1, y0, z1).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x0, y1, z0).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x0, y0, z0).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        }
    }

    private void drawFluidVerticalQuadX(
            VertexConsumer builder,
            PoseStack poseStack,
            float x,
            float y0, float z0,
            float y1, float z1,
            float u0, float v0, float u1, float v1,
            int packedLight,
            int color,
            boolean west
    ) {
        int a = ((color >> 24) & 0xFF);
        int r = ((color >> 16) & 0xFF);
        int g = ((color >> 8) & 0xFF);
        int b = (color & 0xFF);
        if (a == 0) {
            a = 180;
        }

        Matrix4f matrix = poseStack.last().pose();

        if (west) {
            builder.addVertex(matrix, x, y0, z1).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x, y1, z1).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x, y1, z0).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x, y0, z0).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        } else {
            builder.addVertex(matrix, x, y0, z0).setColor(r, g, b, a).setUv(u0, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x, y1, z0).setColor(r, g, b, a).setUv(u0, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x, y1, z1).setColor(r, g, b, a).setUv(u1, v0).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
            builder.addVertex(matrix, x, y0, z1).setColor(r, g, b, a).setUv(u1, v1).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
        }
    }

    private FluidRenderData resolveFluidRenderData(GasSensorVeinEntry vein) {
        String registryName = vein.getFluidRegistryName();
        if (registryName == null || registryName.isBlank()) {
            return null;
        }

        ResourceLocation id = ResourceLocation.tryParse(registryName);
        if (id == null) {
            return null;
        }

        Fluid fluid = BuiltInRegistries.FLUID.get(id);
        if (fluid == null) {
            return null;
        }

        FluidState state = fluid.defaultFluidState();
        if (state.isEmpty()) {
            return null;
        }

        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(state.getType());
        if (ext == null) {
            return null;
        }

        ResourceLocation stillTex = ext.getStillTexture(new FluidStack(fluid, 1000));
        ResourceLocation flowTex = ext.getFlowingTexture(new FluidStack(fluid, 1000));
        if (stillTex == null || flowTex == null) {
            return null;
        }

        TextureAtlasSprite still = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTex);
        TextureAtlasSprite flow = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(flowTex);

        int tint = 0xBFFFFFFF;
        try {
            tint = ext.getTintColor(new FluidStack(fluid, 1000));
            if ((tint >>> 24) == 0) {
                tint = 0xBF000000 | tint;
            }
        } catch (Throwable ignored) {
        }

        return new FluidRenderData(state, still, flow, tint);
    }

    private long previewCellKey(int x, int y, int z) {
        long xx = (((long) x) & 0x1FFFFFL) << 42;
        long yy = (((long) y) & 0x1FFFFFL) << 21;
        long zz = (((long) z) & 0x1FFFFFL);
        return xx | yy | zz;
    }

    private void renderDetailInfoBox(GuiGraphics graphics, int y, int x, int w, int h) {
        int contentX = x + 12;
        int contentW = w - 24;
        int maxY = y + h - 10;
        int yy = y + 10;

        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.detail.window.title"), contentX, yy, contentW, TEXT_MAIN, maxY);
        yy += 4;
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.detail.controls"), contentX, yy, contentW, TEXT_DIM, maxY);

        GasSensorVeinEntry vein = selectedVein().orElse(null);
        if (vein == null) {
            drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.none.body"), contentX, yy + 6, contentW, TEXT_MUTED, maxY);
            return;
        }

        yy += 6;
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.name", veinName(vein)), contentX, yy, contentW, TEXT_MAIN, maxY);
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.type", veinType(vein)), contentX, yy, contentW, TEXT_DIM, maxY);
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.volume", formatBuckets(veinBuckets(vein))), contentX, yy, contentW, TEXT_HIGHLIGHT, maxY);
        drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.preview_cells", vein.getPreviewCellCount()), contentX, yy, contentW, TEXT_HIGHLIGHT, maxY);
    }

    private void renderMapMode(GuiGraphics graphics, GasSensorScanState state, int mouseX, int mouseY) {
        int x = mainAreaX() + MAIN_INNER_PAD;
        int y = mainAreaY() + MAIN_INNER_PAD;
        int w = mainAreaW() - MAIN_INNER_PAD * 2;
        int h = mainAreaH() - MAIN_INNER_PAD * 2;

        graphics.fill(x, y, x + w, y + h, 0xFF0B0805);
        graphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0xFF110C08);

        int legendX = mapLegendX();
        int legendY = mapLegendY();
        int legendW = mapLegendW();

        graphics.fill(legendX, legendY, legendX + legendW, legendY + MAP_LEGEND_H, 0xAA140E09);
        graphics.fill(legendX, legendY, legendX + legendW, legendY + 1, BORDER);
        graphics.fill(legendX, legendY + MAP_LEGEND_H - 1, legendX + legendW, legendY + MAP_LEGEND_H, BORDER_SOFT);

        int textX = legendX + 10;
        int textW = Math.max(60, legendW - 20);
        int maxY = legendY + MAP_LEGEND_H - 6;

        int legendTextY = legendY + 8;
        legendTextY = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.map.legend.player"), textX, legendTextY, textW, TEXT_MAIN, maxY);
        legendTextY = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.map.legend.veins"), textX, legendTextY + 2, textW, TEXT_DIM, maxY);
        drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.map.controls"), textX, legendTextY + 4, textW, TEXT_MUTED, maxY);

        String zoomText = Component.translatable("iu.gas_sensor.map.zoom", Math.round(this.mapZoom * 100.0F)).getString();
        graphics.drawString(
                this.font,
                zoomText,
                legendX + legendW - 10 - this.font.width(zoomText),
                legendY + MAP_LEGEND_H - this.font.lineHeight - 6,
                TEXT_HIGHLIGHT,
                false
        );

        int viewX = mapViewportX();
        int viewY = mapViewportY();
        int viewW = mapViewportW();
        int viewH = mapViewportH();

        graphics.fill(viewX, viewY, viewX + viewW, viewY + viewH, 0xFF0E0A06);
        graphics.fill(viewX + 1, viewY + 1, viewX + viewW - 1, viewY + viewH - 1, 0xFF150F09);

        graphics.enableScissor(viewX, viewY, viewX + viewW, viewY + viewH);
        renderMapGrid(graphics, state, viewX, viewY, viewW, viewH, mouseX, mouseY);
        graphics.disableScissor();
    }

    private void renderMapGrid(GuiGraphics graphics, GasSensorScanState state, int x, int y, int w, int h, int mouseX, int mouseY) {
        int radius = Math.max(1, this.radiusChunks);
        float cell = mapCellSize();

        int playerChunkX = playerChunkX();
        int playerChunkZ = playerChunkZ();


        float centerX = x + w / 2.0F + this.mapPanX;
        float centerY = y + h / 2.0F + this.mapPanY;

        for (int gz = -radius; gz <= radius; gz++) {
            for (int gx = -radius; gx <= radius; gx++) {
                float sx = centerX + gx * cell - cell / 2.0F;
                float sy = centerY + gz * cell - cell / 2.0F;

                if (sx + cell < x || sy + cell < y || sx > x + w || sy > y + h) {
                    continue;
                }

                int fill = gx == 0 && gz == 0 ? MAP_CENTER : MAP_CELL;
                int ix = Math.round(sx);
                int iy = Math.round(sy);
                int ix2 = Math.round(sx + cell);
                int iy2 = Math.round(sy + cell);

                graphics.fill(ix, iy, ix2, iy2, fill);

                int line = (gx == 0 || gz == 0) ? MAP_GRID_BOLD : MAP_GRID;
                graphics.fill(ix, iy, ix2, iy + 1, line);
                graphics.fill(ix, iy2 - 1, ix2, iy2, line);
                graphics.fill(ix, iy, ix + 1, iy2, line);
                graphics.fill(ix2 - 1, iy, ix2, iy2, line);
            }
        }

        if (state != null) {
            int centerChunkX = state.getCenterChunkX();
            int centerChunkZ = state.getCenterChunkZ();
            int dxCenter = centerChunkX - playerChunkX;
            int dzCenter = centerChunkZ - playerChunkZ;
            float scanCx = centerX + dxCenter * cell;
            float scanCy = centerY + dzCenter * cell;
            int scanSize = Math.max(6, Math.round(cell * 0.34F));

            graphics.fill(
                    Math.round(scanCx - scanSize / 2.0F),
                    Math.round(scanCy - scanSize / 2.0F),
                    Math.round(scanCx + scanSize / 2.0F),
                    Math.round(scanCy + scanSize / 2.0F),
                    0xFFF0F0F0
            );
        } else {
            int playerSize = Math.max(6, Math.round(cell * 0.34F));
            int px = Math.round(centerX - playerSize / 2.0F);
            int py = Math.round(centerY - playerSize / 2.0F);
            graphics.fill(px, py, px + playerSize, py + playerSize, 0xFFF0F0F0);
        }

        for (GasSensorVeinEntry vein : this.visibleVeins) {
            int dx = vein.getChunkX() - playerChunkX;
            int dz = vein.getChunkZ() - playerChunkZ;

            float sx = centerX + dx * cell;
            float sy = centerY + dz * cell;
            int size = Objects.equals(this.selectedVeinKey, vein.getId())
                    ? Math.max(8, Math.round(cell * 0.48F))
                    : Math.max(6, Math.round(cell * 0.34F));

            int color = veinColor(vein);
            int left = Math.round(sx - size / 2.0F);
            int top = Math.round(sy - size / 2.0F);
            int right = Math.round(sx + size / 2.0F);
            int bottom = Math.round(sy + size / 2.0F);

            graphics.fill(left, top, right, bottom, color);
            graphics.fill(left, top, right, top + 1, 0x66FFFFFF);

            if (mouseX >= left && mouseX < right && mouseY >= top && mouseY < bottom) {
                this.hoveredTooltip = buildVeinTooltip(vein);
            }
        }
    }

    private float mapFitZoom() {
        int radius = Math.max(1, this.radiusChunks);
        int chunksAcross = radius * 2 + 1;

        float usableW = Math.max(40.0F, mapViewportW() - 12.0F);
        float usableH = Math.max(40.0F, mapViewportH() - 12.0F);

        return Math.min(
                usableW / (chunksAcross * MAP_BASE_CELL),
                usableH / (chunksAcross * MAP_BASE_CELL)
        );
    }

    private float mapCellSize() {
        return MAP_BASE_CELL * this.mapZoom;
    }

    private void renderStatusPanel(GuiGraphics graphics, GasSensorScanState state) {
        int x = sideAreaX() + 18;
        int y = sideAreaY() + 18;
        int w = sideAreaW() - 36;
        int h = statusPanelH() - 36;

        graphics.fill(x, y, x + w, y + h, 0x660F0B07);

        if (state != null && !state.isSupportedDimension()) {
            int maxY = y + h - 8;
            int yy = y + 20;
            yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.status.unsupported"), x + 16, yy, w - 32, TEXT_MAIN, maxY);
            drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.status.unsupported_hint"), x + 16, yy + 6, w - 32, TEXT_DIM, maxY);
            return;
        }

        boolean scanning = snapshotScanning(state);
        float progress = Mth.clamp((float) snapshotProgress(state), 0.0F, 1.0F);
        if (!scanning && progress <= 0.0F) {
            progress = 1.0F;
        }

        int ringRadius = Math.min(30, Math.max(22, h / 4));
        int ringCx = x + 76;
        int ringCy = y + Math.max(54, h / 2);
        drawCircularProgress(graphics, ringCx, ringCy, ringRadius, progress, scanning ? ACCENT : ACCENT_GREEN);

        String percent = Math.round(progress * 100.0F) + "%";
        graphics.drawCenteredString(this.font, percent, ringCx, ringCy - 4, TEXT_MAIN);

        int textX = x + 140;
        int textW = w - (textX - x) - 10;
        int maxY = y + h - 8;
        int yy = y + 18;
        yy = drawWrappedComponent(
                graphics,
                scanning ? Component.translatable("iu.gas_sensor.status.scanning") : Component.translatable("iu.gas_sensor.status.complete"),
                textX,
                yy,
                textW,
                TEXT_MAIN,
                maxY
        );
        yy += 6;
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.status.progress", percent), textX, yy, textW, TEXT_DIM, maxY);
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.status.chunk", snapshotChunkX(state), snapshotChunkZ(state)), textX, yy, textW, TEXT_DIM, maxY);
        drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.status.found", snapshotFoundCount(state)), textX, yy, textW, TEXT_HIGHLIGHT, maxY);
    }

    private void drawCircularProgress(GuiGraphics graphics, int cx, int cy, int radius, float progress, int color) {
        int dots = 56;
        for (int i = 0; i < dots; i++) {
            float a = (float) (-Math.PI / 2.0 + (Math.PI * 2.0 * i / dots));
            int dx = Math.round(cx + Mth.cos(a) * radius);
            int dy = Math.round(cy + Mth.sin(a) * radius);
            boolean active = i < Math.round(progress * dots);
            int dotColor = active ? color : 0xFF3A2A1B;
            int size = active ? 4 : 3;
            graphics.fill(dx - size / 2, dy - size / 2, dx + size / 2, dy + size / 2, dotColor);
        }
    }

    private void renderSelectedPanel(GuiGraphics graphics) {
        int x = sideAreaX() + 18;
        int y = selectedPanelY() + 18;
        int w = sideAreaW() - 36;
        int h = selectedPanelH() - 36;
        int maxY = y + h - 8;

        graphics.fill(x, y, x + w, y + h, 0x660F0B07);
        graphics.drawString(this.font, Component.translatable("iu.gas_sensor.selected.title"), x + 14, y + 14, TEXT_MAIN, false);

        int contentX = x + 14;
        int contentW = w - 28;
        int yy = y + 40;

        GasSensorVeinEntry vein = selectedVein().orElse(null);
        if (vein == null) {
            drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.none.body"), contentX, yy, contentW, TEXT_DIM, maxY);
            return;
        }

        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.name", veinName(vein)), contentX, yy, contentW, TEXT_MAIN, maxY);
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.type", veinType(vein)), contentX, yy, contentW, TEXT_DIM, maxY);
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.coords", vein.getBlockX(), vein.getBlockY(), vein.getBlockZ()), contentX, yy, contentW, TEXT_DIM, maxY);
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.chunk", vein.getChunkX(), vein.getChunkZ()), contentX, yy, contentW, TEXT_DIM, maxY);
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.volume", formatBuckets(veinBuckets(vein))), contentX, yy, contentW, TEXT_HIGHLIGHT, maxY);
        yy = drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.distance", formatDistance(veinDistanceMeters(vein))), contentX, yy, contentW, TEXT_DIM, maxY);
        drawWrappedComponent(graphics, Component.translatable("iu.gas_sensor.selected.preview_cells", vein.getPreviewCellCount()), contentX, yy, contentW, TEXT_HIGHLIGHT, maxY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (button != 0) {
            return false;
        }

        if (inside(mouseX, mouseY, topSliderX(), topSliderY(), topSliderW(), MODE_BUTTON_H)) {
            this.sliderDragging = true;
            updateRadiusFromMouse(mouseX);
            return true;
        }

        if (this.activeMode == Mode.LIST && inside(mouseX, mouseY, listViewportX(), listViewportY(), listViewportW(), listViewportH())) {
            if (inside(mouseX, mouseY, listScrollbarX(), listViewportY(), listScrollbarW(), listViewportH())) {
                this.listScrollbarDragging = true;
                this.listScrollbarGrabOffset = (int) mouseY - listScrollbarThumbY(listViewportY(), listViewportH());
                return true;
            }

            selectVeinFromList(mouseX, mouseY);
            return true;
        }

        if (this.activeMode == Mode.DETAIL && inside(mouseX, mouseY, detailPreviewX(), detailPreviewY(), detailPreviewW(), detailPreviewH())) {
            this.detailDragging = true;
            return true;
        }

        if (this.activeMode == Mode.MAP && inside(mouseX, mouseY, mapViewportX(), mapViewportY(), mapViewportW(), mapViewportH())) {
            if (!selectVeinFromMap(mouseX, mouseY)) {
                this.mapDragging = true;
            }
            return true;
        }

        return false;
    }

    private void selectVeinFromList(double mouseX, double mouseY) {
        int x = listViewportX();
        int y = listViewportY();
        int w = listRowsW();

        int drawY = y - this.listScroll;
        for (GasSensorVeinEntry vein : this.visibleVeins) {
            if (inside(mouseX, mouseY, x, drawY, w, LIST_ROW_H)) {
                this.selectedVeinKey = vein.getId();
                GasSensorClientCache.setSelectedVeinId(this.selectedVeinKey);
                return;
            }
            drawY += LIST_ROW_H + 8;
        }
    }

    private boolean selectVeinFromMap(double mouseX, double mouseY) {
        int x = mapViewportX();
        int y = mapViewportY();
        int w = mapViewportW();
        int h = mapViewportH();

        int radius = Math.max(1, this.radiusChunks);
        int playerChunkX = playerChunkX();
        int playerChunkZ = playerChunkZ();

        float cell = mapCellSize();

        float centerX = x + w / 2.0F + this.mapPanX;
        float centerY = y + h / 2.0F + this.mapPanY;

        for (GasSensorVeinEntry vein : this.visibleVeins) {
            int dx = vein.getChunkX() - playerChunkX;
            int dz = vein.getChunkZ() - playerChunkZ;

            float sx = centerX + dx * cell;
            float sy = centerY + dz * cell;
            int size = Math.max(8, Math.round(cell * 0.48F));

            if (mouseX >= sx - size / 2.0F && mouseX < sx + size / 2.0F && mouseY >= sy - size / 2.0F && mouseY < sy + size / 2.0F) {
                this.selectedVeinKey = vein.getId();
                GasSensorClientCache.setSelectedVeinId(this.selectedVeinKey);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.sliderDragging = false;
        this.listScrollbarDragging = false;
        this.detailDragging = false;
        this.mapDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button != 0) {
            return false;
        }

        if (this.sliderDragging) {
            updateRadiusFromMouse(mouseX);
            return true;
        }

        if (this.listScrollbarDragging) {
            updateListScrollFromMouse(mouseY);
            return true;
        }

        if (this.detailDragging) {
            this.detailYaw += (float) dragX * 0.95F;
            this.detailPitch = Mth.clamp(this.detailPitch + (float) (-dragY * 0.006F), 0.28F, 1.22F);
            return true;
        }

        if (this.mapDragging) {
            this.mapPanX += (float) dragX;
            this.mapPanY += (float) dragY;
            clampMapPan();
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double delta) {
        if (this.activeMode == Mode.LIST && inside(mouseX, mouseY, listViewportX(), listViewportY(), listViewportW(), listViewportH())) {
            this.listScroll = Mth.clamp(this.listScroll - (int) (delta * 28.0), 0, listMaxScroll());
            return true;
        }

        if (this.activeMode == Mode.DETAIL && inside(mouseX, mouseY, detailPreviewX(), detailPreviewY(), detailPreviewW(), detailPreviewH())) {
            this.detailZoom = Mth.clamp(this.detailZoom + (float) delta * 0.08F, 0.45F, 2.60F);
            return true;
        }

        if (this.activeMode == Mode.MAP && inside(mouseX, mouseY, mapViewportX(), mapViewportY(), mapViewportW(), mapViewportH())) {
            this.mapZoom = Mth.clamp(this.mapZoom + (float) delta * 0.08F, 0.02F, 3.20F);
            clampMapPan();
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, deltaX, delta);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return this.searchBox != null && this.searchBox.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox != null && this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (keyCode == 49) {
            setMode(Mode.LIST);
            return true;
        }
        if (keyCode == 50) {
            setMode(Mode.DETAIL);
            return true;
        }
        if (keyCode == 51) {
            setMode(Mode.MAP);
            return true;
        }
        if (keyCode == 82) {
            requestServerScan();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateRadiusFromMouse(double mouseX) {
        int x = radiusTrackX();
        int w = radiusTrackW();
        float t = (float) ((mouseX - x) / Math.max(1.0, w));
        t = Mth.clamp(t, 0.0F, 1.0F);
        this.radiusChunks = 1 + Math.round(t * 31.0F);
    }

    private void updateListScrollFromMouse(double mouseY) {
        int y = listViewportY();
        int h = listViewportH();
        int maxScroll = listMaxScroll();
        if (maxScroll <= 0) {
            this.listScroll = 0;
            return;
        }

        int contentH = listContentHeight();
        int thumbH = Math.max(22, (int) ((h / (float) contentH) * h));
        int movable = Math.max(1, h - thumbH);
        int thumbY = (int) mouseY - y - this.listScrollbarGrabOffset;
        float t = Mth.clamp(thumbY / (float) movable, 0.0F, 1.0F);
        this.listScroll = Math.round(t * maxScroll);
    }

    private int listScrollbarThumbY(int y, int h) {
        int maxScroll = listMaxScroll();
        if (maxScroll <= 0) {
            return y;
        }
        int contentH = listContentHeight();
        int thumbH = Math.max(22, (int) ((h / (float) contentH) * h));
        int movable = h - thumbH;
        return y + Math.round((this.listScroll / (float) maxScroll) * movable);
    }

    private int listContentHeight() {
        if (this.visibleVeins.isEmpty()) {
            return 0;
        }
        return this.visibleVeins.size() * LIST_ROW_H + Math.max(0, this.visibleVeins.size() - 1) * 8;
    }

    private int listMaxScroll() {
        return Math.max(0, listContentHeight() - listViewportH());
    }

    private void requestServerScan() {
        this.listScroll = 0;
        this.selectedVeinKey = "";
        GasSensorClientCache.setSelectedVeinId("");
        new PacketGasSensorScanRequest(this.radiusChunks, this.minecraft.player);
        resetMapView();
    }

    private void toggleFollow() {
        GasSensorVeinEntry vein = selectedVein().orElse(null);
        if (vein == null) {
            return;
        }

        this.selectedVeinKey = vein.getId();
        GasSensorClientCache.setSelectedVeinId(this.selectedVeinKey);
        GasSensorClientCache.toggleFollowSelected();
    }

    private Component followButtonText() {
        return GasSensorClientCache.isFollowingSelected()
                ? Component.translatable("iu.gas_sensor.button.follow_off")
                : Component.translatable("iu.gas_sensor.button.follow_on");
    }

    private void rebuildVisibleVeins(GasSensorScanState state) {
        List<GasSensorVeinEntry> all = snapshotVeins(state);
        String query = this.searchBox == null ? "" : this.searchBox.getValue().trim().toLowerCase(Locale.ROOT);

        List<GasSensorVeinEntry> out = new ArrayList<>();
        for (GasSensorVeinEntry vein : all) {
            String hay = (veinName(vein) + " " + veinType(vein) + " " + vein.getFluidRegistryName()).toLowerCase(Locale.ROOT);
            if (query.isEmpty() || hay.contains(query)) {
                out.add(vein);
            }
        }

        out.sort(Comparator.comparingDouble(this::veinDistanceMeters));
        this.visibleVeins = out;

        if (selectedVein().isEmpty()) {
            this.selectedVeinKey = out.isEmpty() ? "" : out.get(0).getId();
            GasSensorClientCache.setSelectedVeinId(this.selectedVeinKey);
        }
    }

    private java.util.Optional<GasSensorVeinEntry> selectedVein() {
        if (this.selectedVeinKey == null || this.selectedVeinKey.isEmpty()) {
            return java.util.Optional.empty();
        }
        for (GasSensorVeinEntry vein : this.visibleVeins) {
            if (Objects.equals(this.selectedVeinKey, vein.getId())) {
                return java.util.Optional.of(vein);
            }
        }
        return java.util.Optional.empty();
    }

    private List<Component> buildVeinTooltip(GasSensorVeinEntry vein) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal(veinName(vein)));
        lines.add(Component.translatable("iu.gas_sensor.tooltip.coords", vein.getBlockX(), vein.getBlockY(), vein.getBlockZ()));
        lines.add(Component.translatable("iu.gas_sensor.tooltip.volume", formatBuckets(veinBuckets(vein))));
        lines.add(Component.translatable("iu.gas_sensor.tooltip.distance", formatDistance(veinDistanceMeters(vein))));
        return lines;
    }

    private int drawWrappedComponent(GuiGraphics graphics, Component text, int x, int y, int width, int color, int maxY) {
        List<FormattedCharSequence> lines = this.font.split(text, Math.max(20, width));
        int yy = y;
        for (FormattedCharSequence line : lines) {
            if (yy + this.font.lineHeight > maxY) {
                break;
            }
            graphics.drawString(this.font, line, x, yy, color, false);
            yy += this.font.lineHeight + 2;
        }
        return yy;
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

    private int headerBottomY() {
        return panelY() + HEADER_H;
    }

    private int topControlsY() {
        return headerBottomY() + 10;
    }

    private int tabsY() {
        return topControlsY() + SEARCH_H + 14;
    }

    private int contentY() {
        return tabsY() + MODE_BUTTON_H + 14;
    }

    private int contentH() {
        return panelY() + panelH() - FOOTER_PAD - contentY();
    }

    private int topControlsLeft() {
        return panelX() + OUTER_PAD;
    }

    private int topControlsRight() {
        return panelX() + panelW() - OUTER_PAD;
    }

    private int topActionButtonW() {
        int contentW = topControlsRight() - topControlsLeft();
        return Mth.clamp(contentW / 7, 140, 180);
    }

    private int topButtonsX() {
        int actionW = topActionButtonW();
        return topControlsRight() - actionW * 2 - 12;
    }

    private int topSearchW() {
        return Math.max(260, topButtonsX() - topControlsLeft() - 12);
    }

    private int tabButtonW() {
        int available = panelW() - OUTER_PAD * 2 - 320 - 42;
        return Mth.clamp(available / 3, 104, 128);
    }

    private int topSliderX() {
        return topControlsLeft() + (tabButtonW() + 12) * 3 + 18;
    }

    private int topSliderY() {
        return tabsY();
    }

    private int topSliderW() {
        return topControlsRight() - topSliderX();
    }

    private int radiusTrackX() {
        return topSliderX() + 136;
    }

    private int radiusTrackW() {
        return Math.max(56, topSliderW() - 150);
    }

    private int sideAreaW() {
        return Mth.clamp(panelW() / 4, 320, 380);
    }

    private int leftPanelW() {
        return Math.max(260, panelW() - sideAreaW() - CONTENT_GAP - OUTER_PAD * 2);
    }

    private int mainAreaX() {
        return panelX() + OUTER_PAD;
    }

    private int mainAreaY() {
        return contentY();
    }

    private int mainAreaW() {
        return leftPanelW();
    }

    private int mainAreaH() {
        return contentH();
    }

    private int sideAreaX() {
        return mainAreaX() + mainAreaW() + CONTENT_GAP;
    }

    private int sideAreaY() {
        return contentY();
    }

    private int sideAreaH() {
        return contentH();
    }

    private int statusPanelH() {
        return Math.max(160, (int) (sideAreaH() * 0.40F));
    }

    private int selectedPanelY() {
        return sideAreaY() + statusPanelH() + CONTENT_GAP;
    }

    private int selectedPanelH() {
        return sideAreaH() - statusPanelH() - CONTENT_GAP;
    }

    private int listViewportX() {
        return mainAreaX() + MAIN_INNER_PAD;
    }

    private int listViewportY() {
        return mainAreaY() + MAIN_INNER_PAD;
    }

    private int listViewportW() {
        return mainAreaW() - MAIN_INNER_PAD * 2;
    }

    private int listViewportH() {
        return mainAreaH() - MAIN_INNER_PAD * 2;
    }

    private int listScrollbarW() {
        return 8;
    }

    private int listScrollbarX() {
        return listViewportX() + listViewportW() - listScrollbarW();
    }

    private int listRowsW() {
        return listViewportW() - listScrollbarW() - 6;
    }

    private int detailPreviewX() {
        return mainAreaX() + MAIN_INNER_PAD;
    }

    private int detailPreviewY() {
        return mainAreaY() + MAIN_INNER_PAD;
    }

    private int detailPreviewW() {
        return mainAreaW() - MAIN_INNER_PAD * 2;
    }

    private int detailPreviewH() {
        return (mainAreaH() - MAIN_INNER_PAD * 2) * DETAIL_PREVIEW_SPLIT[0] / 100 - 8;
    }

    private int mapLegendX() {
        return mainAreaX() + MAIN_INNER_PAD + 8;
    }

    private int mapLegendY() {
        return mainAreaY() + MAIN_INNER_PAD + 8;
    }

    private int mapLegendW() {
        return mainAreaW() - (MAIN_INNER_PAD + 8) * 2;
    }

    private int mapViewportX() {
        return mainAreaX() + MAIN_INNER_PAD + 8;
    }

    private int mapViewportY() {
        return mapLegendY() + MAP_LEGEND_H + 8;
    }

    private int mapViewportW() {
        return mainAreaW() - (MAIN_INNER_PAD + 8) * 2;
    }

    private int mapViewportH() {
        return mainAreaH() - MAIN_INNER_PAD * 2 - 16 - MAP_LEGEND_H - 8;
    }

    private void drawCenteredBoxText(GuiGraphics graphics, int x, int y, int w, int h, Component text, int color) {
        List<FormattedCharSequence> lines = this.font.split(text, Math.max(40, w - 20));
        int totalH = lines.size() * (this.font.lineHeight + 2);
        int yy = y + Math.max(0, (h - totalH) / 2);
        for (FormattedCharSequence line : lines) {
            graphics.drawString(this.font, line, x + Math.max(0, (w - this.font.width(line)) / 2), yy, color, false);
            yy += this.font.lineHeight + 2;
        }
    }

    private boolean inside(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
    }

    private String trimToWidth(String text, int maxWidth) {
        return this.font.plainSubstrByWidth(text, Math.max(10, maxWidth));
    }

    private String formatBuckets(double buckets) {
        return String.format(Locale.ROOT, "%.1f b", buckets);
    }

    private String formatDistance(double meters) {
        return String.format(Locale.ROOT, "%.1f m", meters);
    }

    private int lighten(int color, float amount) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        r = Mth.clamp((int) (r + (255 - r) * amount), 0, 255);
        g = Mth.clamp((int) (g + (255 - g) * amount), 0, 255);
        b = Mth.clamp((int) (b + (255 - b) * amount), 0, 255);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    private int multiplyColor(int color, float mul) {
        int a = (color >>> 24) & 0xFF;
        if (a == 0) {
            a = 0xFF;
        }
        int r = Mth.clamp((int) (((color >> 16) & 0xFF) * mul), 0, 255);
        int g = Mth.clamp((int) (((color >> 8) & 0xFF) * mul), 0, 255);
        int b = Mth.clamp((int) ((color & 0xFF) * mul), 0, 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private int playerChunkX() {
        return this.minecraft != null && this.minecraft.player != null ? new ChunkPos(this.minecraft.player.blockPosition()).x : 0;
    }

    private int playerChunkZ() {
        return this.minecraft != null && this.minecraft.player != null ? new ChunkPos(this.minecraft.player.blockPosition()).z : 0;
    }

    private double playerX() {
        return this.minecraft != null && this.minecraft.player != null ? this.minecraft.player.getX() : 0.0D;
    }

    private double playerY() {
        return this.minecraft != null && this.minecraft.player != null ? this.minecraft.player.getY() : 0.0D;
    }

    private double playerZ() {
        return this.minecraft != null && this.minecraft.player != null ? this.minecraft.player.getZ() : 0.0D;
    }

    private double veinDistanceMeters(GasSensorVeinEntry vein) {
        double dx = playerX() - vein.getBlockX();
        double dy = playerY() - vein.getBlockY();
        double dz = playerZ() - vein.getBlockZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private String veinName(GasSensorVeinEntry vein) {
        if (vein.getTranslationKey() != null && !vein.getTranslationKey().isBlank()) {
            return Component.translatable(vein.getTranslationKey()).getString();
        }
        if (vein.getTypeName() != null && !vein.getTypeName().isBlank()) {
            return vein.getTypeName();
        }
        return Component.translatable("iu.gas_sensor.unknown").getString();
    }

    private String veinType(GasSensorVeinEntry vein) {
        if (vein.getTypeName() != null && !vein.getTypeName().isBlank()) {
            return vein.getTypeName();
        }
        return veinName(vein);
    }

    private double veinBuckets(GasSensorVeinEntry vein) {
        return vein.getAmountMb() / 1000.0D;
    }

    private int veinColor(GasSensorVeinEntry vein) {
        return guessVeinColor(vein.getTypeName(), veinName(vein));
    }

    private int guessVeinColor(String type, String name) {
        String s = ((type == null ? "" : type) + " " + (name == null ? "" : name)).toLowerCase(Locale.ROOT);
        if (s.contains("fluor") || s.contains("фтор")) return 0xFFE6D45C;
        if (s.contains("iod") || s.contains("йод")) return 0xFFD03EF0;
        if (s.contains("газ") || s.contains("gas")) return 0xFF38C8FF;
        if (s.contains("chlor") || s.contains("хлор")) return 0xFF45C943;
        if (s.contains("brom") || s.contains("бром")) return 0xFFE69546;
        return 0xFF8FB4FF;
    }

    private GasSensorScanState snapshot() {
        return GasSensorClientCache.getState();
    }

    private void clampMapPan() {
        int radius = Math.max(1, this.radiusChunks);
        int chunksAcross = radius * 2 + 1;

        int viewW = mapViewportW();
        int viewH = mapViewportH();

        float cell = mapCellSize();

        float contentW = chunksAcross * cell;
        float contentH = chunksAcross * cell;


        float maxPanX = Math.max(0.0F, (contentW - (viewW - 12.0F)) * 0.5F);
        float maxPanY = Math.max(0.0F, (contentH - (viewH - 12.0F)) * 0.5F);

        this.mapPanX = Mth.clamp(this.mapPanX, -maxPanX, maxPanX);
        this.mapPanY = Mth.clamp(this.mapPanY, -maxPanY, maxPanY);
    }

    private List<GasSensorVeinEntry> snapshotVeins(GasSensorScanState state) {
        return state == null ? List.of() : state.getVeins();
    }

    private boolean snapshotScanning(GasSensorScanState state) {
        return state != null && state.isScanning();
    }

    private double snapshotProgress(GasSensorScanState state) {
        return state == null ? 0.0D : state.getProgress();
    }

    private int snapshotChunkX(GasSensorScanState state) {
        return state == null ? 0 : state.getCurrentChunkX();
    }

    private int snapshotChunkZ(GasSensorScanState state) {
        return state == null ? 0 : state.getCurrentChunkZ();
    }

    private int snapshotFoundCount(GasSensorScanState state) {
        return state == null ? 0 : state.getFoundCount();
    }

    private enum Mode {
        LIST,
        DETAIL,
        MAP
    }

    private record FluidRenderData(
            FluidState state,
            TextureAtlasSprite still,
            TextureAtlasSprite flow,
            int tint
    ) {
    }

    private static final class ProjectedCell {
        private final float screenX;
        private final float screenY;
        private final float depth;
        private final int color;
        private final float size;

        private ProjectedCell(float screenX, float screenY, float depth, int color, float size) {
            this.screenX = screenX;
            this.screenY = screenY;
            this.depth = depth;
            this.color = color;
            this.size = size;
        }

        public float screenX() {
            return screenX;
        }

        public float screenY() {
            return screenY;
        }

        public float depth() {
            return depth;
        }

        public int color() {
            return color;
        }

        public float size() {
            return size;
        }
    }

    private static class OrangeButton extends AbstractWidget {

        private final int accent;
        private final PressAction pressAction;
        private boolean selected;

        private OrangeButton(int x, int y, int width, int height, Component message, int accent, PressAction pressAction) {
            super(x, y, width, height, message);
            this.accent = accent;
            this.pressAction = pressAction;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            if (this.active && this.visible && this.pressAction != null) {
                this.pressAction.onPress(this);
            }
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            boolean hovered = this.isHoveredOrFocused();

            int outer = selected ? 0xFF8D5A1F : hovered ? 0xFF6F4618 : 0xFF4E3315;
            int inner = selected ? 0xFF23170C : hovered ? 0xFF1C130B : 0xFF150F09;
            int top = selected ? 0xFF332014 : hovered ? 0xFF281A10 : 0xFF1D140C;

            graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, outer);
            graphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1, this.getY() + this.height - 1, inner);
            graphics.fill(this.getX() + 2, this.getY() + 2, this.getX() + this.width - 2, this.getY() + this.height / 2, top);
            graphics.fill(this.getX() + 4, this.getY() + this.height - 6, this.getX() + this.width - 4, this.getY() + this.height - 2, this.accent);

            if (hovered) {
                graphics.fill(this.getX() + 2, this.getY() + 2, this.getX() + this.width - 2, this.getY() + 4, 0x30FFFFFF);
            }
            if (selected) {
                graphics.fill(this.getX() + 2, this.getY() + 2, this.getX() + this.width - 2, this.getY() + 4, 0x55FFFFFF);
            }

            Font font = Minecraft.getInstance().font;
            int textX = this.getX() + this.width / 2;
            int textY = this.getY() + (this.height - font.lineHeight) / 2 + 1;
            graphics.drawCenteredString(font, this.getMessage(), textX, textY, 0xFFF3E7D1);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }

        public interface PressAction {
            void onPress(OrangeButton button);
        }
    }

    private static class OrangeEditBox extends EditBox {

        public OrangeEditBox(Font font, int x, int y, int width, int height, Component hint) {
            super(font, x, y, width, height, hint);
            this.setHint(hint);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF1A120B);
            graphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, 0xFF22170E);

            graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + 1, 0xFF8A5A22);
            graphics.fill(this.getX(), this.getY() + this.getHeight() - 1, this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF4B3116);
            graphics.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.getHeight(), 0xFF8A5A22);
            graphics.fill(this.getX() + this.getWidth() - 1, this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF4B3116);

            int textInset = 5;

            graphics.enableScissor(
                    this.getX() + textInset,
                    this.getY() + 2,
                    this.getX() + this.getWidth() - 4,
                    this.getY() + this.getHeight() - 2
            );

            graphics.pose().pushPose();
            graphics.pose().translate(textInset, textInset, 0.0F);
            super.renderWidget(graphics, mouseX, mouseY, partialTick);
            graphics.pose().popPose();

            graphics.disableScissor();
        }
    }
}