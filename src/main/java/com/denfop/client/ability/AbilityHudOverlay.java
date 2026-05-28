package com.denfop.client.ability;

import com.denfop.ability.AbilityClientState;
import com.denfop.ability.AbilityManager;
import com.denfop.ability.EnumPlayerAbility;
import com.denfop.ability.IPlayerAbility;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public final class AbilityHudOverlay implements LayeredDraw.Layer {

    private static final int PANEL_WIDTH = 188;
    private static final int PANEL_HEIGHT = 120;

    private static final int SLOT_SIZE = 46;
    private static final int SLOT_X_OFFSET = 12;
    private static final int SLOT_Y_OFFSET = 34;

    private static final int INFO_X_OFFSET = 72;
    private static final int INFO_Y_OFFSET = 37;

    private static final int STATUS_STRIP_X_OFFSET = 8;
    private static final int STATUS_STRIP_Y_OFFSET = 81;
    private static final int STATUS_STRIP_HEIGHT = 14;

    private static final int BAR_X_OFFSET = 12;
    private static final int BAR_Y_OFFSET = 104;
    private static final int BAR_HEIGHT = 8;

    private static final int SCREEN_MARGIN_RIGHT = 16;
    private static final int SCREEN_MARGIN_BOTTOM = 16;

    private static float HUD_SCALE = 0.5F;

    AbilityHudOverlay() {
    }

    public static float getHudScale() {
        return HUD_SCALE;
    }

    public static void setHudScale(final float scale) {
        HUD_SCALE = Mth.clamp(scale, 0.25F, 2.0F);
    }

    public void render(
            final GuiGraphics guiGraphics, DeltaTracker deltaTracker

    ) {
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        final ItemStack stack = mc.player.getMainHandItem();
        if (stack.isEmpty()) {
            return;
        }

        final IPlayerAbility ability = AbilityManager.getAbility(stack);
        if (ability == null) {
            return;
        }

        final EnumPlayerAbility type = ability.type();
        final int remainingTicks = AbilityClientState.getRemainingTicks(type);
        final float readyProgress = AbilityClientState.getReadyProgress(type);

        final int accent = type.getAccentColor();
        final int accentSoft = multiplyColor(accent, 0.70F);
        final int accentDark = multiplyColor(accent, 0.35F);

        final float time = mc.player.tickCount + partialTick;
        final float pulse = 0.75F + 0.25F * (0.5F + 0.5F * Mth.sin(time * 0.18F));
        final float slowPulse = 0.85F + 0.15F * (0.5F + 0.5F * Mth.sin(time * 0.08F));

        final boolean lowFood = !mc.player.isCreative()
                && mc.player.getFoodData().getFoodLevel() < Math.max(1, type.getMinimumFoodCost());

        final String title = getTitle(type);
        final String subtitle = getSubtitle(type);
        final String readyText = tr("iu.ability.status.ready");
        final String cooldownText = remainingTicks > 0 ? formatTicks(remainingTicks) : readyText;
        final String statusText = lowFood
                ? tr("iu.ability.status.low_food")
                : (remainingTicks > 0 ? tr("iu.ability.status.cooldown") : readyText);
        final int statusColor = lowFood ? 0xFFE26060 : (remainingTicks > 0 ? 0xFFE2B85F : accent);

        final float scale = HUD_SCALE;
        final int scaledWidth = Mth.ceil(PANEL_WIDTH * scale);
        final int scaledHeight = Mth.ceil(PANEL_HEIGHT * scale);

        final int baseX = screenWidth - scaledWidth - SCREEN_MARGIN_RIGHT;
        final int baseY = screenHeight - scaledHeight - SCREEN_MARGIN_BOTTOM;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(baseX, baseY, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);

        drawPanelShadow(guiGraphics, 0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        drawPanel(guiGraphics, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, accentDark, accentSoft, accent, slowPulse);
        drawHeader(guiGraphics, 0, 0, PANEL_WIDTH, title, subtitle, statusText, statusColor);

        final int slotX = SLOT_X_OFFSET;
        final int slotY = SLOT_Y_OFFSET;
        drawSlot(guiGraphics, slotX, slotY, SLOT_SIZE, accent, type.getPanelColor(), pulse);
        guiGraphics.renderItem(stack, slotX + 15, slotY + 15);

        drawRingArc(
                guiGraphics.pose(),
                slotX + SLOT_SIZE / 2.0F,
                slotY + SLOT_SIZE / 2.0F,
                20.0F,
                23.0F,
                1.0F,
                -90.0F,
                0x44242B36
        );

        drawRingArc(
                guiGraphics.pose(),
                slotX + SLOT_SIZE / 2.0F,
                slotY + SLOT_SIZE / 2.0F,
                20.0F,
                23.0F,
                readyProgress,
                -90.0F,
                withScaledAlpha(accent, remainingTicks > 0 ? 0.95F : pulse)
        );

        if (remainingTicks > 0) {
            drawCooldownShade(
                    guiGraphics.pose(),
                    slotX + SLOT_SIZE / 2.0F,
                    slotY + SLOT_SIZE / 2.0F,
                    18.0F,
                    remainingTicks / (float) Math.max(1, type.getDefaultCooldownTicks()),
                    0x98000000
            );
        } else {
            drawReadyHalo(
                    guiGraphics.pose(),
                    slotX + SLOT_SIZE / 2.0F,
                    slotY + SLOT_SIZE / 2.0F,
                    18.5F,
                    withScaledAlpha(accent, 0.16F * pulse)
            );
        }

        drawInfoBlock(guiGraphics, INFO_X_OFFSET, INFO_Y_OFFSET, cooldownText, accent, lowFood, readyText);

        final String bottomText = lowFood
                ? tr("iu.ability.bottom.need_food")
                : (remainingTicks > 0 ? tr("iu.ability.bottom.recovering") : tr("iu.ability.bottom.press_activate"));

        drawBottomStatusStrip(
                guiGraphics,
                STATUS_STRIP_X_OFFSET + 3,
                STATUS_STRIP_Y_OFFSET + 5,
                PANEL_WIDTH - STATUS_STRIP_X_OFFSET * 2 - 6,
                STATUS_STRIP_HEIGHT,
                bottomText,
                0xFFD0DAE5
        );

        drawSegmentedBar(
                guiGraphics,
                BAR_X_OFFSET,
                BAR_Y_OFFSET,
                PANEL_WIDTH - BAR_X_OFFSET * 2,
                BAR_HEIGHT,
                readyProgress,
                accent,
                remainingTicks <= 0 ? pulse : 1.0F
        );

        guiGraphics.pose().popPose();
    }

    private static void drawPanelShadow(final GuiGraphics guiGraphics, final int x, final int y, final int w, final int h) {
        guiGraphics.fill(x - 7, y - 7, x + w + 7, y + h + 7, 0x18000000);
        guiGraphics.fill(x - 4, y - 4, x + w + 4, y + h + 4, 0x26000000);
        guiGraphics.fill(x - 2, y - 2, x + w + 2, y + h + 2, 0x36000000);
    }

    private static void drawPanel(
            final GuiGraphics guiGraphics,
            final int x,
            final int y,
            final int w,
            final int h,
            final int accentDark,
            final int accentSoft,
            final int accent,
            final float pulse
    ) {
        guiGraphics.fill(x, y, x + w, y + h, 0xEE0A0F15);
        guiGraphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0xF0121922);
        guiGraphics.fill(x + 2, y + 2, x + w - 2, y + h - 2, 0xF017202B);

        guiGraphics.fill(x, y, x + w, y + 2, accentSoft);
        guiGraphics.fill(x, y, x + 2, y + h, accentDark);
        guiGraphics.fill(x + w - 2, y, x + w, y + h, 0xCC10161D);
        guiGraphics.fill(x, y + h - 2, x + w, y + h, 0xCC0A0F14);

        guiGraphics.fill(x + 8, y + 27, x + w - 8, y + 28, withScaledAlpha(accent, 0.25F + 0.08F * pulse));

        drawCorner(guiGraphics, x + 4, y + 4, accentSoft);
        drawCorner(guiGraphics, x + w - 9, y + 4, accentSoft);
        drawCorner(guiGraphics, x + 4, y + h - 9, accentDark);
        drawCorner(guiGraphics, x + w - 9, y + h - 9, accentDark);
    }

    private static void drawHeader(
            final GuiGraphics guiGraphics,
            final int x,
            final int y,
            final int panelWidth,
            final String title,
            final String subtitle,
            final String statusText,
            final int statusColor
    ) {
        final Minecraft mc = Minecraft.getInstance();

        final int badgeHeight = 14;
        final int badgeWidth = Math.max(34, mc.font.width(statusText) + 14);
        final int badgeX = x + panelWidth - badgeWidth - 10;
        final int badgeY = y + 7;

        guiGraphics.fill(badgeX, badgeY, badgeX + badgeWidth, badgeY + badgeHeight, 0xCC0E151D);

        guiGraphics.fill(badgeX, badgeY, badgeX + badgeWidth, badgeY + 1, statusColor);
        guiGraphics.fill(badgeX, badgeY + badgeHeight - 1, badgeX + badgeWidth, badgeY + badgeHeight, statusColor);
        guiGraphics.fill(badgeX, badgeY, badgeX + 1, badgeY + badgeHeight, statusColor);
        guiGraphics.fill(badgeX + badgeWidth - 1, badgeY, badgeX + badgeWidth, badgeY + badgeHeight, statusColor);

        if (badgeWidth > 4 && badgeHeight > 4) {
            guiGraphics.fill(
                    badgeX + 1,
                    badgeY + 1,
                    badgeX + badgeWidth - 1,
                    badgeY + 2,
                    withScaledAlpha(0xFFFFFFFF, 0.18F)
            );
        }

        drawScaledCenteredText(guiGraphics, statusText, badgeX + 2, badgeY + 3, badgeWidth - 4, 0xFFF3F8FD);

        final int titleX = x + 10;
        final int titleY = y + 8;
        final int subtitleY = y + 18;
        final int maxTextWidth = badgeX - titleX - 8;

        drawScaledLeftText(guiGraphics, title, titleX, titleY, maxTextWidth, 0xFFF1F6FB);
        drawScaledLeftText(guiGraphics, subtitle, titleX, subtitleY, maxTextWidth, 0xFF93A6B8);
    }

    private static void drawInfoBlock(
            final GuiGraphics guiGraphics,
            final int x,
            final int y,
            final String cooldownText,
            final int accent,
            final boolean lowFood,
            final String readyText
    ) {
        drawScaledLeftText(guiGraphics, tr("iu.ability.label.cooldown"), x, y, 56, 0xFF97A5B7);
        drawScaledLeftText(guiGraphics, cooldownText, x + 58, y, 56, cooldownText.equals(readyText) ? accent : 0xFFF2D39A);

        drawScaledLeftText(guiGraphics, tr("iu.ability.label.food"), x, y + 14, 56, 0xFF97A5B7);
        drawScaledLeftText(guiGraphics, tr("iu.ability.label.food_range"), x + 58, y + 14, 56, lowFood ? 0xFFFF8B8B : 0xFFE9EEF5);

        drawScaledLeftText(guiGraphics, tr("iu.ability.label.key"), x, y + 28, 56, 0xFF97A5B7);
        drawScaledLeftText(guiGraphics, tr("iu.ability.label.activate_key"), x + 58, y + 28, 56, accent);
    }

    private static void drawSlot(
            final GuiGraphics guiGraphics,
            final int x,
            final int y,
            final int size,
            final int accent,
            final int innerColor,
            final float pulse
    ) {
        guiGraphics.fill(x - 2, y - 2, x + size + 2, y + size + 2, 0xAA05080C);
        guiGraphics.fill(x - 1, y - 1, x + size + 1, y + size + 1, withScaledAlpha(accent, 0.35F));
        guiGraphics.fill(x, y, x + size, y + size, 0xE010151C);
        guiGraphics.fill(x + 2, y + 2, x + size - 2, y + size - 2, innerColor);
        guiGraphics.fill(x + 2, y + size - 4, x + size - 2, y + size - 2, 0x66060A10);
    }

    private static void drawBottomStatusStrip(
            final GuiGraphics guiGraphics,
            final int x,
            final int y,
            final int width,
            final int height,
            final String text,
            final int textColor
    ) {
        final int borderColor = 0xFF3A4756;
        final int fillColor = 0x8A0B1118;

        guiGraphics.fill(x, y, x + width, y + height, fillColor);

        guiGraphics.fill(x, y, x + width, y + 1, borderColor);
        guiGraphics.fill(x, y + height - 1, x + width, y + height, borderColor);
        guiGraphics.fill(x, y, x + 1, y + height, borderColor);
        guiGraphics.fill(x + width - 1, y, x + width, y + height, borderColor);

        if (width > 4 && height > 4) {
            guiGraphics.fill(
                    x + 1,
                    y + 1,
                    x + width - 1,
                    y + 2,
                    withScaledAlpha(0xFFFFFFFF, 0.14F)
            );
        }

        drawScaledLeftText(guiGraphics, text, x + 4, y + 3, width - 8, textColor);
    }

    private static void drawSegmentedBar(
            final GuiGraphics guiGraphics,
            final int x,
            final int y,
            final int w,
            final int h,
            final float progress,
            final int accent,
            final float glow
    ) {
        guiGraphics.fill(x - 1, y - 1, x + w + 1, y + h + 1, 0xAA04070B);
        guiGraphics.fill(x, y, x + w, y + h, 0xEE111922);

        final int segments = 14;
        final int spacing = 2;

        final int totalSpacing = (segments - 1) * spacing;
        final int usableWidth = w - totalSpacing;

        final int baseSegmentWidth = usableWidth / segments;
        final int remainder = usableWidth % segments;

        final float clampedProgress = Mth.clamp(progress, 0.0F, 1.0F);
        final float filledSegments = clampedProgress * segments;

        int cursorX = x;

        for (int i = 0; i < segments; i++) {
            final int currentSegmentWidth = baseSegmentWidth + (i < remainder ? 1 : 0);
            final int sx = cursorX;
            final int ex = sx + currentSegmentWidth;

            guiGraphics.fill(sx, y + 1, ex, y + h - 1, 0xCC091018);

            final float part = Mth.clamp(filledSegments - i, 0.0F, 1.0F);
            if (part > 0.0F) {
                final int fillWidth = Math.max(1, Math.min(currentSegmentWidth, (int) Math.ceil(currentSegmentWidth * part)));
                final int fillColor = withScaledAlpha(accent, 0.88F + 0.12F * glow);

                guiGraphics.fill(sx, y + 1, sx + fillWidth, y + h - 1, fillColor);
                guiGraphics.fill(sx, y + 1, Math.min(ex, sx + fillWidth), y + 2, withScaledAlpha(0xFFFFFFFF, 0.18F));
            }

            cursorX = ex + spacing;
        }
    }

    private static void drawReadyHalo(
            final PoseStack poseStack,
            final float centerX,
            final float centerY,
            final float radius,
            final int color
    ) {
        drawRingArc(poseStack, centerX, centerY, radius, radius + 3.0F, 1.0F, -90.0F, color);
    }

    private static void drawCooldownShade(
            final PoseStack poseStack,
            final float centerX,
            final float centerY,
            final float radius,
            final float fillFraction,
            final int color
    ) {
        if (fillFraction <= 0.0F) {
            return;
        }

        final float clamped = Mth.clamp(fillFraction, 0.0F, 1.0F);
        final int segments = Math.max(12, Mth.ceil(60.0F * clamped));
        final float startAngle = -90.0F;
        final float endAngle = startAngle + 360.0F * clamped;

        final float a = ((color >> 24) & 255) / 255.0F;
        final float r = ((color >> 16) & 255) / 255.0F;
        final float g = ((color >> 8) & 255) / 255.0F;
        final float b = (color & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        final BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);


        final var matrix = poseStack.last().pose();
        buffer.addVertex(matrix, centerX, centerY, 0.0F).setColor(r, g, b, a);

        for (int i = 0; i <= segments; i++) {
            final float angle = startAngle + (endAngle - startAngle) * (i / (float) segments);
            final float radians = angle * ((float) Math.PI / 180.0F);
            final float px = centerX + Mth.cos(radians) * radius;
            final float py = centerY + Mth.sin(radians) * radius;
            buffer.addVertex(matrix, px, py, 0.0F).setColor(r, g, b, a);
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private static void drawRingArc(
            final PoseStack poseStack,
            final float centerX,
            final float centerY,
            final float innerRadius,
            final float outerRadius,
            final float fraction,
            final float startAngleDeg,
            final int color
    ) {
        final float clamped = Mth.clamp(fraction, 0.0F, 1.0F);
        if (clamped <= 0.0F) {
            return;
        }

        final float endAngle = startAngleDeg + 360.0F * clamped;
        final int segments = Math.max(18, (int) (90.0F * clamped));

        final float a = ((color >> 24) & 255) / 255.0F;
        final float r = ((color >> 16) & 255) / 255.0F;
        final float g = ((color >> 8) & 255) / 255.0F;
        final float b = (color & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        final BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        final var matrix = poseStack.last().pose();

        for (int i = 0; i <= segments; i++) {
            final float angle = startAngleDeg + (endAngle - startAngleDeg) * (i / (float) segments);
            final float radians = angle * ((float) Math.PI / 180.0F);
            final float cos = Mth.cos(radians);
            final float sin = Mth.sin(radians);

            buffer.addVertex(matrix, centerX + cos * outerRadius, centerY + sin * outerRadius, 0.0F).setColor(r, g, b, a);
            buffer.addVertex(matrix, centerX + cos * innerRadius, centerY + sin * innerRadius, 0.0F).setColor(r, g, b, a);
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private static void drawCorner(final GuiGraphics guiGraphics, final int x, final int y, final int color) {
        guiGraphics.fill(x, y, x + 4, y + 1, color);
        guiGraphics.fill(x, y, x + 1, y + 4, color);
    }

    private static void drawScaledLeftText(
            final GuiGraphics guiGraphics,
            final String text,
            final int x,
            final int y,
            final int maxWidth,
            final int color
    ) {
        final Minecraft mc = Minecraft.getInstance();
        final int textWidth = mc.font.width(text);

        if (textWidth <= maxWidth || textWidth <= 0) {
            guiGraphics.drawString(mc.font, text, x, y, color, false);
            return;
        }

        final float scale = Math.max(0.60F, Math.min(1.0F, maxWidth / (float) textWidth));

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(mc.font, text, 0, 0, color, false);
        guiGraphics.pose().popPose();
    }

    private static void drawScaledCenteredText(
            final GuiGraphics guiGraphics,
            final String text,
            final int x,
            final int y,
            final int maxWidth,
            final int color
    ) {
        final Minecraft mc = Minecraft.getInstance();
        final int textWidth = mc.font.width(text);

        if (textWidth <= maxWidth || textWidth <= 0) {
            final int drawX = x + (maxWidth - textWidth) / 2;
            guiGraphics.drawString(mc.font, text, drawX, y, color, false);
            return;
        }

        final float scale = Math.max(0.60F, Math.min(1.0F, maxWidth / (float) textWidth));
        final float scaledWidth = textWidth * scale;
        final float drawX = x + (maxWidth - scaledWidth) / 2.0F;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(drawX, y, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(mc.font, text, 0, 0, color, false);
        guiGraphics.pose().popPose();
    }

    private static String getTitle(final EnumPlayerAbility type) {
        return switch (type) {
            case PICKAXE_VEIN -> tr("iu.ability.pickaxe_vein.title");
            case AXE_TREE -> tr("iu.ability.axe_tree.title");
        };
    }

    private static String getSubtitle(final EnumPlayerAbility type) {
        return switch (type) {
            case PICKAXE_VEIN -> tr("iu.ability.pickaxe_vein.subtitle");
            case AXE_TREE -> tr("iu.ability.axe_tree.subtitle");
        };
    }

    private static String formatTicks(final int ticks) {
        if (ticks <= 0) {
            return tr("iu.ability.status.ready");
        }

        final int totalSeconds = ticks / 20;
        final int minutes = totalSeconds / 60;
        final int seconds = totalSeconds % 60;

        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        }

        return String.format("%.1fs", ticks / 20.0F);
    }

    private static String tr(final String key) {
        return Component.translatable(key).getString();
    }

    private static int multiplyColor(final int color, final float factor) {
        final int a = (color >> 24) & 255;
        final int r = Mth.clamp((int) (((color >> 16) & 255) * factor), 0, 255);
        final int g = Mth.clamp((int) (((color >> 8) & 255) * factor), 0, 255);
        final int b = Mth.clamp((int) ((color & 255) * factor), 0, 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int withScaledAlpha(final int color, final float scale) {
        final int alpha = Mth.clamp((int) (((color >> 24) & 255) * scale), 0, 255);
        return (alpha << 24) | (color & 0x00FFFFFF);
    }
}