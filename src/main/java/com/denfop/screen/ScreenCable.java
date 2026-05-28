package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ImageInterfaceWidget;
import com.denfop.containermenu.ContainerMenuCable;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.screen.cable.CablePreviewHitResult;
import com.denfop.screen.cable.CablePreviewPart;
import com.denfop.screen.cable.CablePreviewRenderer;
import com.denfop.screen.cable.CablePreviewStateFactory;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class ScreenCable<T extends ContainerMenuCable> extends ScreenMain<ContainerMenuCable> {

    private static final int PREVIEW_X = 20;
    private static final int PREVIEW_Y = 18;
    private static final int PREVIEW_W = 196;
    private static final int PREVIEW_H = 138;

    private static final int HEADER_X = 12;
    private static final int HEADER_Y = 6;

    private static final int STATUS_PANEL_Y = 156;
    private static final int STATUS_PANEL_H = 22;

    private static final int HINT_PANEL_Y = 180;
    private static final int HINT_PANEL_H = 38;

    private static final int CONTENT_X = 12;
    private static final int CONTENT_W = 212;

    private static final float DEFAULT_YAW = -35.0F;
    private static final float DEFAULT_PITCH = 25.0F;
    private static final float DEFAULT_ZOOM = 0.96F;

    private final CablePreviewRenderer previewRenderer = new CablePreviewRenderer();
    private final EnumMap<Direction, Boolean> pendingExpectedBlacklistState = new EnumMap<>(Direction.class);
    private final EnumMap<Direction, BlockState> currentNeighborStates = new EnumMap<>(Direction.class);

    private CablePreviewStateFactory.PreparedPreview preparedPreview;
    private CablePreviewHitResult hoveredHit;
    private OverlayButtonBounds overlayButtonBounds;

    private Direction stickyOverlayDirection;
    private Vec3 stickyOverlayAnchor = new Vec3(0.5D, 0.5D, 0.5D);

    private float yaw = DEFAULT_YAW;
    private float pitch = DEFAULT_PITCH;
    private float zoom = DEFAULT_ZOOM;

    private int stickyOverlayGraceTicks = 0;
    private boolean draggingPreview = false;

    public ScreenCable(ContainerMenuCable guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 236;
        this.imageHeight = 222;

        this.addWidget(new ImageInterfaceWidget(this, 0, 0, imageWidth, imageHeight));
    }

    private Component tr(String key, Object... args) {
        return Component.translatable(key, args);
    }

    public void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.limiter.info"));
            List<String> compatibleUpgrades = ListInformationUtils.limiter_info;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton != 0) {
            return;
        }

        if (this.overlayButtonBounds != null && this.overlayButtonBounds.contains(mouseX, mouseY)) {
            toggleDirection(this.overlayButtonBounds.direction());
            this.draggingPreview = false;
            return;
        }

        if (isInsidePreview(mouseX, mouseY)) {
            this.draggingPreview = true;
        }
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
            this.yaw += (float) dragX * 0.9F;
            this.pitch += (float) dragY * 0.65F;
            this.pitch = Mth.clamp(this.pitch, -89.0F, 89.0F);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double delta) {
        if (isInsidePreview(mouseX, mouseY)) {
            this.zoom = Mth.clamp(this.zoom + (float) delta * 0.08F, 0.72F, 1.80F);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, deltaX, delta);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
        com.mojang.blaze3d.systems.RenderSystem.depthMask(false);

        drawOverlayButton(guiGraphics, mouseX, mouseY);

        this.handleUpgradeTooltip(mouseX - this.guiLeft, mouseY - this.guiTop);
        renderHoverTooltip(guiGraphics, mouseX, mouseY);

        com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
    }

    private boolean shouldKeepStickyOverlay(int mouseX, int mouseY, OverlayButtonBounds previousOverlay) {
        if (previousOverlay == null) {
            return false;
        }

        if (previousOverlay.contains(mouseX, mouseY)) {
            return true;
        }

        int previewLeft = this.guiLeft + PREVIEW_X;
        int previewTop = this.guiTop + PREVIEW_Y;
        int previewRight = previewLeft + PREVIEW_W;
        int previewBottom = previewTop + PREVIEW_H;

        int unionLeft = Math.min(previewLeft, previousOverlay.x()) - 8;
        int unionTop = Math.min(previewTop, previousOverlay.y()) - 8;
        int unionRight = Math.max(previewRight, previousOverlay.x() + previousOverlay.w()) + 8;
        int unionBottom = Math.max(previewBottom, previousOverlay.y() + previousOverlay.h()) + 8;

        return mouseX >= unionLeft
                && mouseX <= unionRight
                && mouseY >= unionTop
                && mouseY <= unionBottom;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(guiGraphics, partialTicks, mouseX, mouseY);

        int previewAbsX = this.guiLeft + PREVIEW_X;
        int previewAbsY = this.guiTop + PREVIEW_Y;

        EnumSet<Direction> displayBlacklist = getDisplayBlacklist();
        this.preparedPreview = CablePreviewStateFactory.prepare(this.container.base.getBlockState(), displayBlacklist);

        this.currentNeighborStates.clear();
        this.currentNeighborStates.putAll(collectNeighborStates());

        drawPanels(guiGraphics, previewAbsX, previewAbsY);
        drawHeader(guiGraphics, displayBlacklist);

        guiGraphics.enableScissor(previewAbsX, previewAbsY, previewAbsX + PREVIEW_W, previewAbsY + PREVIEW_H);

        this.hoveredHit = this.previewRenderer.pick(
                mouseX,
                mouseY,
                previewAbsX,
                previewAbsY,
                PREVIEW_W,
                PREVIEW_H,
                this.preparedPreview,
                this.yaw,
                this.pitch,
                this.zoom
        );

        this.previewRenderer.render(
                guiGraphics,
                previewAbsX,
                previewAbsY,
                PREVIEW_W,
                PREVIEW_H,
                this.preparedPreview,
                displayBlacklist,
                this.hoveredHit,
                this.currentNeighborStates,
                this.yaw,
                this.pitch,
                this.zoom
        );

        guiGraphics.disableScissor();
        com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
        com.mojang.blaze3d.systems.RenderSystem.depthMask(false);
        drawDirectionBadges(guiGraphics, displayBlacklist);
        drawStatus(guiGraphics, displayBlacklist);
        drawHints(guiGraphics);
        com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
        OverlayButtonBounds previousOverlay = this.overlayButtonBounds;
        boolean keepSticky = shouldKeepStickyOverlay(mouseX, mouseY, previousOverlay);

        if (this.hoveredHit != null && this.hoveredHit.part().isToggleable()) {
            this.stickyOverlayDirection = this.hoveredHit.direction();
            this.stickyOverlayAnchor = this.hoveredHit.anchor();
            this.stickyOverlayGraceTicks = 8;
        } else if (keepSticky) {
            this.stickyOverlayGraceTicks = 8;
        } else if (this.stickyOverlayGraceTicks > 0) {
            this.stickyOverlayGraceTicks--;
        } else {
            this.stickyOverlayDirection = null;
        }

        this.overlayButtonBounds = buildOverlayButton(displayBlacklist, keepSticky || this.stickyOverlayGraceTicks > 0);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_progressbars1.png");
    }

    private void drawPanels(GuiGraphics guiGraphics, int previewAbsX, int previewAbsY) {
        guiGraphics.fill(
                this.guiLeft + 4,
                this.guiTop + 4,
                this.guiLeft + this.imageWidth - 4,
                this.guiTop + this.imageHeight - 4,
                0xA0060910
        );

        guiGraphics.fill(previewAbsX - 2, previewAbsY - 2, previewAbsX + PREVIEW_W + 2, previewAbsY + PREVIEW_H + 2, 0xC0080A10);
        guiGraphics.fill(previewAbsX - 1, previewAbsY - 1, previewAbsX + PREVIEW_W + 1, previewAbsY + PREVIEW_H + 1, 0xFF2B3245);
        guiGraphics.fill(previewAbsX, previewAbsY, previewAbsX + PREVIEW_W, previewAbsY + PREVIEW_H, 0xFF0F1726);

        int contentLeft = this.guiLeft + CONTENT_X;
        int contentRight = contentLeft + CONTENT_W;

        guiGraphics.fill(
                contentLeft,
                this.guiTop + STATUS_PANEL_Y,
                contentRight,
                this.guiTop + STATUS_PANEL_Y + STATUS_PANEL_H,
                0xA0141924
        );

        guiGraphics.fill(
                contentLeft,
                this.guiTop + HINT_PANEL_Y,
                contentRight,
                this.guiTop + HINT_PANEL_Y + HINT_PANEL_H,
                0xA0141924
        );
    }

    private void drawHeader(GuiGraphics guiGraphics, EnumSet<Direction> displayBlacklist) {
        guiGraphics.drawString(
                this.font,
                tr("iu.cable.preview.title"),
                this.guiLeft + HEADER_X,
                this.guiTop + HEADER_Y,
                0xE9EEF8,
                false
        );

        int connected = 6 - displayBlacklist.size();
        drawChip(
                guiGraphics,
                this.guiLeft + 178,
                this.guiTop + 5,
                46,
                12,
                0xCC17301F,
                tr("iu.cable.preview.header.on", connected),
                0x90FFB0
        );
    }

    private void drawStatus(GuiGraphics guiGraphics, EnumSet<Direction> displayBlacklist) {
        int connected = 6 - displayBlacklist.size();
        int disconnected = displayBlacklist.size();

        Component connectedText = tr("iu.cable.preview.status.connected", connected);
        Component disconnectedText = tr("iu.cable.preview.status.disconnected", disconnected);

        int connectedWidth = Math.max(92, this.font.width(connectedText) + 18);
        int disconnectedWidth = Math.max(98, this.font.width(disconnectedText) + 18);

        int gap = 8;
        int contentLeft = this.guiLeft + CONTENT_X + 2;
        int contentWidth = CONTENT_W - 4;

        int totalWidth = connectedWidth + gap + disconnectedWidth;
        int startX = contentLeft + Math.max(0, (contentWidth - totalWidth) / 2);
        int y = this.guiTop + STATUS_PANEL_Y + 4;

        drawChip(guiGraphics, startX, y, connectedWidth, 14, 0xCC17301F, connectedText, 0x8BFFAC);
        drawChip(guiGraphics, startX + connectedWidth + gap, y, disconnectedWidth, 14, 0xCC341A1A, disconnectedText, 0xFF9B90);

        if (!this.pendingExpectedBlacklistState.isEmpty()) {
            Component syncText = tr("iu.cable.preview.status.sync");
            int syncWidth = Math.min(CONTENT_W - 8, Math.max(150, this.font.width(syncText) + 16));
            int syncX = this.guiLeft + CONTENT_X + (CONTENT_W - syncWidth) / 2;
            int syncY = this.guiTop + STATUS_PANEL_Y - 16;

            drawChip(guiGraphics, syncX, syncY, syncWidth, 13, 0xCC203041, syncText, 0x9AD7FF);
        }
    }

    private void drawHints(GuiGraphics guiGraphics) {
        int panelX = this.guiLeft + CONTENT_X;
        int panelY = this.guiTop + HINT_PANEL_Y;
        int panelW = CONTENT_W;
        int panelH = HINT_PANEL_H;

        int textX = panelX + 4;
        int textY = panelY + 4;
        int maxWidth = panelW - 8;
        int maxBottom = panelY + panelH - 3;

        drawWrappedTextBlock(
                guiGraphics,
                tr("iu.cable.preview.hint.hover"),
                textX,
                textY,
                maxWidth,
                0xD8DFEE,
                maxBottom
        );

        drawWrappedTextBlock(
                guiGraphics,
                tr("iu.cable.preview.hint.controls"),
                textX,
                textY + 11,
                maxWidth,
                0x95A3BB,
                maxBottom
        );
    }

    private int drawWrappedTextBlock(GuiGraphics guiGraphics,
                                     Component text,
                                     int x,
                                     int y,
                                     int maxWidth,
                                     int color,
                                     int maxBottom) {
        List<FormattedCharSequence> lines = this.font.split(text, maxWidth);
        int lineHeight = this.font.lineHeight + 1;
        int currentY = y;

        for (FormattedCharSequence line : lines) {
            if (currentY + this.font.lineHeight > maxBottom) {
                break;
            }

            guiGraphics.drawString(this.font, line, x, currentY, color, false);
            currentY += lineHeight;
        }

        return currentY;
    }

    private void drawChip(GuiGraphics guiGraphics, int x, int y, int w, int h, int bgColor, Component text, int textColor) {
        guiGraphics.fill(x, y, x + w, y + h, 0xA0000000);
        guiGraphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, bgColor);
        guiGraphics.drawCenteredString(this.font, text, x + (w / 2), y + (h - 8) / 2, textColor);
    }

    private void drawDirectionBadges(GuiGraphics guiGraphics, EnumSet<Direction> displayBlacklist) {
        int previewAbsX = this.guiLeft + PREVIEW_X;
        int previewAbsY = this.guiTop + PREVIEW_Y;

        for (Direction direction : Direction.values()) {
            Vec3 anchor = directionAnchor(direction);
            CablePreviewRenderer.ScreenProjection projection = this.previewRenderer.projectPoint(
                    anchor,
                    previewAbsX,
                    previewAbsY,
                    PREVIEW_W,
                    PREVIEW_H,
                    this.yaw,
                    this.pitch,
                    this.zoom
            );

            int badgeW = 16;
            int badgeH = 12;
            int x = Mth.clamp(Math.round(projection.x()) - badgeW / 2, previewAbsX + 2, previewAbsX + PREVIEW_W - badgeW - 2);
            int y = Mth.clamp(Math.round(projection.y()) - badgeH / 2, previewAbsY + 2, previewAbsY + PREVIEW_H - badgeH - 2);

            boolean hovered = this.hoveredHit != null && direction == this.hoveredHit.direction();
            boolean disconnected = displayBlacklist.contains(direction);
            boolean hasNeighbor = this.currentNeighborStates.containsKey(direction);

            int outer = hovered ? 0xFF5BAFFF : (disconnected ? 0xFF6E3B3B : 0xFF355940);
            int inner = hovered ? 0xE01E314D : (disconnected ? 0xD03A1E1E : 0xD0183220);
            int textColor = hovered ? 0xFFFFFF : (hasNeighbor ? 0xF7FBFF : 0xDDE4EF);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 500.0F);
            guiGraphics.fill(x, y, x + badgeW, y + badgeH, outer);
            guiGraphics.fill(x + 1, y + 1, x + badgeW - 1, y + badgeH - 1, inner);
            guiGraphics.drawCenteredString(this.font, shortDirection(direction), x + badgeW / 2, y + 2, textColor);
            guiGraphics.pose().popPose();
        }
    }

    private OverlayButtonBounds buildOverlayButton(EnumSet<Direction> displayBlacklist, boolean keepSticky) {
        Direction direction = null;
        Vec3 anchor = null;

        if (this.hoveredHit != null && this.hoveredHit.part().isToggleable()) {
            direction = this.hoveredHit.direction();
            anchor = this.hoveredHit.anchor();
        } else if (keepSticky && this.stickyOverlayDirection != null) {
            direction = this.stickyOverlayDirection;
            anchor = this.stickyOverlayAnchor;
        }

        if (direction == null || anchor == null) {
            return null;
        }

        Component label = displayBlacklist.contains(direction)
                ? tr("iu.cable.preview.button.connect")
                : tr("iu.cable.preview.button.disconnect");

        int previewAbsX = this.guiLeft + PREVIEW_X;
        int previewAbsY = this.guiTop + PREVIEW_Y;

        CablePreviewRenderer.ScreenProjection anchorProjection = this.previewRenderer.projectPoint(
                anchor,
                previewAbsX,
                previewAbsY,
                PREVIEW_W,
                PREVIEW_H,
                this.yaw,
                this.pitch,
                this.zoom
        );

        CablePreviewRenderer.ScreenProjection centerProjection = this.previewRenderer.projectPoint(
                new Vec3(0.5D, 0.5D, 0.5D),
                previewAbsX,
                previewAbsY,
                PREVIEW_W,
                PREVIEW_H,
                this.yaw,
                this.pitch,
                this.zoom
        );

        int width = Math.max(78, this.font.width(label) + 14);
        int height = 16;

        float dx = anchorProjection.x() - centerProjection.x();
        float dy = anchorProjection.y() - centerProjection.y();

        if (Math.abs(dx) < 0.001F && Math.abs(dy) < 0.001F) {
            dx = switch (direction) {
                case EAST -> 1.0F;
                case WEST -> -1.0F;
                default -> 0.0F;
            };
            dy = switch (direction) {
                case UP -> -1.0F;
                case DOWN -> 1.0F;
                case NORTH -> -0.65F;
                case SOUTH -> 0.65F;
                default -> 0.0F;
            };
        }

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len < 0.001F) {
            len = 1.0F;
        }

        float nx = dx / len;
        float ny = dy / len;

        float distance = 52.0F + Math.max(width, height) * 0.35F;

        int x = Math.round(anchorProjection.x() + nx * distance - width / 2.0F);
        int y = Math.round(anchorProjection.y() + ny * distance - height / 2.0F);

        int minX = this.guiLeft + 6;
        int maxX = this.guiLeft + this.imageWidth - width - 6;
        int minY = this.guiTop + 6;
        int maxY = this.guiTop + this.imageHeight - height - 6;

        x = Mth.clamp(x, minX, maxX);
        y = Mth.clamp(y, minY, maxY);

        int centerSafeLeft = Math.round(centerProjection.x() - 38);
        int centerSafeRight = Math.round(centerProjection.x() + 38);
        int centerSafeTop = Math.round(centerProjection.y() - 30);
        int centerSafeBottom = Math.round(centerProjection.y() + 30);

        boolean intersectsCenter =
                x < centerSafeRight &&
                        x + width > centerSafeLeft &&
                        y < centerSafeBottom &&
                        y + height > centerSafeTop;

        if (intersectsCenter) {
            x = Math.round(anchorProjection.x() + nx * (distance + 26.0F) - width / 2.0F);
            y = Math.round(anchorProjection.y() + ny * (distance + 26.0F) - height / 2.0F);

            x = Mth.clamp(x, minX, maxX);
            y = Mth.clamp(y, minY, maxY);
        }

        return new OverlayButtonBounds(x, y, width, height, direction, label);
    }

    private void drawOverlayButton(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.overlayButtonBounds == null) {
            return;
        }

        boolean hovered = this.overlayButtonBounds.contains(mouseX, mouseY);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 500.0F);
        int x = this.overlayButtonBounds.x();
        int y = this.overlayButtonBounds.y();
        int w = this.overlayButtonBounds.w();
        int h = this.overlayButtonBounds.h();

        int shadow = hovered ? 0x66264C8F : 0x44224466;
        int outer = hovered ? 0xFF6A7FA8 : 0xFF4A5871;
        int inner = hovered ? 0xF0303A4F : 0xF01D2432;
        int border2 = hovered ? 0xFF90C4FF : 0xFF6D87B2;
        int text = hovered ? 0xFFFFFF : 0xEAF2FF;

        guiGraphics.fill(x - 2, y - 2, x + w + 2, y + h + 2, shadow);
        guiGraphics.fill(x - 1, y - 1, x + w + 1, y + h + 1, outer);
        guiGraphics.fill(x, y, x + w, y + h, border2);
        guiGraphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, inner);

        guiGraphics.drawCenteredString(
                this.font,
                this.overlayButtonBounds.label(),
                x + (w / 2),
                y + (h - 8) / 2,
                text
        );
        com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
        guiGraphics.pose().popPose();
    }

    private void renderHoverTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.overlayButtonBounds != null && this.overlayButtonBounds.contains(mouseX, mouseY)) {
            return;
        }

        if (this.hoveredHit == null) {
            return;
        }

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(getPartName(this.hoveredHit.part()));

        Direction direction = this.hoveredHit.direction();
        if (direction != null) {
            boolean disconnected = getDisplayBlacklist().contains(direction);
            tooltip.add(disconnected
                    ? tr("iu.cable.preview.tooltip.side_disconnected")
                    : tr("iu.cable.preview.tooltip.side_connected"));

            BlockState neighborState = this.currentNeighborStates.get(direction);
            if (neighborState != null && !neighborState.isAir()) {
                tooltip.add(tr(
                        "iu.cable.preview.tooltip.neighbor",
                        Component.translatable(neighborState.getBlock().getDescriptionId())
                ));
            } else {
                tooltip.add(tr("iu.cable.preview.tooltip.neighbor_empty"));
            }

            tooltip.add(tr("iu.cable.preview.tooltip.direction", shortDirection(direction)));
            tooltip.add(disconnected
                    ? tr("iu.cable.preview.tooltip.action_connect")
                    : tr("iu.cable.preview.tooltip.action_disconnect"));

            if (this.pendingExpectedBlacklistState.containsKey(direction)) {
                tooltip.add(tr("iu.cable.preview.tooltip.waiting_sync"));
            }
        } else {
            tooltip.add(tr("iu.cable.preview.tooltip.center"));
        }

        guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
    }

    private Component getPartName(CablePreviewPart part) {
        return switch (part) {
            case CENTER -> tr("iu.cable.preview.part.center");
            case NORTH -> tr("iu.cable.preview.part.north");
            case SOUTH -> tr("iu.cable.preview.part.south");
            case EAST -> tr("iu.cable.preview.part.east");
            case WEST -> tr("iu.cable.preview.part.west");
            case UP -> tr("iu.cable.preview.part.up");
            case DOWN -> tr("iu.cable.preview.part.down");
        };
    }

    private boolean isInsidePreview(double mouseX, double mouseY) {
        int previewAbsX = this.guiLeft + PREVIEW_X;
        int previewAbsY = this.guiTop + PREVIEW_Y;
        return mouseX >= previewAbsX
                && mouseX < previewAbsX + PREVIEW_W
                && mouseY >= previewAbsY
                && mouseY < previewAbsY + PREVIEW_H;
    }

    private void toggleDirection(Direction direction) {
        if (direction == null) {
            return;
        }

        EnumSet<Direction> currentDisplay = getDisplayBlacklist();
        boolean isCurrentlyBlacklisted = currentDisplay.contains(direction);
        boolean newBlacklistedState = !isCurrentlyBlacklisted;

        this.pendingExpectedBlacklistState.put(direction, newBlacklistedState);

        new PacketUpdateServerTile(this.container.base, direction.ordinal());
    }

    private EnumSet<Direction> getDisplayBlacklist() {
        EnumSet<Direction> serverState = EnumSet.noneOf(Direction.class);
        if (this.container.base.getBlackList() != null) {
            serverState.addAll(this.container.base.getBlackList());
        }

        if (this.pendingExpectedBlacklistState.isEmpty()) {
            return serverState;
        }

        EnumSet<Direction> visualState = EnumSet.copyOf(serverState);

        List<Direction> completed = new ArrayList<>();
        for (var entry : this.pendingExpectedBlacklistState.entrySet()) {
            Direction direction = entry.getKey();
            boolean expectedBlacklisted = entry.getValue();
            boolean actualBlacklisted = serverState.contains(direction);

            if (actualBlacklisted == expectedBlacklisted) {
                completed.add(direction);
                continue;
            }

            if (expectedBlacklisted) {
                visualState.add(direction);
            } else {
                visualState.remove(direction);
            }
        }

        for (Direction direction : completed) {
            this.pendingExpectedBlacklistState.remove(direction);
        }

        return visualState;
    }

    private EnumMap<Direction, BlockState> collectNeighborStates() {
        EnumMap<Direction, BlockState> result = new EnumMap<>(Direction.class);

        if (this.container.base == null || this.container.base.getLevel() == null) {
            return result;
        }

        for (Direction direction : Direction.values()) {
            BlockState state = this.container.base.getLevel().getBlockState(this.container.base.getBlockPos().relative(direction));
            if (state != null && !state.isAir()) {
                result.put(direction, state);
            }
        }

        return result;
    }

    private Vec3 directionAnchor(Direction direction) {
        return new Vec3(
                0.5D + direction.getStepX() * 1.55D,
                0.5D + direction.getStepY() * 1.55D,
                0.5D + direction.getStepZ() * 1.55D
        );
    }

    private String shortDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case WEST -> "W";
            case EAST -> "E";
            case UP -> "U";
            case DOWN -> "D";
        };
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 82) {
            this.yaw = DEFAULT_YAW;
            this.pitch = DEFAULT_PITCH;
            this.zoom = DEFAULT_ZOOM;
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private record OverlayButtonBounds(int x, int y, int w, int h, Direction direction, Component label) {
        private boolean contains(int mouseX, int mouseY) {
            return mouseX >= this.x
                    && mouseX < this.x + this.w
                    && mouseY >= this.y
                    && mouseY < this.y + this.h;
        }
    }
}