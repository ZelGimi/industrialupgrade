package com.denfop.client.intro;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class IntroDashboardThirdScreen extends Screen {

    private static final int FRAME_FILL = 0xD911151D;
    private static final int FRAME_BORDER = 0xFF394A63;
    private static final int PANEL_FILL = 0xFF101923;
    private static final int PANEL_FILL_DARK = 0xFF0E141C;
    private static final int PANEL_BORDER = 0xFF2E4158;
    private static final int TEXT_MAIN = 0xFFEAF2FF;
    private static final int TEXT_SUB = 0xFF9FB4CE;
    private static final int BUTTON_FILL = 0xFF182638;
    private static final int BUTTON_BORDER = 0xFF70A7FF;
    private static final int BUTTON_ACTIVE = 0xFF2C6DDA;
    private static final int GREEN = 0xFF2FA84F;
    private static final int ORANGE = 0xFFF0A020;
    private static final int HEADER_H = 18;
    private static final int GAP = 6;
    private static final int PARTNER_CARD_H = 30;
    private static final int PARTNER_GAP = 5;
    private static final int MOD_CARD_W = 92;
    private static final int MOD_CARD_H = 42;
    private static final int MOD_CARD_GAP = 6;
    private static final String BACK_LABEL = "<<";
    private static final long PARTNER_BANNER_SWITCH_MS = 5000L;
    private static final long PARTNER_BANNER_TRANSITION_MS = 450L;
    private static final int PARTNER_DOT_SIZE = 6;
    private static final int PARTNER_DOT_GAP = 5;
    private static final long AUTO_SCROLL_IDLE_MS = 6000L;
    private static final long AUTO_SCROLL_EDGE_PAUSE_MS = 1200L;
    private static final float AUTO_SCROLL_SPEED_PX_PER_SEC = 18.0F;

    private final IntroDashboardData data;
    private final IntroImageManager imageManager;
    private final AutoScrollState supportAutoScroll = new AutoScrollState();

    private int partnersScroll;
    private int modsScrollX;
    private int birthdayTicks;
    private CompletableFuture<IntroVersionCheckResult> versionFuture;
    private IntroVersionCheckResult versionResult;
    private int partnerBannerIndex;
    private int partnerBannerPreviousIndex = -1;
    private long partnerBannerTransitionStartAt = -1L;
    private long nextPartnerBannerSwitchAt;
    private int supportScroll;
    private long lastAutoScrollTickAt;

    public IntroDashboardThirdScreen(IntroDashboardData data) {
        super(Component.translatable("intro.page3.screen_title"));
        this.data = data;
        this.imageManager = IntroImageManager.getInstance();
    }

    private String text(String value) {
        return IntroLocalization.text(value);
    }

    private String tr(String key) {
        return IntroLocalization.tr(key);
    }

    private String tr(String key, Object... args) {
        return IntroLocalization.tr(key, args);
    }

    private Component component(String value) {
        return Component.literal(text(value));
    }

    @Override
    protected void init() {
        versionFuture = IntroVersionCheckService.checkAsync();
        long now = Util.getMillis();
        nextPartnerBannerSwitchAt = now + PARTNER_BANNER_SWITCH_MS;
        lastAutoScrollTickAt = now;
        supportAutoScroll.init(now, supportScroll);
        clampScrolls(layout());
    }

    private void notifySupportUserInput() {
        supportAutoScroll.onUserInput(Util.getMillis(), supportScroll);
    }

    private void updateSupportAutoScroll(Rect panel) {
        long now = Util.getMillis();
        long deltaMs = Math.max(0L, now - lastAutoScrollTickAt);
        lastAutoScrollTickAt = now;
        supportScroll = supportAutoScroll.update(now, deltaMs, supportScroll, getSupportMaxScroll(panel));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        Layout l = layout();
        updateSupportAutoScroll(l.support());
        clampScrolls(l);

        if (versionFuture != null && versionFuture.isDone() && versionResult == null) {
            versionResult = versionFuture.getNow(null);
        }

        long now = Util.getMillis();
        if (partnerBannerPreviousIndex != -1 && now - partnerBannerTransitionStartAt >= PARTNER_BANNER_TRANSITION_MS) {
            partnerBannerPreviousIndex = -1;
            partnerBannerTransitionStartAt = -1L;
        }

        if (data.getThirdPagePartners().size() > 1 && partnerBannerPreviousIndex == -1 && now >= nextPartnerBannerSwitchAt) {
            switchPartnerBanner((partnerBannerIndex + 1) % data.getThirdPagePartners().size());
            nextPartnerBannerSwitchAt = now + PARTNER_BANNER_SWITCH_MS;
        }

        if (birthdayTicks > 0) {
            birthdayTicks--;
            if (birthdayTicks % 4 == 0) spawnBirthdayFireworks();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.fill(0, 0, width, height, 0xA0101318);
        Layout l = layout();
        clampScrolls(l);

        IntroDrawUtil.drawPanel(guiGraphics, l.frame().x(), l.frame().y(), l.frame().w(), l.frame().h(), FRAME_FILL, FRAME_BORDER);
        renderHeader(guiGraphics, l);

        List<Component> tooltip = null;
        tooltip = firstNonNull(tooltip, renderPartnersPanel(guiGraphics, l.partners(), mouseX, mouseY));
        tooltip = firstNonNull(tooltip, renderVersionPanel(guiGraphics, l.version(), mouseX, mouseY));
        tooltip = firstNonNull(tooltip, renderSupportPanel(guiGraphics, l.support(), mouseX, mouseY));
        tooltip = firstNonNull(tooltip, renderRecommendedModsPanel(guiGraphics, l.mods(), mouseX, mouseY));


        if (tooltip != null) guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
    }

    private void renderHeader(GuiGraphics guiGraphics, Layout l) {
        IntroDrawUtil.drawPanel(guiGraphics, l.nav().x(), l.nav().y(), l.nav().w(), l.nav().h(), PANEL_FILL_DARK, PANEL_BORDER);
        drawNavButton(guiGraphics, l.backBtn(), BACK_LABEL, false);
        drawCenteredText(guiGraphics, tr("intro.page3.title"), l.nav().x() + l.nav().w() / 2, l.nav().y() + 5, TEXT_MAIN);
    }

    private List<Component> renderPartnersPanel(GuiGraphics guiGraphics, Rect panel, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, panel.x(), panel.y(), panel.w(), panel.h(), PANEL_FILL, PANEL_BORDER);
        drawCenteredText(guiGraphics, tr("intro.page3.partner_banners"), panel.x() + panel.w() / 2, panel.y() + 4, TEXT_MAIN);

        List<IntroExternalBanner> banners = data.getThirdPagePartners();
        if (banners.isEmpty()) {
            guiGraphics.drawString(font, tr("intro.page3.no_partner_banners"), panel.x() + 8, panel.y() + 22, TEXT_SUB, false);
            return null;
        }

        Rect imageRect = getPartnerBannerImageRect(panel);
        Rect viewport = getPartnerBannerViewport(panel);
        IntroDrawUtil.drawPanel(guiGraphics, imageRect.x(), imageRect.y(), imageRect.w(), imageRect.h(), 0xFF0C121B, 0xFF243548);
        long now = Util.getMillis();

        guiGraphics.enableScissor(viewport.x(), viewport.y(), viewport.x() + viewport.w(), viewport.y() + viewport.h());
        if (partnerBannerPreviousIndex != -1) {
            float t = smootherStep(getPartnerBannerTransitionProgress(now));
            IntroExternalBanner previousBanner = banners.get(Mth.clamp(partnerBannerPreviousIndex, 0, banners.size() - 1));
            IntroExternalBanner currentBanner = banners.get(Mth.clamp(partnerBannerIndex, 0, banners.size() - 1));
            renderPartnerBannerSlide(guiGraphics, previousBanner, viewport, 1.0F - t);
            renderPartnerBannerSlide(guiGraphics, currentBanner, viewport, t);
        } else {
            IntroExternalBanner currentBanner = banners.get(Mth.clamp(partnerBannerIndex, 0, banners.size() - 1));
            renderPartnerBannerSlide(guiGraphics, currentBanner, viewport, 1.0F);
        }
        guiGraphics.disableScissor();

        if (banners.size() > 1) {
            Rect prev = getPartnerPrevArrowRect(viewport);
            Rect next = getPartnerNextArrowRect(viewport);
            drawPartnerArrowButton(guiGraphics, prev, "<", isInside(mouseX, mouseY, prev));
            drawPartnerArrowButton(guiGraphics, next, ">", isInside(mouseX, mouseY, next));
        }

        int totalW = banners.size() * PARTNER_DOT_SIZE + Math.max(0, banners.size() - 1) * PARTNER_DOT_GAP;
        int startX = panel.x() + (panel.w() - totalW) / 2;
        int dotY = getPartnerBannerDotY(panel);
        for (int i = 0; i < banners.size(); i++) {
            int x = startX + i * (PARTNER_DOT_SIZE + PARTNER_DOT_GAP);
            int fill = i == partnerBannerIndex ? BUTTON_ACTIVE : 0xFF2C3C52;
            IntroDrawUtil.drawPanel(guiGraphics, x, dotY, PARTNER_DOT_SIZE, PARTNER_DOT_SIZE, fill, 0xFFFFFFFF);
        }

        if (isInside(mouseX, mouseY, viewport)) {
            IntroExternalBanner currentBanner = banners.get(Mth.clamp(partnerBannerIndex, 0, banners.size() - 1));
            return List.of(component(currentBanner.title()), component(currentBanner.subtitle()), Component.literal(tr("intro.tooltip.click_open")));
        }
        return null;
    }

    private void renderPartnerBannerSlide(GuiGraphics guiGraphics, IntroExternalBanner banner, Rect viewport, float alpha) {
        alpha = Mth.clamp(alpha, 0.0F, 1.0F);
        if (alpha <= 0.001F) return;
        IntroRemoteImage image = imageManager.get(banner.iconSource());
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        if (image.isReady()) {
            IntroDrawUtil.drawStretchedImage(guiGraphics, image, viewport.x(), viewport.y(), viewport.w(), viewport.h());
        } else {
            guiGraphics.fill(viewport.x(), viewport.y(), viewport.x() + viewport.w(), viewport.y() + viewport.h(), withAlpha(0xFF1A2330, alpha));
            String title = font.plainSubstrByWidth(text(banner.title()), viewport.w() - 12);
            String subtitle = font.plainSubstrByWidth(text(banner.subtitle()), viewport.w() - 12);
            drawCenteredText(guiGraphics, title, viewport.x() + viewport.w() / 2, viewport.y() + viewport.h() / 2 - 10, withAlpha(TEXT_MAIN, alpha));
            drawCenteredText(guiGraphics, subtitle, viewport.x() + viewport.w() / 2, viewport.y() + viewport.h() / 2 + 2, withAlpha(TEXT_SUB, alpha));
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private List<Component> renderVersionPanel(GuiGraphics guiGraphics, Rect panel, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, panel.x(), panel.y(), panel.w(), panel.h(), PANEL_FILL, PANEL_BORDER);
        drawCenteredText(guiGraphics, tr("intro.page3.version_check"), panel.x() + panel.w() / 2, panel.y() + 4, TEXT_MAIN);

        String mcVersion = versionResult != null ? versionResult.mcVersion() : tr("intro.version.checking_value");
        String localVersion = versionResult != null ? versionResult.localVersion() : tr("intro.version.checking_value");
        String remoteVersion = versionResult == null
                ? tr("intro.version.checking_value")
                : (versionResult.remoteAvailable() ? versionResult.remoteVersion() : tr("intro.version.remote_current"));

        int stateColor;
        String stateText;
        if (versionResult == null) {
            stateColor = BUTTON_ACTIVE;
            stateText = tr("intro.version.status.checking");
        } else if (!versionResult.remoteAvailable()) {
            stateColor = GREEN;
            stateText = tr("intro.version.status.actual");
        } else if (versionResult.updateRecommended()) {
            stateColor = ORANGE;
            stateText = tr("intro.version.status.update");
        } else {
            stateColor = GREEN;
            stateText = tr("intro.version.status.actual");
        }

        int x = panel.x() + 8;
        int y = panel.y() + 24;
        guiGraphics.drawString(font, tr("intro.version.mc", mcVersion), x, y, TEXT_MAIN, false);
        y += 12;
        guiGraphics.drawString(font, tr("intro.version.installed", localVersion), x, y, TEXT_MAIN, false);
        y += 12;
        guiGraphics.drawString(font, tr("intro.version.remote", remoteVersion), x, y, TEXT_MAIN, false);
        drawStatusBadge(guiGraphics, panel.x() + panel.w() - 74, panel.y() + 22, 64, stateText, stateColor);

        Rect birthdayBtn = getBirthdayButtonRect(panel);
        if (isBirthdayDay()) {
            IntroDrawUtil.drawPanel(guiGraphics, birthdayBtn.x(), birthdayBtn.y(), birthdayBtn.w(), birthdayBtn.h(), 0xFF6A1E85, 0xFFFFFFFF);
            drawCenteredScaledText(guiGraphics, tr("intro.birthday.button"), birthdayBtn.x() + birthdayBtn.w() / 2, birthdayBtn.y() + 4, 0.70F, 0xFFFFFFFF);
            if (isInside(mouseX, mouseY, birthdayBtn)) {
                return List.of(Component.literal(tr("intro.birthday.button")), Component.literal(tr("intro.birthday.tooltip")));
            }
        }
        return null;
    }

    private List<Component> renderSupportPanel(GuiGraphics guiGraphics, Rect panel, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, panel.x(), panel.y(), panel.w(), panel.h(), PANEL_FILL, PANEL_BORDER);
        drawCenteredText(guiGraphics, tr("intro.page3.support_links"), panel.x() + panel.w() / 2, panel.y() + 4, TEXT_MAIN);

        List<IntroExternalBanner> links = data.getThirdPageSupportLinks();
        if (links.isEmpty()) {
            guiGraphics.drawString(font, tr("intro.page3.no_support_links"), panel.x() + 8, panel.y() + 22, TEXT_SUB, false);
            return null;
        }

        Rect viewport = getSupportViewport(panel);
        int contentH = getSupportContentHeight();
        int cardW = getSupportCardWidth(panel);
        int cardH = getSupportCardHeight();
        int gap = getSupportCardGap();
        List<Component> hovered = null;

        guiGraphics.enableScissor(viewport.x(), viewport.y(), viewport.x() + viewport.w(), viewport.y() + viewport.h());
        int x = viewport.x() + (viewport.w() - cardW) / 2;
        int y = contentH <= viewport.h() ? viewport.y() + (viewport.h() - contentH) / 2 : viewport.y() - supportScroll;

        for (IntroExternalBanner link : links) {
            Rect card = new Rect(x, y, cardW, cardH);
            if (card.y() + card.h() >= viewport.y() - 2 && card.y() <= viewport.y() + viewport.h() + 2) {
                Rect inner = new Rect(card.x() + 2, card.y() + 2, card.w() - 4, card.h() - 4);
                IntroDrawUtil.drawPanel(guiGraphics, card.x(), card.y(), card.w(), card.h(), 0xFF131C28, 0xFF304154);
                IntroRemoteImage image = imageManager.get(link.iconSource());
                if (image != null && image.isReady()) {
                    IntroDrawUtil.drawStretchedImage(guiGraphics, image, inner.x(), inner.y(), inner.w(), inner.h());
                } else {
                    guiGraphics.fill(inner.x(), inner.y(), inner.x() + inner.w(), inner.y() + inner.h(), 0xFF0E1620);
                    String title = font.plainSubstrByWidth(text(link.title()), inner.w() - 8);
                    String subtitle = font.plainSubstrByWidth(text(link.subtitle()), inner.w() - 8);
                    drawCenteredText(guiGraphics, title, inner.x() + inner.w() / 2, inner.y() + 12, TEXT_MAIN);
                    drawCenteredScaledText(guiGraphics, subtitle, inner.x() + inner.w() / 2, inner.y() + 24, 0.70F, TEXT_SUB);
                }
                if (isInside(mouseX, mouseY, card)) {
                    hovered = List.of(component(link.title()), Component.literal(tr("intro.tooltip.click_open")));
                }
            }
            y += cardH + gap;
        }
        guiGraphics.disableScissor();
        renderSupportScrollbar(guiGraphics, panel);
        return hovered;
    }

    private List<Component> renderRecommendedModsPanel(GuiGraphics guiGraphics, Rect panel, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, panel.x(), panel.y(), panel.w(), panel.h(), PANEL_FILL, PANEL_BORDER);
        drawCenteredText(guiGraphics, tr("intro.page3.recommended_mods"), panel.x() + panel.w() / 2, panel.y() + 4, TEXT_MAIN);

        List<IntroAddonEntry> mods = data.getThirdPageRecommendedMods();
        if (mods.isEmpty()) {
            guiGraphics.drawString(font, tr("intro.page3.no_recommended_mods"), panel.x() + 8, panel.y() + 22, TEXT_SUB, false);
            return null;
        }

        int viewX = panel.x() + 6;
        int viewY = panel.y() + 20;
        int viewW = panel.w() - 12;
        int viewH = panel.h() - 30;
        guiGraphics.enableScissor(viewX, viewY, viewX + viewW, viewY + viewH);
        int x = viewX - modsScrollX;
        List<Component> hovered = null;

        for (IntroAddonEntry mod : mods) {
            Rect card = new Rect(x, viewY + 2, MOD_CARD_W, MOD_CARD_H);
            IntroDrawUtil.drawPanel(guiGraphics, card.x(), card.y(), card.w(), card.h(), 0xFF131C28, 0xFF304154);
            IntroDrawUtil.renderChipIcon(guiGraphics, font, imageManager, mod.getIconSource(), text(mod.getName()), card.x() + (card.w() - 20) / 2, card.y() + 5, 20);
            String title = font.plainSubstrByWidth(text(mod.getName()), card.w() - 8);
            drawCenteredScaledText(guiGraphics, title, card.x() + card.w() / 2, card.y() + 28, 0.75F, TEXT_MAIN);
            String subtitle = font.plainSubstrByWidth(text(mod.getSubtitle()), card.w() - 8);
            drawCenteredScaledText(guiGraphics, subtitle, card.x() + card.w() / 2, card.y() + 36, 0.60F, TEXT_SUB);
            if (isInside(mouseX, mouseY, card))
                hovered = List.of(component(mod.getName()), component(mod.getSubtitle()));
            x += MOD_CARD_W + MOD_CARD_GAP;
        }
        guiGraphics.disableScissor();
        renderHorizontalScrollbar(guiGraphics, panel, modsScrollX, getModsContentWidth());
        return hovered;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double delta) {
        Layout l = layout();
        clampScrolls(l);
        if (isInside(mouseX, mouseY, l.support())) {
            notifySupportUserInput();
            supportScroll = Mth.clamp(supportScroll - (int) (delta * 20), 0, getSupportMaxScroll(l.support()));
            return true;
        }
        if (isInside(mouseX, mouseY, l.partners())) {
            partnersScroll = Mth.clamp(partnersScroll - (int) (delta * 20), 0, getPartnersMaxScroll(l.partners()));
            return true;
        }
        if (isInside(mouseX, mouseY, l.mods())) {
            modsScrollX = Mth.clamp(modsScrollX - (int) (delta * 22), 0, getModsMaxScroll(l.mods()));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, deltaX, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        Layout l = layout();
        if (isInside(mouseX, mouseY, l.backBtn())) {
            minecraft.setScreen(new IntroDashboardSecondScreen(data));
            return true;
        }
        if (handlePartnerBannerClick(l.partners(), mouseX, mouseY)) return true;
        if (handleSupportLinkClick(l.support(), mouseX, mouseY)) return true;
        if (isBirthdayDay() && isInside(mouseX, mouseY, getBirthdayButtonRect(l.version()))) {
            birthdayTicks = 80;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handlePartnerBannerClick(Rect panel, double mouseX, double mouseY) {
        List<IntroExternalBanner> banners = data.getThirdPagePartners();
        if (banners.isEmpty()) return false;
        Rect viewport = getPartnerBannerViewport(panel);
        if (banners.size() > 1) {
            Rect prev = getPartnerPrevArrowRect(viewport);
            Rect next = getPartnerNextArrowRect(viewport);
            if (isInside(mouseX, mouseY, prev)) {
                int nextIndex = partnerBannerIndex - 1;
                if (nextIndex < 0) nextIndex = banners.size() - 1;
                switchPartnerBanner(nextIndex);
                nextPartnerBannerSwitchAt = Util.getMillis() + PARTNER_BANNER_SWITCH_MS;
                return true;
            }
            if (isInside(mouseX, mouseY, next)) {
                switchPartnerBanner((partnerBannerIndex + 1) % banners.size());
                nextPartnerBannerSwitchAt = Util.getMillis() + PARTNER_BANNER_SWITCH_MS;
                return true;
            }
            int totalW = banners.size() * PARTNER_DOT_SIZE + Math.max(0, banners.size() - 1) * PARTNER_DOT_GAP;
            int startX = panel.x() + (panel.w() - totalW) / 2;
            int dotY = getPartnerBannerDotY(panel);
            for (int i = 0; i < banners.size(); i++) {
                Rect dot = new Rect(startX + i * (PARTNER_DOT_SIZE + PARTNER_DOT_GAP), dotY, PARTNER_DOT_SIZE, PARTNER_DOT_SIZE);
                if (isInside(mouseX, mouseY, dot)) {
                    switchPartnerBanner(i);
                    nextPartnerBannerSwitchAt = Util.getMillis() + PARTNER_BANNER_SWITCH_MS;
                    return true;
                }
            }
        }
        if (isInside(mouseX, mouseY, viewport)) {
            IntroExternalBanner currentBanner = banners.get(Mth.clamp(partnerBannerIndex, 0, banners.size() - 1));
            openLink(currentBanner.url());
            return true;
        }
        return false;
    }

    private boolean handleSupportLinkClick(Rect panel, double mouseX, double mouseY) {
        List<IntroExternalBanner> links = data.getThirdPageSupportLinks();
        if (links.isEmpty()) return false;
        Rect viewport = getSupportViewport(panel);
        int contentH = getSupportContentHeight();
        int cardW = getSupportCardWidth(panel);
        int cardH = getSupportCardHeight();
        int gap = getSupportCardGap();
        int x = viewport.x() + (viewport.w() - cardW) / 2;
        int y = contentH <= viewport.h() ? viewport.y() + (viewport.h() - contentH) / 2 : viewport.y() - supportScroll;
        for (IntroExternalBanner link : links) {
            Rect card = new Rect(x, y, cardW, cardH);
            if (isInside(mouseX, mouseY, card)) {
                notifySupportUserInput();
                openLink(link.url());
                return true;
            }
            y += cardH + gap;
        }
        return false;
    }

    private void openLink(String url) {
        if (url == null || url.isBlank()) return;
        Util.getPlatform().openUri(url);
    }

    private void spawnBirthdayFireworks() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        double baseX = mc.player.getX();
        double baseY = mc.player.getY() + 1.4D;
        double baseZ = mc.player.getZ();
        for (int i = 0; i < 28; i++) {
            double angle = mc.level.random.nextDouble() * Math.PI * 2.0D;
            double radius = 0.4D + mc.level.random.nextDouble() * 1.2D;
            double speed = 0.04D + mc.level.random.nextDouble() * 0.12D;
            double px = baseX + Math.cos(angle) * radius;
            double py = baseY + mc.level.random.nextDouble() * 1.3D;
            double pz = baseZ + Math.sin(angle) * radius;
            double vx = Math.cos(angle) * speed;
            double vy = 0.08D + mc.level.random.nextDouble() * 0.08D;
            double vz = Math.sin(angle) * speed;
            mc.level.addParticle(ParticleTypes.FIREWORK, px, py, pz, vx, vy, vz);
            mc.level.addParticle(ParticleTypes.HAPPY_VILLAGER, px, py, pz, vx * 0.6D, vy * 0.6D, vz * 0.6D);
        }
    }

    private boolean isBirthdayDay() {
        LocalDate now = LocalDate.now();
        return now.getMonthValue() == 3 && now.getDayOfMonth() == 30;
    }

    private Rect getBirthdayButtonRect(Rect panel) {
        return new Rect(panel.x() + 8, panel.y() + panel.h() - 22, panel.w() - 16, 14);
    }

    private int getPartnersContentHeight() {
        int count = data.getThirdPagePartners().size();
        if (count <= 0) return 1;
        return count * PARTNER_CARD_H + Math.max(0, count - 1) * PARTNER_GAP;
    }

    private int getPartnersMaxScroll(Rect panel) {
        ScrollArea area = createScrollArea(panel.x(), panel.y(), panel.w(), panel.h());
        return Math.max(0, getPartnersContentHeight() - area.contentH());
    }

    private int getModsContentWidth() {
        int count = data.getThirdPageRecommendedMods().size();
        if (count <= 0) return 1;
        return count * MOD_CARD_W + Math.max(0, count - 1) * MOD_CARD_GAP;
    }

    private int getModsMaxScroll(Rect panel) {
        int viewW = panel.w() - 12;
        return Math.max(0, getModsContentWidth() - viewW);
    }

    private void clampScrolls(Layout l) {
        partnersScroll = Mth.clamp(partnersScroll, 0, getPartnersMaxScroll(l.partners()));
        modsScrollX = Mth.clamp(modsScrollX, 0, getModsMaxScroll(l.mods()));
        supportScroll = Mth.clamp(supportScroll, 0, getSupportMaxScroll(l.support()));
    }

    private int getSupportCardHeight() {
        return 52;
    }

    private int getSupportCardGap() {
        return 8;
    }

    private int getSupportContentHeight() {
        int count = data.getThirdPageSupportLinks().size();
        if (count <= 0) return 1;
        return count * getSupportCardHeight() + Math.max(0, count - 1) * getSupportCardGap();
    }

    private boolean isSupportScrollbarVisible(Rect panel) {
        return getSupportContentHeight() > (panel.h() - 26);
    }

    private int getSupportContentWidth(Rect panel) {
        return panel.w() - 12 - (isSupportScrollbarVisible(panel) ? 8 : 0);
    }

    private int getSupportCardWidth(Rect panel) {
        return Math.max(80, getSupportContentWidth(panel) - 4);
    }

    private Rect getSupportViewport(Rect panel) {
        return new Rect(panel.x() + 6, panel.y() + 20, getSupportContentWidth(panel), panel.h() - 26);
    }

    private int getSupportMaxScroll(Rect panel) {
        return Math.max(0, getSupportContentHeight() - (panel.h() - 26));
    }

    private Rect getSupportScrollbarTrack(Rect panel) {
        return new Rect(panel.x() + panel.w() - 8, panel.y() + 20, 6, panel.h() - 26);
    }

    private void renderSupportScrollbar(GuiGraphics guiGraphics, Rect panel) {
        if (!isSupportScrollbarVisible(panel)) return;
        Rect track = getSupportScrollbarTrack(panel);
        int maxScroll = getSupportMaxScroll(panel);
        if (maxScroll <= 0) return;
        int contentHeight = getSupportContentHeight();
        IntroDrawUtil.drawPanel(guiGraphics, track.x(), track.y(), track.w(), track.h(), 0xFF111822, 0xFF283546);
        int thumbH = getVerticalThumbHeight(track.h(), track.h(), contentHeight);
        int thumbY = track.y() + Math.round((track.h() - thumbH) * (supportScroll / (float) maxScroll));
        IntroDrawUtil.drawPanel(guiGraphics, track.x() + 1, thumbY, track.w() - 2, thumbH, 0xFF4C78B8, 0xFF8DBAFF);
    }

    private Rect getPartnerBannerImageRect(Rect panel) {
        int innerPad = 6;
        int topPad = 18;
        int dotsH = 18;
        int bottomPad = 6;
        return new Rect(panel.x() + innerPad, panel.y() + topPad, panel.w() - innerPad * 2, panel.h() - topPad - dotsH - bottomPad);
    }

    private Rect getPartnerBannerViewport(Rect panel) {
        Rect imageRect = getPartnerBannerImageRect(panel);
        return new Rect(imageRect.x() + 2, imageRect.y() + 2, imageRect.w() - 4, imageRect.h() - 4);
    }

    private int getPartnerBannerDotY(Rect panel) {
        return panel.y() + panel.h() - 11;
    }

    private Rect getPartnerPrevArrowRect(Rect viewport) {
        return new Rect(viewport.x() + 4, viewport.y() + viewport.h() / 2 - 10, 12, 20);
    }

    private Rect getPartnerNextArrowRect(Rect viewport) {
        return new Rect(viewport.x() + viewport.w() - 16, viewport.y() + viewport.h() / 2 - 10, 12, 20);
    }

    private void drawPartnerArrowButton(GuiGraphics guiGraphics, Rect rect, String label, boolean hovered) {
        IntroDrawUtil.drawPanel(guiGraphics, rect.x(), rect.y(), rect.w(), rect.h(), hovered ? 0xCC29486F : 0xAA182638, 0xFFFFFFFF);
        drawCenteredText(guiGraphics, label, rect.x() + rect.w() / 2, rect.y() + 6, 0xFFFFFFFF);
    }

    private void renderHorizontalScrollbar(GuiGraphics guiGraphics, Rect panel, int scroll, int contentWidth) {
        int viewW = panel.w() - 12;
        int maxScroll = Math.max(0, contentWidth - viewW);
        if (maxScroll <= 0) return;
        int trackX = panel.x() + 6;
        int trackY = panel.y() + panel.h() - 8;
        int trackW = panel.w() - 12;
        int trackH = 4;
        guiGraphics.fill(trackX, trackY, trackX + trackW, trackY + trackH, 0xFF1A2635);
        int thumbW = Mth.clamp((int) ((trackW * (double) viewW) / contentWidth), 18, trackW);
        int thumbX = trackX + Math.round((trackW - thumbW) * (scroll / (float) maxScroll));
        guiGraphics.fill(thumbX, trackY, thumbX + thumbW, trackY + trackH, 0xFF70A7FF);
    }

    private int getVerticalThumbHeight(int viewportHeight, int trackHeight, int contentHeight) {
        if (contentHeight <= 0) return trackHeight;
        return Mth.clamp((int) ((trackHeight * (double) viewportHeight) / contentHeight), 18, trackHeight);
    }

    private void drawNavButton(GuiGraphics guiGraphics, Rect rect, String label, boolean active) {
        IntroDrawUtil.drawPanel(guiGraphics, rect.x(), rect.y(), rect.w(), rect.h(), active ? BUTTON_ACTIVE : BUTTON_FILL, active ? 0xFFFFFFFF : BUTTON_BORDER);
        drawCenteredScaledText(guiGraphics, label, rect.x() + rect.w() / 2, rect.y() + 3, 0.70F, 0xFFFFFFFF);
    }

    private void drawStatusBadge(GuiGraphics guiGraphics, int x, int y, int w, String label, int fillColor) {
        int h = 12;
        guiGraphics.fill(x, y, x + w, y + h, fillColor);
        guiGraphics.fill(x, y, x + w, y + 1, 0xFFFFFFFF);
        guiGraphics.fill(x, y + h - 1, x + w, y + h, 0xFF1A1A1A);
        guiGraphics.fill(x, y, x + 1, y + h, 0xFFFFFFFF);
        guiGraphics.fill(x + w - 1, y, x + w, y + h, 0xFF1A1A1A);
        drawCenteredScaledText(guiGraphics, label, x + w / 2, y + 3, 0.65F, 0xFFFFFFFF);
    }

    private void drawCenteredText(GuiGraphics guiGraphics, String label, int centerX, int y, int color) {
        int w = font.width(label);
        guiGraphics.drawString(font, label, centerX - w / 2, y, color, false);
    }

    private void drawCenteredScaledText(GuiGraphics guiGraphics, String label, int centerX, int y, float scale, int color) {
        int scaledW = Math.round(font.width(label) * scale);
        int x = centerX - scaledW / 2;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(font, label, 0, 0, color, false);
        guiGraphics.pose().popPose();
    }

    private int withAlpha(int rgb, float alpha) {
        int a = Mth.clamp((int) (alpha * 255.0F), 0, 255);
        return (a << 24) | (rgb & 0x00FFFFFF);
    }

    private float smootherStep(float t) {
        t = Mth.clamp(t, 0.0F, 1.0F);
        return t * t * t * (t * (t * 6.0F - 15.0F) + 10.0F);
    }

    private void switchPartnerBanner(int newIndex) {
        List<IntroExternalBanner> banners = data.getThirdPagePartners();
        if (banners.isEmpty()) return;
        newIndex = Mth.clamp(newIndex, 0, banners.size() - 1);
        if (newIndex == partnerBannerIndex) return;
        partnerBannerPreviousIndex = partnerBannerIndex;
        partnerBannerIndex = newIndex;
        partnerBannerTransitionStartAt = Util.getMillis();
    }

    private float getPartnerBannerTransitionProgress(long now) {
        if (partnerBannerPreviousIndex == -1 || partnerBannerTransitionStartAt < 0L) return 1.0F;
        float t = (now - partnerBannerTransitionStartAt) / (float) PARTNER_BANNER_TRANSITION_MS;
        return Mth.clamp(t, 0.0F, 1.0F);
    }

    private <T> T firstNonNull(T a, T b) {
        return a != null ? a : b;
    }

    private ScrollArea createScrollArea(int panelX, int panelY, int panelW, int panelH) {
        int contentX = panelX + 6;
        int contentY = panelY + 20;
        int contentW = panelW - 18;
        int contentH = panelH - 26;
        int scrollX = panelX + panelW - 8;
        int scrollY = contentY;
        int scrollW = 6;
        int scrollH = contentH;
        return new ScrollArea(panelX, panelY, panelW, panelH, contentX, contentY, contentW, contentH, scrollX, scrollY, scrollW, scrollH);
    }

    private Layout layout() {
        int screenPad = 8;
        int maxFrameW = 720;
        int maxFrameH = 390;
        int frameW = Math.min(width - screenPad * 2, maxFrameW);
        int frameH = Math.min(height - screenPad * 2, maxFrameH);
        int frameX = (width - frameW) / 2;
        int frameY = (height - frameH) / 2;
        Rect frame = new Rect(frameX, frameY, frameW, frameH);
        Rect nav = new Rect(frameX + 8, frameY + 6, frameW - 16, HEADER_H);
        Rect backBtn = new Rect(nav.x() + 6, nav.y() + 3, 34, 12);
        int bodyX = frameX + 8;
        int bodyY = nav.y() + nav.h() + 6;
        int bodyW = frameW - 16;
        int bodyH = frameH - (bodyY - frameY) - 8;
        int topH = 175;
        int leftW = 255;
        int centerW = 165;
        int rightW = bodyW - leftW - centerW - GAP * 2;
        Rect partners = new Rect(bodyX, bodyY, leftW, topH);
        Rect version = new Rect(bodyX + leftW + GAP, bodyY, centerW, topH);
        Rect support = new Rect(bodyX + leftW + GAP + centerW + GAP, bodyY, rightW, topH);
        Rect mods = new Rect(bodyX, bodyY + topH + GAP, bodyW, bodyH - topH - GAP);
        return new Layout(frame, nav, backBtn, partners, version, support, mods);
    }

    private boolean isInside(double mouseX, double mouseY, Rect rect) {
        return mouseX >= rect.x() && mouseX < rect.x() + rect.w() && mouseY >= rect.y() && mouseY < rect.y() + rect.h();
    }

    private record Layout(Rect frame, Rect nav, Rect backBtn, Rect partners, Rect version, Rect support, Rect mods) {
    }

    private record Rect(int x, int y, int w, int h) {
    }

    private record ScrollArea(int panelX, int panelY, int panelW, int panelH, int contentX, int contentY, int contentW,
                              int contentH, int scrollX, int scrollY, int scrollW, int scrollH) {
    }

    private static final class AutoScrollState {
        private float exactScroll;
        private long lastUserInputAt;
        private long edgePauseUntil;
        private boolean autoActive;
        private int direction = 1;

        void init(long now, int currentScroll) {
            this.exactScroll = currentScroll;
            this.lastUserInputAt = now;
            this.edgePauseUntil = 0L;
            this.autoActive = false;
            this.direction = 1;
        }

        void onUserInput(long now, int currentScroll) {
            this.exactScroll = currentScroll;
            this.lastUserInputAt = now;
            this.edgePauseUntil = 0L;
            this.autoActive = false;
        }

        int update(long now, long deltaMs, int currentScroll, int maxScroll) {
            if (maxScroll <= 0) {
                exactScroll = 0.0F;
                autoActive = false;
                direction = 1;
                return 0;
            }
            if (!autoActive) {
                exactScroll = currentScroll;
                if (now - lastUserInputAt < AUTO_SCROLL_IDLE_MS) return currentScroll;
                autoActive = true;
                direction = currentScroll >= maxScroll ? -1 : 1;
                edgePauseUntil = now + 350L;
            }
            if (now < edgePauseUntil) return Mth.clamp(Math.round(exactScroll), 0, maxScroll);
            float delta = AUTO_SCROLL_SPEED_PX_PER_SEC * (deltaMs / 1000.0F);
            exactScroll += direction * delta;
            if (exactScroll <= 0.0F) {
                exactScroll = 0.0F;
                direction = 1;
                edgePauseUntil = now + AUTO_SCROLL_EDGE_PAUSE_MS;
            } else if (exactScroll >= maxScroll) {
                exactScroll = maxScroll;
                direction = -1;
                edgePauseUntil = now + AUTO_SCROLL_EDGE_PAUSE_MS;
            }
            return Mth.clamp(Math.round(exactScroll), 0, maxScroll);
        }
    }
}
