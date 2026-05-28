package com.denfop.client.intro;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.*;

public class IntroDashboardSecondScreen extends Screen {

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
    private static final int RED = 0xFFD84A4A;
    private static final int HEADER_H = 18;
    private static final int GAP = 6;
    private static final int FORMER_HEAD_SIZE = 32;
    private static final float NAME_SCALE = 0.75F;
    private static final float ROLE_SCALE = 0.75F;
    private static final int FORMER_GAP = 8;
    private static final int BILLBOARD_SWITCH_MS = 5000;
    private static final int DOT_SIZE = 6;
    private static final int DOT_GAP = 5;
    private static final int ORANGE = 0xFFF0A020;
    private static final float FAQ_ANIM_SPEED = 0.10F;
    private static final int FAQ_CATEGORY_TEXT = 0xFFF4F8FF;
    private static final int FAQ_CATEGORY_LINE = 0xFF3A506E;
    private static final int FAQ_ROW_FILL = 0xFF172231;
    private static final int FAQ_ROW_BORDER = 0xFF35506F;
    private static final int FAQ_ROW_HOVER_FILL = 0xFF1D2C3F;
    private static final int FAQ_ROW_HOVER_BORDER = 0xFF6FA8FF;
    private static final int FAQ_ROW_OPEN_FILL = 0xFF21324A;
    private static final int FAQ_ROW_OPEN_BORDER = 0xFF8BB9FF;
    private static final int FAQ_ANSWER_FILL = 0xFF111A26;
    private static final int FAQ_ANSWER_BORDER = 0xFF2C415A;
    private static final int FAQ_ACCENT = 0xFF5EA2FF;
    private static final long BILLBOARD_TRANSITION_MS = 550L;
    private static final long AUTO_SCROLL_IDLE_MS = 6000L;
    private static final long AUTO_SCROLL_EDGE_PAUSE_MS = 1200L;
    private static final float AUTO_SCROLL_SPEED_PX_PER_SEC = 18.0F;

    private final IntroDashboardData data;
    private final IntroImageManager imageManager;
    private final IntroHeadProfileCache headProfileCache;
    private final List<LanguageSupportEntry> languageEntries;
    private final List<IntroFaqEntry> faqEntries;
    private final List<IntroFormerMember> formerWorkers;
    private final List<IntroBillboardSlide> billboardSlides;
    private final List<String> currentChangelogFallbackLines;
    private final List<String> fullChangelogFallbackLines;
    private final Set<Integer> expandedFaq = new HashSet<>();
    private final Map<Integer, Float> faqAnimation = new HashMap<>();
    private final AutoScrollState formerAutoScroll = new AutoScrollState();
    private final AutoScrollState fullChangelogAutoScroll = new AutoScrollState();
    private final AutoScrollState changelogPreviewAutoScroll = new AutoScrollState();

    private int faqScroll;
    private int formerScroll;
    private int changelogPreviewScroll;
    private int fullChangelogScroll;
    private int billboardIndex;
    private long nextBillboardSwitchAt;
    private boolean fullChangelogOpen;
    private DragTarget dragTarget = DragTarget.NONE;
    private double scrollbarGrabOffset;
    private int languageScroll;
    private int billboardPreviousIndex = -1;
    private long billboardTransitionStartAt = -1L;
    private long lastAutoScrollTickAt;

    public IntroDashboardSecondScreen(IntroDashboardData data) {
        super(Component.translatable("intro.page2.screen_title"));
        this.data = data;
        this.imageManager = IntroImageManager.getInstance();
        this.headProfileCache = IntroHeadProfileCache.getInstance();
        this.languageEntries = buildLanguageEntries();
        this.faqEntries = data.getFaqEntries();
        this.formerWorkers = data.getFormerMembers();
        this.billboardSlides = data.getBillboardSlides();
        this.currentChangelogFallbackLines = data.getCurrentChangelogFallbackLines();
        this.fullChangelogFallbackLines = data.getFullChangelogFallbackLines();
    }

    private String text(String value) {
        return IntroLocalization.text(value);
    }

    private String tr(String key) {
        return IntroLocalization.tr(key);
    }

    private Component component(String value) {
        return Component.literal(text(value));
    }

    private List<String> getPreviewChangelogSource() {
        List<String> source = IntroChangelogCache.getCurrentChangelogLines(currentChangelogFallbackLines);
        if (source == null || source.isEmpty()) {
            return List.of(tr("intro.changelog.no_data"));
        }
        return source;
    }

    private List<String> getFullChangelogSource() {
        List<String> source = IntroChangelogCache.getFullChangelogLines(fullChangelogFallbackLines);
        if (source == null || source.isEmpty()) {
            return List.of(tr("intro.changelog.no_data"));
        }
        return source;
    }

    @Override
    protected void init() {
        long now = Util.getMillis();
        IntroChangelogCache.request();
        nextBillboardSwitchAt = now + BILLBOARD_SWITCH_MS;
        lastAutoScrollTickAt = now;
        formerAutoScroll.init(now, formerScroll);
        changelogPreviewAutoScroll.init(now, changelogPreviewScroll);
        fullChangelogAutoScroll.init(now, fullChangelogScroll);
        clampAll(layout());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        Layout l = layout();
        updateIdleAutoScroll(l);
        clampAll(l);

        if (billboardPreviousIndex != -1 && Util.getMillis() - billboardTransitionStartAt >= BILLBOARD_TRANSITION_MS) {
            billboardPreviousIndex = -1;
            billboardTransitionStartAt = -1L;
        }

        if (!fullChangelogOpen && !billboardSlides.isEmpty()) {
            long now = Util.getMillis();
            if (now >= nextBillboardSwitchAt) {
                switchBillboard((billboardIndex + 1) % billboardSlides.size());
                nextBillboardSwitchAt = now + BILLBOARD_SWITCH_MS;
            }
        }

        updateFaqAnimations();
    }

    private void updateIdleAutoScroll(Layout l) {
        long now = Util.getMillis();
        long deltaMs = Math.max(0L, now - lastAutoScrollTickAt);
        lastAutoScrollTickAt = now;

        int formerMax = Math.max(0, getFormerWorkersContentHeight(l.former().contentW()) - l.former().contentH());
        formerScroll = formerAutoScroll.update(now, deltaMs, formerScroll, formerMax);

        int previewMax = Math.max(0, getWrappedLinesHeight(buildWrappedLines(getPreviewChangelogSource(), l.changelog().contentW())) - l.changelog().contentH());
        changelogPreviewScroll = changelogPreviewAutoScroll.update(now, deltaMs, changelogPreviewScroll, previewMax);

        if (fullChangelogOpen) {
            Rect modal = getModalRect(l);
            ScrollArea modalArea = createScrollArea(modal.x(), modal.y(), modal.w(), modal.h());
            int modalMax = Math.max(0, getWrappedLinesHeight(buildWrappedLines(getFullChangelogSource(), modalArea.contentW())) - modalArea.contentH());
            fullChangelogScroll = fullChangelogAutoScroll.update(now, deltaMs, fullChangelogScroll, modalMax);
        }
    }

    private void updateFaqAnimations() {
        for (int i = 0; i < faqEntries.size(); i++) {
            float current = faqAnimation.getOrDefault(i, expandedFaq.contains(i) ? 1.0F : 0.0F);
            float target = expandedFaq.contains(i) ? 1.0F : 0.0F;
            float next = approach(current, target, FAQ_ANIM_SPEED);
            if (Math.abs(next - target) < 0.001F) next = target;
            faqAnimation.put(i, next);
        }
    }

