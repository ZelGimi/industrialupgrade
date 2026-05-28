package com.denfop.api.storage.autocrafting.graph;

import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.denfop.api.widget.ScreenWidget.bindBlockTexture;
import static com.denfop.api.widget.ScreenWidget.getBlockTextureMap;
import static net.minecraft.client.gui.GuiComponent.fill;

public class CraftGraphRenderer {

    private static final String I18N_GRAPH = "iu.autocraft.graph";

    private final Minecraft mc;
    private final Font font;
    @SuppressWarnings("unused")
    private final ScreenIndustrialUpgrade<?> gui;

    public CraftGraphRenderer(Minecraft mc, ScreenIndustrialUpgrade<?> gui) {
        this.mc = mc;
        this.font = mc.font;
        this.gui = gui;
    }

    public void render(PoseStack poseStack, CraftGraph graph, int panelX, int panelY, int panelWidth, int panelHeight,
                       double cameraX, double cameraY, double zoom, int mouseX, int mouseY,
                       Integer hoveredNodeId, Integer selectedNodeId, Set<Integer> highlightedNodes) {
        if (graph == null) {
            return;
        }

        renderEdges(poseStack, graph, panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom, highlightedNodes);
        renderNodes(poseStack, graph, panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom,
                hoveredNodeId, selectedNodeId, highlightedNodes);
    }

