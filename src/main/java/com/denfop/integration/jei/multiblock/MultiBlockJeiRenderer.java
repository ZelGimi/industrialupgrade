package com.denfop.integration.jei.multiblock;

import com.denfop.api.multiblock.MultiBlockStructure;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.util.Map;

public final class MultiBlockJeiRenderer {

    private static final int LAYER_INTERVAL_TICKS = 24;
    private static final int LAYER_DROP_ANIMATION_TICKS = 7;
    private static final float LAYER_DROP_DISTANCE = 0.75F;

    private MultiBlockJeiRenderer() {
    }

    @Nullable
    public static HoveredBlock render(
            final GuiGraphics guiGraphics,
            final MultiBlockStructure structure,
            final int x,
            final int y,
            final int width,
            final int height,
            final double mouseX,
            final double mouseY,
            final LayerMode layerMode,
            final int manualLayerIndex,
            final float yaw,
            final float pitch
    ) {
        if (!canRender(structure) || width <= 0 || height <= 0) {
            return null;
        }

        final HoveredBlock hovered = findHoveredBlock(
                structure,
                x,
                y,
                width,
                height,
                mouseX,
                mouseY,
                layerMode,
                manualLayerIndex,
                yaw,
                pitch
        );

        final Minecraft minecraft = Minecraft.getInstance();
        final PoseStack poseStack = guiGraphics.pose();
        final MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

        enableAbsoluteScissor(guiGraphics, x, y, width, height);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        Lighting.setupFor3DItems();

        poseStack.pushPose();

        try {
            applyBaseTransform(poseStack, structure, x, y, width, height, yaw, pitch);

            final int visibleLayerIndex = getVisibleLayerIndex(structure, layerMode, manualLayerIndex);
            final int visibleY = structure.minHeight + visibleLayerIndex;
            final float dropOffset = getLayerDropOffset(structure, layerMode);

            renderBlocks(
                    minecraft,
                    poseStack,
                    bufferSource,
                    structure,
                    layerMode,
                    visibleY,
                    dropOffset
            );

            if (hovered != null) {
                renderHoveredOutline(poseStack, bufferSource, hovered.relativePos());
            }
        } finally {
            bufferSource.endBatch();

            poseStack.popPose();

            Lighting.setupForFlatItems();
            RenderSystem.lineWidth(1.0F);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            guiGraphics.disableScissor();
        }

        return hovered;
    }

    @Nullable
    public static HoveredBlock findHoveredBlock(
            final MultiBlockStructure structure,
            final int x,
            final int y,
            final int width,
            final int height,
            final double mouseX,
            final double mouseY,
            final LayerMode layerMode,
            final int manualLayerIndex,
            final float yaw,
            final float pitch
    ) {
        if (!canRender(structure)) {
            return null;
        }

        if (mouseX < x || mouseY < y || mouseX >= x + width || mouseY >= y + height) {
            return null;
        }

        final Matrix4f matrix = createProjectionMatrix(structure, x, y, width, height, yaw, pitch);
        final int visibleLayerIndex = getVisibleLayerIndex(structure, layerMode, manualLayerIndex);
        final int visibleY = structure.minHeight + visibleLayerIndex;

        HoveredBlock result = null;
        double bestScore = Double.MAX_VALUE;

        for (final Map.Entry<BlockPos, ItemStack> entry : structure.ItemStackMap.entrySet()) {
            final BlockPos pos = entry.getKey();
            final ItemStack stack = entry.getValue();

            if (pos == null || stack == null || stack.isEmpty()) {
                continue;
            }

            if (!isLayerVisible(pos.getY(), structure, layerMode, visibleY)) {
                continue;
            }

            final double[] projected = projectToScreen(pos, matrix);

            final double dx = mouseX - projected[0];
            final double dy = mouseY - projected[1];
            final double distanceSq = dx * dx + dy * dy;

            if (distanceSq > 225.0D) {
                continue;
            }

            final double score = distanceSq - projected[2] * 0.001D;
            if (score < bestScore) {
                bestScore = score;
                result = new HoveredBlock(
                        pos.immutable(),
                        stack.copy(),
                        structure.RotationMap.getOrDefault(pos, Direction.NORTH)
                );
            }
        }

        return result;
    }

    public static int getVisibleLayerIndex(
            final MultiBlockStructure structure,
            final LayerMode layerMode,
            final int manualLayerIndex
    ) {
        if (!canRender(structure)) {
            return 0;
        }

        final int layerCount = getLayerCount(structure);

        if (layerMode == LayerMode.ALL) {
            return layerCount - 1;
        }

        if (layerMode == LayerMode.SINGLE) {
            return Mth.clamp(manualLayerIndex, 0, layerCount - 1);
        }

        final long ticks = getRenderTicks();
        return (int) ((ticks / LAYER_INTERVAL_TICKS) % layerCount);
    }

    private static boolean isLayerVisible(
            final int blockY,
            final MultiBlockStructure structure,
            final LayerMode layerMode,
            final int visibleY
    ) {
        if (layerMode == LayerMode.ALL) {
            return true;
        }

        if (layerMode == LayerMode.SINGLE) {
            return blockY == visibleY;
        }

        return blockY <= visibleY;
    }