    private float approach(float current, float target, float speed) {
        if (current < target) return Math.min(current + speed, target);
        if (current > target) return Math.max(current - speed, target);
        return current;
    }

    private float getFaqProgress(int index) {
        return faqAnimation.getOrDefault(index, expandedFaq.contains(index) ? 1.0F : 0.0F);
    }

    private float easeFaq(float t) {
        return 1.0F - (float) Math.pow(1.0F - t, 3.0F);
    }

    private int getAnimatedFaqAnswerHeight(int index, int width) {
        IntroFaqEntry entry = faqEntries.get(index);
        List<FormattedCharSequence> lines = font.split(Component.literal(text(entry.answer())), width - 12);
        int fullHeight = lines.size() * (font.lineHeight + 1) + 6;
        float progress = easeFaq(getFaqProgress(index));
        return Math.max(0, Math.round(fullHeight * progress));
    }

    private void notifyFormerUserInput() {
        formerAutoScroll.onUserInput(Util.getMillis(), formerScroll);
    }

    private void notifyFullChangelogUserInput() {
        fullChangelogAutoScroll.onUserInput(Util.getMillis(), fullChangelogScroll);
    }

    private void notifyChangelogPreviewUserInput() {
        changelogPreviewAutoScroll.onUserInput(Util.getMillis(), changelogPreviewScroll);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.fill(0, 0, width, height, 0xA0101318);
        Layout l = layout();
        clampAll(l);

        IntroDrawUtil.drawPanel(guiGraphics, l.frame().x(), l.frame().y(), l.frame().w(), l.frame().h(), FRAME_FILL, FRAME_BORDER);
        renderPageHeader(guiGraphics, l);

        List<Component> tooltip = null;
        renderLanguagesPanel(guiGraphics, l.languages());
        tooltip = firstNonNull(tooltip, renderFaqPanel(guiGraphics, l.faq(), mouseX, mouseY));
        tooltip = firstNonNull(tooltip, renderFormerWorkersPanel(guiGraphics, l.former(), mouseX, mouseY));
        tooltip = firstNonNull(tooltip, renderBillboardPanel(guiGraphics, l.billboard(), mouseX, mouseY));
        renderChangelogPanel(guiGraphics, l.changelog());

        if (fullChangelogOpen) {
            renderFullChangelogModal(guiGraphics, l);
        }


        if (!fullChangelogOpen && tooltip != null) {
            guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    private void renderPageHeader(GuiGraphics guiGraphics, Layout l) {
        IntroDrawUtil.drawPanel(guiGraphics, l.nav().x(), l.nav().y(), l.nav().w(), l.nav().h(), PANEL_FILL_DARK, PANEL_BORDER);
        drawNavButton(guiGraphics, getPrevPageButtonPage2(l), "<<", false);
        drawNavButton(guiGraphics, getNextPageButtonPage2(l), ">>", false);
    }

    private Rect getPrevPageButtonPage2(Layout l) {
        return new Rect(l.nav().x() + 6, l.nav().y() + 3, 46, 12);
    }

    private Rect getNextPageButtonPage2(Layout l) {
        return new Rect(l.nav().x() + l.nav().w() - 52, l.nav().y() + 3, 46, 12);
    }

    private void drawNavButton(GuiGraphics guiGraphics, Rect rect, String label, boolean active) {
        IntroDrawUtil.drawPanel(guiGraphics, rect.x(), rect.y(), rect.w(), rect.h(), active ? BUTTON_ACTIVE : BUTTON_FILL, active ? 0xFFFFFFFF : BUTTON_BORDER);
        drawCenteredScaledText(guiGraphics, label, rect.x() + rect.w() / 2, rect.y() + 3, 0.65F, 0xFFFFFFFF);
    }

    private void renderLanguagesPanel(GuiGraphics guiGraphics, Rect panel) {
        IntroDrawUtil.drawPanel(guiGraphics, panel.x(), panel.y(), panel.w(), panel.h(), PANEL_FILL, PANEL_BORDER);
        drawCenteredText(guiGraphics, tr("intro.page2.language_support"), panel.x() + panel.w() / 2, panel.y() + 4, TEXT_MAIN);

        String currentCode = Minecraft.getInstance().getLanguageManager().getSelected();
        Rect viewport = getLanguagesViewport(panel);
        int contentW = getLanguagesContentWidth(panel);
        int rowH = getLanguageRowHeight();
        int rowGap = getLanguageRowGap();

        guiGraphics.enableScissor(viewport.x(), viewport.y(), viewport.x() + viewport.w(), viewport.y() + viewport.h());
        int rowY = viewport.y() - languageScroll;
        long now = Util.getMillis();

        for (LanguageSupportEntry entry : languageEntries) {
            Rect row = new Rect(viewport.x(), rowY, contentW, rowH);
            if (row.y() + row.h() >= viewport.y() && row.y() <= viewport.y() + viewport.h()) {
                boolean current = entry.code().equals(currentCode);
                int rowFill = current ? 0xFF1A2940 : 0xFF131C28;
                int rowBorder = current ? 0xFF75AFFF : 0xFF304154;
                int statusColor = getLanguageStateColor(entry.state());
                IntroDrawUtil.drawPanel(guiGraphics, row.x(), row.y(), row.w(), row.h(), rowFill, rowBorder);
                guiGraphics.fill(row.x() + 2, row.y() + 2, row.x() + 6, row.y() + row.h() - 2, statusColor);

                int badgeW = 30;
                int barW = 34;
                int textX = row.x() + 10;
                int textY = row.y() + 4;
                int barX = row.x() + row.w() - badgeW - barW - 8;
                int barY = row.y() + 6;
                int badgeX = row.x() + row.w() - badgeW - 3;
                int badgeY = row.y() + 3;
                int textW = barX - textX - 6;
                drawMarqueeText(guiGraphics, entry.displayName(), textX, textY, textW, TEXT_MAIN, now);
                drawLanguageProgressBar(guiGraphics, barX, barY, barW, 4, getLanguageProgress(entry.state()), statusColor);
                drawLanguageSmallBadge(guiGraphics, badgeX, badgeY, badgeW, getLanguageStateText(entry.state()), statusColor);
            }
            rowY += rowH + rowGap;
        }
        guiGraphics.disableScissor();
        renderLanguagesScrollbar(guiGraphics, panel);
    }

    private String getLanguageStateText(LanguageSupportState state) {
        return switch (state) {
            case FULL -> tr("intro.language.state.full");
            case PARTIAL -> tr("intro.language.state.partial");
            case UNSUPPORTED -> tr("intro.language.state.unsupported");
        };
    }

    private int getLanguageStateColor(LanguageSupportState state) {
        return switch (state) {
            case FULL -> GREEN;
            case PARTIAL -> ORANGE;
            case UNSUPPORTED -> RED;
        };
    }

    private float getLanguageProgress(LanguageSupportState state) {
        return switch (state) {
            case FULL -> 1.0F;
            case PARTIAL -> 0.55F;
            case UNSUPPORTED -> 0.08F;
        };
    }

    private void drawLanguageProgressBar(GuiGraphics guiGraphics, int x, int y, int w, int h, float progress, int fillColor) {
        progress = Mth.clamp(progress, 0.0F, 1.0F);
        guiGraphics.fill(x, y, x + w, y + h, 0xFF0E1620);
        guiGraphics.fill(x, y, x + w, y + 1, 0xFF3A4D66);
        guiGraphics.fill(x, y + h - 1, x + w, y + h, 0xFF1E2A38);
        guiGraphics.fill(x, y, x + 1, y + h, 0xFF3A4D66);
        guiGraphics.fill(x + w - 1, y, x + w, y + h, 0xFF1E2A38);
        int fillW = Math.max(1, Math.round((w - 2) * progress));
        guiGraphics.fill(x + 1, y + 1, x + 1 + fillW, y + h - 1, fillColor);
    }

    private void drawLanguageSmallBadge(GuiGraphics guiGraphics, int x, int y, int w, String label, int fillColor) {
        int h = 10;
        guiGraphics.fill(x, y, x + w, y + h, fillColor);
        guiGraphics.fill(x, y, x + w, y + 1, 0xFFFFFFFF);
        guiGraphics.fill(x, y + h - 1, x + w, y + h, 0xFF1A1A1A);
        guiGraphics.fill(x, y, x + 1, y + h, 0xFFFFFFFF);
        guiGraphics.fill(x + w - 1, y, x + w, y + h, 0xFF1A1A1A);
        drawCenteredScaledText(guiGraphics, label, x + w / 2, y + 2, 0.55F, 0xFFFFFFFF);
    }

    private List<Component> renderFaqPanel(GuiGraphics guiGraphics, ScrollArea panel, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, panel.panelX(), panel.panelY(), panel.panelW(), panel.panelH(), PANEL_FILL, PANEL_BORDER);
        drawCenteredText(guiGraphics, tr("intro.page2.faq"), panel.panelX() + panel.panelW() / 2, panel.panelY() + 4, TEXT_MAIN);

        guiGraphics.enableScissor(panel.contentX(), panel.contentY(), panel.contentX() + panel.contentW(), panel.contentY() + panel.contentH());
        int y = panel.contentY() - faqScroll;
        String lastMain = null;
        List<Component> hovered = null;

        for (int i = 0; i < faqEntries.size(); i++) {
            IntroFaqEntry entry = faqEntries.get(i);
            String main = text(entry.main());
            if (!main.equals(lastMain)) {
                guiGraphics.drawString(font, main, panel.contentX(), y, FAQ_CATEGORY_TEXT, false);
                int lineX = panel.contentX() + font.width(main) + 6;
                int lineY = y + font.lineHeight / 2 + 1;
                guiGraphics.fill(lineX, lineY, panel.contentX() + panel.contentW(), lineY + 1, FAQ_CATEGORY_LINE);
                y += font.lineHeight + 6;
                lastMain = main;
            }

            float progress = easeFaq(getFaqProgress(i));
            Rect row = new Rect(panel.contentX(), y, panel.contentW(), 16);
            boolean hoveredRow = isInside(mouseX, mouseY, row);
            boolean opened = progress > 0.001F;
            int rowFill = opened ? FAQ_ROW_OPEN_FILL : (hoveredRow ? FAQ_ROW_HOVER_FILL : FAQ_ROW_FILL);
            int rowBorder = opened ? FAQ_ROW_OPEN_BORDER : (hoveredRow ? FAQ_ROW_HOVER_BORDER : FAQ_ROW_BORDER);
            IntroDrawUtil.drawPanel(guiGraphics, row.x(), row.y(), row.w(), row.h(), rowFill, rowBorder);
            guiGraphics.fill(row.x() + 1, row.y() + 1, row.x() + 3, row.y() + row.h() - 1, FAQ_ACCENT);
            guiGraphics.drawString(font, opened ? "v" : ">", row.x() + 6, row.y() + 4, 0xFFFFFFFF, false);
            String q = font.plainSubstrByWidth(text(entry.question()), row.w() - 24);
            guiGraphics.drawString(font, q, row.x() + 16, row.y() + 4, TEXT_MAIN, false);

            if (hoveredRow) hovered = List.of(component(entry.question()));
            y += 16 + 4;

            List<FormattedCharSequence> lines = font.split(Component.literal(text(entry.answer())), panel.contentW() - 12);
            int animatedHeight = getAnimatedFaqAnswerHeight(i, panel.contentW());
            if (animatedHeight > 0) {
                int answerX = panel.contentX() + 4;
                int answerY = y;
                int answerW = panel.contentW() - 8;
                IntroDrawUtil.drawPanel(guiGraphics, answerX, answerY, answerW, animatedHeight, FAQ_ANSWER_FILL, FAQ_ANSWER_BORDER);
                int textY = answerY + 4;
                int textBottom = answerY + animatedHeight - 4;
                float textAlpha = Mth.clamp(progress, 0.0F, 1.0F);
                for (FormattedCharSequence line : lines) {
                    if (textY + font.lineHeight <= textBottom)
                        guiGraphics.drawString(font, line, answerX + 5, textY, withAlpha(TEXT_SUB, textAlpha), false);
                    else break;
                    textY += font.lineHeight + 1;
                }
                y += animatedHeight + 5;
            }
        }
        guiGraphics.disableScissor();
        renderScrollBar(guiGraphics, panel, faqScroll, getFaqContentHeight(panel.contentW()));
        return hovered;
    }

    private List<Component> renderFormerWorkersPanel(GuiGraphics guiGraphics, ScrollArea panel, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, panel.panelX(), panel.panelY(), panel.panelW(), panel.panelH(), PANEL_FILL, PANEL_BORDER);
        drawCenteredText(guiGraphics, tr("intro.page2.former_workers"), panel.panelX() + panel.panelW() / 2, panel.panelY() + 4, TEXT_MAIN);

        guiGraphics.enableScissor(panel.contentX(), panel.contentY(), panel.contentX() + panel.contentW(), panel.contentY() + panel.contentH());
        int y = panel.contentY() - formerScroll;
        int centerX = panel.panelX() + panel.panelW() / 2;
        List<Component> hovered = null;

        for (IntroFormerMember entry : formerWorkers) {
            int itemH = getFormerWorkerItemHeight(panel.contentW(), entry);
            int headX = centerX - FORMER_HEAD_SIZE / 2;
            int headY = y;
            if (headProfileCache.hasResolvedHead(entry.nickname())) {
                headProfileCache.renderHead(guiGraphics, entry.nickname(), headX, headY, FORMER_HEAD_SIZE);
            } else {
                IntroDrawUtil.drawGeneratedBadge(guiGraphics, font, entry.nickname(), headX, headY, FORMER_HEAD_SIZE);
                headProfileCache.renderHead(guiGraphics, entry.nickname(), headX, headY, FORMER_HEAD_SIZE);
            }

            String displayName = font.plainSubstrByWidth(text(entry.displayName()), Math.max(1, (int) ((panel.contentW() - 6) / NAME_SCALE)));
            int nameY = headY + FORMER_HEAD_SIZE + 4;
            drawCenteredScaledText(guiGraphics, displayName, centerX, nameY, NAME_SCALE, TEXT_MAIN);
            int nameH = Math.max(8, Math.round(font.lineHeight * NAME_SCALE));
            int badgeY = nameY + nameH + 4;
            drawWrapBadge(guiGraphics, centerX, badgeY, panel.contentW() - 6, text(entry.role()), getFormerRoleBadgeFillColor(entry.role()), ROLE_SCALE);

            if (isInside(mouseX, mouseY, new Rect(panel.contentX(), y, panel.contentW(), itemH))) {
                hovered = List.of(component(entry.displayName()), component(entry.role()), Component.literal(entry.period()));
            }
            y += itemH + FORMER_GAP;
        }
        guiGraphics.disableScissor();
        if (!formerAutoScroll.shouldHideScrollbar(Util.getMillis()))
            renderScrollBar(guiGraphics, panel, formerScroll, getFormerWorkersContentHeight(panel.contentW()));
        return hovered;
    }

