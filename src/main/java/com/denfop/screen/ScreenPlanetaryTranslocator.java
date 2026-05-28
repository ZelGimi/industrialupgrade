package com.denfop.screen;

import com.denfop.api.space.*;
import com.denfop.client.space.SpaceTeleportClientState;
import com.denfop.items.space.teleport.*;
import com.denfop.network.packet.PacketPlanetaryReturnRequest;
import com.denfop.network.packet.PacketPlanetaryTeleportRequest;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.*;
import java.lang.System;

public class ScreenPlanetaryTranslocator extends Screen {

    private static final int GUI_W = 540;
    private static final int GUI_H = 304;

    private static final int LEFT_PANEL_X = 14;
    private static final int LEFT_PANEL_Y = 30;
    private static final int LEFT_PANEL_W = 212;
    private static final int LEFT_PANEL_H = 258;

    private static final int LIST_VIEW_X = 18;
    private static final int LIST_VIEW_Y = 42;
    private static final int LIST_VIEW_W = 204;
    private static final int LIST_VIEW_H = 218;

    private static final int LIST_FOOTER_X = 18;
    private static final int LIST_FOOTER_Y = 264;
    private static final int LIST_FOOTER_W = 138;
    private static final int LIST_FOOTER_H = 18;

    private static final int RIGHT_PANEL_X = 236;
    private static final int RIGHT_PANEL_Y = 30;
    private static final int RIGHT_PANEL_W = 290;
    private static final int RIGHT_PANEL_H = 258;

    private static final int PREVIEW_BOX_X = 252;
    private static final int PREVIEW_BOX_Y = 52;
    private static final int PREVIEW_BOX_W = 132;
    private static final int PREVIEW_BOX_H = 122;

    private static final int INFO_X = 394;
    private static final int INFO_Y = 56;
    private static final int INFO_W = 118;
    private static final int INFO_H = 120;

    private static final int TELEPORT_BTN_X = 332;
    private static final int TELEPORT_BTN_Y = 238;
    private static final int TELEPORT_BTN_W = 176;
    private static final int TELEPORT_BTN_H = 24;

    private static final int RETURN_BTN_X = 332;
    private static final int RETURN_BTN_Y = 266;
    private static final int RETURN_BTN_W = 176;
    private static final int RETURN_BTN_H = 18;

    private static final int ROW_H = 26;
    private static final int INDENT_SYSTEM_CHILD = 14;
    private static final int INDENT_PLANET_CHILD = 16;

    private static final float EXPAND_SPEED = 8.5F;

    private final SpaceTeleportScreenData data;
    private final Map<String, SpaceTeleportScreenData.Entry> bodyEntryLookup = new HashMap<>();
    private final List<float[]> stars = new ArrayList<>();
    private final List<SystemNode> systemNodes = new ArrayList<>();
    private final Deque<ScissorRect> scissorStack = new ArrayDeque<>();

    private SystemNode selectedSystemNode;
    private BodyNode selectedBodyNode;

    private float scrollY = 0.0F;
    private long lastFrameMs = System.currentTimeMillis();
    private boolean hasActiveAnimations = false;
    private float renderPartialTick = 0.0F;

    public ScreenPlanetaryTranslocator(final SpaceTeleportScreenData data) {
        super(Component.literal(fallback("iu.space.tp.title")));
        this.data = data;

        for (SpaceTeleportScreenData.Entry entry : data.entries) {
            if (entry != null && entry.isBodyEntry()) {
                bodyEntryLookup.put(entry.bodyName, entry);
            }
        }

        generateStars();
        buildTree();
        initSelection();
    }