    private static void renderBlocks(
            final Minecraft minecraft,
            final PoseStack poseStack,
            final MultiBufferSource.BufferSource bufferSource,
            final MultiBlockStructure structure,
            final LayerMode layerMode,
            final int visibleY,
            final float dropOffset
    ) {
        for (final Map.Entry<BlockPos, ItemStack> entry : structure.ItemStackMap.entrySet()) {
            final BlockPos pos = entry.getKey();
            final ItemStack stack = entry.getValue();

            if (pos == null || stack == null || stack.isEmpty()) {
                continue;
            }

            if (!isLayerVisible(pos.getY(), structure, layerMode, visibleY)) {
                continue;
            }

            final Direction direction = structure.RotationMap.getOrDefault(pos, Direction.NORTH);
            final BakedModel bakedModel = structure.bakedModelMap == null ? null : structure.bakedModelMap.get(pos);

            final float animatedYOffset;
            if (layerMode == LayerMode.AUTO && pos.getY() == visibleY) {
                animatedYOffset = dropOffset;
            } else {
                animatedYOffset = 0.0F;
            }

            renderSingleStack(
                    minecraft,
                    poseStack,
                    bufferSource,
                    stack,
                    bakedModel,
                    pos,
                    direction,
                    animatedYOffset
            );
        }
    }

    private static void renderSingleStack(
            final Minecraft minecraft,
            final PoseStack poseStack,
            final MultiBufferSource.BufferSource bufferSource,
            final ItemStack stack,
            @Nullable final BakedModel bakedModel,
            final BlockPos pos,
            final Direction direction,
            final float yOffset
    ) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            poseStack.pushPose();

            poseStack.translate(
                    pos.getX(),
                    pos.getY() + yOffset,
                    pos.getZ()
            );

            final BlockState state = applyDirectionToState(
                    blockItem.getBlock().defaultBlockState(),
                    direction
            );

            minecraft.getBlockRenderer().renderSingleBlock(
                    state,
                    poseStack,
                    bufferSource,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY
            );

