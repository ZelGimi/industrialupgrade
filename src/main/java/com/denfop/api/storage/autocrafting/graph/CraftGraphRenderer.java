package com.denfop.api.storage.autocrafting.graph;

import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.denfop.api.widget.ScreenWidget.bindBlockTexture;
import static com.denfop.api.widget.ScreenWidget.getBlockTextureMap;

public class CraftGraphRenderer {
    private static final String I18N_GRAPH = "iu.autocraft.graph";

    private final Minecraft mc;
    private final Font font;
    private final ScreenIndustrialUpgrade<?> gui;

    public CraftGraphRenderer(Minecraft mc, ScreenIndustrialUpgrade<?> gui) {
        this.mc = mc;
        this.font = mc.font;
        this.gui = gui;
    }

    public void render(GuiGraphics graphics, CraftGraph graph, int panelX, int panelY, int panelWidth, int panelHeight, double cameraX, double cameraY, double zoom, int mouseX, int mouseY, Integer hoveredNodeId, Integer selectedNodeId, Set<Integer> highlightedNodes) {
        if (graph == null) {
            return;
        }
        renderEdges(graphics, graph, panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom, highlightedNodes);
        renderNodes(graphics, graph, panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom, hoveredNodeId, selectedNodeId, highlightedNodes);
    }

    private void renderEdges(GuiGraphics graphics, CraftGraph graph, int panelX, int panelY, int panelWidth, int panelHeight, double cameraX, double cameraY, double zoom, Set<Integer> highlightedNodes) {
        if (zoom < 0.18D) {
            return;
        }
        for (CraftEdge edge : graph.getEdges()) {
            CraftNode from = graph.getNode(edge.getFromNodeId());
            CraftNode to = graph.getNode(edge.getToNodeId());
            if (from == null || to == null) {
                continue;
            }
            ScreenPoint a = worldToScreen(from.getX(), from.getY() + 8.0f, panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom);
            ScreenPoint b = worldToScreen(to.getX(), to.getY() - 8.0f, panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom);
            if (!lineVisible(a.x, a.y, b.x, b.y, panelX, panelY, panelWidth, panelHeight)) {
                continue;
            }
            boolean highlight = highlightedNodes != null && highlightedNodes.contains(from.getId()) && highlightedNodes.contains(to.getId());
            if (highlightedNodes != null && !highlightedNodes.isEmpty() && !highlight) {
                continue;
            }
            int color = highlight ? 0xFF80D8FF : 0xFF5E6A73;
            if (zoom < 0.32D) {
                color = highlight ? 0xCC80D8FF : 0x885E6A73;
            }
            drawFastEdge(graphics, a.x, a.y, b.x, b.y, color, zoom);
        }
    }

    private void renderNodes(GuiGraphics graphics, CraftGraph graph, int panelX, int panelY, int panelWidth, int panelHeight, double cameraX, double cameraY, double zoom, Integer hoveredNodeId, Integer selectedNodeId, Set<Integer> highlightedNodes) {
        for (CraftNode node : graph.getNodes()) {
            Rect rect = getNodeScreenRect(node, panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom);
            if (!rect.intersects(panelX, panelY, panelWidth, panelHeight)) {
                continue;
            }
            boolean hovered = hoveredNodeId != null && hoveredNodeId == node.getId();
            boolean selected = selectedNodeId != null && selectedNodeId == node.getId();
            boolean highlighted = highlightedNodes != null && highlightedNodes.contains(node.getId());
            renderNode(graphics, node, rect, zoom, hovered, selected, highlighted);
        }
    }

