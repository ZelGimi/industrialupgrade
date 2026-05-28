package com.denfop.items.armour.special;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.widget.ButtonListSliderWidget;
import com.denfop.api.widget.SliderWidget;
import com.denfop.network.packet.PacketColorPicker;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.render.streak.PlayerStreakPreviewRenderer;
import com.denfop.render.streak.RGB;
import com.denfop.screen.ScreenMain;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class WidgetColorPicker<T extends ContainerMenuStreak> extends ScreenMain<ContainerMenuStreak>
        implements ButtonListSliderWidget.GuiResponder, SliderWidget.FormatHelper {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/Color.png".toLowerCase());

    private static final int SCREEN_WIDTH = 392;
    private static final int SCREEN_HEIGHT = 330;

    private static final int LEFT_PANEL_W = 160;
    private static final int RIGHT_PANEL_W = 194;
    private static final int PANEL_H = 298;

    private static final int OUTER_MARGIN = 14;
    private static final int INNER_GAP = 10;

    private static final List<Integer> PRESET_COLORS = List.of(
            0xFFFFFF, 0xE4E4E4, 0xB9B9B9, 0x7C7C7C, 0x3A3A3A, 0x000000,
            0xFF4D4D, 0xFF7F50, 0xFFA500, 0xFFD966, 0xFFFF00, 0xC6E377,
            0x6CC04A, 0x00C853, 0x00E5FF, 0x4FC3F7, 0x2196F3, 0x3F51B5,
            0x673AB7, 0x9C27B0, 0xE91E63, 0xFF4081, 0x8D6E63, 0xBCAAA4,
            0xD32F2F, 0xF57C00, 0xFBC02D, 0x388E3C, 0x00796B, 0x1976D2,
            0x512DA8, 0xC2185B, 0x795548, 0x607D8B, 0xAEEA00, 0x64FFDA
    );

    private PlayerStreakInfo colorPicker;

    private int originalRgb;
    private boolean originalRainbow;

    private int currentRgb;
    private float hue;
    private float saturation;
    private float value;
    private boolean rainbowMode;

    private HsvColorPaletteWidget paletteWidget;
    private HueSliderWidget hueWidget;
    private PresetColorScrollWidget presetWidget;
    private EditBox hexBox;
    private Button rainbowButton;
    private Button applyButton;
    private Button resetButton;
    private Button backButton;

    private boolean updatingHex;

    public WidgetColorPicker(ContainerMenuStreak container, final ItemStack itemStack1) {
        super(container);
        this.componentList.clear();
        this.title = Component.empty();
        this.imageWidth = SCREEN_WIDTH;
        this.imageHeight = SCREEN_HEIGHT;
    }

    private static int rgbToInt(int r, int g, int b) {
        return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    private static String formatHex(int rgb) {
        return String.format("#%06X", rgb & 0xFFFFFF);
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.imageWidth = SCREEN_WIDTH;
        this.imageHeight = SCREEN_HEIGHT;
        super.init();

        this.colorPicker = IUCore.mapStreakInfo.get(this.container.player.getName().getString());
        if (this.colorPicker == null) {
            this.colorPicker = new PlayerStreakInfo(new RGB((short) 0, (short) 255, (short) 255), false);
            IUCore.mapStreakInfo.put(this.container.player.getName().getString(), this.colorPicker);
            new PacketColorPicker(this.colorPicker, this.container.player.getName().getString(), this.container.player.registryAccess());
        }

        this.originalRgb = rgbToInt(
                this.colorPicker.getRgb().getRed(),
                this.colorPicker.getRgb().getGreen(),
                this.colorPicker.getRgb().getBlue()
        );
        this.originalRainbow = this.colorPicker.isRainbow();

        this.currentRgb = this.originalRgb;
        this.rainbowMode = this.originalRainbow;
        loadHsvFromCurrentRgb();

        final int leftX = (this.width - this.imageWidth) / 2 + OUTER_MARGIN;
        final int rightX = leftX + LEFT_PANEL_W + INNER_GAP;
        final int panelY = (this.height - this.imageHeight) / 2 + 16;

        final int paletteX = leftX + 14;
        final int paletteY = panelY + 72;
        final int paletteSize = 104;
        final int hueX = paletteX + paletteSize + 8;
        final int hueW = 12;

        this.hexBox = new EditBox(this.font, leftX + 14, panelY + 34, 88, 18, Component.translatable("iu.color_picker.hex"));
        this.hexBox.setMaxLength(7);
        this.hexBox.setValue(formatHex(this.currentRgb));
        this.hexBox.setFilter(value -> value.matches("#?[0-9a-fA-F]{0,6}"));
        this.hexBox.setResponder(this::onHexEdited);
        this.addRenderableWidget(this.hexBox);

        this.rainbowButton = this.addRenderableWidget(
                Button.builder(Component.empty(), button -> {
                    this.rainbowMode = !this.rainbowMode;
                    updateRainbowButtonText();
                    pushColorToLiveState();
                }).bounds(leftX + 108, panelY + 34, 50, 18).build()
        );
        updateRainbowButtonText();

        this.paletteWidget = this.addRenderableWidget(new HsvColorPaletteWidget(
                paletteX,
                paletteY,
                paletteSize,
                paletteSize,
                this.hue,
                this.saturation,
                this.value,
                (sat, val) -> {
                    this.saturation = sat;
                    this.value = val;
                    updateRgbFromHsv(true);
                }
        ));

        this.hueWidget = this.addRenderableWidget(new HueSliderWidget(
                hueX,
                paletteY,
                hueW,
                paletteSize,
                this.hue,
                newHue -> {
                    this.hue = newHue;
                    this.paletteWidget.setHue(this.hue);
                    updateRgbFromHsv(true);
                }
        ));

        this.presetWidget = this.addRenderableWidget(new PresetColorScrollWidget(
                rightX + 14,
                panelY + 220,
                166,
                44,
                PRESET_COLORS,
                this.currentRgb,
                color -> {
                    this.rainbowMode = false;
                    updateRainbowButtonText();
                    setColorFromRgb(color, true);
                }
        ));

        this.applyButton = this.addRenderableWidget(
                Button.builder(Component.translatable("iu.color_picker.apply"), button -> {
                    pushColorToLiveState();
                    this.onClose();
                }).bounds(rightX + 14, panelY + 268, 52, 20).build()
        );

        this.resetButton = this.addRenderableWidget(
                Button.builder(Component.translatable("iu.color_picker.reset"), button -> {
                    this.rainbowMode = this.originalRainbow;
                    updateRainbowButtonText();
                    setColorFromRgb(this.originalRgb, false);
                    pushColorToLiveState();
                }).bounds(rightX + 72, panelY + 268, 52, 20).build()
        );

        this.backButton = this.addRenderableWidget(
                Button.builder(Component.translatable("iu.color_picker.back"), button -> this.onClose())
                        .bounds(rightX + 128, panelY + 268, 52, 20)
                        .build()
        );

        syncWidgetsFromState();
    }

    private void updateRainbowButtonText() {
        if (this.rainbowButton != null) {
            this.rainbowButton.setMessage(this.rainbowMode
                    ? Component.translatable("iu.color_picker.rainbow")
                    : Component.translatable("iu.color_picker.static"));
        }
    }

    private void onHexEdited(String value) {
        if (this.updatingHex) {
            return;
        }

        String raw = value.startsWith("#") ? value.substring(1) : value;
        if (raw.length() != 6) {
            return;
        }

        try {
            int rgb = Integer.parseInt(raw, 16) & 0xFFFFFF;
            this.rainbowMode = false;
            updateRainbowButtonText();
            setColorFromRgb(rgb, true);
        } catch (NumberFormatException ignored) {
        }
    }

    private void setColorFromRgb(int rgb, boolean sendPacket) {
        this.currentRgb = rgb & 0xFFFFFF;
        loadHsvFromCurrentRgb();
        syncWidgetsFromState();
        if (sendPacket) {
            pushColorToLiveState();
        }
    }

    private void updateRgbFromHsv(boolean sendPacket) {
        this.currentRgb = Mth.hsvToRgb(this.hue, this.saturation, this.value) & 0xFFFFFF;
        syncWidgetsFromState();
        if (sendPacket) {
            pushColorToLiveState();
        }
    }

    private void loadHsvFromCurrentRgb() {
        int r = (this.currentRgb >> 16) & 0xFF;
        int g = (this.currentRgb >> 8) & 0xFF;
        int b = this.currentRgb & 0xFF;

        float[] hsv = Color.RGBtoHSB(r, g, b, null);
        this.hue = hsv[0];
        this.saturation = hsv[1];
        this.value = hsv[2];
    }

    private void syncWidgetsFromState() {
        if (this.paletteWidget != null) {
            this.paletteWidget.setHue(this.hue);
            this.paletteWidget.setSaturationValue(this.saturation, this.value);
        }

        if (this.hueWidget != null) {
            this.hueWidget.setHue(this.hue);
        }

        if (this.presetWidget != null) {
            this.presetWidget.setSelectedColor(this.currentRgb);
        }

        if (this.hexBox != null) {
            this.updatingHex = true;
            this.hexBox.setValue(formatHex(this.currentRgb));
            this.updatingHex = false;
        }
    }

    private void pushColorToLiveState() {
        String playerName = this.container.player.getName().getString();

        PlayerStreakInfo liveInfo = IUCore.mapStreakInfo.get(playerName);
        if (liveInfo == null) {
            liveInfo = new PlayerStreakInfo(new RGB((short) 0, (short) 255, (short) 255), false);
            IUCore.mapStreakInfo.put(playerName, liveInfo);
        }

        liveInfo.getRgb().setRed((short) ((this.currentRgb >> 16) & 0xFF));
        liveInfo.getRgb().setGreen((short) ((this.currentRgb >> 8) & 0xFF));
        liveInfo.getRgb().setBlue((short) (this.currentRgb & 0xFF));
        liveInfo.setRainbow(this.rainbowMode);

        this.colorPicker = liveInfo;

        new PacketColorPicker(liveInfo, playerName, this.container.player.registryAccess());
    }

    @Override
    public void updateTickInterface() {
        super.updateTickInterface();
    }

    @Override
    public void updateTick() {
        super.updateTick();

    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.disableDepthTest();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, final float partialTicks, final int mouseX, final int mouseY) {
        final int leftX = this.guiLeft + OUTER_MARGIN;
        final int rightX = leftX + LEFT_PANEL_W + INNER_GAP;
        final int panelY = this.guiTop + 16;

        guiGraphics.fill(0, 0, this.width, this.height, 0xC0101016);

        ColorPickerRenderUtil.drawPanel(guiGraphics, this.guiLeft, this.guiTop, this.imageWidth, this.imageHeight, 0xF51A1C22, 0xFF343A46);
        ColorPickerRenderUtil.drawPanel(guiGraphics, leftX, panelY, LEFT_PANEL_W, PANEL_H, 0xF71B2029, 0xFF353B47);
        ColorPickerRenderUtil.drawPanel(guiGraphics, rightX, panelY, RIGHT_PANEL_W, PANEL_H, 0xF71B2029, 0xFF353B47);

        guiGraphics.drawString(this.font, Component.translatable("iu.color_picker.title"), leftX + 14, panelY + 10, 0xF2F6FF, false);
        guiGraphics.drawString(this.font, Component.translatable("iu.color_picker.hex"), leftX + 14, panelY + 24, 0xAEB8CC, false);
        guiGraphics.drawString(this.font, Component.translatable("iu.color_picker.preview"), rightX + 14, panelY + 10, 0xF2F6FF, false);

        final int previewX = rightX + 14;
        final int previewY = panelY + 24;
        final int previewW = 166;
        final int previewH = 140;

        ColorPickerRenderUtil.drawInsetPanel(guiGraphics, previewX, previewY, previewW, previewH, 0xFF10141B, 0xFF2B3240);
        PlayerStreakPreviewRenderer.renderPreview(
                guiGraphics,
                previewX,
                previewY,
                previewW,
                previewH,
                this.container.player,
                this.colorPicker,
                partialTicks,
                mouseX,
                mouseY
        );

        final int liveColor = PlayerStreakPreviewRenderer.resolvePreviewRgb(this.colorPicker, partialTicks);

        guiGraphics.drawString(this.font, Component.translatable("iu.color_picker.current"), rightX + 14, panelY + 172, 0xAEB8CC, false);
        guiGraphics.drawString(this.font, Component.translatable("iu.color_picker.original"), rightX + 110, panelY + 172, 0xAEB8CC, false);

        drawColorSwatch(guiGraphics, rightX + 14, panelY + 184, 64, 18, liveColor);
        drawColorSwatch(guiGraphics, rightX + 110, panelY + 184, 64, 18, this.originalRgb);

        guiGraphics.drawString(this.font, Component.translatable("iu.color_picker.presets"), rightX + 14, panelY + 208, 0xAEB8CC, false);

        int r = (this.currentRgb >> 16) & 0xFF;
        int g = (this.currentRgb >> 8) & 0xFF;
        int b = this.currentRgb & 0xFF;

        guiGraphics.drawString(this.font, Component.translatable("iu.color_picker.red_value", r), leftX + 14, panelY + 188, 0xFF8A8A, false);
        guiGraphics.drawString(this.font, Component.translatable("iu.color_picker.green_value", g), leftX + 58, panelY + 188, 0x8DFF9B, false);
        guiGraphics.drawString(this.font, Component.translatable("iu.color_picker.blue_value", b), leftX + 104, panelY + 188, 0x8EC5FF, false);

        guiGraphics.drawString(
                this.font,
                this.rainbowMode
                        ? Component.translatable("iu.color_picker.mode_rainbow")
                        : Component.translatable("iu.color_picker.mode_static"),
                leftX + 14,
                panelY + 208,
                this.rainbowMode ? 0x8DEBFF : 0xAEB8CC,
                false
        );

        guiGraphics.drawWordWrap(
                this.font,
                Component.translatable("iu.color_picker.tip_palette_preview"),
                leftX + 14,
                panelY + 226,
                LEFT_PANEL_W - 28,
                0x7F8AA3
        );
    }

    private void drawColorSwatch(GuiGraphics guiGraphics, int x, int y, int w, int h, int rgb) {
        ColorPickerRenderUtil.drawInsetPanel(guiGraphics, x, y, w, h, 0xFF12161D, 0xFF313949);
        ColorPickerRenderUtil.drawCheckerboard(guiGraphics, x + 1, y + 1, w - 2, h - 2);
        guiGraphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0xFF000000 | (rgb & 0xFFFFFF));
    }

    @Nonnull
    @Override
    public String getText(final int id, @Nonnull final String name, final float value) {
        return "";
    }

    @Override
    public void setEntryValue(final int id, final boolean value) {
    }

    @Override
    public void setEntryValue(final int id, final float value) {
    }

    @Override
    public void setEntryValue(final int id, @Nonnull final String value) {
    }

    @Override
    protected ResourceLocation getTexture() {
        return BACKGROUND;
    }
}