package com.denfop.render.oilrefiner;

import com.denfop.render.RenderFluidBlock;
import com.denfop.tiles.mechanism.TileOilRefiner;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class TileEntityOilRefinerRender implements BlockEntityRenderer<TileOilRefiner> {

    private final BlockEntityRendererProvider.Context contex;
    private float rotation = 0;
    private float prevRotation = 0;

    public TileEntityOilRefinerRender(BlockEntityRendererProvider.Context context) {
        this.contex = context;
    }

    @Override
    public void render(TileOilRefiner tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int packedLight, int packedOverlay) {
        if (!tile.getFluidTank(0).getFluid().isEmpty()) {
            poseStack.pushPose();
            float dopY = 0;
            if (tile.facing == 2) {
                poseStack.translate(0.437 + 0.425, 0.25 + dopY, 0.0125 + 0.4175);
            }
            if (tile.facing == 3) {
                poseStack.translate(0.437 + 0.45, 0.25 + dopY, +0.8825 + 0.435);
            }
            if (tile.facing == 4) {
                poseStack.translate(0.0125 + 0.4175, 0.25 + dopY, +0.437 + 0.425);
            }
            if (tile.facing == 5) {
                poseStack.translate(0.8825 + 0.435, 0.25 + dopY, +0.437 + 0.45);
            }
            final float scale = tile.getFluidTank(0).getFluidAmount() * 1F / tile.getFluidTank(0).getCapacity();
            RenderFluidBlock.renderFluid(tile.getFluidTank(0).getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale * 0.88f, 0.125f);
            poseStack.popPose();
        }
        if (!tile.getFluidTank(1).getFluid().isEmpty()) {
            poseStack.pushPose();
            float dopY = 0;
            if (tile.facing == 2) {
                poseStack.translate(0.8225 + 0.43, 0.25 + dopY, 0.25 + 0.4375);
            }
            if (tile.facing == 3) {
                poseStack.translate(0.8225 + 0.435, 0.254 + dopY, 0.626 + 0.435);
            }
            if (tile.facing == 4) {
                poseStack.translate(0.25 + 0.4375, 0.25 + dopY, 0.8225 + 0.43);
            }
            if (tile.facing == 5) {
                poseStack.translate(0.626 + 0.435, 0.25 + dopY, 0.8225 + 0.435);
            }
            final float scale = tile.getFluidTank(1).getFluidAmount() * 1F / tile.getFluidTank(1).getCapacity();
            RenderFluidBlock.renderFluid(tile.getFluidTank(1).getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale * 0.88f, 0.125f);
            poseStack.popPose();
        }
        if (!tile.getFluidTank(2).getFluid().isEmpty()) {
            poseStack.pushPose();
            float dopY = 0;
            if (tile.facing == 2) {
                poseStack.translate(+0.0495 + 0.43, +0.25 + dopY, +0.25 + 0.4375);
            }
            if (tile.facing == 3) {
                poseStack.translate(+0.0495 + 0.435, +0.25 + dopY, +0.626 + 0.435);
            }
            if (tile.facing == 4) {
                poseStack.translate(+0.25 + 0.4375, +0.25 + dopY, +0.0495 + 0.43);
            }
            if (tile.facing == 5) {
                poseStack.translate(+0.626 + 0.435, +0.25 + dopY, +0.0495 + 0.435);
            }

            final float scale = tile.getFluidTank(2).getFluidAmount() * 1F / tile.getFluidTank(2).getCapacity();
            RenderFluidBlock.renderFluid(tile.getFluidTank(2).getFluid(), bufferSource, tile.getLevel(), tile.getPos(), poseStack, scale * 0.88f, 0.125f);
            poseStack.popPose();
        }
    }
}
