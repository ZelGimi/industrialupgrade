package com.denfop.api.space.dimension.worldgen.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SpaceGeyserBlockEntityRenderer implements BlockEntityRenderer<SpaceGeyserBlockEntity> {

    public SpaceGeyserBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
    }


    @Override
    public void render(
            final SpaceGeyserBlockEntity blockEntity,
            final float partialTick,
            final PoseStack poseStack,
            final MultiBufferSource bufferSource,
            final int packedLight,
            final int packedOverlay
    ) {
        final Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        final BlockState sourceState = blockEntity.getSourceState();
        if (sourceState == null || sourceState.isAir()) {
            return;
        }

        poseStack.pushPose();

        if (!this.renderExactSourceModel(level, blockEntity.getBlockPos(), sourceState, poseStack, bufferSource, packedOverlay)) {
            this.renderSpriteFallback(level, blockEntity.getBlockPos(), sourceState, poseStack, bufferSource, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }

    private boolean renderExactSourceModel(
            final Level level,
            final BlockPos renderPos,
            final BlockState sourceState,
            final PoseStack poseStack,
            final MultiBufferSource bufferSource,
            final int packedOverlay
    ) {
        if (sourceState.getRenderShape() != RenderShape.MODEL) {
            return false;
        }

        final Minecraft minecraft = Minecraft.getInstance();
        final BlockRenderDispatcher dispatcher = minecraft.getBlockRenderer();
        final BakedModel model = dispatcher.getBlockModel(sourceState);

        if (model == null) {
            return false;
        }

        try {
            final RenderType renderType = ItemBlockRenderTypes.getChunkRenderType(sourceState);
            final VertexConsumer consumer = bufferSource.getBuffer(renderType);
            final RandomSource random = RandomSource.create();
            final long seed = sourceState.getSeed(renderPos);

            dispatcher.getModelRenderer().tesselateBlock(
                    level,
                    model,
                    sourceState,
                    renderPos,
                    poseStack,
                    consumer,
                    false,
                    random,
                    seed,
                    packedOverlay
            );

            return true;
        } catch (final Throwable ignored) {
            return false;
        }
    }

    private void renderSpriteFallback(
            final Level level,
            final BlockPos renderPos,
            final BlockState sourceState,
            final PoseStack poseStack,
            final MultiBufferSource bufferSource,
            final int packedLight,
            final int packedOverlay
    ) {
        final Minecraft minecraft = Minecraft.getInstance();
        final BlockRenderDispatcher dispatcher = minecraft.getBlockRenderer();
        final BakedModel model = dispatcher.getBlockModel(sourceState);
        final Map<Direction, FaceAppearance> faces = this.resolveFaces(level, renderPos, sourceState, model);

        final RenderType renderType;
        try {
            renderType = ItemBlockRenderTypes.getChunkRenderType(sourceState);
        } catch (final Throwable ignored) {
            return;
        }

        final VertexConsumer consumer = bufferSource.getBuffer(renderType);

        this.renderFaceUp(poseStack, consumer, faces.get(Direction.UP), packedLight, packedOverlay);
        this.renderFaceDown(poseStack, consumer, faces.get(Direction.DOWN), packedLight, packedOverlay);
        this.renderFaceNorth(poseStack, consumer, faces.get(Direction.NORTH), packedLight, packedOverlay);
        this.renderFaceSouth(poseStack, consumer, faces.get(Direction.SOUTH), packedLight, packedOverlay);
        this.renderFaceWest(poseStack, consumer, faces.get(Direction.WEST), packedLight, packedOverlay);
        this.renderFaceEast(poseStack, consumer, faces.get(Direction.EAST), packedLight, packedOverlay);
    }

    private Map<Direction, FaceAppearance> resolveFaces(
            final Level level,
            final BlockPos pos,
            final BlockState state,
            final BakedModel model
    ) {
        final Map<Direction, FaceAppearance> map = new EnumMap<>(Direction.class);

        map.put(Direction.UP, this.resolveFace(level, pos, state, model, Direction.UP));
        map.put(Direction.DOWN, this.resolveFace(level, pos, state, model, Direction.DOWN));
        map.put(Direction.NORTH, this.resolveFace(level, pos, state, model, Direction.NORTH));
        map.put(Direction.SOUTH, this.resolveFace(level, pos, state, model, Direction.SOUTH));
        map.put(Direction.WEST, this.resolveFace(level, pos, state, model, Direction.WEST));
        map.put(Direction.EAST, this.resolveFace(level, pos, state, model, Direction.EAST));

        return map;
    }

    private FaceAppearance resolveFace(
            final Level level,
            final BlockPos pos,
            final BlockState state,
            final BakedModel model,
            final Direction direction
    ) {
        final Minecraft minecraft = Minecraft.getInstance();
        final BlockColors blockColors = minecraft.getBlockColors();

        final RandomSource directRandom = RandomSource.create(state.getSeed(pos) + direction.ordinal());
        final List<BakedQuad> directQuads = model.getQuads(state, direction, directRandom);

        if (!directQuads.isEmpty()) {
            final BakedQuad quad = directQuads.get(0);
            return new FaceAppearance(quad.getSprite(), this.resolveTint(blockColors, level, pos, state, quad));
        }

        final RandomSource allRandom = RandomSource.create(state.getSeed(pos) ^ (31L + direction.ordinal()));
        final List<BakedQuad> allQuads = model.getQuads(state, null, allRandom);

        for (final BakedQuad quad : allQuads) {
            if (quad.getDirection() == direction) {
                return new FaceAppearance(quad.getSprite(), this.resolveTint(blockColors, level, pos, state, quad));
            }
        }

        final TextureAtlasSprite particle = model.getParticleIcon();
        final int tint = blockColors.getColor(state, level, pos, 0);
        return new FaceAppearance(particle, tint == -1 ? 0xFFFFFF : tint);
    }

    private int resolveTint(
            final BlockColors blockColors,
            final Level level,
            final BlockPos pos,
            final BlockState state,
            final BakedQuad quad
    ) {
        if (!quad.isTinted()) {
            return 0xFFFFFF;
        }

        final int tint = blockColors.getColor(state, level, pos, quad.getTintIndex());
        return tint == -1 ? 0xFFFFFF : tint;
    }

    private void renderFaceUp(
            final PoseStack poseStack,
            final VertexConsumer consumer,
            final FaceAppearance face,
            final int packedLight,
            final int packedOverlay
    ) {
        final PoseStack.Pose pose = poseStack.last();
        final Matrix4f matrix4f = pose.pose();
        PoseStack.Pose matrix3f = pose;

        final float u0 = face.sprite.getU0();
        final float u1 = face.sprite.getU1();
        final float v0 = face.sprite.getV0();
        final float v1 = face.sprite.getV1();

        final int r = (face.color >> 16) & 255;
        final int g = (face.color >> 8) & 255;
        final int b = face.color & 255;

        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 1.0F, 0.0F, r, g, b, u0, v0, packedLight, packedOverlay, 0.0F, 1.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 1.0F, 1.0F, r, g, b, u0, v1, packedLight, packedOverlay, 0.0F, 1.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 1.0F, 1.0F, r, g, b, u1, v1, packedLight, packedOverlay, 0.0F, 1.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 1.0F, 0.0F, r, g, b, u1, v0, packedLight, packedOverlay, 0.0F, 1.0F, 0.0F);
    }

    private void renderFaceDown(
            final PoseStack poseStack,
            final VertexConsumer consumer,
            final FaceAppearance face,
            final int packedLight,
            final int packedOverlay
    ) {
        final PoseStack.Pose pose = poseStack.last();
        final Matrix4f matrix4f = pose.pose();
        final PoseStack.Pose matrix3f = pose;

        final float u0 = face.sprite.getU0();
        final float u1 = face.sprite.getU1();
        final float v0 = face.sprite.getV0();
        final float v1 = face.sprite.getV1();

        final int r = (face.color >> 16) & 255;
        final int g = (face.color >> 8) & 255;
        final int b = face.color & 255;

        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 0.0F, 0.0F, r, g, b, u0, v0, packedLight, packedOverlay, 0.0F, -1.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 0.0F, 0.0F, r, g, b, u1, v0, packedLight, packedOverlay, 0.0F, -1.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 0.0F, 1.0F, r, g, b, u1, v1, packedLight, packedOverlay, 0.0F, -1.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 0.0F, 1.0F, r, g, b, u0, v1, packedLight, packedOverlay, 0.0F, -1.0F, 0.0F);
    }

    private void renderFaceNorth(
            final PoseStack poseStack,
            final VertexConsumer consumer,
            final FaceAppearance face,
            final int packedLight,
            final int packedOverlay
    ) {
        final PoseStack.Pose pose = poseStack.last();
        final Matrix4f matrix4f = pose.pose();
        final PoseStack.Pose matrix3f = pose;

        final float u0 = face.sprite.getU0();
        final float u1 = face.sprite.getU1();
        final float v0 = face.sprite.getV0();
        final float v1 = face.sprite.getV1();

        final int r = (face.color >> 16) & 255;
        final int g = (face.color >> 8) & 255;
        final int b = face.color & 255;

        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 0.0F, 0.0F, r, g, b, u0, v1, packedLight, packedOverlay, 0.0F, 0.0F, -1.0F);
        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 1.0F, 0.0F, r, g, b, u0, v0, packedLight, packedOverlay, 0.0F, 0.0F, -1.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 1.0F, 0.0F, r, g, b, u1, v0, packedLight, packedOverlay, 0.0F, 0.0F, -1.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 0.0F, 0.0F, r, g, b, u1, v1, packedLight, packedOverlay, 0.0F, 0.0F, -1.0F);
    }

    private void renderFaceSouth(
            final PoseStack poseStack,
            final VertexConsumer consumer,
            final FaceAppearance face,
            final int packedLight,
            final int packedOverlay
    ) {
        final PoseStack.Pose pose = poseStack.last();
        final Matrix4f matrix4f = pose.pose();
        final PoseStack.Pose matrix3f = pose;

        final float u0 = face.sprite.getU0();
        final float u1 = face.sprite.getU1();
        final float v0 = face.sprite.getV0();
        final float v1 = face.sprite.getV1();

        final int r = (face.color >> 16) & 255;
        final int g = (face.color >> 8) & 255;
        final int b = face.color & 255;

        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 0.0F, 1.0F, r, g, b, u0, v1, packedLight, packedOverlay, 0.0F, 0.0F, 1.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 0.0F, 1.0F, r, g, b, u1, v1, packedLight, packedOverlay, 0.0F, 0.0F, 1.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 1.0F, 1.0F, r, g, b, u1, v0, packedLight, packedOverlay, 0.0F, 0.0F, 1.0F);
        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 1.0F, 1.0F, r, g, b, u0, v0, packedLight, packedOverlay, 0.0F, 0.0F, 1.0F);
    }

    private void renderFaceWest(
            final PoseStack poseStack,
            final VertexConsumer consumer,
            final FaceAppearance face,
            final int packedLight,
            final int packedOverlay
    ) {
        final PoseStack.Pose pose = poseStack.last();
        final Matrix4f matrix4f = pose.pose();
        final PoseStack.Pose matrix3f = pose;

        final float u0 = face.sprite.getU0();
        final float u1 = face.sprite.getU1();
        final float v0 = face.sprite.getV0();
        final float v1 = face.sprite.getV1();

        final int r = (face.color >> 16) & 255;
        final int g = (face.color >> 8) & 255;
        final int b = face.color & 255;

        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 0.0F, 0.0F, r, g, b, u0, v1, packedLight, packedOverlay, -1.0F, 0.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 0.0F, 1.0F, r, g, b, u1, v1, packedLight, packedOverlay, -1.0F, 0.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 1.0F, 1.0F, r, g, b, u1, v0, packedLight, packedOverlay, -1.0F, 0.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 0.0F, 1.0F, 0.0F, r, g, b, u0, v0, packedLight, packedOverlay, -1.0F, 0.0F, 0.0F);
    }

    private void renderFaceEast(
            final PoseStack poseStack,
            final VertexConsumer consumer,
            final FaceAppearance face,
            final int packedLight,
            final int packedOverlay
    ) {
        final PoseStack.Pose pose = poseStack.last();
        final Matrix4f matrix4f = pose.pose();
        final PoseStack.Pose matrix3f = pose;

        final float u0 = face.sprite.getU0();
        final float u1 = face.sprite.getU1();
        final float v0 = face.sprite.getV0();
        final float v1 = face.sprite.getV1();

        final int r = (face.color >> 16) & 255;
        final int g = (face.color >> 8) & 255;
        final int b = face.color & 255;

        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 0.0F, 0.0F, r, g, b, u0, v1, packedLight, packedOverlay, 1.0F, 0.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 1.0F, 0.0F, r, g, b, u0, v0, packedLight, packedOverlay, 1.0F, 0.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 1.0F, 1.0F, r, g, b, u1, v0, packedLight, packedOverlay, 1.0F, 0.0F, 0.0F);
        this.vertex(consumer, matrix4f, matrix3f, 1.0F, 0.0F, 1.0F, r, g, b, u1, v1, packedLight, packedOverlay, 1.0F, 0.0F, 0.0F);
    }

    private void vertex(
            final VertexConsumer consumer,
            final Matrix4f pose,
            final PoseStack.Pose normal,
            final float x,
            final float y,
            final float z,
            final int r,
            final int g,
            final int b,
            final float u,
            final float v,
            final int packedLight,
            final int packedOverlay,
            final float nx,
            final float ny,
            final float nz
    ) {
        consumer.addVertex(pose, x, y, z)
                .setColor(r, g, b, 255)
                .setUv(u, v)
                .setOverlay(packedOverlay == 0 ? OverlayTexture.NO_OVERLAY : packedOverlay)
                .setLight(packedLight)
                .setNormal(normal, nx, ny, nz);
    }

    private record FaceAppearance(TextureAtlasSprite sprite, int color) {
    }
}