    private List<Component> renderBillboardPanel(GuiGraphics guiGraphics, Rect panel, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, panel.x(), panel.y(), panel.w(), panel.h(), PANEL_FILL, PANEL_BORDER);
        drawCenteredText(guiGraphics, tr("intro.page2.screenshots_billboard"), panel.x() + panel.w() / 2, panel.y() + 4, TEXT_MAIN);

        if (billboardSlides.isEmpty()) {
            guiGraphics.drawString(font, tr("intro.page2.no_slides"), panel.x() + 8, panel.y() + 20, TEXT_SUB, false);
            return null;
        }

        Rect imageRect = new Rect(panel.x() + 6, panel.y() + 18, panel.w() - 12, panel.h() - 36);
        IntroDrawUtil.drawPanel(guiGraphics, imageRect.x(), imageRect.y(), imageRect.w(), imageRect.h(), 0xFF0C121B, 0xFF243548);
        Rect viewport = new Rect(imageRect.x() + 1, imageRect.y() + 1, imageRect.w() - 2, imageRect.h() - 2);
        long now = Util.getMillis();
        guiGraphics.enableScissor(viewport.x(), viewport.y(), viewport.x() + viewport.w(), viewport.y() + viewport.h());
        if (billboardPreviousIndex != -1) {
            float t = smootherStep(getBillboardTransitionProgress(now));
            renderBillboardSlide(guiGraphics, billboardSlides.get(Mth.clamp(billboardPreviousIndex, 0, billboardSlides.size() - 1)), viewport, 1.0F - t);
            renderBillboardSlide(guiGraphics, billboardSlides.get(Mth.clamp(billboardIndex, 0, billboardSlides.size() - 1)), viewport, t);
        } else {
            renderBillboardSlide(guiGraphics, billboardSlides.get(Mth.clamp(billboardIndex, 0, billboardSlides.size() - 1)), viewport, 1.0F);
        }
        guiGraphics.disableScissor();