    private void renderEdges(PoseStack poseStack, CraftGraph graph, int panelX, int panelY, int panelWidth, int panelHeight,
                             double cameraX, double cameraY, double zoom, Set<Integer> highlightedNodes) {
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

            boolean highlight = highlightedNodes != null
                    && highlightedNodes.contains(from.getId())
                    && highlightedNodes.contains(to.getId());

            if (highlightedNodes != null && !highlightedNodes.isEmpty() && !highlight) {
                continue;
            }

            int color = highlight ? 0xFF80D8FF : 0xFF5E6A73;
            if (zoom < 0.32D) {
                color = highlight ? 0xCC80D8FF : 0x885E6A73;
            }

            drawFastEdge(poseStack, a.x, a.y, b.x, b.y, color, zoom);
        }
    }

    private void renderNodes(PoseStack poseStack, CraftGraph graph, int panelX, int panelY, int panelWidth, int panelHeight,
                             double cameraX, double cameraY, double zoom,
                             Integer hoveredNodeId, Integer selectedNodeId, Set<Integer> highlightedNodes) {
        for (CraftNode node : graph.getNodes()) {
            Rect rect = getNodeScreenRect(node, panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom);
            if (!rect.intersects(panelX, panelY, panelWidth, panelHeight)) {
                continue;
            }

            boolean hovered = hoveredNodeId != null && hoveredNodeId == node.getId();
            boolean selected = selectedNodeId != null && selectedNodeId == node.getId();
            boolean highlighted = highlightedNodes != null && highlightedNodes.contains(node.getId());

            renderNode(poseStack, node, rect, zoom, hovered, selected, highlighted);
        }
    }

    private void renderNode(PoseStack poseStack, CraftNode node, Rect rect, double zoom,
                            boolean hovered, boolean selected, boolean highlighted) {
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
            fill(poseStack, rect.left - pad, rect.top - pad, rect.right + pad, rect.bottom + pad, 0x44000000);
            fillBorder(poseStack, rect.left - pad, rect.top - pad, rect.right + pad, rect.bottom + pad, outlineColor, 1);
        }

        renderScaledStack(poseStack, node.getStack(), node.getRequiredAmount() + node.getAvailableAmount(), iconX, iconY, zoom);
    }

    private void renderScaledStack(PoseStack poseStack, SameStack sameStack, int count, int x, int y, double zoom) {
        if (sameStack == null || sameStack.isEmpty()) {
            return;
        }

        float itemScale = (float) Mth.clamp(zoom, 0.22D, 1.0D);

        if (sameStack.isItem()) {
            ItemStack renderStack = sameStack.getStack().copy();
            renderStack.setCount(Math.max(1, sameStack.getAmount()));
            renderItemScaled(poseStack, renderStack, x, y, itemScale);
        } else if (sameStack.isFluid()) {
            renderFluidScaled(poseStack, sameStack.getFluidStack(), x, y, itemScale);
        }

        if (zoom >= 0.42D && count > 1) {
            String text = ModUtils.getString(count);
            float textScale = Math.max(0.38F, itemScale * 0.48F);
            int textX = x + Mth.floor(16.0F * itemScale - this.font.width(text) * textScale);
            int textY = y + Mth.floor(16.0F * itemScale - 7.0F * textScale);
            drawScaledText(poseStack, text, textX, textY, textScale, 0xFFFFFF, true);
        }
    }

    private void renderItemScaled(PoseStack poseStack, ItemStack stack, int x, int y, float scale) {
        poseStack.pushPose();
        poseStack.translate(x, y, 200.0F);
        poseStack.scale(scale, scale, 1.0F);

        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        RenderSystem.applyModelViewMatrix();

        try {
            Lighting.setupForFlatItems();
            this.mc.getItemRenderer().renderAndDecorateItem(stack, 0, 0);
        } finally {
            Lighting.setupFor3DItems();
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
            poseStack.popPose();
        }
    }

    private void renderFluidScaled(PoseStack poseStack, FluidStack fluidStack, int x, int y, float scale) {
        if (fluidStack == null || fluidStack.isEmpty()) {
            return;
        }

        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
        if (extensions == null) {
            return;
        }

        if (extensions.getStillTexture(fluidStack) == null) {
            return;
        }

        TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fluidStack));
        int color = extensions.getTintColor(fluidStack);

        float a = ((color >> 24) & 0xFF) / 255.0F;
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        if (a <= 0.0F) {
            a = 1.0F;
        }

        float size = 16.0F * scale;
        float left = x;
        float top = y;
        float right = left + size;
        float bottom = top + size;
        float z = 200.0F;

        bindBlockTexture();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        buffer.vertex(poseStack.last().pose(), left, bottom, z).uv(sprite.getU0(), sprite.getV1()).color(r, g, b, a).endVertex();
        buffer.vertex(poseStack.last().pose(), right, bottom, z).uv(sprite.getU1(), sprite.getV1()).color(r, g, b, a).endVertex();
        buffer.vertex(poseStack.last().pose(), right, top, z).uv(sprite.getU1(), sprite.getV0()).color(r, g, b, a).endVertex();
        buffer.vertex(poseStack.last().pose(), left, top, z).uv(sprite.getU0(), sprite.getV0()).color(r, g, b, a).endVertex();

        BufferUploader.drawWithShader(buffer.end());

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void drawScaledText(PoseStack poseStack, String text, int x, int y, float scale, int color, boolean shadow) {
        if (text == null || text.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(x, y, 300.0F);
        poseStack.scale(scale, scale, 1.0F);

        if (shadow) {
            this.font.drawShadow(poseStack, text, 0.0F, 0.0F, color);
        } else {
            this.font.draw(poseStack, text, 0.0F, 0.0F, color);
        }

        poseStack.popPose();
    }

    public Integer findHoveredNode(CraftGraph graph, int panelX, int panelY, int panelWidth, int panelHeight,
                                   double cameraX, double cameraY, double zoom, double mouseX, double mouseY) {
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

        lines.add(Component.translatable(I18N_GRAPH + ".tooltip.type", getNodeTypeName(node.getType())));

        if (node.getCraftTime() > 0) {
            lines.add(Component.translatable(I18N_GRAPH + ".tooltip.craft_time", node.getCraftTime()));
        }

        return lines;
    }

    private Component getNodeTypeName(CraftNodeType type) {
        return Component.translatable(I18N_GRAPH + ".node_type." + type.name().toLowerCase(Locale.ROOT));
    }

    private Rect getNodeScreenRect(CraftNode node, int panelX, int panelY, int panelWidth, int panelHeight,
                                   double cameraX, double cameraY, double zoom) {
        ScreenPoint center = worldToScreen(node.getX(), node.getY(), panelX, panelY, panelWidth, panelHeight, cameraX, cameraY, zoom);
        int size = Math.max(6, (int) Math.round(16 * zoom));
        return new Rect(center.x - size / 2, center.y - size / 2, center.x + size / 2, center.y + size / 2);
    }

    private ScreenPoint worldToScreen(double worldX, double worldY, int panelX, int panelY, int panelWidth, int panelHeight,
                                      double cameraX, double cameraY, double zoom) {
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

    private void drawFastEdge(PoseStack poseStack, int x1, int y1, int x2, int y2, int color, double zoom) {
        int thickness = zoom >= 0.85D ? 2 : 1;
        int midY = y1 + (y2 - y1) / 2;

        fillThinRect(poseStack, x1, Math.min(y1, midY), thickness, Math.abs(midY - y1) + thickness, color);
        fillThinRect(poseStack, Math.min(x1, x2), midY, Math.abs(x2 - x1) + thickness, thickness, color);
        fillThinRect(poseStack, x2, Math.min(midY, y2), thickness, Math.abs(y2 - midY) + thickness, color);
    }

    private void fillThinRect(PoseStack poseStack, int x, int y, int w, int h, int color) {
        GuiComponent.fill(poseStack, x, y, x + Math.max(1, w), y + Math.max(1, h), color);
    }

    private void fillBorder(PoseStack poseStack, int left, int top, int right, int bottom, int color, int thickness) {
        GuiComponent.fill(poseStack, left, top, right, top + thickness, color);
        GuiComponent.fill(poseStack, left, bottom - thickness, right, bottom, color);
        GuiComponent.fill(poseStack, left, top, left + thickness, bottom, color);
        GuiComponent.fill(poseStack, right - thickness, top, right, bottom, color);
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