            poseStack.popPose();
            return;
        }

        poseStack.pushPose();

        poseStack.translate(
                pos.getX() + 0.5F,
                pos.getY() + 0.5F + yOffset,
                pos.getZ() + 0.5F
        );

        applyItemDirectionRotation(poseStack, direction);

        if (bakedModel != null) {
            minecraft.getItemRenderer().render(
                    stack,
                    ItemDisplayContext.FIXED,
                    false,
                    poseStack,
                    bufferSource,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    bakedModel
            );
        } else {
            minecraft.getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    minecraft.level,
                    0
            );
        }

        poseStack.popPose();
    }

    private static BlockState applyDirectionToState(final BlockState state, final Direction direction) {
        final Direction safeDirection = direction == null ? Direction.NORTH : direction;

        if (state.hasProperty(BlockStateProperties.FACING)) {
            return state.setValue(BlockStateProperties.FACING, safeDirection);
        }

        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            final Direction horizontal = safeDirection.getAxis().isVertical() ? Direction.NORTH : safeDirection;
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, horizontal);
        }

        if (state.hasProperty(BlockStateProperties.AXIS)) {
            return state.setValue(BlockStateProperties.AXIS, safeDirection.getAxis());
        }

        for (final var property : state.getProperties()) {
            if (property instanceof DirectionProperty directionProperty) {
                if (directionProperty.getPossibleValues().contains(safeDirection)) {
                    return state.setValue(directionProperty, safeDirection);
                }

                final Direction horizontal = safeDirection.getAxis().isVertical() ? Direction.NORTH : safeDirection;
                if (directionProperty.getPossibleValues().contains(horizontal)) {
                    return state.setValue(directionProperty, horizontal);
                }
            }
        }

        return state;
    }

    private static void applyItemDirectionRotation(final PoseStack poseStack, final Direction direction) {
        final Direction safeDirection = direction == null ? Direction.NORTH : direction;

        switch (safeDirection) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            default -> {
            }
        }
    }

    private static void applyBaseTransform(
            final PoseStack poseStack,
            final MultiBlockStructure structure,
            final int x,
            final int y,
            final int width,
            final int height,
            final float yaw,
            final float pitch
    ) {
        final int centerX = x + width / 2;
        final int centerY = y + height / 2 + Mth.clamp(height / 14, 3, 10);
        final float scale = computeFitScale(structure, width, height);

        poseStack.translate(centerX, centerY, 420.0F);
        poseStack.scale(scale, -scale, scale);
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));

        poseStack.translate(
                -getCenterX(structure),
                -getCenterY(structure),
                -getCenterZ(structure)
        );
    }

    private static Matrix4f createProjectionMatrix(
            final MultiBlockStructure structure,
            final int x,
            final int y,
            final int width,
            final int height,
            final float yaw,
            final float pitch
    ) {
        final int centerX = x + width / 2;
        final int centerY = y + height / 2 + Mth.clamp(height / 14, 3, 10);
        final float scale = computeFitScale(structure, width, height);

        return new Matrix4f()
                .translation(centerX, centerY, 420.0F)
                .scale(scale, -scale, scale)
                .rotate(Axis.XP.rotationDegrees(pitch))
                .rotate(Axis.YP.rotationDegrees(yaw))
                .translate(
                        -getCenterX(structure),
                        -getCenterY(structure),
                        -getCenterZ(structure)
                );
    }

    private static float computeFitScale(
            final MultiBlockStructure structure,
            final int width,
            final int height
    ) {
        final int sizeX = Math.max(1, structure.maxLength - structure.minLength + 1);
        final int sizeY = Math.max(1, structure.maxHeight - structure.minHeight + 1);
        final int sizeZ = Math.max(1, structure.maxWeight - structure.minWeight + 1);

        final float safeWidth = Math.max(24.0F, width - 16.0F);
        final float safeHeight = Math.max(24.0F, height - 14.0F);

        final float projectedWidth = Math.max(1.0F, (sizeX + sizeZ) * 0.72F);
        final float projectedHeight = Math.max(1.0F, sizeY + (sizeX + sizeZ) * 0.26F);

        final float byWidth = safeWidth / projectedWidth;
        final float byHeight = safeHeight / projectedHeight;

        return Mth.clamp(Math.min(byWidth, byHeight), 4.0F, 24.0F);
    }

    private static float getLayerDropOffset(
            final MultiBlockStructure structure,
            final LayerMode layerMode
    ) {
        if (layerMode != LayerMode.AUTO || getLayerCount(structure) <= 1) {
            return 0.0F;
        }

        final long localTick = getRenderTicks() % LAYER_INTERVAL_TICKS;
        final float progress = Mth.clamp(
                (float) localTick / (float) LAYER_DROP_ANIMATION_TICKS,
                0.0F,
                1.0F
        );

        final float inv = 1.0F - progress;
        final float eased = 1.0F - inv * inv * inv;

        return (1.0F - eased) * LAYER_DROP_DISTANCE;
    }

    private static void renderHoveredOutline(
            final PoseStack poseStack,
            final MultiBufferSource.BufferSource bufferSource,
            final BlockPos pos
    ) {
        RenderSystem.lineWidth(2.0F);

        LevelRenderer.renderLineBox(
                poseStack,
                bufferSource.getBuffer(RenderType.lines()),
                pos.getX() - 0.002D,
                pos.getY() - 0.002D,
                pos.getZ() - 0.002D,
                pos.getX() + 1.002D,
                pos.getY() + 1.002D,
                pos.getZ() + 1.002D,
                0.35F,
                0.85F,
                1.0F,
                1.0F
        );

        RenderSystem.lineWidth(1.0F);
    }

    private static void enableAbsoluteScissor(
            final GuiGraphics guiGraphics,
            final int x,
            final int y,
            final int width,
            final int height
    ) {
        final Matrix4f matrix = guiGraphics.pose().last().pose();

        final Vector4f min = new Vector4f(x, y, 0.0F, 1.0F);
        final Vector4f max = new Vector4f(x + width, y + height, 0.0F, 1.0F);

        min.mul(matrix);
        max.mul(matrix);

        final int scissorX1 = Mth.floor(Math.min(min.x(), max.x()));
        final int scissorY1 = Mth.floor(Math.min(min.y(), max.y()));
        final int scissorX2 = Mth.ceil(Math.max(min.x(), max.x()));
        final int scissorY2 = Mth.ceil(Math.max(min.y(), max.y()));

        guiGraphics.enableScissor(scissorX1, scissorY1, scissorX2, scissorY2);
    }

    private static double[] projectToScreen(final BlockPos pos, final Matrix4f matrix) {
        final Vector4f vector = new Vector4f(
                pos.getX() + 0.5F,
                pos.getY() + 0.5F,
                pos.getZ() + 0.5F,
                1.0F
        );

        vector.mul(matrix);

        return new double[]{
                vector.x(),
                vector.y(),
                vector.z()
        };
    }

    private static boolean canRender(final MultiBlockStructure structure) {
        return structure != null
                && structure.ItemStackMap != null
                && !structure.ItemStackMap.isEmpty();
    }

    private static int getLayerCount(final MultiBlockStructure structure) {
        if (!canRender(structure)) {
            return 0;
        }

        return Math.max(1, structure.maxHeight - structure.minHeight + 1);
    }

    private static float getCenterX(final MultiBlockStructure structure) {
        return (structure.minLength + structure.maxLength + 1) / 2.0F;
    }

    private static float getCenterY(final MultiBlockStructure structure) {
        return (structure.minHeight + structure.maxHeight + 1) / 2.0F;
    }

    private static float getCenterZ(final MultiBlockStructure structure) {
        return (structure.minWeight + structure.maxWeight + 1) / 2.0F;
    }

    private static long getRenderTicks() {
        final Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level != null) {
            return minecraft.level.getGameTime();
        }

        return Util.getMillis() / 50L;
    }

    public enum LayerMode {
        AUTO,
        ALL,
        SINGLE
    }

    public record HoveredBlock(BlockPos relativePos, ItemStack stack, Direction direction) {
    }
}