    private static String fallback(final String key) {
        String translated = Localization.translate(key);

        return translated;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private int guiLeft() {
        return (this.width - GUI_W) / 2;
    }

    private int guiTop() {
        return (this.height - GUI_H) / 2;
    }

    private void generateStars() {
        Random random = new Random(9127311L);
        stars.clear();
        for (int i = 0; i < 240; i++) {
            stars.add(new float[]{
                    random.nextFloat(),
                    random.nextFloat(),
                    0.4F + random.nextFloat() * 0.8F
            });
        }
    }

    private IBody resolveEstimatedBody() {
        if (selectedBodyNode != null) {
            return selectedBodyNode.body;
        }

        String bodyName = !safe(data.activeBodyName).isEmpty() ? data.activeBodyName : data.selectedBodyName;
        if (safe(bodyName).isEmpty()) {
            return null;
        }

        return SpaceNet.instance.getBodyFromName(bodyName);
    }

    private void buildTree() {
        systemNodes.clear();

        List<ISystem> systems = new ArrayList<>(SpaceNet.instance.getSystem());
        systems.removeIf(system -> system == null || system.getStarList() == null || system.getStarList().isEmpty());

        for (ISystem system : systems) {
            IStar mainStar = system.getStarList().get(0);
            if (mainStar == null) {
                continue;
            }

            SystemNode systemNode = new SystemNode(system, mainStar);

            List<IPlanet> planets = new ArrayList<>(mainStar.getPlanetList());
            planets.sort(Comparator.comparingDouble(IBody::getDistance));

            for (IPlanet planet : planets) {
                BodyNode planetNode = new BodyNode(BodyKind.PLANET, planet, systemNode, null);
                List<ISatellite> satellites = new ArrayList<>(planet.getSatelliteList());
                satellites.sort(Comparator.comparingDouble(ISatellite::getDistanceFromPlanet));
                for (ISatellite satellite : satellites) {
                    planetNode.satellites.add(new BodyNode(BodyKind.SATELLITE, satellite, systemNode, planetNode));
                }
                systemNode.children.add(planetNode);
            }

            List<IAsteroid> asteroids = new ArrayList<>(mainStar.getAsteroidList());
            asteroids.sort(Comparator.comparingDouble(IBody::getDistance));
            for (IAsteroid asteroid : asteroids) {
                systemNode.children.add(new BodyNode(BodyKind.ASTEROID, asteroid, systemNode, null));
            }

            systemNodes.add(systemNode);
        }

        systemNodes.sort(Comparator.comparing(node -> node.system.getName()));
    }

    private void initSelection() {
        String preferredBody = safe(!safe(data.activeBodyName).isEmpty() ? data.activeBodyName : data.selectedBodyName);

        if (!preferredBody.isEmpty()) {
            for (SystemNode systemNode : systemNodes) {
                for (BodyNode bodyNode : systemNode.children) {
                    if (bodyNode.body.getName().equals(preferredBody)) {
                        selectedSystemNode = systemNode;
                        selectedBodyNode = bodyNode;
                        systemNode.expandProgress = 1.0F;
                        systemNode.expandTarget = 1.0F;
                        return;
                    }
                    for (BodyNode satelliteNode : bodyNode.satellites) {
                        if (satelliteNode.body.getName().equals(preferredBody)) {
                            selectedSystemNode = systemNode;
                            selectedBodyNode = satelliteNode;
                            systemNode.expandProgress = 1.0F;
                            systemNode.expandTarget = 1.0F;
                            bodyNode.expandProgress = 1.0F;
                            bodyNode.expandTarget = 1.0F;
                            return;
                        }
                    }
                }
            }
        }

        if (!systemNodes.isEmpty()) {
            selectedSystemNode = systemNodes.get(0);
            selectedBodyNode = null;
        }
    }

    @Override
    public void render(final GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderPartialTick = partialTick;
        tickAnimations();
        clampScroll();

        renderBackground(graphics, mouseX, mouseY, partialTick);
        renderStarField(graphics);

        int left = guiLeft();
        int top = guiTop();

        drawFrame(graphics, left, top, GUI_W, GUI_H);
        drawPanel(graphics, left + LEFT_PANEL_X, top + LEFT_PANEL_Y, LEFT_PANEL_W, LEFT_PANEL_H);
        drawPanel(graphics, left + RIGHT_PANEL_X, top + RIGHT_PANEL_Y, RIGHT_PANEL_W, RIGHT_PANEL_H);

        graphics.drawString(this.font, fallback("iu.space.tp.title"), left + 18, top + 8, 0xFFFFFF, false);
        graphics.drawString(this.font, fallback("iu.space.tp.list"), left + LEFT_PANEL_X + 6, top + 20, 0x9EEBFF, false);
        graphics.drawString(this.font, fallback("iu.space.tp.preview"), left + RIGHT_PANEL_X + 8, top + 20, 0x9EEBFF, false);

        renderLeftList(graphics, mouseX, mouseY);
        renderRightInfo(graphics, mouseX, mouseY);


    }

    private void tickAnimations() {
        long now = System.currentTimeMillis();
        float delta = Math.min(0.05F, (now - lastFrameMs) / 1000.0F);
        lastFrameMs = now;
        hasActiveAnimations = false;

        for (SystemNode systemNode : systemNodes) {
            systemNode.expandProgress = approach(systemNode.expandProgress, systemNode.expandTarget, delta * EXPAND_SPEED);
            if (Math.abs(systemNode.expandProgress - systemNode.expandTarget) > 0.001F) {
                hasActiveAnimations = true;
            }

            for (BodyNode bodyNode : systemNode.children) {
                bodyNode.expandProgress = approach(bodyNode.expandProgress, bodyNode.expandTarget, delta * EXPAND_SPEED);
                if (Math.abs(bodyNode.expandProgress - bodyNode.expandTarget) > 0.001F) {
                    hasActiveAnimations = true;
                }
            }
        }
    }

    private float approach(final float value, final float target, final float step) {
        if (value < target) {
            return Math.min(target, value + step);
        }
        if (value > target) {
            return Math.max(target, value - step);
        }
        return value;
    }

    private void drawFrame(final GuiGraphics graphics, final int x, final int y, final int w, final int h) {
        graphics.fillGradient(x, y, x + w, y + h, 0xD00B1220, 0xE0080E18);
        graphics.fill(x, y, x + w, y + 1, 0xFF40E0FF);
        graphics.fill(x, y + h - 1, x + w, y + h, 0xFF40E0FF);
        graphics.fill(x, y, x + 1, y + h, 0xFF40E0FF);
        graphics.fill(x + w - 1, y, x + w, y + h, 0xFF40E0FF);
    }

    private void drawPanel(final GuiGraphics graphics, final int x, final int y, final int w, final int h) {
        graphics.fillGradient(x, y, x + w, y + h, 0xA3142338, 0xB0141D2E);
        graphics.fill(x, y, x + w, y + 1, 0xA049E6FF);
        graphics.fill(x, y, x + 1, y + h, 0xA049E6FF);
        graphics.fill(x + w - 1, y, x + w, y + h, 0x80224466);
        graphics.fill(x, y + h - 1, x + w, y + h, 0x80224466);
    }

    private void renderStarField(final GuiGraphics graphics) {
        long time = System.currentTimeMillis();
        for (float[] star : stars) {
            int x = (int) (star[0] * this.width);
            int y = (int) (star[1] * this.height);
            int blink = (int) ((Math.sin((time / 360.0D) + star[0] * 14.0D + star[1] * 9.0D) * 0.5D + 0.5D) * 110.0D);
            int alpha = 40 + blink;
            int size = 1 + (int) star[2];
            graphics.fill(x, y, x + size, y + size, (alpha << 24) | 0xD9F8FF);
        }
    }

    private void renderLeftList(final GuiGraphics graphics, final int mouseX, final int mouseY) {
        int left = guiLeft();
        int top = guiTop();

        int viewportX = left + LIST_VIEW_X;
        int viewportY = top + LIST_VIEW_Y;
        int viewportW = LIST_VIEW_W;
        int viewportH = LIST_VIEW_H;

        int footerX = left + LIST_FOOTER_X;
        int footerY = top + LIST_FOOTER_Y;

        drawPanel(graphics, footerX, footerY, LIST_FOOTER_W, LIST_FOOTER_H);
        graphics.drawString(this.font, fallback("iu.space.tp.close"), footerX + 42, footerY + 5, 0xFFFFFF, false);

        pushScissor(viewportX, viewportY, viewportX + viewportW, viewportY + viewportH);

        float y = viewportY - scrollY;
        for (SystemNode systemNode : systemNodes) {
            y += renderSystemBlock(graphics, systemNode, viewportX + 2, y, viewportW - 4, mouseX, mouseY);
        }

        popScissor();

        renderScrollbar(graphics, viewportX + viewportW - 4, viewportY, viewportH, getTotalContentHeight());

        graphics.fill(viewportX, viewportY, viewportX + viewportW, viewportY + 1, 0x884BE9FF);
        graphics.fill(viewportX, viewportY + viewportH - 1, viewportX + viewportW, viewportY + viewportH, 0x66224466);
        graphics.fill(viewportX, viewportY, viewportX + 1, viewportY + viewportH, 0x884BE9FF);
        graphics.fill(viewportX + viewportW - 1, viewportY, viewportX + viewportW, viewportY + viewportH, 0x66224466);
    }

    private float renderSystemBlock(final GuiGraphics graphics, final SystemNode systemNode, final int x, final float y, final int width, final int mouseX, final int mouseY) {
        float startY = y;
        renderSystemRow(graphics, systemNode, x, (int) y, width, mouseX, mouseY);
        float currentY = y + ROW_H;

        float naturalChildrenHeight = getSystemChildrenNaturalHeight(systemNode);
        float visibleChildrenHeight = naturalChildrenHeight * easeOut(systemNode.expandProgress);

        if (visibleChildrenHeight > 0.5F) {
            float childY = currentY;
            float remaining = visibleChildrenHeight;

            for (BodyNode bodyNode : systemNode.children) {
                float blockHeight = renderBodyBlockClipped(
                        graphics,
                        bodyNode,
                        x + INDENT_SYSTEM_CHILD,
                        childY,
                        width - INDENT_SYSTEM_CHILD,
                        mouseX,
                        mouseY,
                        systemNode.expandProgress,
                        remaining
                );
                childY += blockHeight;
                remaining -= blockHeight;
                if (remaining <= 0.01F) {
                    break;
                }
            }

            currentY += visibleChildrenHeight;
        }

        return currentY - startY;
    }

    private float renderBodyBlockClipped(final GuiGraphics graphics, final BodyNode bodyNode, final int x, final float y, final int width, final int mouseX, final int mouseY, final float parentAlpha, final float visibleHeightBudget) {
        if (visibleHeightBudget <= 0.01F) {
            return 0.0F;
        }

        float consumed = 0.0F;

        if (visibleHeightBudget >= 1.0F) {
            renderBodyRow(graphics, bodyNode, x, (int) y, width, mouseX, mouseY, parentAlpha);
        }
        consumed += Math.min(ROW_H, visibleHeightBudget);

        if (visibleHeightBudget <= ROW_H) {
            return consumed;
        }

        if (!bodyNode.satellites.isEmpty()) {
            float satellitesNaturalHeight = bodyNode.satellites.size() * ROW_H;
            float visibleSatHeight = satellitesNaturalHeight * easeOut(bodyNode.expandProgress);
            float allowedSatHeight = Math.min(visibleSatHeight, visibleHeightBudget - ROW_H);

            float childY = y + ROW_H;
            float remaining = allowedSatHeight;

            for (BodyNode satelliteNode : bodyNode.satellites) {
                if (remaining < 1.0F) {
                    break;
                }
                renderBodyRow(
                        graphics,
                        satelliteNode,
                        x + INDENT_PLANET_CHILD,
                        (int) childY,
                        width - INDENT_PLANET_CHILD,
                        mouseX,
                        mouseY,
                        parentAlpha * bodyNode.expandProgress
                );
                childY += ROW_H;
                remaining -= ROW_H;
            }

            consumed += allowedSatHeight;
        }

        return consumed;
    }

    private void renderSystemRow(final GuiGraphics graphics, final SystemNode systemNode, final int x, final int y, final int width, final int mouseX, final int mouseY) {
        boolean selected = selectedSystemNode == systemNode && selectedBodyNode == null;
        boolean hovered = isMouseOver(mouseX, mouseY, x, y, width, ROW_H);

        int fill = selected ? 0xCC255B8C : hovered ? 0xAA1B314E : 0x88203045;
        graphics.fillGradient(x, y, x + width, y + ROW_H - 1, fill, fill + 0x00080E12);
        graphics.fill(x, y, x + width, y + 1, selected ? 0xFF5CEBFF : 0x8857C6FF);

        renderGlowBehindIcon(graphics, x + 16, y + 13, 12, 0x604BE9FF);
        renderStarPreview3D(graphics, systemNode.star, x + 16, y + 13, 9.2F, 12.0F);

        String starName = fallback("iu.body." + systemNode.star.getName());
        String systemName = formatSystemName(systemNode.system.getName());

        drawScrollingLine(graphics, starName, x + 30, y + 4, 96, 0xFFFFFF, y * 13L);
        drawScrollingLine(graphics, systemName, x + 30, y + 14, 96, 0x8EDFFF, y * 13L + 77L);

        String rightText = systemNode.expandTarget > 0.5F
                ? fallback("iu.space.tp.collapse")
                : fallback("iu.space.tp.open_planets");
        drawScrollingLine(graphics, rightText, x + width - 66, y + 9, 58, 0x9EFAFF, y * 23L);

        drawExpandMarker(graphics, x + width - 14, y + 6, systemNode.expandProgress, 0xAAFFFFFF);
    }

    private void renderBodyRow(final GuiGraphics graphics, final BodyNode bodyNode, final int x, final int y, final int width, final int mouseX, final int mouseY, final float alphaMul) {
        boolean selected = selectedBodyNode == bodyNode;
        boolean hovered = isMouseOver(mouseX, mouseY, x, y, width, ROW_H);

        int baseFill = selected ? 0xCC2C6396 : hovered ? 0xAA223A59 : 0x88203045;
        int fill = multiplyAlpha(baseFill, alphaMul);
        graphics.fillGradient(x, y, x + width, y + ROW_H - 1, fill, fill + 0x00080E12);
        graphics.fill(x, y, x + width, y + 1, multiplyAlpha(selected ? 0xFF5CEBFF : 0x8857C6FF, alphaMul));

        int iconColor = switch (bodyNode.kind) {
            case PLANET -> 0x503FE9FF;
            case SATELLITE -> 0x50D8E4FF;
            case ASTEROID -> 0x50FFC97A;
        };
        renderGlowBehindIcon(graphics, x + 16, y + 13, 11, multiplyAlpha(iconColor, alphaMul));
        renderBodyPreview3D(graphics, bodyNode.body, x + 16, y + 13, bodyNode.kind == BodyKind.SATELLITE ? 8.0F : 8.8F, 10.0F);

        String name = fallback("iu.body." + bodyNode.body.getName());
        SpaceTeleportScreenData.Entry entry = bodyEntryLookup.get(bodyNode.body.getName());

        drawScrollingLine(graphics, name, x + 30, y + 4, 92, multiplyAlpha(0xFFFFFF, alphaMul), y * 31L);

        String subLine;
        int subColor;
        if (entry != null) {
            subLine = ModUtils.getString(entry.researchPercent) + "%";
            subColor = entry.researchPercent >= 100.0D ? 0x60FF60 : 0xFFD560;
        } else {
            subLine = fallback("iu.space.tp.no_data");
            subColor = 0xAAAAAA;
        }
        drawScrollingLine(graphics, subLine, x + 30, y + 14, 46, multiplyAlpha(subColor, alphaMul), y * 31L + 44L);

        String status = getStatusText(bodyNode);
        int statusColor = getStatusColor(bodyNode);
        drawScrollingLine(graphics, status, x + width - 76, y + 9, 64, multiplyAlpha(statusColor, alphaMul), y * 17L + 3L);
    }

    private void renderRightInfo(final GuiGraphics graphics, final int mouseX, final int mouseY) {
        int left = guiLeft();
        int top = guiTop();

        drawPanel(graphics, left + PREVIEW_BOX_X, top + PREVIEW_BOX_Y, PREVIEW_BOX_W, PREVIEW_BOX_H);
        drawPanel(graphics, left + TELEPORT_BTN_X, top + TELEPORT_BTN_Y, TELEPORT_BTN_W, TELEPORT_BTN_H);
        drawPanel(graphics, left + RETURN_BTN_X, top + RETURN_BTN_Y, RETURN_BTN_W, RETURN_BTN_H);

        Object selected = getSelectedObject();
        if (selected instanceof SystemNode systemNode) {
            renderGlowBehindIcon(graphics, left + PREVIEW_BOX_X + PREVIEW_BOX_W / 2, top + PREVIEW_BOX_Y + PREVIEW_BOX_H / 2, 30, 0x504FEAFF);
            renderStarPreview3D(graphics, systemNode.star, left + PREVIEW_BOX_X + PREVIEW_BOX_W / 2, top + PREVIEW_BOX_Y + PREVIEW_BOX_H / 2, 30.0F, 18.0F);
        } else if (selected instanceof BodyNode bodyNode) {
            renderGlowBehindIcon(graphics, left + PREVIEW_BOX_X + PREVIEW_BOX_W / 2, top + PREVIEW_BOX_Y + PREVIEW_BOX_H / 2, 26, 0x404FEAFF);
            renderBodyPreview3D(graphics, bodyNode.body, left + PREVIEW_BOX_X + PREVIEW_BOX_W / 2, top + PREVIEW_BOX_Y + PREVIEW_BOX_H / 2, resolvePreviewRadius(bodyNode.body), 18.0F);
        }

        List<InfoLine> infoLines = buildInfoLines();
        int lineY = top + INFO_Y;
        for (int i = 0; i < infoLines.size(); i++) {
            InfoLine line = infoLines.get(i);
            drawScrollingLine(graphics, line.text, left + INFO_X, lineY, INFO_W, line.color, 1000L + i * 91L);
            lineY += 16;
            if (lineY > top + INFO_Y + INFO_H) {
                break;
            }
        }

        SpaceTeleportClientState runtime = SpaceTeleportClientState.INSTANCE;

        double charge = runtime.isActive() ? runtime.getCharge() : data.currentCharge;
        double maxCharge = runtime.isActive() ? runtime.getMaxCharge() : data.maxCharge;
        float chargeRatio = maxCharge <= 0.0D ? 0.0F : (float) (charge / maxCharge);
        long ticksToCritical = runtime.isActive() ? runtime.getTicksUntilCritical() : estimateFieldTicks(charge);
        long countdown = runtime.isActive() ? runtime.getCountdownTicks() : data.returnCountdownTicks;
        boolean sessionActive = runtime.isActive() || data.sessionActive;
        SpaceTeleportPhase phase = runtime.isActive() ? runtime.getPhase() : data.phase;
        SpaceTeleportReason reason = runtime.isActive() ? runtime.getReason() : data.reason;
        boolean itemPresent = runtime.isActive() ? runtime.isItemPresent() : data.itemPresent;
        String activeBodyName = safe(runtime.getBodyName()).isEmpty() ? safe(data.activeBodyName) : runtime.getBodyName();

        graphics.drawString(this.font,
                fallback("iu.space.tp.charge") + ": " + ModUtils.getString(charge) + " EF",
                left + 252, top + 186, 0xEAF9FF, false);

        renderBar(graphics, left + 252, top + 202, 230, 10, chargeRatio, 0xFF48D8FF, 0xFF15374E);

        graphics.drawString(this.font,
                fallback("iu.space.tp.time_left") + ": " + formatTicks(ticksToCritical),
                left + 252, top + 220, 0xFFFFFF, false);

        if (sessionActive) {
            String activeBodyText = !activeBodyName.isEmpty()
                    ? fallback("iu.body." + activeBodyName)
                    : fallback("iu.space.tp.none");

            graphics.drawString(this.font,
                    fallback("iu.space.tp.active_on") + ": " + activeBodyText,
                    left + 252, top + 236, 0x8EDFFF, false);

            String phaseText = switch (phase) {
                case OUTBOUND_PREP -> fallback("iu.space.tp.phase.outbound");
                case ACTIVE -> fallback("iu.space.tp.phase.active");
                case RETURN_PREP -> fallback("iu.space.tp.phase.return");
                default -> fallback("iu.space.tp.phase.inactive");
            };

            graphics.drawString(this.font,
                    fallback("iu.space.tp.phase") + ": " + phaseText,
                    left + 252, top + 252, 0xFFFFFF, false);

            if (phase == SpaceTeleportPhase.RETURN_PREP) {
                graphics.drawString(this.font,
                        fallback("iu.space.tp.return_countdown") + ": " + formatTicks(countdown),
                        left + 252, top + 268, 0xFF9D9D, false);
            } else if (!itemPresent) {
                graphics.drawString(this.font,
                        fallback("iu.space.tp.warning_item_missing"),
                        left + 252, top + 268, 0xFF7070, false);
            } else if (reason == SpaceTeleportReason.LOW_CHARGE) {
                graphics.drawString(this.font,
                        fallback("iu.space.tp.warning_low_charge"),
                        left + 252, top + 268, 0xFF7070, false);
            } else {
                graphics.drawString(this.font,
                        fallback("iu.space.tp.safe_state"),
                        left + 252, top + 268, 0x76FF76, false);
            }
        }

        boolean canTeleport = canTeleportSelected();
        int tpColor = canTeleport ? 0xFFFFFF : 0xAAA0A0;
        graphics.drawString(this.font,
                fallback("iu.space.tp.teleport_button"),
                left + TELEPORT_BTN_X + 58, top + TELEPORT_BTN_Y + 8, tpColor, false);

        boolean canReturn = sessionActive;
        int rtColor = canReturn ? 0xFFFFFF : 0xAAA0A0;
        graphics.drawString(this.font,
                fallback("iu.space.tp.return_button"),
                left + RETURN_BTN_X + 34, top + RETURN_BTN_Y + 5, rtColor, false);

        if (isMouseOver(mouseX, mouseY, left + TELEPORT_BTN_X, top + TELEPORT_BTN_Y, TELEPORT_BTN_W, TELEPORT_BTN_H) && !canTeleport) {
            drawTooltip(graphics, mouseX, mouseY, fallback("iu.space.tp.need_body"));
        }
    }

    private List<InfoLine> buildInfoLines() {
        List<InfoLine> lines = new ArrayList<>();
        Object selected = getSelectedObject();

        if (selected instanceof SystemNode systemNode) {
            String systemName = formatSystemName(systemNode.system.getName());
            String starName = fallback("iu.body." + systemNode.star.getName());

            lines.add(new InfoLine(starName, 0xFFFFFF));
            lines.add(new InfoLine(fallback("iu.space.tp.system") + ": " + systemName, 0xA6EFFF));
            lines.add(new InfoLine(fallback("iu.space.tp.planets") + ": " + countPlanets(systemNode), 0x7CFF7C));
            lines.add(new InfoLine(fallback("iu.space.tp.satellites") + ": " + countSatellites(systemNode), 0x8EDFFF));
            lines.add(new InfoLine(fallback("iu.space.tp.asteroids") + ": " + countAsteroids(systemNode), 0xFFD57A));
            lines.add(new InfoLine(fallback("iu.space.tp.system_hint"), 0xD7E8FF));
            return lines;
        }

        if (selected instanceof BodyNode bodyNode) {
            IBody body = bodyNode.body;
            SpaceTeleportScreenData.Entry entry = bodyEntryLookup.get(body.getName());
            EnumLevels level = resolveLevel(body);
            boolean dimExists = entry != null && entry.dimensionExists;
            boolean research100 = entry != null && entry.researchPercent >= 100.0D;
            boolean bodyAccessible = isBodyAccessible(bodyNode);

            lines.add(new InfoLine(fallback("iu.body." + body.getName()), 0xFFFFFF));
            lines.add(new InfoLine(
                    fallback("iu.space.tp.research") + ": " + (entry == null ? "0%" : ModUtils.getString(entry.researchPercent) + "%"),
                    research100 ? 0x76FF76 : 0xFFD560
            ));
            lines.add(new InfoLine(
                    fallback("iu.space.tp.dimension") + ": " + (dimExists ? fallback("iu.space.tp.available") : fallback("iu.space.tp.unavailable")),
                    dimExists ? 0x76FF76 : 0xFF8C8C
            ));
            lines.add(new InfoLine(
                    fallback("iu.space.tp.access") + ": " + (bodyAccessible ? fallback("iu.space.tp.ready") : fallback("iu.space.tp.need_100")),
                    bodyAccessible ? 0x76FF76 : 0xFFD560
            ));
            lines.add(new InfoLine("Level: " + level.name(), level == EnumLevels.NONE ? 0xFF8C8C : 0x8EDFFF));

            if (body instanceof IPlanet planet) {
                lines.add(new InfoLine(fallback("iu.space.tp.temperature") + ": " + planet.getTemperature() + "C°", 0xFFFFFF));
                lines.add(new InfoLine(fallback("iu.space.tp.oxygen") + ": " + yesNo(planet.hasOxygen()), planet.hasOxygen() ? 0x76FF76 : 0xFFB0B0));
                if (!planet.getSatelliteList().isEmpty()) {
                    lines.add(new InfoLine(fallback("iu.space.tp.moons_hint"), 0xBEEAFF));
                }
            } else if (body instanceof ISatellite satellite) {
                lines.add(new InfoLine(fallback("iu.space.tp.temperature") + ": " + satellite.getTemperature() + "C°", 0xFFFFFF));
                lines.add(new InfoLine(fallback("iu.space.tp.oxygen") + ": " + yesNo(satellite.hasOxygen()), satellite.hasOxygen() ? 0x76FF76 : 0xFFB0B0));
                lines.add(new InfoLine(fallback("iu.space.tp.parent_planet") + ": " + fallback("iu.body." + satellite.getPlanet().getName()), 0x8EDFFF));
            } else if (body instanceof IAsteroid asteroid) {
                lines.add(new InfoLine(fallback("iu.space.tp.temperature") + ": " + asteroid.getTemperature() + "C°", 0xFFFFFF));
                lines.add(new InfoLine(fallback("iu.space.tp.asteroid_info"), 0xDDEAFF));
            }

            double maxCharge = data.maxCharge > 0.0D ? data.maxCharge : SpaceTeleportEnergyLogic.DEFAULT_MAX_CHARGE;
            double maxStaySeconds = SpaceTeleportEnergyLogic.getDesiredStaySeconds(body);
            double drainPerSecond = SpaceTeleportEnergyLogic.getActiveDrainPerSecond(body, maxCharge, SpaceTeleportController.CRITICAL_CHARGE);
            double teleportCost = SpaceTeleportEnergyLogic.getTeleportCost(body, SpaceTeleportController.TELEPORT_START_COST);

            lines.add(new InfoLine(fallback("iu.space.tp.max_stay") + ": " + formatTicks((long) (maxStaySeconds * 20.0D)), 0xFFD560));
            lines.add(new InfoLine(fallback("iu.space.tp.energy_drain") + ": " + ModUtils.getString(drainPerSecond) + " EF/s", 0xEAF9FF));
            lines.add(new InfoLine(fallback("iu.space.tp.teleport_cost") + ": " + ModUtils.getString(teleportCost) + " EF", 0x8EDFFF));

            return lines;
        }

        lines.add(new InfoLine(fallback("iu.space.tp.none"), 0xFFFFFF));
        return lines;
    }

    private boolean canTeleportSelected() {
        if (selectedBodyNode == null) {
            return false;
        }
        return isBodyAccessible(selectedBodyNode);
    }

    private boolean isBodyAccessible(final BodyNode node) {
        if (node == null) {
            return false;
        }
        SpaceTeleportScreenData.Entry entry = bodyEntryLookup.get(node.body.getName());
        if (entry == null) {
            return false;
        }
        EnumLevels level = resolveLevel(node.body);
        return entry.dimensionExists && entry.researchPercent >= 100.0D && level != EnumLevels.NONE;
    }

    private Object getSelectedObject() {
        return selectedBodyNode != null ? selectedBodyNode : selectedSystemNode;
    }

    @Override
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double deltaX, final double deltaY) {
        int left = guiLeft();
        int top = guiTop();

        int vx = left + LIST_VIEW_X;
        int vy = top + LIST_VIEW_Y;
        if (mouseX >= vx && mouseX <= vx + LIST_VIEW_W && mouseY >= vy && mouseY <= vy + LIST_VIEW_H) {
            float maxScroll = Math.max(0.0F, getTotalContentHeight() - LIST_VIEW_H);
            scrollY = Mth.clamp(scrollY - (float) deltaY * 18.0F, 0.0F, maxScroll);
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        int left = guiLeft();
        int top = guiTop();

        if (isMouseOver(mouseX, mouseY, left + LIST_FOOTER_X, top + LIST_FOOTER_Y, LIST_FOOTER_W, LIST_FOOTER_H)) {
            onClose();
            return true;
        }

        if (isMouseOver(mouseX, mouseY, left + TELEPORT_BTN_X, top + TELEPORT_BTN_Y, TELEPORT_BTN_W, TELEPORT_BTN_H)) {
            if (canTeleportSelected()) {
                new PacketPlanetaryTeleportRequest(selectedBodyNode.body.getName(), Minecraft.getInstance().level);
                onClose();
            }
            return true;
        }

        if (isMouseOver(mouseX, mouseY, left + RETURN_BTN_X, top + RETURN_BTN_Y, RETURN_BTN_W, RETURN_BTN_H)) {
            SpaceTeleportClientState runtime = SpaceTeleportClientState.INSTANCE;
            if (runtime.isActive() || data.sessionActive) {
                new PacketPlanetaryReturnRequest(true, Minecraft.getInstance().level);
                onClose();
            }
            return true;
        }

        if (hasActiveAnimations) {
            return true;
        }

        HitResult hit = hitTestList((int) mouseX, (int) mouseY);
        if (hit != null) {
            if (hit.systemNode != null && hit.bodyNode == null) {
                onSystemClicked(hit.systemNode);
                return true;
            }
            if (hit.bodyNode != null) {
                onBodyClicked(hit.bodyNode);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void onSystemClicked(final SystemNode clicked) {
        selectedSystemNode = clicked;
        selectedBodyNode = null;

        boolean open = clicked.expandTarget < 0.5F;
        for (SystemNode systemNode : systemNodes) {
            systemNode.expandTarget = systemNode == clicked && open ? 1.0F : 0.0F;
            if (systemNode != clicked) {
                for (BodyNode bodyNode : systemNode.children) {
                    bodyNode.expandTarget = 0.0F;
                }
            }
        }

        if (!open) {
            for (BodyNode bodyNode : clicked.children) {
                bodyNode.expandTarget = 0.0F;
            }
        }

        clampScroll();
    }

    private void onBodyClicked(final BodyNode clicked) {
        selectedSystemNode = clicked.parentSystem;
        selectedBodyNode = clicked;

        if (clicked.kind == BodyKind.PLANET && !clicked.satellites.isEmpty()) {
            boolean open = clicked.expandTarget < 0.5F;
            for (BodyNode sibling : clicked.parentSystem.children) {
                if (sibling.kind == BodyKind.PLANET && sibling != clicked) {
                    sibling.expandTarget = 0.0F;
                }
            }
            clicked.expandTarget = open ? 1.0F : 0.0F;
        }

        clampScroll();
    }

    private HitResult hitTestList(final int mouseX, final int mouseY) {
        int left = guiLeft();
        int top = guiTop();

        int viewportX = left + LIST_VIEW_X;
        int viewportY = top + LIST_VIEW_Y;
        int viewportW = LIST_VIEW_W;
        int viewportH = LIST_VIEW_H;

        if (!isMouseOver(mouseX, mouseY, viewportX, viewportY, viewportW, viewportH)) {
            return null;
        }

        float y = viewportY - scrollY;

        for (SystemNode systemNode : systemNodes) {
            if (isMouseOverClipped(mouseX, mouseY, viewportX + 2, (int) y, viewportW - 4, ROW_H, viewportX, viewportY, viewportW, viewportH)) {
                return new HitResult(systemNode, null);
            }

            y += ROW_H;

            float visibleChildrenHeight = getSystemChildrenNaturalHeight(systemNode) * easeOut(systemNode.expandProgress);
            float remainingSystemHeight = visibleChildrenHeight;

            for (BodyNode bodyNode : systemNode.children) {
                if (remainingSystemHeight <= 0.01F) {
                    break;
                }

                BodyHitTestResult result = hitTestBodyBlock(
                        mouseX,
                        mouseY,
                        systemNode,
                        bodyNode,
                        viewportX + 2 + INDENT_SYSTEM_CHILD,
                        y,
                        viewportW - 4 - INDENT_SYSTEM_CHILD,
                        viewportX,
                        viewportY,
                        viewportW,
                        viewportH,
                        remainingSystemHeight
                );

                if (result.hit != null) {
                    return result.hit;
                }

                y += result.consumedHeight;
                remainingSystemHeight -= result.consumedHeight;
            }
        }

        return null;
    }

    private BodyHitTestResult hitTestBodyBlock(final int mouseX, final int mouseY, final SystemNode systemNode, final BodyNode bodyNode, final int x, final float y, final int width, final int viewportX, final int viewportY, final int viewportW, final int viewportH, final float visibleHeightBudget) {
        if (visibleHeightBudget <= 0.01F) {
            return new BodyHitTestResult(null, 0.0F);
        }

        float consumed = 0.0F;

        int bodyVisibleHeight = Math.min(ROW_H, (int) Math.ceil(visibleHeightBudget));
        if (bodyVisibleHeight >= 1) {
            if (isMouseOverClipped(mouseX, mouseY, x, (int) y, width, bodyVisibleHeight, viewportX, viewportY, viewportW, viewportH)) {
                return new BodyHitTestResult(new HitResult(systemNode, bodyNode), Math.min(ROW_H, visibleHeightBudget));
            }
        }

        consumed += Math.min(ROW_H, visibleHeightBudget);
        if (visibleHeightBudget <= ROW_H) {
            return new BodyHitTestResult(null, consumed);
        }

        if (!bodyNode.satellites.isEmpty()) {
            float satellitesNaturalHeight = bodyNode.satellites.size() * ROW_H * easeOut(bodyNode.expandProgress);
            float allowedSatHeight = Math.min(satellitesNaturalHeight, visibleHeightBudget - ROW_H);

            float childY = y + ROW_H;
            float remainingSatHeight = allowedSatHeight;

            for (BodyNode satelliteNode : bodyNode.satellites) {
                if (remainingSatHeight <= 0.01F) {
                    break;
                }

                int satVisibleHeight = Math.min(ROW_H, (int) Math.ceil(remainingSatHeight));
                if (satVisibleHeight >= 1) {
                    if (isMouseOverClipped(mouseX, mouseY, x + INDENT_PLANET_CHILD, (int) childY, width - INDENT_PLANET_CHILD, satVisibleHeight, viewportX, viewportY, viewportW, viewportH)) {
                        return new BodyHitTestResult(new HitResult(systemNode, satelliteNode), consumed + Math.min(ROW_H, remainingSatHeight));
                    }
                }

                childY += ROW_H;
                remainingSatHeight -= ROW_H;
            }

            consumed += allowedSatHeight;
        }

        return new BodyHitTestResult(null, consumed);
    }

    private boolean isMouseOverClipped(final int mouseX, final int mouseY, final int x, final int y, final int w, final int h, final int clipX, final int clipY, final int clipW, final int clipH) {
        int ix1 = Math.max(x, clipX);
        int iy1 = Math.max(y, clipY);
        int ix2 = Math.min(x + w, clipX + clipW);
        int iy2 = Math.min(y + h, clipY + clipH);

        if (ix2 <= ix1 || iy2 <= iy1) {
            return false;
        }

        return mouseX >= ix1 && mouseX <= ix2 && mouseY >= iy1 && mouseY <= iy2;
    }

    private void renderScrollbar(final GuiGraphics graphics, final int x, final int y, final int h, final float contentHeight) {
        if (contentHeight <= h + 1.0F) {
            return;
        }

        graphics.fill(x, y, x + 3, y + h, 0x55253F59);

        float ratio = h / contentHeight;
        int thumbH = Math.max(18, (int) (h * ratio));
        float maxScroll = Math.max(1.0F, contentHeight - h);
        int thumbY = y + (int) ((scrollY / maxScroll) * (h - thumbH));

        graphics.fill(x, thumbY, x + 3, thumbY + thumbH, 0xFF4BE9FF);
    }

    private float getTotalContentHeight() {
        float total = 0.0F;
        for (SystemNode systemNode : systemNodes) {
            total += ROW_H;
            total += getSystemChildrenNaturalHeight(systemNode) * easeOut(systemNode.expandProgress);
        }
        return total;
    }

    private float getSystemChildrenNaturalHeight(final SystemNode systemNode) {
        float total = 0.0F;
        for (BodyNode bodyNode : systemNode.children) {
            total += ROW_H;
            if (!bodyNode.satellites.isEmpty()) {
                total += bodyNode.satellites.size() * ROW_H * easeOut(bodyNode.expandProgress);
            }
        }
        return total;
    }

    private void clampScroll() {
        float maxScroll = Math.max(0.0F, getTotalContentHeight() - LIST_VIEW_H);
        scrollY = Mth.clamp(scrollY, 0.0F, maxScroll);
    }

    private boolean isMouseOver(final double mouseX, final double mouseY, final int x, final int y, final int w, final int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    private void drawTooltip(final GuiGraphics graphics, final int mouseX, final int mouseY, final String text) {
        graphics.renderTooltip(this.font, List.of(Component.literal(text)), Optional.empty(), mouseX, mouseY);
    }

    private void drawExpandMarker(final GuiGraphics graphics, final int x, final int y, final float progress, final int color) {
        int midX = x + 4;
        int midY = y + 4;
        graphics.fill(midX - 3, midY - 1, midX + 3, midY + 1, color);
        int alpha = (int) ((1.0F - progress) * ((color >> 24) & 255));
        int vColor = (alpha << 24) | (color & 0x00FFFFFF);
        graphics.fill(midX - 1, midY - 3, midX + 1, midY + 3, vColor);
    }

    private void drawScrollingLine(final GuiGraphics graphics, final String text, final int x, final int y, final int maxWidth, final int color, final long seed) {
        if (text == null) {
            return;
        }

        int width = this.font.width(text);
        if (width <= maxWidth) {
            graphics.drawString(this.font, text, x, y, color, false);
            return;
        }

        long time = System.currentTimeMillis() + seed * 17L;
        float hold = 800.0F;
        float speed = 28.0F;
        float overflow = width - maxWidth;
        float travel = overflow + 14.0F;
        float loop = hold + travel * speed + hold;
        float t = time % (long) loop;

        float offset;
        if (t < hold) {
            offset = 0.0F;
        } else if (t < hold + travel * speed) {
            offset = (t - hold) / speed;
        } else {
            offset = travel;
        }

        pushScissor(x, y, x + maxWidth, y + 10);
        graphics.drawString(this.font, text, x - (int) offset, y, color, false);
        popScissor();
    }

    private void pushScissor(final int x1, final int y1, final int x2, final int y2) {
        ScissorRect next = new ScissorRect(x1, y1, x2, y2);

        if (!scissorStack.isEmpty()) {
            ScissorRect prev = scissorStack.peekLast();
            next = new ScissorRect(Math.max(prev.x1, next.x1), Math.max(prev.y1, next.y1), Math.min(prev.x2, next.x2), Math.min(prev.y2, next.y2));
        }

        if (next.x2 < next.x1) {
            next = new ScissorRect(next.x1, next.y1, next.x1, next.y2);
        }
        if (next.y2 < next.y1) {
            next = new ScissorRect(next.x1, next.y1, next.x2, next.y1);
        }

        scissorStack.addLast(next);
        applyCurrentScissor();
    }

    private void popScissor() {
        if (!scissorStack.isEmpty()) {
            scissorStack.removeLast();
        }
        applyCurrentScissor();
    }

    private void applyCurrentScissor() {
        if (scissorStack.isEmpty()) {
            RenderSystem.disableScissor();
            return;
        }

        ScissorRect rect = scissorStack.peekLast();
        Window window = Minecraft.getInstance().getWindow();
        double scale = window.getGuiScale();
        int screenHeight = window.getHeight();

        int sx = (int) (rect.x1 * scale);
        int sy = (int) (screenHeight - rect.y2 * scale);
        int sw = Math.max(0, (int) ((rect.x2 - rect.x1) * scale));
        int sh = Math.max(0, (int) ((rect.y2 - rect.y1) * scale));

        RenderSystem.enableScissor(sx, sy, sw, sh);
    }

    private void renderBar(final GuiGraphics graphics, final int x, final int y, final int w, final int h, final float progress, final int fill, final int back) {
        graphics.fill(x, y, x + w, y + h, back);
        int fw = Math.max(0, Math.min(w, (int) (w * Mth.clamp(progress, 0.0F, 1.0F))));
        graphics.fill(x, y, x + fw, y + h, fill);
        graphics.fill(x, y, x + w, y + 1, 0xFFFFFFFF);
    }

    private float resolvePreviewRadius(final IBody body) {
        if (body instanceof ISatellite) {
            return 24.0F;
        }
        if (body instanceof IAsteroid) {
            return 22.0F;
        }
        return 28.0F;
    }

    private void renderGlowBehindIcon(final GuiGraphics graphics, final int cx, final int cy, final int radius, final int color) {
        int a = (color >> 24) & 255;
        int rgb = color & 0x00FFFFFF;

        int a1 = Math.max(0, a / 5);
        int a2 = Math.max(0, a / 3);
        int a3 = Math.max(0, a / 2);

        graphics.fill(cx - radius, cy - radius, cx + radius, cy + radius, (a1 << 24) | rgb);
        graphics.fill(cx - radius / 2, cy - radius / 2, cx + radius / 2, cy + radius / 2, (a2 << 24) | rgb);
        graphics.fill(cx - radius / 3, cy - radius / 3, cx + radius / 3, cy + radius / 3, (a3 << 24) | rgb);
    }

    private void renderStarPreview3D(final GuiGraphics graphics, final IStar star, final float x, final float y, final float radius, final float z) {
        float gameTime = Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.getGameTime() + renderPartialTick
                : 0.0F;

        float yaw = (float) star.getRotation((long) gameTime);
        float roll = star.getRotationAngle();
        renderTexturedCube(graphics, star.getLocation(), x, y, radius, z, yaw, 18.0F, roll);
    }

    private void renderBodyPreview3D(final GuiGraphics graphics, final IBody body, final float x, final float y, final float radius, final float z) {
        float gameTime = Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.getGameTime() + renderPartialTick
                : 0.0F;

        float yaw = gameTime * 0.8F;
        float roll = body.getRotationAngle();
        renderTexturedCube(graphics, body.getLocation(), x, y, radius, z, yaw, 16.0F, roll);
    }

    private void renderTexturedCube(final GuiGraphics graphics, final ResourceLocation texture, final float x, final float y, final float radius, final float z, final float yaw, final float pitch, final float roll) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        renderCube(poseStack, radius);

        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    private void renderCube(final PoseStack poseStack, final float radius) {
        float half = radius / 2.0F;
        Matrix4f matrix = poseStack.last().pose();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        builder.addVertex(matrix, -half, -half, -half).setUv(0.0F, 0.0F);
        builder.addVertex(matrix, half, -half, -half).setUv(1.0F, 0.0F);
        builder.addVertex(matrix, half, half, -half).setUv(1.0F, 1.0F);
        builder.addVertex(matrix, -half, half, -half).setUv(0.0F, 1.0F);

        builder.addVertex(matrix, -half, -half, half).setUv(0.0F, 0.0F);
        builder.addVertex(matrix, half, -half, half).setUv(1.0F, 0.0F);
        builder.addVertex(matrix, half, half, half).setUv(1.0F, 1.0F);
        builder.addVertex(matrix, -half, half, half).setUv(0.0F, 1.0F);

        builder.addVertex(matrix, -half, -half, half).setUv(0.0F, 0.0F);
        builder.addVertex(matrix, -half, -half, -half).setUv(1.0F, 0.0F);
        builder.addVertex(matrix, -half, half, -half).setUv(1.0F, 1.0F);
        builder.addVertex(matrix, -half, half, half).setUv(0.0F, 1.0F);

        builder.addVertex(matrix, half, -half, half).setUv(0.0F, 0.0F);
        builder.addVertex(matrix, half, -half, -half).setUv(1.0F, 0.0F);
        builder.addVertex(matrix, half, half, -half).setUv(1.0F, 1.0F);
        builder.addVertex(matrix, half, half, half).setUv(0.0F, 1.0F);

        builder.addVertex(matrix, -half, half, -half).setUv(0.0F, 0.0F);
        builder.addVertex(matrix, half, half, -half).setUv(1.0F, 0.0F);
        builder.addVertex(matrix, half, half, half).setUv(1.0F, 1.0F);
        builder.addVertex(matrix, -half, half, half).setUv(0.0F, 1.0F);

        builder.addVertex(matrix, -half, -half, -half).setUv(0.0F, 0.0F);
        builder.addVertex(matrix, half, -half, -half).setUv(1.0F, 0.0F);
        builder.addVertex(matrix, half, -half, half).setUv(1.0F, 1.0F);
        builder.addVertex(matrix, -half, -half, half).setUv(0.0F, 1.0F);

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }

    private int countPlanets(final SystemNode systemNode) {
        int count = 0;
        for (BodyNode child : systemNode.children) {
            if (child.kind == BodyKind.PLANET) {
                count++;
            }
        }
        return count;
    }

    private int countAsteroids(final SystemNode systemNode) {
        int count = 0;
        for (BodyNode child : systemNode.children) {
            if (child.kind == BodyKind.ASTEROID) {
                count++;
            }
        }
        return count;
    }

    private int countSatellites(final SystemNode systemNode) {
        int count = 0;
        for (BodyNode child : systemNode.children) {
            count += child.satellites.size();
        }
        return count;
    }

    private EnumLevels resolveLevel(final IBody body) {
        if (body instanceof IPlanet planet) {
            return planet.getLevels();
        }
        if (body instanceof ISatellite satellite) {
            return satellite.getLevels();
        }
        if (body instanceof IAsteroid asteroid) {
            return asteroid.getLevels();
        }
        return EnumLevels.NONE;
    }

    private String formatSystemName(final String systemName) {
        if (systemName == null || systemName.isEmpty()) {
            return fallback("iu.space.tp.unknown");
        }
        String name = systemName.replace("system", "").replace("System", "").trim();
        if (name.isEmpty()) {
            name = systemName;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase(Locale.ROOT);
    }

    private String getStatusText(final BodyNode bodyNode) {
        if (bodyNode == null) {
            return fallback("iu.space.tp.none");
        }
        SpaceTeleportScreenData.Entry entry = bodyEntryLookup.get(bodyNode.body.getName());
        if (entry == null) {
            return fallback("iu.space.tp.no_data");
        }
        if (isBodyAccessible(bodyNode)) {
            return fallback("iu.space.tp.ready");
        }
        if (!entry.dimensionExists) {
            return fallback("iu.space.tp.no_dimension");
        }
        if (resolveLevel(bodyNode.body) == EnumLevels.NONE) {
            return fallback("iu.space.tp.blocked");
        }
        return fallback("iu.space.tp.locked");
    }

    private int getStatusColor(final BodyNode bodyNode) {
        if (bodyNode == null) {
            return 0xAAAAAA;
        }
        SpaceTeleportScreenData.Entry entry = bodyEntryLookup.get(bodyNode.body.getName());
        if (entry == null) {
            return 0xAAAAAA;
        }
        if (isBodyAccessible(bodyNode)) {
            return 0x72FF72;
        }
        if (!entry.dimensionExists || resolveLevel(bodyNode.body) == EnumLevels.NONE) {
            return 0xFF8C8C;
        }
        return 0xFFD560;
    }

    private long estimateFieldTicks(final double charge) {
        IBody body = resolveEstimatedBody();
        double maxCharge = data.maxCharge > 0.0D ? data.maxCharge : SpaceTeleportEnergyLogic.DEFAULT_MAX_CHARGE;

        return SpaceTeleportEnergyLogic.estimateRemainingTicks(charge, body, maxCharge, SpaceTeleportController.CRITICAL_CHARGE);
    }

    private String formatTicks(final long ticks) {
        long totalSeconds = Math.max(0L, ticks / 20L);
        long hours = totalSeconds / 3600L;
        long minutes = (totalSeconds % 3600L) / 60L;
        long seconds = totalSeconds % 60L;

        if (hours > 0L) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }

    private String yesNo(final boolean flag) {
        return flag ? fallback("iu.space_yes") : fallback("iu.space_no");
    }

    private String safe(final String value) {
        return value == null ? "" : value;
    }

    private float easeOut(final float t) {
        float clamped = Mth.clamp(t, 0.0F, 1.0F);
        return 1.0F - (float) Math.pow(1.0F - clamped, 3.0D);
    }

    private int multiplyAlpha(final int color, final float alphaMul) {
        int a = (color >> 24) & 255;
        int na = Mth.clamp((int) (a * alphaMul), 0, 255);
        return (na << 24) | (color & 0x00FFFFFF);
    }

    private enum BodyKind {
        PLANET,
        SATELLITE,
        ASTEROID
    }

    private static final class SystemNode {
        private final ISystem system;
        private final IStar star;
        private final List<BodyNode> children = new ArrayList<>();
        private float expandProgress = 0.0F;
        private float expandTarget = 0.0F;

        private SystemNode(final ISystem system, final IStar star) {
            this.system = system;
            this.star = star;
        }
    }

    private static final class BodyNode {
        private final BodyKind kind;
        private final IBody body;
        private final SystemNode parentSystem;
        private final BodyNode parentPlanet;
        private final List<BodyNode> satellites = new ArrayList<>();
        private float expandProgress = 0.0F;
        private float expandTarget = 0.0F;

        private BodyNode(final BodyKind kind, final IBody body, final SystemNode parentSystem, final BodyNode parentPlanet) {
            this.kind = kind;
            this.body = body;
            this.parentSystem = parentSystem;
            this.parentPlanet = parentPlanet;
        }
    }

    private static final class HitResult {
        private final SystemNode systemNode;
        private final BodyNode bodyNode;

        private HitResult(final SystemNode systemNode, final BodyNode bodyNode) {
            this.systemNode = systemNode;
            this.bodyNode = bodyNode;
        }
    }

    private static final class ScissorRect {
        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        private ScissorRect(final int x1, final int y1, final int x2, final int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    private static final class BodyHitTestResult {
        private final HitResult hit;
        private final float consumedHeight;

        private BodyHitTestResult(final HitResult hit, final float consumedHeight) {
            this.hit = hit;
            this.consumedHeight = consumedHeight;
        }
    }

    private static final class InfoLine {
        private final String text;
        private final int color;

        private InfoLine(final String text, final int color) {
            this.text = text;
            this.color = color;
        }
    }
}
