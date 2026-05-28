package com.denfop.integration.jei.multiblock;

import com.denfop.api.multiblock.MultiBlockStructure;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

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
            final PoseStack poseStack,
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
        final MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

        enableAbsoluteScissor(poseStack, x, y, width, height);

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
            RenderSystem.disableScissor();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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

            final HoverBox hoverBox = projectBlockBox(pos, matrix);

            if (!hoverBox.isMouseNear(mouseX, mouseY, 3.5D)) {
                continue;
            }

            final double score =
                    hoverBox.distanceToCenterSq(mouseX, mouseY) * 0.015D
                            + hoverBox.distanceToBoxSq(mouseX, mouseY) * 0.60D
                            - hoverBox.maxZ * 0.002D;

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

            final float animatedYOffset = layerMode == LayerMode.AUTO && pos.getY() == visibleY
                    ? dropOffset
                    : 0.0F;

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
                    ItemTransforms.TransformType.FIXED,
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
                    ItemTransforms.TransformType.FIXED,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
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

        if (state.hasProperty(BlockStateProperties.FACING_HOPPER)) {
            return state.setValue(BlockStateProperties.FACING_HOPPER, safeDirection);
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
            case SOUTH -> poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            case EAST -> poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
            case WEST -> poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
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
        poseStack.mulPose(Vector3f.XP.rotationDegrees(pitch));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(yaw));

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
        final PoseStack stack = new PoseStack();

        applyBaseTransform(
                stack,
                structure,
                x,
                y,
                width,
                height,
                yaw,
                pitch
        );

        return stack.last().pose().copy();
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
            final PoseStack poseStack,
            final int x,
            final int y,
            final int width,
            final int height
    ) {
        final Minecraft minecraft = Minecraft.getInstance();
        final Matrix4f matrix = poseStack.last().pose();

        final Vector4f min = new Vector4f(x, y, 0.0F, 1.0F);
        final Vector4f max = new Vector4f(x + width, y + height, 0.0F, 1.0F);

        min.transform(matrix);
        max.transform(matrix);

        final double guiMinX = Math.min(min.x(), max.x());
        final double guiMinY = Math.min(min.y(), max.y());
        final double guiMaxX = Math.max(min.x(), max.x());
        final double guiMaxY = Math.max(min.y(), max.y());

        final double scale = minecraft.getWindow().getGuiScale();

        final int scissorX = (int) Math.floor(guiMinX * scale);
        final int scissorY = (int) Math.floor(minecraft.getWindow().getHeight() - guiMaxY * scale);
        final int scissorW = Math.max(0, (int) Math.ceil((guiMaxX - guiMinX) * scale));
        final int scissorH = Math.max(0, (int) Math.ceil((guiMaxY - guiMinY) * scale));

        if (scissorW > 0 && scissorH > 0) {
            RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);
        }
    }

    private static HoverBox projectBlockBox(final BlockPos pos, final Matrix4f matrix) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;

        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = 0; dz <= 1; dz++) {
                    final Vector4f vector = new Vector4f(
                            pos.getX() + dx,
                            pos.getY() + dy,
                            pos.getZ() + dz,
                            1.0F
                    );

                    vector.transform(matrix);

                    minX = Math.min(minX, vector.x());
                    minY = Math.min(minY, vector.y());
                    maxX = Math.max(maxX, vector.x());
                    maxY = Math.max(maxY, vector.y());
                    maxZ = Math.max(maxZ, vector.z());
                }
            }
        }

        return new HoverBox(minX, minY, maxX, maxY, maxZ);
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

    private record HoverBox(
            double minX,
            double minY,
            double maxX,
            double maxY,
            double maxZ
    ) {

        boolean isMouseNear(final double mouseX, final double mouseY, final double padding) {
            return mouseX >= this.minX - padding
                    && mouseX <= this.maxX + padding
                    && mouseY >= this.minY - padding
                    && mouseY <= this.maxY + padding;
        }

        double distanceToCenterSq(final double mouseX, final double mouseY) {
            final double centerX = (this.minX + this.maxX) * 0.5D;
            final double centerY = (this.minY + this.maxY) * 0.5D;

            final double dx = mouseX - centerX;
            final double dy = mouseY - centerY;

            return dx * dx + dy * dy;
        }

        double distanceToBoxSq(final double mouseX, final double mouseY) {
            final double dx;

            if (mouseX < this.minX) {
                dx = this.minX - mouseX;
            } else if (mouseX > this.maxX) {
                dx = mouseX - this.maxX;
            } else {
                dx = 0.0D;
            }

            final double dy;

            if (mouseY < this.minY) {
                dy = this.minY - mouseY;
            } else if (mouseY > this.maxY) {
                dy = mouseY - this.maxY;
            } else {
                dy = 0.0D;
            }

            return dx * dx + dy * dy;
        }
    }
}