    private void renderNode(GuiGraphics graphics, CraftNode node, Rect rect, double zoom, boolean hovered, boolean selected, boolean highlighted) {
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;
        if (width <= 2 || height <= 2) {
            return;
        }
        int iconSize = Math.max(4, (int) Math.round(16 * zoom));
        int iconX = rect.left + Math.max(0, (width - iconSize) / 2);
        int iconY = rect.top + Math.max(0, (height - iconSize) / 2);
        if (highlighted || selected || hovered) {
            int outlineColor;
            if (selected) {
                outlineColor = 0xFFFFFFFF;
            } else if (highlighted) {
                outlineColor = 0xFF00E5FF;
            } else {
                outlineColor = 0xFFB0BEC5;
            }
            int pad = Math.max(1, (int) Math.round(1 * zoom));
            graphics.fill(rect.left - pad, rect.top - pad, rect.right + pad, rect.bottom + pad, 0x44000000);
            fillBorder(graphics, rect.left - pad, rect.top - pad, rect.right + pad, rect.bottom + pad, outlineColor, 1);
        }
        renderScaledStack(graphics, node.getStack(), node.getRequiredAmount() + node.getAvailableAmount(), iconX, iconY, zoom);
    }

    private void renderScaledStack(GuiGraphics graphics, SameStack sameStack, int count, int x, int y, double zoom) {
        if (sameStack == null || sameStack.isEmpty()) {
            return;
        }
        float itemScale = (float) Mth.clamp(zoom, 0.22D, 1.0D);
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0.0F);
        graphics.pose().scale(itemScale, itemScale, 1.0F);
        if (sameStack.isItem()) {
            ItemStack renderStack = sameStack.getStack().copy();
            renderStack.setCount(Math.max(1, sameStack.getAmount()));
            graphics.renderItem(renderStack, 0, 0);
        } else if (sameStack.isFluid()) {
            FluidStack fluidStack = sameStack.getFluidStack();
            Fluid fluid = fluidStack.getFluid();
            IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
            if (extensions.getStillTexture(fluidStack) != null) {
                TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fluidStack));
                int color = extensions.getTintColor(fluidStack);
                bindBlockTexture();
                this.gui.drawSprite(graphics, 0, 0, 16, 16, sprite, color, 1.0, false, false);
            }
        }
        if (zoom >= 0.42D && count > 1) {
            String s = ModUtils.getString(count);
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 200.0F);
            graphics.pose().scale(1.0f / itemScale, 1.0f / itemScale, 1.0F);
            float textScale = Math.max(0.38f, itemScale * 0.48f);
            float textWidth = font.width(s) * textScale;
            float iconScreenSize = 16.0f * itemScale;
            float textX = iconScreenSize - textWidth;
            float textY = iconScreenSize - 7.0f * textScale;
            graphics.pose().translate(textX, textY, 0.0F);
            graphics.pose().scale(textScale, textScale, 1.0F);
            graphics.drawString(font, s, 0, 0, 0xFFFFFF, true);
            graphics.pose().popPose();
        }
        graphics.pose().popPose();
    }

    private void drawScaledText(GuiGraphics graphics, String text, int x, int y, float scale, int color) {
        if (text == null || text.isEmpty()) {
            return;
        }
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1.0f);
        graphics.drawString(font, text, 0, 0, color, false);
        graphics.pose().popPose();
    }

    public Integer findHoveredNode(CraftGraph graph, int panelX, int panelY, int panelWidth, int panelHeight, double cameraX, double cameraY, double zoom, double mouseX, double mouseY) {
        if (graph == null) {
            return null;
        }
        for (CraftNode node : graph.getNodes()) {
            Rect rect = getNodeScreenRect(node, panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom);
            if (!rect.intersects(panelX, panelY, panelWidth, panelHeight)) {
                continue;
            }
            if (rect.contains((int) mouseX, (int) mouseY)) {
                return node.getId();
            }
        }
        return null;
    }

    public List<Component> buildTooltip(CraftNode node) {
        List<Component> lines = new ArrayList<>();
        if (node == null) {
            return lines;
        }

        lines.add(Component.literal(CraftGraphBuilder.getNodeTitle(node.getStack())));
        lines.add(Component.translatable(I18N_GRAPH + ".tooltip.required", node.getRequiredAmount()));

        if (node.getType() == CraftNodeType.CRAFTABLE || node.getType() == CraftNodeType.TARGET) {
            lines.add(Component.translatable(I18N_GRAPH + ".tooltip.craft_amount", node.getCraftAmount()));
        }

        lines.add(Component.translatable(I18N_GRAPH + ".tooltip.type", getNodeTypeComponent(node.getType())));

        if (node.getCraftTime() > 0) {
            lines.add(Component.translatable(I18N_GRAPH + ".tooltip.craft_time", node.getCraftTime()));
        }

        return lines;
    }

    private Component getNodeTypeComponent(CraftNodeType type) {
        return Component.translatable(I18N_GRAPH + ".node_type." + type.name().toLowerCase(Locale.ROOT));
    }

    private Rect getNodeScreenRect(CraftNode node, int panelX, int panelY, int panelWidth, int panelHeight, double cameraX, double cameraY, double zoom) {
        ScreenPoint center = worldToScreen(node.getX(), node.getY(), panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom);
        int size = Math.max(6, (int) Math.round(16 * zoom));
        return new Rect(center.x - size / 2, center.y - size / 2, center.x + size / 2, center.y + size / 2);
    }

    private ScreenPoint worldToScreen(double worldX, double worldY, int panelX, int panelY, int panelWidth, int panelHeight, double cameraX, double cameraY, double zoom) {
        int sx = (int) Math.round(panelX + panelWidth / 2.0 + (worldX - cameraX) * zoom);
        int sy = (int) Math.round(panelY + panelHeight / 2.0 + (worldY - cameraY) * zoom);
        return new ScreenPoint(sx, sy);
    }

    private boolean lineVisible(int x1, int y1, int x2, int y2, int panelX, int panelY, int panelWidth, int panelHeight) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        return !(maxX < panelX || minX > panelX + panelWidth || maxY < panelY || minY > panelY + panelHeight);
    }

    private void drawFastEdge(GuiGraphics graphics, int x1, int y1, int x2, int y2, int color, double zoom) {
        int thickness = zoom >= 0.85D ? 2 : 1;
        int midY = y1 + (y2 - y1) / 2;
        fillThinRect(graphics, x1, Math.min(y1, midY), thickness, Math.abs(midY - y1) + thickness, color);
        fillThinRect(graphics, Math.min(x1, x2), midY, Math.abs(x2 - x1) + thickness, thickness, color);
        fillThinRect(graphics, x2, Math.min(midY, y2), thickness, Math.abs(y2 - midY) + thickness, color);
    }

    private void fillThinRect(GuiGraphics graphics, int x, int y, int w, int h, int color) {
        graphics.fill(x, y, x + Math.max(1, w), y + Math.max(1, h), color);
    }

    private void fillBorder(GuiGraphics graphics, int left, int top, int right, int bottom, int color, int thickness) {
        graphics.fill(left, top, right, top + thickness, color);
        graphics.fill(left, bottom - thickness, right, bottom, color);
        graphics.fill(left, top, left + thickness, bottom, color);
        graphics.fill(right - thickness, top, right, bottom, color);
    }

    private int lighten(int argb, int add) {
        int a = (argb >> 24) & 0xFF;
        int r = Mth.clamp(((argb >> 16) & 0xFF) + add, 0, 255);
        int g = Mth.clamp(((argb >> 8) & 0xFF) + add, 0, 255);
        int b = Mth.clamp((argb & 0xFF) + add, 0, 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private String trimToWidth(String text, int width) {
        if (width <= 0) {
            return "";
        }
        return font.plainSubstrByWidth(text, width);
    }

    private record ScreenPoint(int x, int y) {
    }

    private static class Rect {
        final int left;
        final int top;
        final int right;
        final int bottom;

        private Rect(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        boolean contains(int x, int y) {
            return x >= left && x < right && y >= top && y < bottom;
        }

        boolean intersects(int x, int y, int w, int h) {
            return !(right < x || left > x + w || bottom < y || top > y + h);
        }
    }
}