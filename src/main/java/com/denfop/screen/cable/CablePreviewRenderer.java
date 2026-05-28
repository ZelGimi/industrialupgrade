package com.denfop.screen.cable;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

public class CablePreviewRenderer {

    private static final float MODEL_Z = 220.0F;
    private static final int CENTER_Y_OFFSET = 6;

    public void render(GuiGraphics guiGraphics,
                       int x,
                       int y,
                       int width,
                       int height,
                       CablePreviewStateFactory.PreparedPreview preview,
                       EnumSet<Direction> blacklist,
                       CablePreviewHitResult hovered,
                       EnumMap<Direction, BlockState> neighborStates,
                       float yaw,
                       float pitch,
                       float zoom) {
        if (preview == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        Lighting.setupFor3DItems();

        guiGraphics.pose().pushPose();
        applyTransform(guiGraphics, x, y, width, height, yaw, pitch, zoom);

        renderNeighborBlocks(guiGraphics, bufferSource, neighborStates);
        renderBlockModel(guiGraphics, bufferSource, preview);
        renderOutlineState(guiGraphics, bufferSource, preview, blacklist, hovered);

        bufferSource.endBatch();
        guiGraphics.pose().popPose();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        Lighting.setupForFlatItems();
        RenderSystem.enableCull();
    }

    public void renderWithoutOutline(GuiGraphics guiGraphics,
                                     int x,
                                     int y,
                                     int width,
                                     int height,
                                     CablePreviewStateFactory.PreparedPreview preview,
                                     EnumSet<Direction> blacklist,
                                     CablePreviewHitResult hovered,
                                     EnumMap<Direction, BlockState> neighborStates,
                                     float yaw,
                                     float pitch,
                                     float zoom) {
        if (preview == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        Lighting.setupFor3DItems();

        guiGraphics.pose().pushPose();
        applyTransform(guiGraphics, x, y, width, height, yaw, pitch, zoom);

        for (Direction direction : Direction.values()) {
            guiGraphics.pose().pushPose();

            guiGraphics.pose().translate(
                    direction.getStepX(),
                    direction.getStepY(),
                    direction.getStepZ()
            );

            BlockState state = neighborStates.get(direction);
            boolean hasRealBlock = state != null && !state.isAir();

            if (hasRealBlock) {
                minecraft.getBlockRenderer().renderSingleBlock(
                        state,
                        guiGraphics.pose(),
                        bufferSource,
                        LightTexture.FULL_BRIGHT,
                        OverlayTexture.NO_OVERLAY
                );
            }


            guiGraphics.pose().popPose();
        }
        renderBlockModel(guiGraphics, bufferSource, preview);
        renderOutlineState(guiGraphics, bufferSource, preview, blacklist, hovered);

        bufferSource.endBatch();
        guiGraphics.pose().popPose();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        Lighting.setupForFlatItems();
        RenderSystem.enableCull();
    }

    protected void renderBlockModel(GuiGraphics guiGraphics,
                                    MultiBufferSource.BufferSource bufferSource,
                                    CablePreviewStateFactory.PreparedPreview preview) {
        Minecraft minecraft = Minecraft.getInstance();

        minecraft.getBlockRenderer().renderSingleBlock(
                preview.state(),
                guiGraphics.pose(),
                bufferSource,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY
        );
    }

    private void renderNeighborBlocks(GuiGraphics guiGraphics,
                                      MultiBufferSource.BufferSource bufferSource,
                                      EnumMap<Direction, BlockState> neighborStates) {
        Minecraft minecraft = Minecraft.getInstance();

        for (Direction direction : Direction.values()) {
            guiGraphics.pose().pushPose();

            guiGraphics.pose().translate(
                    direction.getStepX(),
                    direction.getStepY(),
                    direction.getStepZ()
            );

            BlockState state = neighborStates.get(direction);
            boolean hasRealBlock = state != null && !state.isAir();

            if (hasRealBlock) {
                minecraft.getBlockRenderer().renderSingleBlock(
                        state,
                        guiGraphics.pose(),
                        bufferSource,
                        LightTexture.FULL_BRIGHT,
                        OverlayTexture.NO_OVERLAY
                );
            }

            AABB outline = new AABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D).inflate(0.002D);
            float r = hasRealBlock ? 0.62F : 0.36F;
            float g = hasRealBlock ? 0.75F : 0.42F;
            float b = hasRealBlock ? 0.95F : 0.62F;
            float a = hasRealBlock ? 0.82F : 0.45F;

            LevelRenderer.renderLineBox(
                    guiGraphics.pose(),
                    bufferSource.getBuffer(RenderType.lines()),
                    (float) outline.minX,
                    (float) outline.minY,
                    (float) outline.minZ,
                    (float) outline.maxX,
                    (float) outline.maxY,
                    (float) outline.maxZ,
                    r, g, b, a
            );

            guiGraphics.pose().popPose();
        }
    }

    private void renderOutlineState(GuiGraphics guiGraphics,
                                    MultiBufferSource.BufferSource bufferSource,
                                    CablePreviewStateFactory.PreparedPreview preview,
                                    EnumSet<Direction> blacklist,
                                    CablePreviewHitResult hovered) {
        for (CablePreviewPart part : CablePreviewPart.values()) {
            List<AABB> boxes = preview.boxes(part);
            if (boxes.isEmpty()) {
                continue;
            }

            float r;
            float g;
            float b;
            float a;

            boolean isHovered = hovered != null && hovered.part() == part;
            boolean isCenter = part == CablePreviewPart.CENTER;

            if (isHovered) {
                r = 0.18F;
                g = 0.82F;
                b = 1.00F;
                a = 1.00F;
            } else if (isCenter) {
                r = 0.76F;
                g = 0.82F;
                b = 0.92F;
                a = 0.45F;
            } else if (blacklist.contains(part.direction())) {
                r = 1.00F;
                g = 0.42F;
                b = 0.30F;
                a = 0.78F;
            } else {
                r = 0.24F;
                g = 1.00F;
                b = 0.52F;
                a = 0.74F;
            }

            for (AABB box : boxes) {
                AABB expanded = box.inflate(0.003D);
                LevelRenderer.renderLineBox(
                        guiGraphics.pose(),
                        bufferSource.getBuffer(RenderType.lines()),
                        (float) expanded.minX,
                        (float) expanded.minY,
                        (float) expanded.minZ,
                        (float) expanded.maxX,
                        (float) expanded.maxY,
                        (float) expanded.maxZ,
                        r, g, b, a
                );
            }
        }
    }

    public CablePreviewHitResult pick(int mouseX,
                                      int mouseY,
                                      int x,
                                      int y,
                                      int width,
                                      int height,
                                      CablePreviewStateFactory.PreparedPreview preview,
                                      float yaw,
                                      float pitch,
                                      float zoom) {
        if (preview == null) {
            return null;
        }

        if (mouseX < x || mouseX >= x + width || mouseY < y || mouseY >= y + height) {
            return null;
        }

        Matrix4f modelToScreen = buildMatrix(x, y, width, height, yaw, pitch, zoom);
        Matrix4f inverse = new Matrix4f(modelToScreen).invert();

        Vector4f near = new Vector4f(mouseX, mouseY, -1000.0F, 1.0F).mul(inverse);
        Vector4f far = new Vector4f(mouseX, mouseY, 1000.0F, 1.0F).mul(inverse);

        Vec3 origin = new Vec3(near.x() / near.w(), near.y() / near.w(), near.z() / near.w());
        Vec3 target = new Vec3(far.x() / far.w(), far.y() / far.w(), far.z() / far.w());
        Vec3 direction = target.subtract(origin).normalize();

        CablePreviewPart bestPart = null;
        AABB bestBox = null;
        double bestDepth = Double.POSITIVE_INFINITY;

        for (CablePreviewPart part : CablePreviewPart.values()) {
            for (AABB box : preview.boxes(part)) {
                double hit = intersectRay(origin, direction, box);
                if (hit < bestDepth) {
                    bestDepth = hit;
                    bestPart = part;
                    bestBox = box;
                } else if (bestPart == CablePreviewPart.CENTER && part != CablePreviewPart.CENTER && Math.abs(hit - bestDepth) < 0.0001D) {
                    bestDepth = hit;
                    bestPart = part;
                    bestBox = box;
                }
            }
        }

        if (bestPart == null || bestBox == null || Double.isInfinite(bestDepth)) {
            return null;
        }

        return new CablePreviewHitResult(bestPart, bestBox, preview.mergedBox(bestPart).getCenter(), bestDepth);
    }

    public ScreenProjection projectPoint(Vec3 point,
                                         int x,
                                         int y,
                                         int width,
                                         int height,
                                         float yaw,
                                         float pitch,
                                         float zoom) {
        Matrix4f matrix = buildMatrix(x, y, width, height, yaw, pitch, zoom);
        Vector4f vec = new Vector4f((float) point.x, (float) point.y, (float) point.z, 1.0F).mul(matrix);
        return new ScreenProjection(vec.x(), vec.y(), vec.z());
    }

    private void applyTransform(GuiGraphics guiGraphics,
                                int x,
                                int y,
                                int width,
                                int height,
                                float yaw,
                                float pitch,
                                float zoom) {
        int centerX = x + (width / 2);
        int centerY = y + (height / 2) + CENTER_Y_OFFSET;
        float scale = computeScale(width, height, zoom);

        guiGraphics.pose().translate(centerX, centerY, 220.0F);
        guiGraphics.pose().scale(scale, -scale, scale);
        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(pitch));
        guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(yaw));
        guiGraphics.pose().translate(-0.5F, -0.5F, -0.5F);
    }

    private Matrix4f buildMatrix(int x, int y, int width, int height, float yaw, float pitch, float zoom) {
        int centerX = x + (width / 2);
        int centerY = y + (height / 2) + CENTER_Y_OFFSET;
        float scale = computeScale(width, height, zoom);

        return new Matrix4f()
                .translation(centerX, centerY, MODEL_Z)
                .scale(scale, -scale, scale)
                .rotate(Axis.XP.rotationDegrees(pitch))
                .rotate(Axis.YP.rotationDegrees(yaw))
                .translate(-0.5F, -0.5F, -0.5F);
    }

    private float computeScale(int width, int height, float zoom) {
        float base = Math.min(width, height) * 0.37F;
        return Mth.clamp(base * zoom, 30.0F, 86.0F);
    }

    private double intersectRay(Vec3 origin, Vec3 direction, AABB box) {
        double tMin = 0.0D;
        double tMax = Double.POSITIVE_INFINITY;

        tMin = intersectAxis(origin.x, direction.x, box.minX, box.maxX, tMin, true);
        tMax = intersectAxis(origin.x, direction.x, box.minX, box.maxX, tMax, false);
        if (tMax < tMin) return Double.POSITIVE_INFINITY;

        tMin = intersectAxis(origin.y, direction.y, box.minY, box.maxY, tMin, true);
        tMax = intersectAxis(origin.y, direction.y, box.minY, box.maxY, tMax, false);
        if (tMax < tMin) return Double.POSITIVE_INFINITY;

        tMin = intersectAxis(origin.z, direction.z, box.minZ, box.maxZ, tMin, true);
        tMax = intersectAxis(origin.z, direction.z, box.minZ, box.maxZ, tMax, false);
        if (tMax < tMin) return Double.POSITIVE_INFINITY;

        return tMin;
    }

    private double intersectAxis(double origin,
                                 double direction,
                                 double min,
                                 double max,
                                 double current,
                                 boolean lowerBound) {
        if (Math.abs(direction) < 1.0E-7D) {
            if (origin < min || origin > max) {
                return lowerBound ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
            }
            return current;
        }

        double inv = 1.0D / direction;
        double t1 = (min - origin) * inv;
        double t2 = (max - origin) * inv;

        double enter = Math.min(t1, t2);
        double exit = Math.max(t1, t2);

        return lowerBound ? Math.max(current, enter) : Math.min(current, exit);
    }


    public record ScreenProjection(float x, float y, float z) {
    }
}