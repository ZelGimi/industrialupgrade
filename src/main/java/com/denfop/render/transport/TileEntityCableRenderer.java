package com.denfop.render.transport;

import com.denfop.blockentity.transport.tiles.BlockEntityMultiCable;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCableRenderer implements BlockEntityRenderer<BlockEntityMultiCable> {

    private static final float FACADE_THICKNESS = 1.0F / 16.0F;
    private static final float SURFACE_EPSILON = 0.001F;
    private static final float EDGE_INSET = 0.001F;

    public TileEntityCableRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlockEntityMultiCable te,
                       float partialTicks,
                       PoseStack poseStack,
                       MultiBufferSource bufferSource,
                       int packedLight,
                       int packedOverlay) {

        if (te == null || te.getLevel() == null || !te.hasAnyFacade()) {
            return;
        }

        poseStack.pushPose();

        for (Direction side : Direction.values()) {
            ItemStack facadeStack = te.getFacade(side);
            if (facadeStack.isEmpty()) {
                continue;
            }

            if (!(facadeStack.getItem() instanceof BlockItem blockItem)) {
                continue;
            }

            BlockState facadeState = blockItem.getBlock().defaultBlockState();
            if (facadeState.getRenderShape() != RenderShape.MODEL) {
                continue;
            }

            this.renderFacadePlate(side, facadeState, poseStack, bufferSource, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }

    private void renderFacadePlate(Direction side,
                                   BlockState facadeState,
                                   PoseStack poseStack,
                                   MultiBufferSource bufferSource,
                                   int packedLight,
                                   int packedOverlay) {
        poseStack.pushPose();
        this.applyPlateTransform(poseStack, side);

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                facadeState,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay
        );

        poseStack.popPose();
    }

    private void applyPlateTransform(PoseStack poseStack, Direction side) {
        float min = EDGE_INSET;
        float max = 1.0F - EDGE_INSET;
        float planar = max - min;

        switch (side) {
            case NORTH -> {
                poseStack.translate(min, min, -SURFACE_EPSILON);
                poseStack.scale(planar, planar, FACADE_THICKNESS);
            }
            case SOUTH -> {
                poseStack.translate(min, min, 1.0F - FACADE_THICKNESS + SURFACE_EPSILON);
                poseStack.scale(planar, planar, FACADE_THICKNESS);
            }
            case WEST -> {
                poseStack.translate(-SURFACE_EPSILON, min, min);
                poseStack.scale(FACADE_THICKNESS, planar, planar);
            }
            case EAST -> {
                poseStack.translate(1.0F - FACADE_THICKNESS + SURFACE_EPSILON, min, min);
                poseStack.scale(FACADE_THICKNESS, planar, planar);
            }
            case DOWN -> {
                poseStack.translate(min, -SURFACE_EPSILON, min);
                poseStack.scale(planar, FACADE_THICKNESS, planar);
            }
            case UP -> {
                poseStack.translate(min, 1.0F - FACADE_THICKNESS + SURFACE_EPSILON, min);
                poseStack.scale(planar, FACADE_THICKNESS, planar);
            }
        }
    }
}