        int totalW = billboardSlides.size() * DOT_SIZE + Math.max(0, billboardSlides.size() - 1) * DOT_GAP;
        int startX = panel.x() + (panel.w() - totalW) / 2;
        int dotY = panel.y() + panel.h() - 11;
        for (int i = 0; i < billboardSlides.size(); i++) {
            int x = startX + i * (DOT_SIZE + DOT_GAP);
            int fill = i == billboardIndex ? BUTTON_ACTIVE : 0xFF2C3C52;
            IntroDrawUtil.drawPanel(guiGraphics, x, dotY, DOT_SIZE, DOT_SIZE, fill, 0xFFFFFFFF);
        }

        if (isInside(mouseX, mouseY, viewport)) {
            IntroBillboardSlide currentSlide = billboardSlides.get(Mth.clamp(billboardIndex, 0, billboardSlides.size() - 1));
            return List.of(component(currentSlide.title()));
        }
        return null;
    }

    private void renderBillboardSlide(GuiGraphics guiGraphics, IntroBillboardSlide slide, Rect viewport, float alpha) {
        alpha = Mth.clamp(alpha, 0.0F, 1.0F);
        if (alpha <= 0.001F) return;
        IntroRemoteImage image = imageManager.get(slide.source());
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        if (image.isReady()) {
            IntroDrawUtil.drawFittedImageBiased(guiGraphics, image, viewport.x(), viewport.y(), viewport.w(), viewport.h(), getAutoBillboardVerticalBias(image, viewport));
        } else {
            guiGraphics.fill(viewport.x(), viewport.y(), viewport.x() + viewport.w(), viewport.y() + viewport.h(), withAlpha(0xFF1A2330, alpha));
            String status = image.isFailed() ? tr("intro.image.unavailable") : tr("intro.image.loading");
            drawCenteredText(guiGraphics, status, viewport.x() + viewport.w() / 2, viewport.y() + viewport.h() / 2 - 4, withAlpha(TEXT_SUB, alpha));
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private float getAutoBillboardVerticalBias(IntroRemoteImage image, Rect viewport) {
        if (!image.isReady()) return 0.5F;
        float scale = Math.min(viewport.w() / (float) image.getWidth(), viewport.h() / (float) image.getHeight());
        int fittedHeight = Math.max(1, Math.round(image.getHeight() * scale));
        int freeY = viewport.h() - fittedHeight;
        if (freeY <= 4) return 0.5F;
        float aspect = image.getWidth() / (float) image.getHeight();
        return aspect >= 1.45F ? 0.18F : 0.5F;
    }

    private void renderChangelogPanel(GuiGraphics guiGraphics, ScrollArea panel) {
        IntroDrawUtil.drawPanel(guiGraphics, panel.panelX(), panel.panelY(), panel.panelW(), panel.panelH(), PANEL_FILL, PANEL_BORDER);
        drawCenteredText(guiGraphics, tr("intro.page2.changelog"), panel.panelX() + panel.panelW() / 2, panel.panelY() + 4, TEXT_MAIN);
        Rect button = getChangelogButton(panel);
        IntroDrawUtil.drawPanel(guiGraphics, button.x(), button.y(), button.w(), button.h(), BUTTON_FILL, BUTTON_BORDER);
        drawCenteredScaledText(guiGraphics, tr("intro.page2.view_full"), button.x() + button.w() / 2, button.y() + 3, 0.70F, 0xFFFFFFFF);

        List<FormattedCharSequence> previewLines = buildWrappedLines(getPreviewChangelogSource(), panel.contentW());
        guiGraphics.enableScissor(panel.contentX(), panel.contentY(), panel.contentX() + panel.contentW(), panel.contentY() + panel.contentH());
        int y = panel.contentY() - changelogPreviewScroll;
        for (FormattedCharSequence line : previewLines) {
            guiGraphics.drawString(font, line, panel.contentX(), y, TEXT_SUB, false);
            y += font.lineHeight + 2;
        }
        guiGraphics.disableScissor();
        if (!changelogPreviewAutoScroll.shouldHideScrollbar(Util.getMillis()))
            renderScrollBar(guiGraphics, panel, changelogPreviewScroll, getWrappedLinesHeight(previewLines));
    }

    private void renderFullChangelogModal(GuiGraphics guiGraphics, Layout l) {
        guiGraphics.fill(0, 0, width, height, 0x90000000);
        Rect modal = getModalRect(l);
        IntroDrawUtil.drawPanel(guiGraphics, modal.x(), modal.y(), modal.w(), modal.h(), 0xFF111822, 0xFF6A88B4);
        drawCenteredText(guiGraphics, tr("intro.page2.full_changelog"), modal.x() + modal.w() / 2, modal.y() + 6, TEXT_MAIN);
        Rect close = getModalCloseRect(modal);
        IntroDrawUtil.drawPanel(guiGraphics, close.x(), close.y(), close.w(), close.h(), 0xFF5A2630, 0xFFFFFFFF);
        drawCenteredText(guiGraphics, "X", close.x() + close.w() / 2, close.y() + 3, 0xFFFFFFFF);

        ScrollArea panel = createScrollArea(modal.x(), modal.y(), modal.w(), modal.h());
        List<FormattedCharSequence> lines = buildWrappedLines(getFullChangelogSource(), panel.contentW());
        guiGraphics.enableScissor(panel.contentX(), panel.contentY(), panel.contentX() + panel.contentW(), panel.contentY() + panel.contentH());
        int y = panel.contentY() - fullChangelogScroll;
        for (FormattedCharSequence line : lines) {
            guiGraphics.drawString(font, line, panel.contentX(), y, TEXT_MAIN, false);
            y += font.lineHeight + 2;
        }
        guiGraphics.disableScissor();
        if (!fullChangelogAutoScroll.shouldHideScrollbar(Util.getMillis()))
            renderScrollBar(guiGraphics, panel, fullChangelogScroll, getWrappedLinesHeight(lines));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double delta) {
        Layout l = layout();
        clampAll(l);
        if (fullChangelogOpen) {
            Rect modal = getModalRect(l);
            if (isInside(mouseX, mouseY, modal)) {
                notifyFullChangelogUserInput();
                ScrollArea modalArea = createScrollArea(modal.x(), modal.y(), modal.w(), modal.h());
                int max = Math.max(0, getWrappedLinesHeight(buildWrappedLines(getFullChangelogSource(), modalArea.contentW())) - modalArea.contentH());
                fullChangelogScroll = Mth.clamp(fullChangelogScroll - (int) (delta * 18), 0, max);
                return true;
            }
            return true;
        }
        if (isInside(mouseX, mouseY, l.languages())) {
            languageScroll = Mth.clamp(languageScroll - (int) (delta * 18), 0, getLanguagesMaxScroll(l.languages()));
            return true;
        }
        if (isInside(mouseX, mouseY, l.faq().toRect())) {
            int max = Math.max(0, getFaqContentHeight(l.faq().contentW()) - l.faq().contentH());
            faqScroll = Mth.clamp(faqScroll - (int) (delta * 18), 0, max);
            return true;
        }
        if (isInside(mouseX, mouseY, l.former().toRect())) {
            notifyFormerUserInput();
            int max = Math.max(0, getFormerWorkersContentHeight(l.former().contentW()) - l.former().contentH());
            formerScroll = Mth.clamp(formerScroll - (int) (delta * 18), 0, max);
            return true;
        }
        if (isInside(mouseX, mouseY, l.changelog().toRect())) {
            notifyChangelogPreviewUserInput();
            int max = Math.max(0, getWrappedLinesHeight(buildWrappedLines(getPreviewChangelogSource(), l.changelog().contentW())) - l.changelog().contentH());
            changelogPreviewScroll = Mth.clamp(changelogPreviewScroll - (int) (delta * 18), 0, max);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, deltaX, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Layout l = layout();
        clampAll(l);
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        if (fullChangelogOpen) {
            Rect modal = getModalRect(l);
            Rect close = getModalCloseRect(modal);
            notifyFullChangelogUserInput();
            if (isInside(mouseX, mouseY, close)) {
                fullChangelogOpen = false;
                dragTarget = DragTarget.NONE;
                return true;
            }
            ScrollArea modalArea = createScrollArea(modal.x(), modal.y(), modal.w(), modal.h());
            int modalContentHeight = getWrappedLinesHeight(buildWrappedLines(getFullChangelogSource(), modalArea.contentW()));
            if (tryStartDrag(mouseX, mouseY, modalArea, DragTarget.MODAL_CHANGELOG, fullChangelogScroll, modalContentHeight))
                return true;
            return isInside(mouseX, mouseY, modal);
        }

        if (isInside(mouseX, mouseY, getPrevPageButtonPage2(l))) {
            minecraft.setScreen(new IntroDashboardScreen(data));
            return true;
        }
        if (isInside(mouseX, mouseY, getNextPageButtonPage2(l))) {
            minecraft.setScreen(new IntroDashboardThirdScreen(data));
            return true;
        }
        if (handleFaqToggleClick(l.faq(), mouseX, mouseY)) {
            clampAll(l);
            return true;
        }
        if (handleBillboardClick(l.billboard(), mouseX, mouseY)) return true;
        if (isInside(mouseX, mouseY, getChangelogButton(l.changelog()))) {
            fullChangelogOpen = true;
            fullChangelogScroll = 0;
            dragTarget = DragTarget.NONE;
            notifyFullChangelogUserInput();
            return true;
        }
        if (tryStartLanguageDrag(mouseX, mouseY, l.languages())) return true;
        if (tryStartDrag(mouseX, mouseY, l.faq(), DragTarget.FAQ, faqScroll, getFaqContentHeight(l.faq().contentW())))
            return true;
        if (isInside(mouseX, mouseY, l.former().toRect())) notifyFormerUserInput();
        if (tryStartDrag(mouseX, mouseY, l.former(), DragTarget.FORMER, formerScroll, getFormerWorkersContentHeight(l.former().contentW())))
            return true;
        if (isInside(mouseX, mouseY, l.changelog().toRect())) notifyChangelogPreviewUserInput();
        if (tryStartDrag(mouseX, mouseY, l.changelog(), DragTarget.CHANGELOG_PREVIEW, changelogPreviewScroll, getWrappedLinesHeight(buildWrappedLines(getPreviewChangelogSource(), l.changelog().contentW()))))
            return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handleFaqToggleClick(ScrollArea panel, double mouseX, double mouseY) {
        int y = panel.contentY() - faqScroll;
        String lastMain = null;
        for (int i = 0; i < faqEntries.size(); i++) {
            IntroFaqEntry entry = faqEntries.get(i);
            String main = text(entry.main());
            if (!main.equals(lastMain)) {
                y += font.lineHeight + 6;
                lastMain = main;
            }
            Rect row = new Rect(panel.contentX(), y, panel.contentW(), 16);
            if (isInside(mouseX, mouseY, row)) {
                faqAnimation.put(i, getFaqProgress(i));
                if (expandedFaq.contains(i)) expandedFaq.remove(i);
                else expandedFaq.add(i);
                return true;
            }
            y += 16 + 4;
            int animatedHeight = getAnimatedFaqAnswerHeight(i, panel.contentW());
            if (animatedHeight > 0) y += animatedHeight + 5;
        }
        return false;
    }

    private boolean handleBillboardClick(Rect panel, double mouseX, double mouseY) {
        if (billboardSlides.isEmpty()) return false;
        int totalW = billboardSlides.size() * DOT_SIZE + Math.max(0, billboardSlides.size() - 1) * DOT_GAP;
        int startX = panel.x() + (panel.w() - totalW) / 2;
        int dotY = panel.y() + panel.h() - 11;
        for (int i = 0; i < billboardSlides.size(); i++) {
            Rect dot = new Rect(startX + i * (DOT_SIZE + DOT_GAP), dotY, DOT_SIZE, DOT_SIZE);
            if (isInside(mouseX, mouseY, dot)) {
                switchBillboard(i);
                nextBillboardSwitchAt = Util.getMillis() + BILLBOARD_SWITCH_MS;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        Layout l = layout();
        clampAll(l);
        switch (dragTarget) {
            case LANGUAGES -> {
                dragLanguageScroll(l.languages(), mouseY);
                return true;
            }
            case FAQ -> {
                dragScroll(l.faq(), mouseY, getFaqContentHeight(l.faq().contentW()), ScrollSetter.FAQ);
                return true;
            }
            case FORMER -> {
                notifyFormerUserInput();
                dragScroll(l.former(), mouseY, getFormerWorkersContentHeight(l.former().contentW()), ScrollSetter.FORMER);
                return true;
            }
            case CHANGELOG_PREVIEW -> {
                notifyChangelogPreviewUserInput();
                dragScroll(l.changelog(), mouseY, getWrappedLinesHeight(buildWrappedLines(getPreviewChangelogSource(), l.changelog().contentW())), ScrollSetter.CHANGELOG_PREVIEW);
                return true;
            }
            case MODAL_CHANGELOG -> {
                notifyFullChangelogUserInput();
                Rect modal = getModalRect(l);
                ScrollArea modalArea = createScrollArea(modal.x(), modal.y(), modal.w(), modal.h());
                dragScroll(modalArea, mouseY, getWrappedLinesHeight(buildWrappedLines(getFullChangelogSource(), modalArea.contentW())), ScrollSetter.MODAL_CHANGELOG);
                return true;
            }
            default -> {
                return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragTarget = DragTarget.NONE;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (fullChangelogOpen && keyCode == 256) {
            fullChangelogOpen = false;
            dragTarget = DragTarget.NONE;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean tryStartDrag(double mouseX, double mouseY, ScrollArea panel, DragTarget target, int scroll, int contentHeight) {
        int maxScroll = Math.max(0, contentHeight - panel.contentH());
        if (maxScroll <= 0) return false;
        int thumbH = getSectionThumbHeight(panel.contentH(), panel.scrollH(), contentHeight);
        int thumbY = getThumbY(scroll, maxScroll, panel.scrollY(), panel.scrollH(), thumbH);
        Rect thumbRect = new Rect(panel.scrollX(), thumbY, panel.scrollW(), thumbH);
        if (isInside(mouseX, mouseY, thumbRect)) {
            dragTarget = target;
            scrollbarGrabOffset = mouseY - thumbY;
            return true;
        }
        return false;
    }

    private void dragScroll(ScrollArea panel, double mouseY, int contentHeight, ScrollSetter setter) {
        int maxScroll = Math.max(0, contentHeight - panel.contentH());
        if (maxScroll <= 0) return;
        int thumbH = getSectionThumbHeight(panel.contentH(), panel.scrollH(), contentHeight);
        double thumbY = mouseY - scrollbarGrabOffset;
        double ratio = (thumbY - panel.scrollY()) / Math.max(1.0, (panel.scrollH() - thumbH));
        ratio = Mth.clamp((float) ratio, 0.0F, 1.0F);
        int scroll = (int) Math.round(ratio * maxScroll);
        switch (setter) {
            case FAQ -> faqScroll = scroll;
            case FORMER -> formerScroll = scroll;
            case CHANGELOG_PREVIEW -> changelogPreviewScroll = scroll;
            case MODAL_CHANGELOG -> fullChangelogScroll = scroll;
        }
    }

    private void switchBillboard(int newIndex) {
        if (billboardSlides.isEmpty()) return;
        newIndex = Mth.clamp(newIndex, 0, billboardSlides.size() - 1);
        if (newIndex == billboardIndex) return;
        billboardPreviousIndex = billboardIndex;
        billboardIndex = newIndex;
        billboardTransitionStartAt = Util.getMillis();
    }

    private float getBillboardTransitionProgress(long now) {
        if (billboardPreviousIndex == -1 || billboardTransitionStartAt < 0L) return 1.0F;
        float t = (now - billboardTransitionStartAt) / (float) BILLBOARD_TRANSITION_MS;
        return Mth.clamp(t, 0.0F, 1.0F);
    }

    private List<LanguageSupportEntry> buildLanguageEntries() {
        LinkedHashMap<String, LanguageSupportEntry> map = new LinkedHashMap<>();
        map.put("uk_ua", new LanguageSupportEntry("uk_ua", "Українська", LanguageSupportState.PARTIAL));
        map.put("en_us", new LanguageSupportEntry("en_us", "English (US)", LanguageSupportState.FULL));
        map.put("en_gb", new LanguageSupportEntry("en_gb", "English (UK)", LanguageSupportState.PARTIAL));
        map.put("ru_ru", new LanguageSupportEntry("ru_ru", "Русский", LanguageSupportState.FULL));
        map.put("zh_cn", new LanguageSupportEntry("zh_cn", "中文", LanguageSupportState.FULL));
        String currentCode = Minecraft.getInstance().getLanguageManager().getSelected();
        if (!map.containsKey(currentCode)) {
            map.put(currentCode, new LanguageSupportEntry(currentCode, currentCode, LanguageSupportState.UNSUPPORTED));
        }
        return new ArrayList<>(map.values());
    }

    private List<FormattedCharSequence> buildWrappedLines(List<String> source, int width) {
        List<FormattedCharSequence> out = new ArrayList<>();
        for (String line : source) {
            String resolved = text(line);
            if (resolved.isEmpty()) out.add(Component.empty().getVisualOrderText());
            else out.addAll(font.split(Component.literal(resolved), width));
        }
        return out;
    }

    private int getWrappedLinesHeight(List<FormattedCharSequence> lines) {
        return Math.max(1, lines.size() * (font.lineHeight + 2));
    }

    private int getFaqContentHeight(int width) {
        int total = 0;
        String lastMain = null;
        for (int i = 0; i < faqEntries.size(); i++) {
            IntroFaqEntry entry = faqEntries.get(i);
            String main = text(entry.main());
            if (!main.equals(lastMain)) {
                total += font.lineHeight + 6;
                lastMain = main;
            }
            total += 16 + 4;
            int animatedHeight = getAnimatedFaqAnswerHeight(i, width);
            if (animatedHeight > 0) total += animatedHeight + 5;
        }
        return Math.max(total, 1);
    }

    private int withAlpha(int rgb, float alpha) {
        int a = Mth.clamp((int) (alpha * 255.0F), 0, 255);
        return (a << 24) | (rgb & 0x00FFFFFF);
    }

    private int getFormerWorkersContentHeight(int width) {
        int total = 0;
        for (int i = 0; i < formerWorkers.size(); i++) {
            total += getFormerWorkerItemHeight(width, formerWorkers.get(i));
            if (i < formerWorkers.size() - 1) total += FORMER_GAP;
        }
        return Math.max(total, 1);
    }

    private int getFormerWorkerItemHeight(int width, IntroFormerMember entry) {
        LabelBadgeMetrics badge = getBadgeMetrics(width - 6, text(entry.role()), ROLE_SCALE);
        int nameH = Math.max(8, Math.round(font.lineHeight * NAME_SCALE));
        return FORMER_HEAD_SIZE + 4 + nameH + 4 + badge.height();
    }

    private int getFormerRoleBadgeFillColor(String role) {
        if (role == null || role.isBlank()) return 0xFF4A5C74;
        String lower = text(role).toLowerCase(java.util.Locale.ROOT);
        if (lower.contains("developer") || lower.contains("разработчик") || lower.contains("розробник"))
            return 0xFFE06A1A;
        if (lower.contains("designer") || lower.contains("дизайнер")) return 0xFF7B4DFF;
        if (lower.contains("localizer") || lower.contains("локализатор") || lower.contains("локалізатор"))
            return 0xFF2C6DDA;
        if (lower.contains("artist") || lower.contains("художник")) return 0xFF00A896;
        if (lower.contains("tester") || lower.contains("тестер")) return 0xFF5A8F29;
        return 0xFF4A5C74;
    }

    private LabelBadgeMetrics getBadgeMetrics(int maxWidth, String label, float scale) {
        int safeWidth = Math.max(28, maxWidth);
        int wrapWidth = Math.max(1, (int) ((safeWidth - 8) / scale));
        List<FormattedCharSequence> lines = font.split(Component.literal(label), wrapWidth);
        int maxLineWidth = 0;
        for (FormattedCharSequence line : lines) maxLineWidth = Math.max(maxLineWidth, font.width(line));
        int scaledTextWidth = Math.max(1, Math.round(maxLineWidth * scale));
        int badgeWidth = Math.max(28, scaledTextWidth + 8);
        badgeWidth = Math.min(badgeWidth, safeWidth);
        int scaledLineHeight = Math.max(1, Math.round(font.lineHeight * scale));
        int badgeHeight = Math.max(10, 2 + lines.size() * scaledLineHeight + 2);
        return new LabelBadgeMetrics(lines, badgeWidth, badgeHeight, scaledLineHeight);
    }

    private void drawWrapBadge(GuiGraphics guiGraphics, int centerX, int y, int maxWidth, String label, int fillColor, float scale) {
        LabelBadgeMetrics metrics = getBadgeMetrics(maxWidth, label, scale);
        int x = centerX - metrics.width() / 2;
        guiGraphics.fill(x, y, x + metrics.width(), y + metrics.height(), fillColor);
        guiGraphics.fill(x, y, x + metrics.width(), y + 1, 0xFFFFFFFF);
        guiGraphics.fill(x, y + metrics.height() - 1, x + metrics.width(), y + metrics.height(), 0xFF1A1A1A);
        guiGraphics.fill(x, y, x + 1, y + metrics.height(), 0xFFFFFFFF);
        guiGraphics.fill(x + metrics.width() - 1, y, x + metrics.width(), y + metrics.height(), 0xFF1A1A1A);
        int lineY = y + 2;
        for (FormattedCharSequence line : metrics.lines()) {
            drawCenteredScaledLine(guiGraphics, line, centerX, lineY, scale, 0xFFFFFFFF);
            lineY += metrics.scaledLineHeight();
        }
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

    private void drawCenteredScaledLine(GuiGraphics guiGraphics, FormattedCharSequence label, int centerX, int y, float scale, int color) {
        int scaledW = Math.round(font.width(label) * scale);
        int x = centerX - scaledW / 2;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(font, label, 0, 0, color, false);
        guiGraphics.pose().popPose();
    }

    private <T> T firstNonNull(T a, T b) {
        return a != null ? a : b;
    }

    private void clampAll(Layout l) {
        languageScroll = Mth.clamp(languageScroll, 0, getLanguagesMaxScroll(l.languages()));
        faqScroll = Mth.clamp(faqScroll, 0, Math.max(0, getFaqContentHeight(l.faq().contentW()) - l.faq().contentH()));
        formerScroll = Mth.clamp(formerScroll, 0, Math.max(0, getFormerWorkersContentHeight(l.former().contentW()) - l.former().contentH()));
        changelogPreviewScroll = Mth.clamp(changelogPreviewScroll, 0, Math.max(0, getWrappedLinesHeight(buildWrappedLines(getPreviewChangelogSource(), l.changelog().contentW())) - l.changelog().contentH()));
        Rect modal = getModalRect(l);
        ScrollArea modalArea = createScrollArea(modal.x(), modal.y(), modal.w(), modal.h());
        fullChangelogScroll = Mth.clamp(fullChangelogScroll, 0, Math.max(0, getWrappedLinesHeight(buildWrappedLines(getFullChangelogSource(), modalArea.contentW())) - modalArea.contentH()));
    }

    private int getSectionThumbHeight(int viewportHeight, int trackHeight, int contentHeight) {
        if (contentHeight <= 0) return trackHeight;
        return Mth.clamp((int) ((trackHeight * (double) viewportHeight) / contentHeight), 18, trackHeight);
    }

    private int getThumbY(int scroll, int maxScroll, int trackY, int trackH, int thumbH) {
        if (maxScroll <= 0) return trackY;
        return trackY + Math.round((trackH - thumbH) * (scroll / (float) maxScroll));
    }

    private int getLanguageRowHeight() {
        return 16;
    }

    private int getLanguageRowGap() {
        return 4;
    }

    private Rect getLanguagesViewport(Rect panel) {
        return new Rect(panel.x() + 6, panel.y() + 20, getLanguagesContentWidth(panel), panel.h() - 26);
    }

    private int getLanguagesContentWidth(Rect panel) {
        return panel.w() - 12 - (isLanguagesScrollbarVisible(panel) ? 8 : 0);
    }

    private int getLanguagesContentHeight() {
        int rows = languageEntries.size();
        if (rows <= 0) return 1;
        return rows * getLanguageRowHeight() + Math.max(0, rows - 1) * getLanguageRowGap();
    }

    private int getLanguagesMaxScroll(Rect panel) {
        return Math.max(0, getLanguagesContentHeight() - (panel.h() - 26));
    }

    private boolean isLanguagesScrollbarVisible(Rect panel) {
        return getLanguagesContentHeight() > (panel.h() - 26);
    }

    private Rect getLanguagesScrollbarTrack(Rect panel) {
        return new Rect(panel.x() + panel.w() - 8, panel.y() + 20, 6, panel.h() - 26);
    }

    private void renderLanguagesScrollbar(GuiGraphics guiGraphics, Rect panel) {
        if (!isLanguagesScrollbarVisible(panel)) return;
        Rect track = getLanguagesScrollbarTrack(panel);
        int maxScroll = getLanguagesMaxScroll(panel);
        int contentHeight = getLanguagesContentHeight();
        IntroDrawUtil.drawPanel(guiGraphics, track.x(), track.y(), track.w(), track.h(), 0xFF111822, 0xFF283546);
        int thumbH = getSectionThumbHeight(track.h(), track.h(), contentHeight);
        int thumbY = getThumbY(languageScroll, maxScroll, track.y(), track.h(), thumbH);
        IntroDrawUtil.drawPanel(guiGraphics, track.x() + 1, thumbY, track.w() - 2, thumbH, 0xFF4C78B8, 0xFF8DBAFF);
    }

    private boolean tryStartLanguageDrag(double mouseX, double mouseY, Rect panel) {
        if (!isLanguagesScrollbarVisible(panel)) return false;
        Rect track = getLanguagesScrollbarTrack(panel);
        int maxScroll = getLanguagesMaxScroll(panel);
        int contentHeight = getLanguagesContentHeight();
        int thumbH = getSectionThumbHeight(track.h(), track.h(), contentHeight);
        int thumbY = getThumbY(languageScroll, maxScroll, track.y(), track.h(), thumbH);
        Rect thumbRect = new Rect(track.x(), thumbY, track.w(), thumbH);
        if (isInside(mouseX, mouseY, thumbRect)) {
            dragTarget = DragTarget.LANGUAGES;
            scrollbarGrabOffset = mouseY - thumbY;
            return true;
        }
        return false;
    }

    private void dragLanguageScroll(Rect panel, double mouseY) {
        Rect track = getLanguagesScrollbarTrack(panel);
        int maxScroll = getLanguagesMaxScroll(panel);
        int contentHeight = getLanguagesContentHeight();
        if (maxScroll <= 0) return;
        int thumbH = getSectionThumbHeight(track.h(), track.h(), contentHeight);
        double thumbY = mouseY - scrollbarGrabOffset;
        double ratio = (thumbY - track.y()) / Math.max(1.0, (track.h() - thumbH));
        ratio = Mth.clamp((float) ratio, 0.0F, 1.0F);
        languageScroll = (int) Math.round(ratio * maxScroll);
    }

    private void drawMarqueeText(GuiGraphics guiGraphics, String label, int x, int y, int width, int color, long now) {
        if (label == null || label.isEmpty() || width <= 4) return;
        String fitted = font.plainSubstrByWidth(label, width);
        if (font.width(label) <= width || fitted.length() >= label.length()) {
            guiGraphics.drawString(font, label, x, y, color, false);
            return;
        }
        int visibleChars = Math.max(1, fitted.length());
        int maxStart = Math.max(0, label.length() - visibleChars);
        if (maxStart <= 0) {
            guiGraphics.drawString(font, fitted, x, y, color, false);
            return;
        }
        long holdStartMs = 2600L;
        long holdEndMs = 1800L;
        long travelMs = Math.max(1200L, maxStart * 520L);
        long cycleMs = holdStartMs + travelMs + holdEndMs + travelMs;
        long t = now % cycleMs;
        float pos;
        if (t < holdStartMs) pos = 0.0F;
        else if (t < holdStartMs + travelMs) pos = maxStart * smootherStep((t - holdStartMs) / (float) travelMs);
        else if (t < holdStartMs + travelMs + holdEndMs) pos = maxStart;
        else pos = maxStart * (1.0F - smootherStep((t - holdStartMs - travelMs - holdEndMs) / (float) travelMs));
        int currentStart = Mth.clamp((int) Math.floor(pos), 0, maxStart);
        int nextStart = Math.min(maxStart, currentStart + 1);
        float frac = pos - currentStart;
        String currentText = font.plainSubstrByWidth(label.substring(currentStart), width + 20);
        if (currentStart == nextStart) {
            guiGraphics.enableScissor(x, y, x + width, y + font.lineHeight);
            guiGraphics.drawString(font, currentText, x, y, color, false);
            guiGraphics.disableScissor();
            return;
        }
        String nextText = font.plainSubstrByWidth(label.substring(nextStart), width + 20);
        String removedChar = label.substring(currentStart, currentStart + 1);
        int shiftWidth = Math.max(1, font.width(removedChar));
        float pixelShift = shiftWidth * frac;
        guiGraphics.enableScissor(x, y, x + width, y + font.lineHeight);
        guiGraphics.drawString(font, currentText, x - Math.round(pixelShift), y, color, false);
        guiGraphics.drawString(font, nextText, x + shiftWidth - Math.round(pixelShift), y, color, false);
        guiGraphics.disableScissor();
    }

    private float smootherStep(float t) {
        t = Mth.clamp(t, 0.0F, 1.0F);
        return t * t * t * (t * (t * 6.0F - 15.0F) + 10.0F);
    }

    private Rect getChangelogButton(ScrollArea panel) {
        return new Rect(panel.panelX() + panel.panelW() - 66, panel.panelY() + 4, 58, 12);
    }

    private Rect getModalRect(Layout l) {
        int w = Math.min(360, l.frame().w() - 40);
        int h = Math.min(220, l.frame().h() - 30);
        int x = l.frame().x() + (l.frame().w() - w) / 2;
        int y = l.frame().y() + (l.frame().h() - h) / 2;
        return new Rect(x, y, w, h);
    }

    private Rect getModalCloseRect(Rect modal) {
        return new Rect(modal.x() + modal.w() - 20, modal.y() + 4, 14, 12);
    }

    private void renderScrollBar(GuiGraphics guiGraphics, ScrollArea panel, int scroll, int contentHeight) {
        int maxScroll = Math.max(0, contentHeight - panel.contentH());
        if (maxScroll <= 0) return;
        IntroDrawUtil.drawPanel(guiGraphics, panel.scrollX(), panel.scrollY(), panel.scrollW(), panel.scrollH(), 0xFF111822, 0xFF283546);
        int thumbH = getSectionThumbHeight(panel.contentH(), panel.scrollH(), contentHeight);
        int thumbY = getThumbY(scroll, maxScroll, panel.scrollY(), panel.scrollH(), thumbH);
        IntroDrawUtil.drawPanel(guiGraphics, panel.scrollX() + 1, thumbY, panel.scrollW() - 2, thumbH, 0xFF4C78B8, 0xFF8DBAFF);
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
        Rect page1Btn = new Rect(nav.x() + 6, nav.y() + 3, 46, 12);
        Rect page2Btn = new Rect(page1Btn.x() + page1Btn.w() + 4, nav.y() + 3, 46, 12);
        int bodyX = frameX + 8;
        int bodyY = nav.y() + nav.h() + 6;
        int bodyW = frameW - 16;
        int bodyH = frameH - (bodyY - frameY) - 8;
        int leftW = 150;
        int centerW = 220;
        int rightW = bodyW - leftW - centerW - GAP * 2;
        int langH = 100;
        int leftBottomH = bodyH - langH - GAP;
        Rect languages = new Rect(bodyX, bodyY, leftW, langH);
        ScrollArea former = createScrollArea(bodyX, bodyY + langH + GAP, leftW, leftBottomH);
        ScrollArea faq = createScrollArea(bodyX + leftW + GAP, bodyY, centerW, bodyH);
        int billboardH = 155;
        int changelogH = bodyH - billboardH - GAP;
        Rect billboard = new Rect(bodyX + leftW + GAP + centerW + GAP, bodyY, rightW, billboardH);
        ScrollArea changelog = createScrollArea(bodyX + leftW + GAP + centerW + GAP, bodyY + billboardH + GAP, rightW, changelogH);
        return new Layout(frame, nav, page1Btn, page2Btn, languages, former, faq, billboard, changelog);
    }

    private boolean isInside(double mouseX, double mouseY, Rect rect) {
        return mouseX >= rect.x() && mouseX < rect.x() + rect.w() && mouseY >= rect.y() && mouseY < rect.y() + rect.h();
    }

    private enum DragTarget {NONE, LANGUAGES, FAQ, FORMER, CHANGELOG_PREVIEW, MODAL_CHANGELOG}

    private enum ScrollSetter {FAQ, FORMER, CHANGELOG_PREVIEW, MODAL_CHANGELOG}

    private enum LanguageSupportState {FULL, PARTIAL, UNSUPPORTED}

    private record Layout(Rect frame, Rect nav, Rect page1Btn, Rect page2Btn, Rect languages, ScrollArea former,
                          ScrollArea faq, Rect billboard, ScrollArea changelog) {
    }

    private record Rect(int x, int y, int w, int h) {
    }

    private record ScrollArea(int panelX, int panelY, int panelW, int panelH, int contentX, int contentY, int contentW,
                              int contentH, int scrollX, int scrollY, int scrollW, int scrollH) {
        Rect toRect() {
            return new Rect(panelX, panelY, panelW, panelH);
        }
    }

    private record LanguageSupportEntry(String code, String displayName, LanguageSupportState state) {
    }

    private record LabelBadgeMetrics(List<FormattedCharSequence> lines, int width, int height, int scaledLineHeight) {
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

        boolean shouldHideScrollbar(long now) {
            return autoActive && now - lastUserInputAt >= AUTO_SCROLL_IDLE_MS;
        }
    }
}
