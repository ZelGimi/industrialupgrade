package com.denfop.client.space;

import com.denfop.Constants;
import com.denfop.items.space.teleport.SpaceTeleportFxType;
import com.denfop.items.space.teleport.SpaceTeleportPhase;
import com.denfop.items.space.teleport.SpaceTeleportReason;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class SpaceTeleportClientOverlays {

    private static final LayeredDraw.Layer OVERLAY = (graphics, deltaTracker) -> {
        SpaceTeleportClientState state = SpaceTeleportClientState.INSTANCE;
        if (!state.isActive() && !state.hasFx()) {
            return;
        }

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        if (state.hasFx()) {
            renderFx(graphics, screenWidth, screenHeight, state);
        }

        renderHud(graphics, screenWidth, screenHeight, state);
    };

    private SpaceTeleportClientOverlays() {
    }

    @SubscribeEvent
    public static void register(final RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "space_teleport_overlay"),
                OVERLAY
        );
    }

    private static void renderHud(final GuiGraphics graphics, final int w, final int h, final SpaceTeleportClientState state) {
        int panelX = 12;
        int panelY = h - 80;
        int panelW = 252;
        int panelH = 64;

        graphics.fillGradient(panelX, panelY, panelX + panelW, panelY + panelH, 0xC0121C2C, 0xD0121824);
        graphics.fill(panelX, panelY, panelX + panelW, panelY + 1, 0xFF47E2FF);

        float chargeRatio = state.getMaxCharge() <= 0D ? 0F : (float) (state.getCharge() / state.getMaxCharge());
        boolean warning = !state.isItemPresent()
                || state.getReason() == SpaceTeleportReason.LOW_CHARGE
                || chargeRatio <= 0.15F
                || state.getPhase() == SpaceTeleportPhase.RETURN_PREP;

        float circleCx = panelX + 28F;
        float circleCy = panelY + 32F;

        renderCircularChargeBar(
                graphics,
                circleCx,
                circleCy,
                18F,
                13F,
                chargeRatio,
                warning
        );

        String percent = (int) (Mth.clamp(chargeRatio, 0F, 1F) * 100F) + "%";
        graphics.drawString(
                Minecraft.getInstance().font,
                percent,
                (int) (circleCx - Minecraft.getInstance().font.width(percent) / 2F),
                (int) (circleCy - 4),
                0xE8FCFF,
                false
        );

        int textX = panelX + 56;

        graphics.drawString(Minecraft.getInstance().font, Localization.translate("iu.space.tp.hud.title"), textX, panelY + 6, 0xFFFFFF, false);

        String body = state.getBodyName().isEmpty()
                ? Localization.translate("iu.space.tp.none")
                : Localization.translate("iu.body." + state.getBodyName());

        graphics.drawString(
                Minecraft.getInstance().font,
                Localization.translate("iu.space.tp.hud.body") + ": " + body,
                textX,
                panelY + 19,
                0xBFE8FF,
                false
        );

        graphics.drawString(
                Minecraft.getInstance().font,
                Localization.translate("iu.space.tp.hud.charge") + ": " + ModUtils.getString(state.getCharge()) + " EF",
                textX,
                panelY + 31,
                0xFFFFFF,
                false
        );

        String bottomLine;
        int color;
        if (state.getPhase() == SpaceTeleportPhase.RETURN_PREP) {
            bottomLine = Localization.translate("iu.space.tp.hud.return_in") + ": " + formatTicks(state.getCountdownTicks());
            color = 0xFF8E8E;
        } else if (!state.isItemPresent()) {
            bottomLine = Localization.translate("iu.space.tp.reason.item_missing");
            color = 0xFF7070;
        } else if (state.getReason() == SpaceTeleportReason.LOW_CHARGE) {
            bottomLine = Localization.translate("iu.space.tp.reason.low_charge");
            color = 0xFF7070;
        } else {
            bottomLine = Localization.translate("iu.space.tp.hud.critical_in") + ": " + formatTicks(state.getTicksUntilCritical());
            color = state.getTicksUntilCritical() <= 20L * 30L ? 0xFFD560 : 0x8CFF8C;
        }

        graphics.drawString(Minecraft.getInstance().font, bottomLine, textX, panelY + 44, color, false);
        renderHudAccentBars(graphics, panelX + panelW - 58, panelY + 19, warning);
    }

    private static void renderHudAccentBars(final GuiGraphics graphics, final int x, final int y, final boolean warning) {
        long time = System.currentTimeMillis();
        float pulse = 0.7F + 0.3F * Mth.sin((time % 1600L) / 1600.0F * (float) Math.PI * 2F);
        int color = warning ? argb((int) (140 * pulse), 255, 110, 110) : argb((int) (120 * pulse), 90, 230, 255);

        for (int i = 0; i < 4; i++) {
            graphics.fill(x + i * 8, y, x + i * 8 + 4, y + 22, color);
        }
    }

    private static void renderCircularChargeBar(
            final GuiGraphics graphics,
            final float cx,
            final float cy,
            final float outerRadius,
            final float innerRadius,
            final float progress,
            final boolean warning
    ) {
        float clamped = Mth.clamp(progress, 0F, 1F);

        drawDonutArc(
                graphics.pose().last().pose(),
                cx,
                cy,
                innerRadius,
                outerRadius,
                -90F,
                360F,
                argb(90, 20, 40, 58)
        );

        drawDonutArc(
                graphics.pose().last().pose(),
                cx,
                cy,
                innerRadius,
                outerRadius,
                -90F,
                360F * clamped,
                warning ? argb(220, 255, 110, 110) : argb(225, 72, 232, 255)
        );

        float pulse = 0.75F + 0.25F * Mth.sin((System.currentTimeMillis() % 1800L) / 1800.0F * (float) Math.PI * 2F);

        drawDonutArc(
                graphics.pose().last().pose(),
                cx,
                cy,
                outerRadius + 1.5F,
                outerRadius + 3.5F,
                -90F,
                360F,
                warning ? argb((int) (46 * pulse), 255, 100, 100) : argb((int) (34 * pulse), 90, 225, 255)
        );

        drawSoftDisc(
                graphics.pose().last().pose(),
                cx,
                cy,
                innerRadius - 2.0F,
                argb(24, 30, 60, 90),
                argb(0, 30, 60, 90)
        );
    }

    private static void renderFx(final GuiGraphics graphics, final int w, final int h, final SpaceTeleportClientState state) {
        SpaceTeleportFxType type = state.getFxType();
        float progress = state.getFxProgress();
        long time = System.currentTimeMillis();

        switch (type) {
            case OUTBOUND_CHARGE -> renderForwardCharge(graphics, w, h, progress, time);
            case RETURN_CHARGE -> renderReturnCharge(graphics, w, h, progress, time);
            case OUTBOUND_TUNNEL -> renderForwardTunnel(graphics, w, h, progress, time);
            case RETURN_TUNNEL -> renderReturnTunnel(graphics, w, h, progress, time);
            case ARRIVAL -> renderForwardArrival(graphics, w, h, progress, time);
            case RETURN_ARRIVAL -> renderReturnArrival(graphics, w, h, progress, time);
            default -> {
            }
        }
    }

    private static void renderForwardCharge(final GuiGraphics graphics, final int w, final int h, final float progress, final long time) {
        float eased = easeInOutCubic(progress);
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float base = Math.min(w, h);

        graphics.fillGradient(0, 0, w, h, argb((int) (90 + 85 * eased), 4, 10, 20), argb((int) (70 + 70 * eased), 1, 4, 9));
        renderNoiseField(graphics, w, h, time, 110 + (int) (90 * eased), 0.95f);
        renderScanlines(graphics, w, h, 0.08f + 0.08f * eased);
        renderChromaticAberration(graphics, w, h, 0.22f + 0.20f * eased, false);
        renderEdgeDistortion(graphics, w, h, time, 0.18f + 0.22f * eased, false, argb(58, 90, 230, 255));

        float rotation = (float) (time * 0.026);
        float pulse = 0.72f + 0.28f * Mth.sin((float) (time * 0.015));

        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * (0.07f + 0.045f * pulse), argb(95, 70, 230, 255), argb(0, 70, 230, 255));
        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * (0.14f + 0.06f * eased), argb(32, 35, 140, 255), argb(0, 35, 140, 255));

        renderSegmentedRingStack(graphics.pose().last().pose(), cx, cy, base * 0.12f, 6, rotation, false, argb(95, 90, 240, 255));
        renderOrbitFragments(graphics.pose().last().pose(), cx, cy, base * 0.22f, time, 20, false);
        renderRadialFlares(graphics.pose().last().pose(), cx, cy, base * 0.08f, base * 0.34f, 26, rotation, argb(60, 120, 240, 255));
        renderTechGlyphOrbit(graphics, cx, cy, base * 0.27f, time, 14, false, argb(88, 150, 245, 255), argb(54, 110, 200, 255));

        if (progress > 0.55f) {
            float burst = Mth.clamp((progress - 0.55f) / 0.45f, 0f, 1f);
            renderShardBurst(
                    graphics.pose().last().pose(),
                    cx,
                    cy,
                    base * (0.10f + burst * 0.16f),
                    base * (0.28f + burst * 0.26f),
                    14,
                    rotation * 1.6f,
                    false,
                    argb((int) (110 * (1f - burst * 0.35f)), 160, 248, 255)
            );
        }

        drawDiamondPulse(graphics.pose().last().pose(), cx, cy, base * (0.08f + 0.03f * pulse), argb(58, 160, 245, 255));
        drawCrossFlare(graphics, cx, cy, base * (0.10f + 0.02f * pulse), 1.0f);
    }

    private static void renderReturnCharge(final GuiGraphics graphics, final int w, final int h, final float progress, final long time) {
        float eased = easeInOutCubic(progress);
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float base = Math.min(w, h);

        graphics.fillGradient(0, 0, w, h, argb((int) (110 + 80 * eased), 12, 4, 18), argb((int) (90 + 65 * eased), 5, 1, 8));
        renderNoiseField(graphics, w, h, time, 130 + (int) (80 * eased), 1.05f);
        renderScanlines(graphics, w, h, 0.11f + 0.05f * eased);
        renderChromaticAberration(graphics, w, h, 0.28f + 0.20f * eased, true);
        renderEdgeDistortion(graphics, w, h, time, 0.22f + 0.25f * eased, true, argb(62, 255, 100, 150));

        float rotation = (float) (-time * 0.032);
        float pulse = 0.72f + 0.28f * Mth.sin((float) (time * 0.021));

        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * (0.05f + 0.03f * pulse), argb(110, 255, 90, 120), argb(0, 255, 90, 120));
        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * (0.11f + 0.04f * eased), argb(35, 160, 50, 90), argb(0, 160, 50, 90));

        renderSegmentedRingStack(graphics.pose().last().pose(), cx, cy, base * 0.18f, 5, rotation, true, argb(84, 255, 105, 150));
        renderConvergingShards(
                graphics.pose().last().pose(),
                cx,
                cy,
                base * 0.64f,
                base * (0.12f + eased * 0.10f),
                18,
                rotation,
                argb(90, 255, 110, 150)
        );

        renderOrbitFragments(graphics.pose().last().pose(), cx, cy, base * 0.19f, time, 16, true);
        renderAngularCracks(graphics.pose().last().pose(), cx, cy, base * (0.12f + 0.06f * eased), rotation, argb(80, 255, 140, 170));
        renderTechGlyphOrbit(graphics, cx, cy, base * 0.23f, time, 12, true, argb(90, 255, 120, 160), argb(52, 190, 80, 120));

        if (progress > 0.66f) {
            float snap = Mth.clamp((progress - 0.66f) / 0.34f, 0f, 1f);
            drawFullRing(
                    graphics.pose().last().pose(),
                    cx,
                    cy,
                    base * (0.09f + snap * 0.08f),
                    base * (0.11f + snap * 0.10f),
                    argb((int) (140 * (1f - snap)), 255, 200, 220)
            );
        }

        drawDiamondPulse(graphics.pose().last().pose(), cx, cy, base * (0.06f + 0.02f * pulse), argb(70, 255, 120, 155));
        drawCrossFlare(graphics, cx, cy, base * (0.08f + 0.02f * pulse), 0.85f);
    }

    private static void renderForwardTunnel(final GuiGraphics graphics, final int w, final int h, final float progress, final long time) {
        float eased = easeInOutCubic(progress);
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float base = Math.min(w, h);

        int veilA = (int) (150 + 70 * (1f - Math.abs(progress - 0.5f) * 1.7f));
        graphics.fillGradient(0, 0, w, h, argb(veilA, 2, 5, 10), argb((int) (veilA * 0.92f), 0, 1, 5));

        renderNoiseField(graphics, w, h, time, 150 + (int) (120 * eased), 1.1f);
        renderScanlines(graphics, w, h, 0.06f);
        renderChromaticAberration(graphics, w, h, 0.42f + 0.26f * eased, false);
        renderEdgeDistortion(graphics, w, h, time, 0.35f + 0.35f * eased, false, argb(82, 100, 235, 255));

        float rotationA = (float) (time * 0.022);
        float rotationB = (float) (-time * 0.031);

        renderSegmentedRingStack(graphics.pose().last().pose(), cx, cy, base * 0.10f, 7, rotationA, false, argb(88, 72, 220, 255));
        renderTunnelStreaks(graphics.pose().last().pose(), cx, cy, base, rotationA, false, argb(92, 115, 242, 255));
        renderTunnelStreaks(graphics.pose().last().pose(), cx, cy, base * 0.86f, rotationB, false, argb(54, 190, 255, 255));

        for (int helix = 0; helix < 3; helix++) {
            drawHelixBand(
                    graphics.pose().last().pose(),
                    cx,
                    cy,
                    base * 0.18f,
                    base * 0.66f,
                    rotationB + helix * 120f,
                    1f,
                    argb(78, 118, 232, 255)
            );
        }

        renderTechGlyphOrbit(graphics, cx, cy, base * 0.38f, time, 18, false, argb(80, 140, 245, 255), argb(44, 90, 210, 255));

        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * 0.11f, argb(100, 150, 250, 255), argb(0, 150, 250, 255));
        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * 0.036f, argb(185, 230, 255, 255), argb(0, 230, 255, 255));

        float crackProgress = 1f - Math.abs(progress - 0.5f) * 2f;
        crackProgress = Mth.clamp(crackProgress, 0f, 1f);
        if (crackProgress > 0f) {
            renderSpaceRupture(graphics.pose().last().pose(), cx, cy, base * (0.12f + 0.05f * crackProgress), rotationA, crackProgress, false);
        }

        if (progress > 0.56f) {
            float shock = Mth.clamp((progress - 0.56f) / 0.44f, 0f, 1f);
            drawFullRing(
                    graphics.pose().last().pose(),
                    cx,
                    cy,
                    base * (0.18f + shock * 0.55f),
                    base * (0.205f + shock * 0.56f),
                    argb((int) (120 * (1f - shock)), 190, 250, 255)
            );
        }

        drawCrossFlare(graphics, cx, cy, base * 0.13f, 1.25f);
    }

    private static void renderReturnTunnel(final GuiGraphics graphics, final int w, final int h, final float progress, final long time) {
        float eased = easeInOutCubic(progress);
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float base = Math.min(w, h);

        int veilA = (int) (165 + 55 * (1f - Math.abs(progress - 0.5f) * 1.8f));
        graphics.fillGradient(0, 0, w, h, argb(veilA, 10, 2, 8), argb((int) (veilA * 0.92f), 4, 0, 4));

        renderNoiseField(graphics, w, h, time, 150 + (int) (110 * eased), 1.05f);
        renderScanlines(graphics, w, h, 0.09f);
        renderChromaticAberration(graphics, w, h, 0.46f + 0.28f * eased, true);
        renderEdgeDistortion(graphics, w, h, time, 0.40f + 0.35f * eased, true, argb(85, 255, 95, 140));

        float rotationA = (float) (-time * 0.026);
        float rotationB = (float) (time * 0.038);

        renderSegmentedRingStack(graphics.pose().last().pose(), cx, cy, base * 0.14f, 6, rotationA, true, argb(86, 255, 105, 150));
        renderTunnelStreaks(graphics.pose().last().pose(), cx, cy, base, rotationA, true, argb(85, 255, 115, 150));
        renderTunnelStreaks(graphics.pose().last().pose(), cx, cy, base * 0.84f, rotationB, true, argb(52, 255, 180, 210));

        renderConvergingShards(
                graphics.pose().last().pose(),
                cx,
                cy,
                base * 0.86f,
                base * (0.07f + 0.06f * eased),
                28,
                rotationB,
                argb(78, 255, 135, 170)
        );

        for (int helix = 0; helix < 3; helix++) {
            drawHelixBand(
                    graphics.pose().last().pose(),
                    cx,
                    cy,
                    base * 0.16f,
                    base * 0.56f,
                    rotationA + helix * 120f,
                    -1f,
                    argb(58, 255, 110, 160)
            );
        }

        renderTechGlyphOrbit(graphics, cx, cy, base * 0.31f, time, 16, true, argb(90, 255, 120, 160), argb(50, 200, 80, 120));

        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * 0.08f, argb(105, 255, 120, 160), argb(0, 255, 120, 160));
        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * 0.026f, argb(170, 255, 220, 240), argb(0, 255, 220, 240));

        float crackProgress = 1f - Math.abs(progress - 0.5f) * 2f;
        crackProgress = Mth.clamp(crackProgress, 0f, 1f);
        if (crackProgress > 0f) {
            renderSpaceRupture(graphics.pose().last().pose(), cx, cy, base * (0.10f + 0.05f * crackProgress), rotationA, crackProgress, true);
        }

        drawCrossFlare(graphics, cx, cy, base * 0.11f, 1.05f);
    }

    private static void renderForwardArrival(final GuiGraphics graphics, final int w, final int h, final float progress, final long time) {
        float eased = easeOutCubic(progress);
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float base = Math.min(w, h);

        int veilA = (int) (165 * (1f - eased));
        graphics.fillGradient(0, 0, w, h, argb(veilA, 6, 12, 20), argb((int) (veilA * 0.78f), 2, 6, 10));

        renderNoiseField(graphics, w, h, time, 120 - (int) (70 * eased), 0.85f);
        renderScanlines(graphics, w, h, 0.12f * (1f - eased));
        renderChromaticAberration(graphics, w, h, 0.28f * (1f - eased), false);
        renderEdgeDistortion(graphics, w, h, time, 0.22f * (1f - eased), false, argb(48, 105, 235, 255));

        renderSegmentedRingStack(graphics.pose().last().pose(), cx, cy, base * 0.12f, 4, (float) (time * 0.012), false, argb((int) (82 * (1f - eased)), 110, 240, 255));
        renderRadialFlares(graphics.pose().last().pose(), cx, cy, base * 0.08f, base * (0.20f + 0.16f * (1f - eased)), 20, (float) (time * 0.014), argb((int) (58 * (1f - eased)), 105, 235, 255));

        float gridA = 1f - eased;
        if (gridA > 0f) {
            renderStabilizationGrid(graphics, w, h, gridA);
        }

        renderTechGlyphOrbit(graphics, cx, cy, base * (0.22f + 0.08f * (1f - eased)), time, 10, false, argb((int) (58 * (1f - eased)), 120, 240, 255), argb((int) (34 * (1f - eased)), 90, 180, 220));

        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * (0.16f - 0.08f * eased), argb((int) (70 * (1f - eased)), 100, 235, 255), argb(0, 100, 235, 255));
        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * (0.05f + 0.01f * (1f - eased)), argb((int) (145 * (1f - eased)), 230, 255, 255), argb(0, 230, 255, 255));

        drawDiamondPulse(graphics.pose().last().pose(), cx, cy, base * (0.07f + 0.01f * (1f - eased)), argb((int) (52 * (1f - eased)), 150, 245, 255));
        drawCrossFlare(graphics, cx, cy, base * (0.10f + 0.02f * (1f - eased)), 1.0f - eased * 0.25f);
    }

    private static void renderReturnArrival(final GuiGraphics graphics, final int w, final int h, final float progress, final long time) {
        float eased = easeOutCubic(progress);
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float base = Math.min(w, h);

        int veilA = (int) (180 * (1f - eased));
        graphics.fillGradient(0, 0, w, h, argb(veilA, 12, 4, 10), argb((int) (veilA * 0.80f), 5, 1, 4));

        renderNoiseField(graphics, w, h, time, 130 - (int) (70 * eased), 0.95f);
        renderScanlines(graphics, w, h, 0.14f * (1f - eased));
        renderChromaticAberration(graphics, w, h, 0.30f * (1f - eased), true);
        renderEdgeDistortion(graphics, w, h, time, 0.24f * (1f - eased), true, argb(52, 255, 95, 140));

        renderSegmentedRingStack(graphics.pose().last().pose(), cx, cy, base * 0.10f, 4, (float) (-time * 0.013), true, argb((int) (86 * (1f - eased)), 255, 110, 150));
        renderConvergingShards(
                graphics.pose().last().pose(),
                cx,
                cy,
                base * (0.34f - 0.16f * eased),
                base * 0.06f,
                14,
                (float) (-time * 0.010),
                argb((int) (52 * (1f - eased)), 255, 125, 165)
        );

        renderSnapLines(graphics.pose().last().pose(), cx, cy, base * (0.12f + 0.18f * (1f - eased)), 12, argb((int) (64 * (1f - eased)), 255, 170, 200));
        renderTechGlyphOrbit(graphics, cx, cy, base * (0.18f + 0.06f * (1f - eased)), time, 8, true, argb((int) (60 * (1f - eased)), 255, 130, 170), argb((int) (35 * (1f - eased)), 200, 90, 120));

        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * (0.12f - 0.05f * eased), argb((int) (72 * (1f - eased)), 255, 120, 170), argb(0, 255, 120, 170));
        drawSoftDisc(graphics.pose().last().pose(), cx, cy, base * (0.04f + 0.01f * (1f - eased)), argb((int) (135 * (1f - eased)), 255, 240, 245), argb(0, 255, 240, 245));

        drawDiamondPulse(graphics.pose().last().pose(), cx, cy, base * (0.06f + 0.01f * (1f - eased)), argb((int) (60 * (1f - eased)), 255, 150, 180));
        drawCrossFlare(graphics, cx, cy, base * (0.09f + 0.02f * (1f - eased)), 0.92f - eased * 0.20f);
    }

    private static void renderChromaticAberration(final GuiGraphics graphics, final int w, final int h, final float intensity, final boolean returnMode) {
        if (intensity <= 0f) {
            return;
        }

        int band = Math.max(10, (int) (24 * intensity));

        int rA = (int) (40 + 85 * intensity);
        int cA = (int) (30 + 70 * intensity);
        int bA = (int) (24 + 60 * intensity);

        int red = returnMode ? argb(rA, 255, 110, 140) : argb(rA, 255, 80, 110);
        int cyan = returnMode ? argb(cA, 120, 255, 180) : argb(cA, 80, 240, 255);
        int blue = returnMode ? argb(bA, 220, 120, 255) : argb(bA, 120, 150, 255);

        graphics.fillGradient(0, 0, band, h, red, 0);
        graphics.fillGradient(w - band, 0, w, h, 0, cyan);
        graphics.fillGradient(0, 0, w, band, blue, 0);
        graphics.fillGradient(0, h - band, w, h, 0, red);

        int innerBand = Math.max(4, band / 3);
        graphics.fillGradient(innerBand, 0, innerBand * 2, h, cyan, 0);
        graphics.fillGradient(w - innerBand * 2, 0, w - innerBand, h, 0, blue);
    }

    private static void renderEdgeDistortion(
            final GuiGraphics graphics,
            final int w,
            final int h,
            final long time,
            final float intensity,
            final boolean returnMode,
            final int color
    ) {
        if (intensity <= 0f) {
            return;
        }

        Matrix4f matrix = graphics.pose().last().pose();
        int a = (color >> 24) & 255;
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        int seg = 42;
        float phase = (float) (time * 0.004);
        float amp = 8.0f + 18.0f * intensity;

        for (int i = 0; i <= seg; i++) {
            float t = i / (float) seg;
            float x = t * w;
            float y = (returnMode ? 1f : -1f) * Mth.sin(phase + t * 9f) * amp + 4f;
            builder.addVertex(matrix, x, y, 0).setColor(r, g, b, a);
        }
        for (int i = 0; i <= seg; i++) {
            float t = i / (float) seg;
            float y = t * h;
            float x = w - ((returnMode ? -1f : 1f) * Mth.cos(phase * 1.3f + t * 8f) * amp + 4f);
            builder.addVertex(matrix, x, y, 0).setColor(r, g, b, a);
        }
        for (int i = 0; i <= seg; i++) {
            float t = i / (float) seg;
            float x = w - t * w;
            float y = h - ((returnMode ? 1f : -1f) * Mth.sin(phase * 0.8f + t * 10f) * amp + 4f);
            builder.addVertex(matrix, x, y, 0).setColor(r, g, b, a);
        }
        for (int i = 0; i <= seg; i++) {
            float t = i / (float) seg;
            float y = h - t * h;
            float x = (returnMode ? -1f : 1f) * Mth.cos(phase * 1.1f + t * 7f) * amp + 4f;
            builder.addVertex(matrix, x, y, 0).setColor(r, g, b, a);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private static void renderTechGlyphOrbit(
            final GuiGraphics graphics,
            final float cx,
            final float cy,
            final float radius,
            final long time,
            final int count,
            final boolean reverse,
            final int colorA,
            final int colorB
    ) {
        float rot = (float) (time * 0.022) * (reverse ? -1f : 1f);

        for (int i = 0; i < count; i++) {
            float angle = (float) Math.toRadians(rot + i * (360f / count));
            float gx = cx + Mth.cos(angle) * radius;
            float gy = cy + Mth.sin(angle) * radius;
            int size = 4 + (i % 3);
            int color = (i % 2 == 0) ? colorA : colorB;
            drawTechGlyph(graphics, gx, gy, size, color, i % 4);
        }
    }

    private static void drawTechGlyph(final GuiGraphics graphics, final float x, final float y, final int size, final int color, final int variant) {
        int ix = Mth.floor(x);
        int iy = Mth.floor(y);

        switch (variant) {
            case 0 -> {
                graphics.fill(ix - size, iy - size, ix - size + 2, iy + size, color);
                graphics.fill(ix - size, iy - size, ix + size, iy - size + 2, color);
                graphics.fill(ix + size - 2, iy, ix + size, iy + size, color);
            }
            case 1 -> {
                graphics.fill(ix - size, iy - 1, ix + size, iy + 1, color);
                graphics.fill(ix - 1, iy - size, ix + 1, iy + size, color);
                graphics.fill(ix + size + 1, iy - 1, ix + size + 3, iy + 1, color);
            }
            case 2 -> {
                graphics.fill(ix - size, iy - size, ix + size, iy - size + 2, color);
                graphics.fill(ix - size, iy + size - 2, ix + size, iy + size, color);
                graphics.fill(ix - size, iy - size, ix - size + 2, iy + size, color);
            }
            default -> {
                graphics.fill(ix - size, iy - size, ix + size, iy - size + 2, color);
                graphics.fill(ix + size - 2, iy - size, ix + size, iy + size, color);
                graphics.fill(ix - size, iy + size - 2, ix + 2, iy + size, color);
            }
        }
    }

    private static void renderSegmentedRingStack(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float startRadius,
            final int count,
            final float rotation,
            final boolean returnMode,
            final int color
    ) {
        for (int i = 0; i < count; i++) {
            float inner = startRadius + i * 14f;
            float outer = inner + 4f + (i % 2);
            float start = rotation + i * 13f * (returnMode ? -1f : 1f);
            int segments = 14 + i * 2;
            drawSegmentedRing(matrix, cx, cy, inner, outer, start, segments, 18f, 8f, color);
        }
    }

    private static void drawSegmentedRing(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float innerRadius,
            final float outerRadius,
            final float startAngleDeg,
            final int segments,
            final float segAngle,
            final float gapAngle,
            final int color
    ) {
        float cursor = startAngleDeg;
        for (int i = 0; i < segments; i++) {
            drawRingArc(matrix, cx, cy, innerRadius, outerRadius, cursor, segAngle, color);
            cursor += segAngle + gapAngle;
        }
    }

    private static void renderTunnelStreaks(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float base,
            final float rotation,
            final boolean inward,
            final int color
    ) {
        int count = 96;
        for (int i = 0; i < count; i++) {
            float a = i * (360f / count) + rotation;
            float inner = inward ? base * 0.14f : base * 0.06f;
            float outer = inward
                    ? base * (0.20f + 0.18f * Math.abs(Mth.sin(i * 0.71f + rotation * 0.01f)))
                    : base * (0.34f + 0.50f * Math.abs(Mth.sin(i * 0.63f + rotation * 0.01f)));

            if (inward) {
                float temp = inner;
                inner = outer;
                outer = temp;
            }

            drawBeam(
                    matrix,
                    cx,
                    cy,
                    a,
                    inner,
                    outer,
                    inward ? 1.1f : 1.8f,
                    0.07f,
                    color,
                    argb(0, (color >> 16) & 255, (color >> 8) & 255, color & 255)
            );
        }
    }

    private static void renderRadialFlares(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float innerRadius,
            final float outerRadius,
            final int count,
            final float rotation,
            final int color
    ) {
        for (int i = 0; i < count; i++) {
            drawBeam(
                    matrix,
                    cx,
                    cy,
                    rotation + i * (360f / count),
                    innerRadius,
                    outerRadius,
                    2.0f,
                    0.08f,
                    color,
                    argb(0, (color >> 16) & 255, (color >> 8) & 255, color & 255)
            );
        }
    }

    private static void renderConvergingShards(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float startRadius,
            final float endRadius,
            final int count,
            final float rotation,
            final int color
    ) {
        for (int i = 0; i < count; i++) {
            float angle = rotation + i * (360f / count);
            drawShard(matrix, cx, cy, angle, startRadius, endRadius, 7.0f + (i % 3) * 2.0f, color);
        }
    }

    private static void renderShardBurst(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float startRadius,
            final float endRadius,
            final int count,
            final float rotation,
            final boolean inward,
            final int color
    ) {
        for (int i = 0; i < count; i++) {
            float angle = rotation + i * (360f / count);
            drawShard(matrix, cx, cy, angle, inward ? endRadius : startRadius, inward ? startRadius : endRadius, 10.0f + (i % 2) * 4.0f, color);
        }
    }

    private static void renderAngularCracks(final Matrix4f matrix, final float cx, final float cy, final float radius, final float rotation, final int color) {
        for (int i = 0; i < 9; i++) {
            float a = rotation + i * 40f;
            drawBeam(matrix, cx, cy, a, radius * 0.30f, radius, 1.1f, 0.5f, color, argb(0, (color >> 16) & 255, (color >> 8) & 255, color & 255));
        }
    }

    private static void renderSnapLines(final Matrix4f matrix, final float cx, final float cy, final float radius, final int count, final int color) {
        for (int i = 0; i < count; i++) {
            float angle = i * (360f / count);
            drawBeam(matrix, cx, cy, angle, radius * 0.15f, radius, 1.7f, 0.15f, color, argb(0, (color >> 16) & 255, (color >> 8) & 255, color & 255));
        }
    }

    private static void renderSpaceRupture(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float radius,
            final float rotation,
            final float alphaMul,
            final boolean returnMode
    ) {

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < 10; i++) {
            float angle = rotation + i * 36f;
            float a1 = (float) Math.toRadians(angle - 6f);
            float a2 = (float) Math.toRadians(angle + 6f);
            float out = radius * (1.02f + 0.28f * (i % 2));
            int alpha = (int) (95 * alphaMul);

            int r = returnMode ? 255 : 160;
            int g = returnMode ? 135 : 245;
            int b = returnMode ? 175 : 255;

            builder.addVertex(matrix, cx, cy, 0).setColor(r, g, b, alpha);
            builder.addVertex(matrix, cx + Mth.cos(a1) * radius * 0.35f, cy + Mth.sin(a1) * radius * 0.35f, 0).setColor(r, g, b, alpha);
            builder.addVertex(matrix, cx + Mth.cos(a2) * out, cy + Mth.sin(a2) * out, 0).setColor(r, g, b, 0);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private static void renderOrbitFragments(final Matrix4f matrix, final float cx, final float cy, final float radius, final long time, final int count, final boolean reverse) {
        for (int i = 0; i < count; i++) {
            float ang = (float) (time * 0.08) * (reverse ? -1f : 1f) + i * (360f / count);
            float r = radius + (i % 3) * 7f;
            drawBeam(matrix, cx, cy, ang, r, r + 8f, 2.4f, 0.5f, reverse ? argb(80, 255, 120, 165) : argb(80, 160, 245, 255), argb(0, 160, 245, 255));
        }
    }

    private static void renderStabilizationGrid(final GuiGraphics graphics, final int w, final int h, final float alpha) {
        int lines = 14;
        int color = argb((int) (22 * alpha), 100, 225, 255);
        for (int i = 0; i < lines; i++) {
            int y = (int) ((i / (float) (lines - 1)) * h);
            graphics.fill(0, y, w, y + 1, color);
        }
    }

    private static void renderScanlines(final GuiGraphics graphics, final int w, final int h, final float alpha) {
        int a = (int) (255 * alpha * 0.18f);
        if (a <= 0) {
            return;
        }
        int color = argb(a, 95, 200, 255);
        for (int y = 0; y < h; y += 3) {
            graphics.fill(0, y, w, y + 1, color);
        }
    }

    private static void renderNoiseField(final GuiGraphics graphics, final int w, final int h, final long time, final int count, final float strength) {
        for (int i = 0; i < count; i++) {
            long seed = time * 31L + i * 977L;
            int x = (int) Math.floorMod(seed * 17L + i * 13L, w);
            int y = (int) Math.floorMod(seed * 29L + i * 7L, h);
            int size = 1 + (int) Math.floorMod(seed, 2L);
            int a = (int) (10 + Math.floorMod(seed, 28L) * strength);
            graphics.fill(x, y, x + size, y + size, argb(a, 180, 235, 255));
        }
    }

    private static void drawCrossFlare(final GuiGraphics graphics, final float cx, final float cy, final float length, final float mul) {
        int a1 = (int) (110 * mul);
        int a2 = (int) (72 * mul);
        graphics.fill((int) (cx - length), (int) cy - 1, (int) (cx + length), (int) cy + 1, argb(a1, 90, 230, 255));
        graphics.fill((int) cx - 1, (int) (cy - length), (int) cx + 1, (int) (cy + length), argb(a2, 90, 230, 255));
    }

    private static void drawDiamondPulse(final Matrix4f matrix, final float cx, final float cy, final float radius, final int color) {
        int a = (color >> 24) & 255;
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        builder.addVertex(matrix, cx, cy, 0).setColor(r, g, b, a);
        builder.addVertex(matrix, cx, cy - radius, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, cx + radius, cy, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, cx, cy + radius, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, cx - radius, cy, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, cx, cy - radius, 0).setColor(r, g, b, 0);
        BufferUploader.drawWithShader(builder.buildOrThrow());

        RenderSystem.disableBlend();
    }

    private static void drawHelixBand(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float innerR,
            final float outerR,
            final float rotationDeg,
            final float direction,
            final int color
    ) {
        int a = (color >> 24) & 255;
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        int segments = 96;
        for (int i = 0; i <= segments; i++) {
            float t = i / (float) segments;
            float radius = Mth.lerp(t, innerR, outerR);
            float angle = (float) Math.toRadians(rotationDeg + direction * (t * 520f));
            float x = cx + Mth.cos(angle) * radius;
            float y = cy + Mth.sin(angle) * radius;
            int alpha = (int) (a * (1f - t));
            builder.addVertex(matrix, x, y, 0).setColor(r, g, b, alpha);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private static void drawShard(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float angleDeg,
            final float startRadius,
            final float endRadius,
            final float width,
            final int color
    ) {
        int a = (color >> 24) & 255;
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;

        float angle = (float) Math.toRadians(angleDeg);
        float dx = Mth.cos(angle);
        float dy = Mth.sin(angle);
        float px = -dy;
        float py = dx;

        float sx = cx + dx * startRadius;
        float sy = cy + dy * startRadius;
        float ex = cx + dx * endRadius;
        float ey = cy + dy * endRadius;


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        builder.addVertex(matrix, ex, ey, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, sx + px * width, sy + py * width, 0).setColor(r, g, b, a);
        builder.addVertex(matrix, sx - px * width, sy - py * width, 0).setColor(r, g, b, a);
        BufferUploader.drawWithShader(builder.buildOrThrow());

        RenderSystem.disableBlend();
    }

    private static void drawSoftDisc(final Matrix4f matrix, final float cx, final float cy, final float radius, final int innerColor, final int outerColor) {
        int ia = (innerColor >> 24) & 255;
        int ir = (innerColor >> 16) & 255;
        int ig = (innerColor >> 8) & 255;
        int ib = innerColor & 255;

        int oa = (outerColor >> 24) & 255;
        int or = (outerColor >> 16) & 255;
        int og = (outerColor >> 8) & 255;
        int ob = outerColor & 255;


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        builder.addVertex(matrix, cx, cy, 0).setColor(ir, ig, ib, ia);

        int segments = 64;
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (Math.PI * 2D * i / segments);
            float x = cx + Mth.cos(angle) * radius;
            float y = cy + Mth.sin(angle) * radius;
            builder.addVertex(matrix, x, y, 0).setColor(or, og, ob, oa);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private static void drawFullRing(final Matrix4f matrix, final float cx, final float cy, final float innerRadius, final float outerRadius, final int color) {
        drawRingArc(matrix, cx, cy, innerRadius, outerRadius, -90f, 360f, color);
    }

    private static void drawDonutArc(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float innerRadius,
            final float outerRadius,
            final float startAngleDeg,
            final float sweepDeg,
            final int color
    ) {
        drawRingArc(matrix, cx, cy, innerRadius, outerRadius, startAngleDeg, sweepDeg, color);
    }

    private static void drawRingArc(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float innerRadius,
            final float outerRadius,
            final float startAngleDeg,
            final float sweepDeg,
            final int color
    ) {
        int a = (color >> 24) & 255;
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        int segments = Math.max(18, (int) (Math.abs(sweepDeg) / 6f));
        for (int i = 0; i <= segments; i++) {
            float t = i / (float) segments;
            float angle = (float) Math.toRadians(startAngleDeg + sweepDeg * t);
            float cos = Mth.cos(angle);
            float sin = Mth.sin(angle);

            builder.addVertex(matrix, cx + cos * outerRadius, cy + sin * outerRadius, 0).setColor(r, g, b, a);
            builder.addVertex(matrix, cx + cos * innerRadius, cy + sin * innerRadius, 0).setColor(r, g, b, a);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private static void drawBeam(
            final Matrix4f matrix,
            final float cx,
            final float cy,
            final float angleDeg,
            final float innerRadius,
            final float outerRadius,
            final float widthInner,
            final float widthOuter,
            final int innerColor,
            final int outerColor
    ) {
        int ia = (innerColor >> 24) & 255;
        int ir = (innerColor >> 16) & 255;
        int ig = (innerColor >> 8) & 255;
        int ib = innerColor & 255;

        int oa = (outerColor >> 24) & 255;
        int or = (outerColor >> 16) & 255;
        int og = (outerColor >> 8) & 255;
        int ob = outerColor & 255;

        float angle = (float) Math.toRadians(angleDeg);
        float dx = Mth.cos(angle);
        float dy = Mth.sin(angle);
        float px = -dy;
        float py = dx;

        float ix = cx + dx * innerRadius;
        float iy = cy + dy * innerRadius;
        float ox = cx + dx * outerRadius;
        float oy = cy + dy * outerRadius;


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.addVertex(matrix, ix + px * widthInner, iy + py * widthInner, 0).setColor(ir, ig, ib, ia);
        builder.addVertex(matrix, ix - px * widthInner, iy - py * widthInner, 0).setColor(ir, ig, ib, ia);
        builder.addVertex(matrix, ox - px * widthOuter, oy - py * widthOuter, 0).setColor(or, og, ob, oa);
        builder.addVertex(matrix, ox + px * widthOuter, oy + py * widthOuter, 0).setColor(or, og, ob, oa);
        BufferUploader.drawWithShader(builder.buildOrThrow());

        RenderSystem.disableBlend();
    }

    private static int argb(final int a, final int r, final int g, final int b) {
        return ((a & 255) << 24) | ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
    }

    private static float easeInOutCubic(final float t) {
        if (t < 0.5f) {
            return 4f * t * t * t;
        }
        return 1f - (float) Math.pow(-2f * t + 2f, 3f) / 2f;
    }

    private static float easeOutCubic(final float t) {
        return 1f - (float) Math.pow(1f - t, 3f);
    }

    private static String formatTicks(final long ticks) {
        long totalSeconds = Math.max(0L, ticks / 20L);
        long hours = totalSeconds / 3600L;
        long minutes = (totalSeconds % 3600L) / 60L;
        long seconds = totalSeconds % 60L;

        if (hours > 0L) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }
}
