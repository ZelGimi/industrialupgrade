package com.denfop.client.intro;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntroDashboardScreen extends Screen {

    private static final int TEAM_HEAD_SIZE = 32;
    private static final float TEAM_NAME_SCALE = 0.75F;
    private static final float TEAM_ROLE_SCALE = 0.75F;
    private static final int TEAM_MEMBER_GAP = 8;

    private static final int TOP_RIGHT_INNER_GAP = 6;
    private static final int TOP_ADDON_ICON_SIZE = 24;
    private static final float TOP_ADDON_NAME_SCALE = 0.70F;
    private static final int TOP_ADDON_ITEM_GAP = 10;
    private static final int TOP_ADDON_BADGE_FILL = 0xFF3B66C4;

    private static final long AUTO_SCROLL_IDLE_MS = 6000L;
    private static final long AUTO_SCROLL_EDGE_PAUSE_MS = 1200L;
    private static final float AUTO_SCROLL_SPEED_PX_PER_SEC = 18.0F;
    private final AutoScrollState teamAutoScroll = new AutoScrollState();
    private final AutoScrollState addonsAutoScroll = new AutoScrollState();
    private final IntroDashboardData data;
    private final IntroImageManager imageManager;
    private final IntroHeadProfileCache headProfileCache;
    private final List<IntroMarkdownBlock> markdownBlocks;
    private long lastAutoScrollTickAt;
    private int contentScroll;
    private int partnerScroll;
    private int teamScroll;
    private int addonsScroll;
    private int changelogScroll;

    private DragTarget dragTarget = DragTarget.NONE;
    private double scrollbarGrabOffset;

    public IntroDashboardScreen(IntroDashboardData data) {
        super(IntroLocalization.component(data.getModTitle()));
        this.data = data;
        this.imageManager = IntroImageManager.getInstance();
        this.headProfileCache = IntroHeadProfileCache.getInstance();
        this.markdownBlocks = new IntroMarkdownParser().parse(data.getMarkdown());
    }

    @Override
    protected void init() {
        long now = Util.getMillis();
        IntroChangelogCache.request();
        lastAutoScrollTickAt = now;
        teamAutoScroll.init(now, teamScroll);
        addonsAutoScroll.init(now, addonsScroll);
        clampAllScrolls(layout());
    }

    @Override
    public void tick() {
        super.tick();
        Layout l = layout();
        updateIdleAutoScroll(l);
        clampAllScrolls(l);
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

    private void notifyTeamUserInput() {
        teamAutoScroll.onUserInput(Util.getMillis(), teamScroll);
    }

    private void notifyAddonsUserInput() {
        addonsAutoScroll.onUserInput(Util.getMillis(), addonsScroll);
    }

    private void updateIdleAutoScroll(Layout l) {
        long now = Util.getMillis();
        long deltaMs = Math.max(0L, now - lastAutoScrollTickAt);
        lastAutoScrollTickAt = now;

        SidebarSections sections = sidebarSections(l);
        SectionMetrics teamMetrics = getTeamSectionMetrics(sections.team());
        SectionMetrics addonMetrics = getAddonSectionMetrics(sections.addons());

        int teamMax = Math.max(0, teamMetrics.contentHeight() - sections.team().contentH());
        int addonsMax = Math.max(0, addonMetrics.contentHeight() - sections.addons().contentH());

        teamScroll = teamAutoScroll.update(now, deltaMs, teamScroll, teamMax);
        addonsScroll = addonsAutoScroll.update(now, deltaMs, addonsScroll, addonsMax);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.fill(0, 0, this.width, this.height, 0xA0101318);

        Layout layout = layout();
        clampAllScrolls(layout);

        renderFrame(guiGraphics, layout);
        renderTopLeftLogo(guiGraphics, layout);
        List<Component> partnerTooltip = renderPartners(guiGraphics, layout, mouseX, mouseY);
        renderContentPanel(guiGraphics, layout, mouseX, mouseY, partialTick);
        List<Component> rightTooltip = renderRightPanel(guiGraphics, layout, mouseX, mouseY);


        if (rightTooltip != null) {
            guiGraphics.renderTooltip(font, rightTooltip, Optional.empty(), mouseX, mouseY);
        } else if (partnerTooltip != null) {
            guiGraphics.renderTooltip(font, partnerTooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    private void renderFrame(GuiGraphics guiGraphics, Layout l) {
        IntroDrawUtil.drawPanel(guiGraphics, l.frameX, l.frameY, l.frameW, l.frameH, 0xD911151D, 0xFF394A63);
        renderPageHeader(guiGraphics, l);
    }

    private void renderPageHeader(GuiGraphics guiGraphics, Layout l) {
        int navX = l.frameX + 8;
        int navY = l.frameY + 6;
        int navH = 18;
        Rect nextBtn = getNextPageButtonPage1(l);
        IntroDrawUtil.drawPanel(guiGraphics, navX, navY, (nextBtn.x() - navX) + nextBtn.w() + 3, navH, 0xFF0E141C, 0xFF314055);
        drawNavButton(guiGraphics, nextBtn.x(), nextBtn.y(), nextBtn.w(), nextBtn.h(), ">>", false);
    }

    private Rect getNextPageButtonPage1(Layout l) {
        int navX = l.frameX + 8;
        int navY = l.frameY + 6;
        return new Rect(navX + 56, navY + 3, 46, 12);
    }

    private void drawNavButton(GuiGraphics guiGraphics, int x, int y, int w, int h, String label, boolean active) {
        IntroDrawUtil.drawPanel(guiGraphics, x, y, w, h, active ? 0xFF2C6DDA : 0xFF182638, active ? 0xFFFFFFFF : 0xFF70A7FF);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + (w - Math.round(font.width(label) * 0.65F)) / 2.0F, y + 3, 0);
        guiGraphics.pose().scale(0.65F, 0.65F, 1.0F);
        guiGraphics.drawString(font, label, 0, 0, 0xFFFFFFFF, false);
        guiGraphics.pose().popPose();
    }

    private void renderTopLeftLogo(GuiGraphics guiGraphics, Layout l) {
        int iconSize = 48;
        int iconX = l.logoX + (l.logoW - iconSize) / 2;
        int iconY = l.logoY + 14;

        String logoSource = data.getLogoSource();
        if (logoSource != null && !logoSource.isBlank()) {
            IntroDrawUtil.renderImageOrFallback(guiGraphics, this.font, imageManager, text(data.getModTitle()), logoSource, iconX, iconY, iconSize, iconSize);
        } else {
            IntroDrawUtil.drawGeneratedBadge(guiGraphics, this.font, text(data.getModTitle()), iconX, iconY, iconSize);
        }
    }

    private List<Component> renderPartners(GuiGraphics guiGraphics, Layout l, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, l.partnerX, l.partnerY, l.partnerW, l.partnerH, 0xFF121A24, 0xFF314055);
        drawCenteredText(guiGraphics, tr("intro.page1.partners"), l.partnerX + l.partnerW / 2, l.partnerY + 8, 0xFFEAF2FF);

        SectionMetrics metrics = getPartnerSectionMetrics(l);
        int contentX = l.partnerX + 8;
        int contentY = l.partnerY + 24;
        int contentW = metrics.usableWidth();
        int contentH = getPartnersViewportHeight(l);
        List<Component> hoveredTooltip = null;

        guiGraphics.enableScissor(contentX, contentY, contentX + contentW, contentY + contentH);

        int iconSize = 24;
        int gap = 8;
        int cols = Math.max(1, (contentW + gap) / (iconSize + gap));
        int total = data.getPartners().size();
        int currentIndex = 0;
        int row = 0;

        while (currentIndex < total) {
            int itemsInRow = Math.min(cols, total - currentIndex);
            int rowWidth = itemsInRow * iconSize + Math.max(0, itemsInRow - 1) * gap;
            int rowStartX = contentX + (contentW - rowWidth) / 2;
            int iconY = contentY + row * (iconSize + gap) - partnerScroll;

            for (int col = 0; col < itemsInRow; col++) {
                IntroPartnerEntry entry = data.getPartners().get(currentIndex + col);
                int iconX = rowStartX + col * (iconSize + gap);

                IntroDrawUtil.renderChipIcon(guiGraphics, font, imageManager, entry.getIconSource(), text(entry.getName()), iconX, iconY, iconSize);

                if (IntroDrawUtil.isMouseOver(mouseX, mouseY, iconX, iconY, iconSize, iconSize)) {
                    hoveredTooltip = List.of(component(entry.getName()), component(entry.getSubtitle()));
                }
            }

            currentIndex += itemsInRow;
            row++;
        }

        guiGraphics.disableScissor();

        if (metrics.scrollbarVisible()) {
            int trackGap = 4;
            int trackW = 6;
            int trackX = contentX + contentW + trackGap;
            int trackY = contentY;
            int trackH = contentH;
            renderPartnerScrollbar(guiGraphics, trackX, trackY, trackW, trackH, metrics.contentHeight());
        }

        return hoveredTooltip;
    }

    private void renderContentPanel(GuiGraphics guiGraphics, Layout l, int mouseX, int mouseY, float partialTick) {
        IntroDrawUtil.drawPanel(guiGraphics, l.contentX, l.contentY, l.contentW, l.contentH, 0xFF0E141C, 0xFF314055);

        int bodyX = l.contentBodyX;
        int bodyY = l.contentBodyY;
        int bodyW = l.contentBodyW;
        int bodyH = l.contentBodyH;

        guiGraphics.enableScissor(bodyX, bodyY, bodyX + bodyW, bodyY + bodyH);

        int blockY = bodyY - contentScroll;
        int innerWidth = l.contentInnerW;

        for (int i = 0; i < markdownBlocks.size(); i++) {
            IntroMarkdownBlock block = markdownBlocks.get(i);
            int h = block.getHeight(innerWidth, font, imageManager);

            if (blockY + h >= bodyY - 120 && blockY <= bodyY + bodyH + 120) {
                block.render(guiGraphics, font, imageManager, bodyX + 2, blockY, innerWidth, mouseX, mouseY, partialTick);
            }

            blockY += h + getBlockSpacing(i);
        }

        guiGraphics.disableScissor();
        renderMainContentScrollbar(guiGraphics, l);
    }

    private void renderMainContentScrollbar(GuiGraphics guiGraphics, Layout l) {
        int maxScroll = getMaxScroll(l);
        if (maxScroll <= 0) {
            return;
        }

        int trackX = l.contentScrollbarX;
        int trackY = l.contentBodyY + 4;
        int trackW = l.contentScrollbarW;
        int trackH = l.contentBodyH - 8;

        IntroDrawUtil.drawPanel(guiGraphics, trackX, trackY, trackW, trackH, 0xFF111822, 0xFF283546);

        int thumbH = getThumbHeight(l, maxScroll);
        int thumbY = getThumbY(contentScroll, maxScroll, trackY, trackH, thumbH);
        IntroDrawUtil.drawPanel(guiGraphics, trackX + 1, thumbY, trackW - 2, thumbH, 0xFF4C78B8, 0xFF8DBAFF);
    }

    private List<Component> renderRightPanel(GuiGraphics guiGraphics, Layout l, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, l.rightX, l.rightY, l.rightW, l.rightH, 0xFF0E141C, 0xFF314055);
        SidebarSections sections = sidebarSections(l);
        List<Component> teamTooltip = renderTeamSection(guiGraphics, sections.team(), mouseX, mouseY);
        List<Component> addonTooltip = renderAddonIconsSection(guiGraphics, sections.addons(), mouseX, mouseY);
        renderVersionChangelogSection(guiGraphics, sections.changelog());
        return addonTooltip != null ? addonTooltip : teamTooltip;
    }

    private List<Component> renderTeamSection(GuiGraphics guiGraphics, ScrollSection section, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, section.panelX(), section.panelY(), section.panelW(), section.panelH(), 0xFF101923, 0xFF2E4158);
        int panelCenterX = section.panelX() + section.panelW() / 2;
        drawCenteredText(guiGraphics, tr("intro.page1.team"), panelCenterX, section.panelY() + 4, 0xFFFFFFFF);

        List<IntroTeamMember> members = data.getTeamMembers();
        if (members.isEmpty()) {
            return null;
        }

        SectionMetrics metrics = getTeamSectionMetrics(section);
        int renderX = section.panelX() + 4;
        int renderY = section.contentY();
        int renderW = metrics.usableWidth();
        int renderH = section.contentH();
        List<Component> hoveredTooltip = null;

        guiGraphics.enableScissor(renderX, renderY, renderX + renderW, renderY + renderH);

        int currentY = renderY - teamScroll;
        for (IntroTeamMember member : members) {
            int memberHeight = getTeamMemberHeight(metrics.usableWidth(), member);
            int headX = panelCenterX - TEAM_HEAD_SIZE / 2;
            int headY = currentY;
            boolean hovered = IntroDrawUtil.isMouseOver(mouseX, mouseY, renderX, currentY, renderW, memberHeight);

            if (headProfileCache.hasResolvedHead(member.getNickname())) {
                headProfileCache.renderHead(guiGraphics, member.getNickname(), headX, headY, TEAM_HEAD_SIZE);
            } else {
                IntroDrawUtil.drawGeneratedBadge(guiGraphics, font, member.getNickname(), headX, headY, TEAM_HEAD_SIZE);
                headProfileCache.renderHead(guiGraphics, member.getNickname(), headX, headY, TEAM_HEAD_SIZE);
            }

            String displayName = getCompactTeamName(member);
            displayName = font.plainSubstrByWidth(displayName, Math.max(1, (int) (metrics.usableWidth() / TEAM_NAME_SCALE)));
            int nameY = headY + TEAM_HEAD_SIZE + 4;
            drawCenteredScaledText(guiGraphics, displayName, panelCenterX, nameY, TEAM_NAME_SCALE, 0xFFFFFFFF);

            String roleLabel = getCompactRoleLabel(member.getRole());
            int nameHeight = Math.max(8, Math.round(font.lineHeight * TEAM_NAME_SCALE));
            int badgeY = nameY + nameHeight + 4;
            drawLabelBadge(guiGraphics, panelCenterX, badgeY, metrics.usableWidth(), roleLabel, getRoleBadgeFillColor(member.getRole()), TEAM_ROLE_SCALE);

            if (hovered) {
                hoveredTooltip = List.of(component(member.getDisplayName()), component(member.getRole()), component(member.getContribution()));
            }

            currentY += memberHeight + TEAM_MEMBER_GAP;
        }

        guiGraphics.disableScissor();
        if (!teamAutoScroll.shouldHideScrollbar(Util.getMillis())) {
            renderSectionScrollbar(guiGraphics, section, teamScroll, metrics.contentHeight());
        }
        return hoveredTooltip;
    }

    private List<Component> renderAddonIconsSection(GuiGraphics guiGraphics, ScrollSection section, int mouseX, int mouseY) {
        IntroDrawUtil.drawPanel(guiGraphics, section.panelX(), section.panelY(), section.panelW(), section.panelH(), 0xFF101923, 0xFF2E4158);
        int panelCenterX = section.panelX() + section.panelW() / 2;
        drawCenteredText(guiGraphics, tr("intro.page1.addons"), panelCenterX, section.panelY() + 4, 0xFFFFFFFF);

        SectionMetrics metrics = getAddonSectionMetrics(section);
        int renderX = section.panelX() + 4;
        int renderY = section.contentY();
        int renderW = metrics.usableWidth();
        int renderH = section.contentH();
        List<Component> hoveredTooltip = null;

        guiGraphics.enableScissor(renderX, renderY, renderX + renderW, renderY + renderH);

        int currentY = renderY - addonsScroll;
        for (IntroAddonEntry addon : data.getAddons()) {
            int itemHeight = getAddonItemHeight(metrics.usableWidth(), addon);
            int iconX = panelCenterX - TOP_ADDON_ICON_SIZE / 2;
            int iconY = currentY;

            IntroDrawUtil.renderChipIcon(guiGraphics, font, imageManager, addon.getIconSource(), text(addon.getName()), iconX, iconY, TOP_ADDON_ICON_SIZE);

            int badgeY = iconY + TOP_ADDON_ICON_SIZE + 4;
            drawLabelBadge(guiGraphics, panelCenterX, badgeY, metrics.usableWidth(), text(addon.getName()), TOP_ADDON_BADGE_FILL, TOP_ADDON_NAME_SCALE);

            boolean hovered = IntroDrawUtil.isMouseOver(mouseX, mouseY, renderX, currentY, renderW, itemHeight);
            if (hovered) {
                hoveredTooltip = List.of(component(addon.getName()), component(addon.getSubtitle()));
            }

            currentY += itemHeight + TOP_ADDON_ITEM_GAP;
        }

        guiGraphics.disableScissor();
        if (!addonsAutoScroll.shouldHideScrollbar(Util.getMillis())) {
            renderSectionScrollbar(guiGraphics, section, addonsScroll, metrics.contentHeight());
        }
        return hoveredTooltip;
    }

    private void renderVersionChangelogSection(GuiGraphics guiGraphics, ScrollSection section) {
        IntroDrawUtil.drawPanel(guiGraphics, section.panelX(), section.panelY(), section.panelW(), section.panelH(), 0xFF101923, 0xFF2E4158);
        drawCenteredText(guiGraphics, tr("intro.page1.version_changelog"), section.panelX() + section.panelW() / 2, section.panelY() + 4, 0xFFFFFFFF);

        guiGraphics.enableScissor(section.contentX(), section.contentY(), section.contentX() + section.contentW(), section.contentY() + section.contentH());

        List<FormattedCharSequence> lines = buildChangelogLines(section.contentW());
        int y = section.contentY() - changelogScroll;

        for (int i = 0; i < lines.size(); i++) {
            int color = (i == 0) ? 0xFFEAF2FF : 0xFF9FB4CE;
            guiGraphics.drawString(font, lines.get(i), section.contentX(), y, color, false);
            y += font.lineHeight + 2;
        }

        guiGraphics.disableScissor();
        renderSectionScrollbar(guiGraphics, section, changelogScroll, getChangelogContentHeight(section.contentW()));
    }

    private void renderSectionScrollbar(GuiGraphics guiGraphics, ScrollSection section, int scroll, int contentHeight) {
        int maxScroll = Math.max(0, contentHeight - section.contentH());
        if (maxScroll <= 0) {
            return;
        }

        IntroDrawUtil.drawPanel(guiGraphics, section.scrollbarX(), section.scrollbarY(), section.scrollbarW(), section.scrollbarH(), 0xFF111822, 0xFF283546);
        int thumbH = getSectionThumbHeight(section.contentH(), section.scrollbarH(), contentHeight);
        int thumbY = getThumbY(scroll, maxScroll, section.scrollbarY(), section.scrollbarH(), thumbH);
        IntroDrawUtil.drawPanel(guiGraphics, section.scrollbarX() + 1, thumbY, section.scrollbarW() - 2, thumbH, 0xFF4C78B8, 0xFF8DBAFF);
    }

    private SectionMetrics getPartnerSectionMetrics(Layout l) {
        int viewportH = getPartnersViewportHeight(l);
        int widthWithoutScrollbar = Math.max(24, l.partnerW - 16);
        int heightWithoutScrollbar = getPartnersContentHeight(widthWithoutScrollbar);
        boolean scrollbarVisible = heightWithoutScrollbar > viewportH;
        int usableWidth = scrollbarVisible ? Math.max(24, l.partnerW - 16 - 4 - 6) : widthWithoutScrollbar;
        int contentHeight = scrollbarVisible ? getPartnersContentHeight(usableWidth) : heightWithoutScrollbar;
        return new SectionMetrics(usableWidth, contentHeight, scrollbarVisible);
    }

    private SectionMetrics getTeamSectionMetrics(ScrollSection section) {
        int widthWithoutScrollbar = Math.max(24, section.panelW() - 8);
        int heightWithoutScrollbar = getTeamContentHeight(widthWithoutScrollbar);
        boolean scrollbarVisible = heightWithoutScrollbar > section.contentH();
        int usableWidth = scrollbarVisible ? Math.max(24, section.contentW()) : widthWithoutScrollbar;
        int contentHeight = scrollbarVisible ? getTeamContentHeight(usableWidth) : heightWithoutScrollbar;
        return new SectionMetrics(usableWidth, contentHeight, scrollbarVisible);
    }

    private SectionMetrics getAddonSectionMetrics(ScrollSection section) {
        int widthWithoutScrollbar = Math.max(24, section.panelW() - 8);
        int heightWithoutScrollbar = getAddonIconsContentHeight(widthWithoutScrollbar);
        boolean scrollbarVisible = heightWithoutScrollbar > section.contentH();
        int usableWidth = scrollbarVisible ? Math.max(24, section.contentW()) : widthWithoutScrollbar;
        int contentHeight = scrollbarVisible ? getAddonIconsContentHeight(usableWidth) : heightWithoutScrollbar;
        return new SectionMetrics(usableWidth, contentHeight, scrollbarVisible);
    }

    private SidebarSections sidebarSections(Layout l) {
        int innerX = l.rightX + 4;
        int innerY = l.rightY + 4;
        int innerW = l.rightW - 8;
        int innerH = l.rightH - 8;
        int sectionGap = 6;
        int splitGap = TOP_RIGHT_INNER_GAP;
        int teamPanelW = (innerW - splitGap) / 2;
        int addonsPanelW = innerW - teamPanelW - splitGap;

        int desiredTopH = 18 + Math.max(getTeamContentHeight(teamPanelW - 8), getAddonIconsContentHeight(addonsPanelW - 8)) + 8;
        int maxTopH = Math.max(110, innerH - 90);
        int topPanelH = Mth.clamp(desiredTopH, 110, maxTopH);

        int bottomY = innerY + topPanelH + sectionGap;
        int bottomH = innerH - topPanelH - sectionGap;
        if (bottomH < 70) {
            bottomH = 70;
            topPanelH = innerH - sectionGap - bottomH;
        }

        ScrollSection team = createScrollSection(innerX, innerY, teamPanelW, topPanelH);
        ScrollSection addons = createScrollSection(innerX + teamPanelW + splitGap, innerY, addonsPanelW, topPanelH);
        ScrollSection changelog = createScrollSection(innerX, bottomY, innerW, bottomH);
        return new SidebarSections(team, addons, changelog);
    }

    private ScrollSection createScrollSection(int panelX, int panelY, int panelW, int panelH) {
        int contentX = panelX + 4;
        int contentY = panelY + 18;
        int contentW = panelW - 14;
        int contentH = panelH - 22;
        int scrollbarX = panelX + panelW - 8;
        int scrollbarY = contentY;
        int scrollbarW = 6;
        int scrollbarH = contentH;
        return new ScrollSection(panelX, panelY, panelW, panelH, contentX, contentY, contentW, contentH, scrollbarX, scrollbarY, scrollbarW, scrollbarH);
    }

    private int getPartnersViewportHeight(Layout l) {
        return l.partnerH - 24 - 8;
    }

    private int getPartnersContentHeight(int contentW) {
        int iconSize = 24;
        int gap = 8;
        int cols = Math.max(1, (contentW + gap) / (iconSize + gap));
        int total = data.getPartners().size();
        if (total <= 0) {
            return 1;
        }
        int rows = (total + cols - 1) / cols;
        return rows * iconSize + Math.max(0, rows - 1) * gap;
    }

    private int getPartnerMaxScroll(Layout l) {
        SectionMetrics metrics = getPartnerSectionMetrics(l);
        return Math.max(0, metrics.contentHeight() - getPartnersViewportHeight(l));
    }

    private void renderPartnerScrollbar(GuiGraphics guiGraphics, int trackX, int trackY, int trackW, int trackH, int contentHeight) {
        int maxScroll = Math.max(0, contentHeight - trackH);
        if (maxScroll <= 0) {
            return;
        }
        IntroDrawUtil.drawPanel(guiGraphics, trackX, trackY, trackW, trackH, 0xFF111822, 0xFF283546);
        int thumbH = getSectionThumbHeight(trackH, trackH, contentHeight);
        int thumbY = getThumbY(partnerScroll, maxScroll, trackY, trackH, thumbH);
        IntroDrawUtil.drawPanel(guiGraphics, trackX + 1, thumbY, trackW - 2, thumbH, 0xFF4C78B8, 0xFF8DBAFF);
    }

    private int getTeamContentHeight(int contentWidth) {
        int total = 0;
        List<IntroTeamMember> members = data.getTeamMembers();
        for (int i = 0; i < members.size(); i++) {
            total += getTeamMemberHeight(contentWidth, members.get(i));
            if (i < members.size() - 1) {
                total += TEAM_MEMBER_GAP;
            }
        }
        return Math.max(total, 1);
    }

    private int getTeamMemberHeight(int contentWidth, IntroTeamMember member) {
        String roleLabel = getCompactRoleLabel(member.getRole());
        LabelBadgeMetrics metrics = getBadgeMetrics(contentWidth - 6, roleLabel, TEAM_ROLE_SCALE);
        int nameHeight = Math.max(8, Math.round(font.lineHeight * TEAM_NAME_SCALE));
        return TEAM_HEAD_SIZE + 4 + nameHeight + 4 + metrics.height();
    }

    private int getAddonIconsContentHeight(int contentWidth) {
        int total = 0;
        List<IntroAddonEntry> addons = data.getAddons();
        for (int i = 0; i < addons.size(); i++) {
            total += getAddonItemHeight(contentWidth, addons.get(i));
            if (i < addons.size() - 1) {
                total += TOP_ADDON_ITEM_GAP;
            }
        }
        return Math.max(total, 1);
    }

    private int getAddonItemHeight(int contentWidth, IntroAddonEntry addon) {
        LabelBadgeMetrics metrics = getBadgeMetrics(contentWidth - 6, text(addon.getName()), TOP_ADDON_NAME_SCALE);
        return TOP_ADDON_ICON_SIZE + 4 + metrics.height();
    }

    private List<String> getPreviewChangelogSource() {
        List<String> source = IntroChangelogCache.getCurrentChangelogLines(data.getCurrentChangelogFallbackLines());
        if (source == null || source.isEmpty()) {
            return List.of(tr("intro.changelog.no_data"));
        }
        return source;
    }

    private List<FormattedCharSequence> buildChangelogLines(int width) {
        List<FormattedCharSequence> lines = new ArrayList<>();
        lines.addAll(font.split(Component.literal(tr("intro.version.installed", com.denfop.Constants.MOD_VERSION)), width));
        lines.add(Component.empty().getVisualOrderText());
        for (String line : getPreviewChangelogSource()) {
            lines.addAll(font.split(Component.literal(text(line)), width));
        }
        return lines;
    }

    private int getChangelogContentHeight(int width) {
        List<FormattedCharSequence> lines = buildChangelogLines(width);
        return Math.max(1, lines.size() * (font.lineHeight + 2));
    }

    private String getCompactTeamName(IntroTeamMember member) {
        if (member.getDisplayName() != null && !member.getDisplayName().isBlank()) {
            return text(member.getDisplayName());
        }
        return member.getNickname();
    }

    private String getCompactRoleLabel(String role) {
        if (role == null || role.isBlank()) {
            return tr("intro.team.role.member");
        }
        String localized = text(role);
        String lower = localized.toLowerCase(java.util.Locale.ROOT);
        if (lower.contains("lead") || lower.contains("ведущий") || lower.contains("головний")) {
            return tr("intro.team.role.short.lead_dev");
        }
        if (lower.contains("developer") || lower.contains("разработчик") || lower.contains("розробник")) {
            return tr("intro.team.role.short.developer");
        }
        if (lower.contains("designer") || lower.contains("дизайнер")) {
            return tr("intro.team.role.short.designer");
        }
        if (lower.contains("localizer") || lower.contains("локализатор") || lower.contains("локалізатор")) {
            return tr("intro.team.role.short.localizer");
        }
        if (lower.contains("artist") || lower.contains("художник")) {
            return tr("intro.team.role.short.artist");
        }
        if (lower.contains("tester") || lower.contains("тестер")) {
            return tr("intro.team.role.short.tester");
        }
        return localized;
    }

    private int getRoleBadgeFillColor(String role) {
        if (role == null) {
            return 0xFF4A5C74;
        }
        String lower = text(role).toLowerCase(java.util.Locale.ROOT);
        if (lower.contains("developer") || lower.contains("разработчик") || lower.contains("розробник")) {
            return 0xFFE06A1A;
        }
        if (lower.contains("designer") || lower.contains("дизайнер")) {
            return 0xFF7B4DFF;
        }
        if (lower.contains("localizer") || lower.contains("локализатор") || lower.contains("локалізатор")) {
            return 0xFF2C6DDA;
        }
        if (lower.contains("artist") || lower.contains("художник")) {
            return 0xFF00A896;
        }
        if (lower.contains("tester") || lower.contains("тестер")) {
            return 0xFF5A8F29;
        }
        return 0xFF4A5C74;
    }

    private LabelBadgeMetrics getBadgeMetrics(int maxWidth, String label, float textScale) {
        int safeMaxWidth = Math.max(28, maxWidth);
        int maxTextWidth = Math.max(1, (int) ((safeMaxWidth - 8) / textScale));
        List<FormattedCharSequence> lines = font.split(Component.literal(label), maxTextWidth);
        int maxLineWidth = 0;
        for (FormattedCharSequence line : lines) {
            maxLineWidth = Math.max(maxLineWidth, font.width(line));
        }
        int scaledTextWidth = Math.max(1, Math.round(maxLineWidth * textScale));
        int badgeWidth = Math.max(28, scaledTextWidth + 8);
        badgeWidth = Math.min(badgeWidth, safeMaxWidth);
        int scaledLineHeight = Math.max(1, Math.round(font.lineHeight * textScale));
        int badgeHeight = Math.max(10, 2 + lines.size() * scaledLineHeight + 2);
        return new LabelBadgeMetrics(lines, badgeWidth, badgeHeight, scaledLineHeight);
    }

    private void drawLabelBadge(GuiGraphics guiGraphics, int centerX, int badgeY, int maxWidth, String label, int fillColor, float textScale) {
        LabelBadgeMetrics metrics = getBadgeMetrics(maxWidth, label, textScale);
        int badgeX = centerX - metrics.width() / 2;
        guiGraphics.fill(badgeX, badgeY, badgeX + metrics.width(), badgeY + metrics.height(), fillColor);
        guiGraphics.fill(badgeX, badgeY, badgeX + metrics.width(), badgeY + 1, 0xFFFFFFFF);
        guiGraphics.fill(badgeX, badgeY + metrics.height() - 1, badgeX + metrics.width(), badgeY + metrics.height(), 0xFF1A1A1A);
        guiGraphics.fill(badgeX, badgeY, badgeX + 1, badgeY + metrics.height(), 0xFFFFFFFF);
        guiGraphics.fill(badgeX + metrics.width() - 1, badgeY, badgeX + metrics.width(), badgeY + metrics.height(), 0xFF1A1A1A);
        int lineY = badgeY + 2;
        for (FormattedCharSequence line : metrics.lines()) {
            drawCenteredScaledLine(guiGraphics, line, centerX, lineY, textScale, 0xFFFFFFFF);
            lineY += metrics.scaledLineHeight();
        }
    }

    private void drawCenteredText(GuiGraphics guiGraphics, String label, int centerX, int y, int color) {
        int textWidth = font.width(label);
        guiGraphics.drawString(font, label, centerX - textWidth / 2, y, color, false);
    }

    private void drawCenteredScaledText(GuiGraphics guiGraphics, String label, int centerX, int y, float scale, int color) {
        int scaledWidth = Math.round(font.width(label) * scale);
        int x = centerX - scaledWidth / 2;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(font, label, 0, 0, color, false);
        guiGraphics.pose().popPose();
    }

    private void drawCenteredScaledLine(GuiGraphics guiGraphics, FormattedCharSequence label, int centerX, int y, float scale, int color) {
        int scaledWidth = Math.round(font.width(label) * scale);
        int x = centerX - scaledWidth / 2;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(font, label, 0, 0, color, false);
        guiGraphics.pose().popPose();
    }

    private int getBlockSpacing(int index) {
        if (index < 0 || index >= markdownBlocks.size() - 1) {
            return 0;
        }
        IntroMarkdownBlock current = markdownBlocks.get(index);
        IntroMarkdownBlock next = markdownBlocks.get(index + 1);
        boolean currentImage = current instanceof IntroImageBlock;
        boolean nextImage = next instanceof IntroImageBlock;
        if (currentImage || nextImage) {
            return 0;
        }
        return 6;
    }

    private void clampAllScrolls(Layout l) {
        contentScroll = Mth.clamp(contentScroll, 0, Math.max(0, getMaxScroll(l)));
        partnerScroll = Mth.clamp(partnerScroll, 0, getPartnerMaxScroll(l));
        SidebarSections sections = sidebarSections(l);
        SectionMetrics teamMetrics = getTeamSectionMetrics(sections.team());
        SectionMetrics addonMetrics = getAddonSectionMetrics(sections.addons());
        teamScroll = Mth.clamp(teamScroll, 0, Math.max(0, teamMetrics.contentHeight() - sections.team().contentH()));
        addonsScroll = Mth.clamp(addonsScroll, 0, Math.max(0, addonMetrics.contentHeight() - sections.addons().contentH()));
        changelogScroll = Mth.clamp(changelogScroll, 0, Math.max(0, getChangelogContentHeight(sections.changelog().contentW()) - sections.changelog().contentH()));
    }

    private int getTotalContentHeight(Layout l) {
        int total = 4;
        for (int i = 0; i < markdownBlocks.size(); i++) {
            IntroMarkdownBlock block = markdownBlocks.get(i);
            total += block.getHeight(l.contentInnerW, font, imageManager);
            total += getBlockSpacing(i);
        }
        return total;
    }

    private int getMaxScroll(Layout l) {
        return Math.max(0, getTotalContentHeight(l) - l.contentBodyH);
    }

    private int getThumbHeight(Layout l, int maxScroll) {
        int trackH = l.contentBodyH - 8;
        int total = getTotalContentHeight(l);
        if (total <= 0) {
            return trackH;
        }
        return Mth.clamp((int) ((trackH * (double) l.contentBodyH) / total), 20, trackH);
    }

    private int getSectionThumbHeight(int viewportHeight, int trackHeight, int contentHeight) {
        if (contentHeight <= 0) {
            return trackHeight;
        }
        return Mth.clamp((int) ((trackHeight * (double) viewportHeight) / contentHeight), 18, trackHeight);
    }

    private int getThumbY(int scroll, int maxScroll, int trackY, int trackH, int thumbH) {
        if (maxScroll <= 0) {
            return trackY;
        }
        return trackY + Math.round((trackH - thumbH) * (scroll / (float) maxScroll));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double delta) {
        Layout l = layout();
        SidebarSections sections = sidebarSections(l);
        SectionMetrics teamMetrics = getTeamSectionMetrics(sections.team());
        SectionMetrics addonMetrics = getAddonSectionMetrics(sections.addons());

        if (IntroDrawUtil.isMouseOver(mouseX, mouseY, l.partnerX, l.partnerY, l.partnerW, l.partnerH)) {
            partnerScroll = Mth.clamp(partnerScroll - (int) (delta * 20), 0, getPartnerMaxScroll(l));
            return true;
        }
        if (IntroDrawUtil.isMouseOver(mouseX, mouseY, sections.team().panelX(), sections.team().panelY(), sections.team().panelW(), sections.team().panelH())) {
            notifyTeamUserInput();
            teamScroll = Mth.clamp(teamScroll - (int) (delta * 20), 0, Math.max(0, teamMetrics.contentHeight() - sections.team().contentH()));
            return true;
        }
        if (IntroDrawUtil.isMouseOver(mouseX, mouseY, sections.addons().panelX(), sections.addons().panelY(), sections.addons().panelW(), sections.addons().panelH())) {
            notifyAddonsUserInput();
            addonsScroll = Mth.clamp(addonsScroll - (int) (delta * 20), 0, Math.max(0, addonMetrics.contentHeight() - sections.addons().contentH()));
            return true;
        }
        if (IntroDrawUtil.isMouseOver(mouseX, mouseY, sections.changelog().panelX(), sections.changelog().panelY(), sections.changelog().panelW(), sections.changelog().panelH())) {
            changelogScroll = Mth.clamp(changelogScroll - (int) (delta * 20), 0, Math.max(0, getChangelogContentHeight(sections.changelog().contentW()) - sections.changelog().contentH()));
            return true;
        }
        if (IntroDrawUtil.isMouseOver(mouseX, mouseY, l.contentX, l.contentY, l.contentW, l.contentH)) {
            contentScroll -= (int) (delta * 24);
            clampAllScrolls(l);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, deltaX, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Layout l = layout();
        SidebarSections sections = sidebarSections(l);
        SectionMetrics teamMetrics = getTeamSectionMetrics(sections.team());
        SectionMetrics addonMetrics = getAddonSectionMetrics(sections.addons());

        if (button == 0) {
            if (IntroDrawUtil.isMouseOver(mouseX, mouseY, sections.team().panelX(), sections.team().panelY(), sections.team().panelW(), sections.team().panelH())) {
                notifyTeamUserInput();
            }
            if (IntroDrawUtil.isMouseOver(mouseX, mouseY, sections.addons().panelX(), sections.addons().panelY(), sections.addons().panelW(), sections.addons().panelH())) {
                notifyAddonsUserInput();
            }
            if (tryStartDraggingMainScrollbar(l, mouseX, mouseY)) return true;
            if (tryStartDraggingPartnerScrollbar(l, mouseX, mouseY)) return true;
            if (tryStartDraggingSectionScrollbar(sections.team(), mouseX, mouseY, DragTarget.TEAM, teamScroll, teamMetrics.contentHeight()))
                return true;
            if (tryStartDraggingSectionScrollbar(sections.addons(), mouseX, mouseY, DragTarget.ADDONS, addonsScroll, addonMetrics.contentHeight()))
                return true;
            if (tryStartDraggingSectionScrollbar(sections.changelog(), mouseX, mouseY, DragTarget.CHANGELOG, changelogScroll, getChangelogContentHeight(sections.changelog().contentW())))
                return true;

            Rect next = getNextPageButtonPage1(l);
            if (IntroDrawUtil.isMouseOver(mouseX, mouseY, next.x(), next.y(), next.w(), next.h())) {
                minecraft.setScreen(new IntroDashboardSecondScreen(data));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean tryStartDraggingMainScrollbar(Layout l, double mouseX, double mouseY) {
        int maxScroll = getMaxScroll(l);
        if (maxScroll <= 0) return false;
        int trackX = l.contentScrollbarX;
        int trackY = l.contentBodyY + 4;
        int trackW = l.contentScrollbarW;
        int trackH = l.contentBodyH - 8;
        int thumbH = getThumbHeight(l, maxScroll);
        int thumbY = getThumbY(contentScroll, maxScroll, trackY, trackH, thumbH);
        if (IntroDrawUtil.isMouseOver(mouseX, mouseY, trackX, thumbY, trackW, thumbH)) {
            dragTarget = DragTarget.CONTENT;
            scrollbarGrabOffset = mouseY - thumbY;
            return true;
        }
        return false;
    }

    private boolean tryStartDraggingPartnerScrollbar(Layout l, double mouseX, double mouseY) {
        SectionMetrics metrics = getPartnerSectionMetrics(l);
        if (!metrics.scrollbarVisible()) return false;
        int contentX = l.partnerX + 8;
        int contentY = l.partnerY + 24;
        int trackGap = 4;
        int trackW = 6;
        int trackX = contentX + metrics.usableWidth() + trackGap;
        int trackY = contentY;
        int trackH = getPartnersViewportHeight(l);
        int maxScroll = Math.max(0, metrics.contentHeight() - trackH);
        int thumbH = getSectionThumbHeight(trackH, trackH, metrics.contentHeight());
        int thumbY = getThumbY(partnerScroll, maxScroll, trackY, trackH, thumbH);
        if (IntroDrawUtil.isMouseOver(mouseX, mouseY, trackX, thumbY, trackW, thumbH)) {
            dragTarget = DragTarget.PARTNERS;
            scrollbarGrabOffset = mouseY - thumbY;
            return true;
        }
        return false;
    }

    private boolean tryStartDraggingSectionScrollbar(ScrollSection section, double mouseX, double mouseY, DragTarget target, int scroll, int contentHeight) {
        int maxScroll = Math.max(0, contentHeight - section.contentH());
        if (maxScroll <= 0) return false;
        int thumbH = getSectionThumbHeight(section.contentH(), section.scrollbarH(), contentHeight);
        int thumbY = getThumbY(scroll, maxScroll, section.scrollbarY(), section.scrollbarH(), thumbH);
        if (IntroDrawUtil.isMouseOver(mouseX, mouseY, section.scrollbarX(), thumbY, section.scrollbarW(), thumbH)) {
            dragTarget = target;
            scrollbarGrabOffset = mouseY - thumbY;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        Layout l = layout();
        SidebarSections sections = sidebarSections(l);
        SectionMetrics teamMetrics = getTeamSectionMetrics(sections.team());
        SectionMetrics addonMetrics = getAddonSectionMetrics(sections.addons());

        switch (dragTarget) {
            case CONTENT -> {
                int maxScroll = getMaxScroll(l);
                if (maxScroll <= 0) return true;
                int trackY = l.contentBodyY + 4;
                int trackH = l.contentBodyH - 8;
                int thumbH = getThumbHeight(l, maxScroll);
                double thumbY = mouseY - scrollbarGrabOffset;
                double ratio = (thumbY - trackY) / Math.max(1.0, (trackH - thumbH));
                ratio = Mth.clamp((float) ratio, 0.0F, 1.0F);
                contentScroll = (int) Math.round(ratio * maxScroll);
                clampAllScrolls(l);
                return true;
            }
            case PARTNERS -> {
                dragPartnerScroll(l, mouseY);
                return true;
            }
            case TEAM -> {
                notifyTeamUserInput();
                dragSectionScroll(sections.team(), mouseY, teamMetrics.contentHeight(), ScrollSetter.TEAM);
                return true;
            }
            case ADDONS -> {
                notifyAddonsUserInput();
                dragSectionScroll(sections.addons(), mouseY, addonMetrics.contentHeight(), ScrollSetter.ADDONS);
                return true;
            }
            case CHANGELOG -> {
                dragSectionScroll(sections.changelog(), mouseY, getChangelogContentHeight(sections.changelog().contentW()), ScrollSetter.CHANGELOG);
                return true;
            }
            default -> {
                return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
            }
        }
    }

    private void dragPartnerScroll(Layout l, double mouseY) {
        SectionMetrics metrics = getPartnerSectionMetrics(l);
        if (!metrics.scrollbarVisible()) return;
        int contentX = l.partnerX + 8;
        int contentY = l.partnerY + 24;
        int trackGap = 4;
        int trackY = contentY;
        int trackH = getPartnersViewportHeight(l);
        int maxScroll = Math.max(0, metrics.contentHeight() - trackH);
        int thumbH = getSectionThumbHeight(trackH, trackH, metrics.contentHeight());
        double thumbY = mouseY - scrollbarGrabOffset;
        double ratio = (thumbY - trackY) / Math.max(1.0, (trackH - thumbH));
        ratio = Mth.clamp((float) ratio, 0.0F, 1.0F);
        partnerScroll = (int) Math.round(ratio * maxScroll);
    }

    private void dragSectionScroll(ScrollSection section, double mouseY, int contentHeight, ScrollSetter setter) {
        int maxScroll = Math.max(0, contentHeight - section.contentH());
        if (maxScroll <= 0) return;
        int thumbH = getSectionThumbHeight(section.contentH(), section.scrollbarH(), contentHeight);
        double thumbY = mouseY - scrollbarGrabOffset;
        double ratio = (thumbY - section.scrollbarY()) / Math.max(1.0, (section.scrollbarH() - thumbH));
        ratio = Mth.clamp((float) ratio, 0.0F, 1.0F);
        int scroll = (int) Math.round(ratio * maxScroll);
        switch (setter) {
            case TEAM -> teamScroll = scroll;
            case ADDONS -> addonsScroll = scroll;
            case CHANGELOG -> changelogScroll = scroll;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragTarget = DragTarget.NONE;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private Layout layout() {
        Minecraft minecraft = Minecraft.getInstance();
        double guiScale = minecraft.getWindow().getGuiScale();
        int screenPad = 8;
        int maxFrameW = guiScale <= 3.0D ? 620 : 700;
        int maxFrameH = guiScale <= 3.0D ? 330 : 380;
        int frameW = Math.min(this.width - screenPad * 2, maxFrameW);
        int frameH = Math.min(this.height - screenPad * 2, maxFrameH);
        int frameX = (this.width - frameW) / 2;
        int frameY = (this.height - frameH) / 2;
        int innerPad = 8;
        int gap = 6;
        int innerX = frameX + innerPad;
        int innerY = frameY + innerPad;
        int innerW = frameW - innerPad * 2;
        int innerH = frameH - innerPad * 2;
        int rightW = Mth.clamp((int) (innerW * 0.36F), 205, 270);
        int leftW = innerW - rightW - gap;
        int topH = 58;
        int logoW = 92;
        int partnerW = leftW - logoW - gap - 10;
        int logoX = innerX;
        int logoY = innerY;
        int logoH = topH;
        int partnerX = logoX + logoW + gap + 10;
        int partnerY = innerY;
        int partnerH = topH;
        int contentX = innerX;
        int contentY = innerY + topH + gap;
        int contentW = leftW;
        int contentH = innerH - topH - gap;
        int rightX = contentX + leftW + gap;
        int rightY = innerY;
        int rightH = innerH;
        int contentHeaderH = 30;
        int contentBodyX = contentX + 4;
        int contentBodyY = contentY + 4;
        int contentBodyW = contentW - 8;
        int contentBodyH = contentH - 8;
        int scrollbarW = 6;
        int contentScrollbarX = contentX + contentW - 9;
        int contentInnerW = contentBodyW - scrollbarW - 3;
        return new Layout(frameX, frameY, frameW, frameH, logoX, logoY, logoW, logoH, partnerX, partnerY, partnerW, partnerH, contentX, contentY, contentW, contentH, contentHeaderH, contentBodyX, contentBodyY, contentBodyW, contentBodyH, contentInnerW, contentScrollbarX, scrollbarW, rightX, rightY, rightW, rightH);
    }

    private enum DragTarget {NONE, CONTENT, PARTNERS, TEAM, ADDONS, CHANGELOG}

    private enum ScrollSetter {TEAM, ADDONS, CHANGELOG}

    private record Layout(
            int frameX, int frameY, int frameW, int frameH,
            int logoX, int logoY, int logoW, int logoH,
            int partnerX, int partnerY, int partnerW, int partnerH,
            int contentX, int contentY, int contentW, int contentH,
            int contentHeaderH, int contentBodyX, int contentBodyY, int contentBodyW, int contentBodyH,
            int contentInnerW,
            int contentScrollbarX, int contentScrollbarW,
            int rightX, int rightY, int rightW, int rightH
    ) {
    }

    private record ScrollSection(
            int panelX, int panelY, int panelW, int panelH,
            int contentX, int contentY, int contentW, int contentH,
            int scrollbarX, int scrollbarY, int scrollbarW, int scrollbarH
    ) {
    }

    private record SidebarSections(ScrollSection team, ScrollSection addons, ScrollSection changelog) {
    }

    private record LabelBadgeMetrics(List<FormattedCharSequence> lines, int width, int height, int scaledLineHeight) {
    }

    private record Rect(int x, int y, int w, int h) {
    }

    private record SectionMetrics(int usableWidth, int contentHeight, boolean scrollbarVisible) {
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
                if (now - lastUserInputAt < AUTO_SCROLL_IDLE_MS) {
                    return currentScroll;
                }
                autoActive = true;
                direction = currentScroll >= maxScroll ? -1 : 1;
                edgePauseUntil = now + 350L;
            }
            if (now < edgePauseUntil) {
                return Mth.clamp(Math.round(exactScroll), 0, maxScroll);
            }
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
